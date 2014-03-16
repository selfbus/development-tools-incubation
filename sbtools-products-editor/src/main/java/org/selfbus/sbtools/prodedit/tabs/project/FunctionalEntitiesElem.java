package org.selfbus.sbtools.prodedit.tabs.project;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.common.gui.tree.IconTreeCellRenderer;
import org.selfbus.sbtools.common.gui.tree.MutableIconTreeNode;
import org.selfbus.sbtools.common.gui.utils.TreeUtils;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInTreeAction;
import org.selfbus.sbtools.prodedit.binding.SelectionInTree;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.components.FormPanel;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.AbstractProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectListener;
import org.selfbus.sbtools.prodedit.model.global.FunctionalEntity;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.tabs.internal.CategoryElem;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * An element that displays the project's {@link FunctionalEntity functional entities}.
 */
public class FunctionalEntitiesElem implements CloseableComponent, CategoryElem
{
   private final MutableIconTreeNode rootNode = new MutableIconTreeNode("/");
   private final DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
   private final SelectionInTree selectionInTree = new SelectionInTree(treeModel);
   private final JTree tree = new JTree(selectionInTree);
   private final JScrollPane listScrollPane = new JScrollPane(tree);

   private final PresentationModel<FunctionalEntity> detailsModel = new PresentationModel<FunctionalEntity>(selectionInTree);
   private final Validator<FunctionalEntity> validator = new FunctionalEntityValidator();
   private final ListValidationHandler<FunctionalEntity> validationHandler = new ListValidationHandler<FunctionalEntity>(detailsModel, validator);
   
   private final JLabel idField;
   private final JTextField nameField, descField;
   private final JToolBar toolBar = new JToolBar();
   private final FormPanel detailsPanel = new FormPanel();

   /**
    * Create a {@link FunctionalEntity functional entities} display element
    */
   public FunctionalEntitiesElem()
   {
      detailsPanel.setLayout(new GridBagLayout());
      validationHandler.setValidatedContainer(detailsPanel);
//      validationHandler.observeSelectionChange(selectionInTree);

      selectionInTree.bindTo(tree);

      idField = BasicComponentFactory.createLabel(detailsModel.getModel("idStr"));

      nameField = BasicComponentFactory.createTextField(validationHandler.getModel("name"), false);
      ValidationComponentUtils.setMandatoryBackground(nameField);

      descField = BasicComponentFactory.createTextField(detailsModel.getModel("description"), false);
      
      detailsPanel.addCaption(I18n.getMessage("FunctionalEntitiesElem.caption"));
      detailsPanel.addFormLine(I18n.getMessage("FunctionalEntitiesElem.name") + ":", nameField);
      detailsPanel.addFormLine(I18n.getMessage("FunctionalEntitiesElem.description") + ":", descField);
      detailsPanel.addFormLine(I18n.getMessage("FunctionalEntitiesElem.id") + ":", idField);
      detailsPanel.addStretch();

      tree.setRootVisible(false);
      tree.setCellRenderer(new IconTreeCellRenderer());

      setupToolBar();
      ProdEdit.getInstance().getProjectService().addListener(projectListener);
   }

   /**
    * Setup the tool bar.
    */
   private void setupToolBar()
   {
      //
      //  Action: add a language
      //
      toolBar.add(new BasicAction("add", I18n.getMessage("LanguagesElem.addTip"), ImageCache.getIcon("icons/add"))
      {
         private static final long serialVersionUID = 1;

         @Override
         public synchronized void actionEvent(ActionEvent event)
         {
            Project project = ProdEdit.getInstance().getProject();
            if (project == null) return;

            FunctionalEntity parentEntity = (FunctionalEntity) selectionInTree.getSelection();
            selectionInTree.setSelection(null);

            /*final FunctionalEntity newEntity =*/ project.createFunctionalEntity(parentEntity);

            // FIXME setSelection is buggy
//            SwingUtilities.invokeLater(new Runnable()
//            {
//               @Override
//               public void run()
//               {
//                  selectionInTree.setSelection(newEntity);
//               }
//            });
         }
      });

      //
      //  Action: remove the current language
      //
      toolBar.add(new RemoveSelectionInTreeAction(selectionInTree, I18n.getMessage("LanguagesElem.removeTip"))); 
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("FunctionalEntitiesElem.title");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JToolBar getToolBar()
   {
      return toolBar;
   }

   @Override
   public JComponent getDetailsPanel()
   {
      return detailsPanel;
   }

   @Override
   public JComponent getListPanel()
   {
      return listScrollPane;
   }

   @Override
   public void close()
   {
      ProdEdit.getInstance().getProjectService().removeListener(projectListener);
      selectionInTree.release();
   }      

//   private void detailsEdited()
//   {
//      FunctionalEntity funcEntity = (FunctionalEntity) getSelectedObject();
//      if (funcEntity != null)
//      {
//         String newName = nameField.getText();
//         if (!newName.equals(funcEntity.name))
//         {
//            funcEntity.name = newName;
//            treeModel.nodeChanged(getSelectedNode());
//         }
//      }
//   }

   /**
    * {@inheritDoc}
    */
   protected void updateContents()
   {
      rootNode.removeAllChildren();

      final Project project = ProdEdit.getInstance().getProjectService().getProject();
      if (project == null)
         return;

      for (FunctionalEntity funcEntity : project.getFunctionalEntities())
      {
         addFunctionalEntity(funcEntity, rootNode);
      }

      treeModel.reload();
      TreeUtils.expandAll(tree);

//      nameField.setEnabled(false);
//      descField.setEnabled(false);

      nameField.setText("");
      descField.setText("");
      idField.setText("");

      // TODO
//      if (!project.getFunctionalEntities().isEmpty())
//         setSelectedObject(project.getFunctionalEntities().iterator().next());
   }

   /**
    * Add a functional entity as child to the parent tree node. Also adds the functional entity's
    * children.
    * 
    * @param funcEntity - the functional entity to add
    * @param parentNode - the parent node
    */
   private void addFunctionalEntity(FunctionalEntity funcEntity, MutableIconTreeNode parentNode)
   {
      MutableIconTreeNode node = new MutableIconTreeNode(funcEntity, true);
      //      node.setIcon(funcEntityIcon);  TODO
      parentNode.add(node);

      for (FunctionalEntity e : funcEntity.getChilds())
      {
         addFunctionalEntity(e, node);
      }
   }

   /**
    * {@inheritDoc}
    */
   protected boolean isRelevant(Object obj)
   {
      return obj instanceof FunctionalEntity;
   }

   /**
    * Called when a node in the tree is selected.
    * 
    * @param obj - the selected user object of the selected tree node.
    */
   protected void objectSelected(Object obj)
   {
      FunctionalEntity funcEntity = (FunctionalEntity) obj;
      if (funcEntity != null)
      {
         nameField.setEnabled(true);
         descField.setEnabled(true);

         nameField.setText(funcEntity.getName());
         descField.setText(funcEntity.getDescription());
         idField.setText(Integer.toString(funcEntity.getId()));
      }
   }

   /**
    * Private validator for form input.
    */
   private class FunctionalEntityValidator implements Validator<FunctionalEntity>
   {
      @Override
      public ValidationResult validate(FunctionalEntity validationTarget)
      {
         ValidationResult result = new ValidationResult();

         if (idField.getText().length() != 2)
            result.addError("ID length must be 2", idField);

         return result;
      }
   }

   /**
    * Private project listener of {@link AbstractTreeView}.
    */
   private final ProjectListener projectListener = new AbstractProjectListener()
   {
      @Override
      public void projectChanged(Project project)
      {
//         selectedObject = null;
         updateContents();
      }
   };
}

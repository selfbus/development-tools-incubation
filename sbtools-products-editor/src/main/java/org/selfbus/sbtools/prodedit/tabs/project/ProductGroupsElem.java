package org.selfbus.sbtools.prodedit.tabs.project;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInListAction;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.AbstractProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectListener;
import org.selfbus.sbtools.prodedit.model.global.Manufacturer;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.tabs.internal.CategoryElem;
import org.selfbus.sbtools.prodedit.utils.FontUtils;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * An element that displays the project's product groups.
 */
public class ProductGroupsElem implements CloseableComponent, CategoryElem
{
   private SelectionInList<ProductGroup> selectionInList = new SelectionInList<ProductGroup>(new LinkedList<ProductGroup>());
   private final PresentationModel<ProductGroup> detailsModel = new PresentationModel<ProductGroup>(selectionInList);
   private final Validator<ProductGroup> validator = new DetailsFormValidator();
   private final ListValidationHandler<ProductGroup> validationHandler = new ListValidationHandler<ProductGroup>(detailsModel, validator);

   @SuppressWarnings("unchecked")
   private final JList<ProductGroup> prodGroupsList = BasicComponentFactory.createList(selectionInList);
   private final JScrollPane prodGroupsScrollPane = new JScrollPane(prodGroupsList);

   private final JToolBar toolBar = new JToolBar();
   private final JPanel detailsPanel;

   private final BindingConverter<Manufacturer, String> manufacturerNameConverter = new BindingConverter<Manufacturer, String>()
   {
      @Override
      public String targetValue(Manufacturer sourceValue)
      {
         return sourceValue == null ? null : sourceValue.getName();
      }
      
      @Override
      public Manufacturer sourceValue(String targetValue)
      {
         throw new IllegalAccessError("This converter is read-only");
      }
   };
   private final ConverterValueModel manufacturerValueModel = new ConverterValueModel(validationHandler.getModel("manufacturer"), manufacturerNameConverter);
   private final JLabel manufacturerField = BasicComponentFactory.createLabel(manufacturerValueModel);

   private final JTextComponent idField = BasicComponentFactory.createTextField(validationHandler.getModel("id"), false);
   private final JTextComponent nameField = BasicComponentFactory.createTextField(validationHandler.getModel("name"), false);
   private final JTextArea descArea = BasicComponentFactory.createTextArea(detailsModel.getModel("description"), false);

   /**
    * Create a product groups display element.
    */
   public ProductGroupsElem()
   {
      FormLayout layout = new FormLayout("6dlu, left:pref, 4dlu, fill:pref:grow, 6dlu",
         "8dlu, pref, 8dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 1dlu, pref, fill:pref:grow, pref, 4dlu");
      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();

      int row = 2;
      builder.addLabel(I18n.getMessage("ProductGroupsElem.caption"), cc.rcw(row, 2, 3)).setFont(
         FontUtils.getCaptionFont());

      row += 2;
      builder.addLabel(I18n.getMessage("ProductGroupsElem.id"), cc.rc(row, 2));
      builder.add(idField, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("ProductGroupsElem.name"), cc.rc(row, 2));
      builder.add(nameField, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("ProductGroupsElem.manufacturer"), cc.rc(row, 2));
      builder.add(manufacturerField, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("ProductGroupsElem.description"), cc.rcw(row, 2, 3));
      row += 2;
      builder.add(descArea, cc.rcw(row, 2, 3));
      descArea.setRows(5);

      builder.add(Box.createVerticalGlue(), cc.rcw(++row, 2, 3));

      JComponent reportPane = ValidationResultViewFactory
         .createReportList(validationHandler.getValidationResultModel());
      builder.add(reportPane, cc.rcw(++row, 2, 3));

      detailsPanel = builder.build();

      prodGroupsList.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e)
         {
            ProductGroup group = selectionInList.getSelection();
            if (group != null && e.getClickCount() == 2)
            {
               ProdEdit.getInstance().showProductGroupTab(group);
            }
         }
      });

      validationHandler.setValidatedContainer(detailsPanel);
      validationHandler.observeSelectionChange(selectionInList);

      ProdEdit.getInstance().getProjectService().addListener(projectListener);
      setupToolBar();

      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            prodGroupsList.setSelectedIndex(0);
         }
      });
   }

   /**
    * Setup the tool bar.
    */
   private void setupToolBar()
   {
      //
      //  Action: add a product group
      //
      toolBar.add(new BasicAction("add", I18n.getMessage("ProductGroupsElem.addTip"), ImageCache
         .getIcon("icons/filenew"))
      {
         private static final long serialVersionUID = 1;

         @Override
         public void actionEvent(ActionEvent event)
         {
            Project project = ProdEdit.getInstance().getProject();
            if (project == null) return;

            Object selectedObject = selectionInList.getSelection();
            if (selectedObject == null && !selectionInList.getList().isEmpty())
               selectedObject = selectionInList.getList().get(0);

            ProductGroup group = project.createProductGroup();
            if (group.getManufacturer() == null && selectedObject instanceof ProductGroup)
            {
               ProductGroup selectedGroup = (ProductGroup) selectedObject;
               group.setManufacturer(selectedGroup.getManufacturer());
            }
            selectionInList.setSelection(group);
            idField.requestFocus();
         }
      });

      //
      //  Action: remove a product group
      //
      RemoveSelectionInListAction removeAction = new RemoveSelectionInListAction(selectionInList,
         I18n.getMessage("LanguagesElem.removeTip"), ImageCache.getIcon("icons/editdelete"));
      removeAction.setConfirmAction(true);
      toolBar.add(removeAction);

      //
      //  Action: edit a product group
      //
      toolBar.add(new BasicAction("edit", I18n.getMessage("ProductGroupsElem.editTip"), ImageCache
         .getIcon("icons/edit"))
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void actionEvent(ActionEvent event)
         {
            ProductGroup group = selectionInList.getSelection();
            if (group != null)
            {
               ProdEdit.getInstance().showProductGroupTab(group);
            }
         }
      });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      ProdEdit.getInstance().getProjectService().removeListener(projectListener);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("ProductGroupsElem.title");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getListPanel()
   {
      return prodGroupsScrollPane;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JToolBar getToolBar()
   {
      return toolBar;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getDetailsPanel()
   {
      return detailsPanel;
   }

   /**
    * {@inheritDoc}
    */
   protected void updateContents()
   {
      final Project project = ProdEdit.getInstance().getProjectService().getProject();
      if (project == null)
         selectionInList.setList(new LinkedList<ProductGroup>());
      else selectionInList.setList(project.getProductGroups());
   }

   /**
    * Private project listener.
    */
   private final ProjectListener projectListener = new AbstractProjectListener()
   {
      @Override
      public void projectChanged(Project project)
      {
         updateContents();
      }
   };

   /**
    * Private validator for details form input.
    */
   private class DetailsFormValidator implements Validator<ProductGroup>
   {
      @Override
      public ValidationResult validate(ProductGroup group)
      {
         Project project = ProdEdit.getInstance().getProjectService().getProject();
         ValidationResult result = new ValidationResult();
         ProductGroup found;

         String id = idField.getText();
         if (id.isEmpty())
            result.addError(I18n.getMessage("ProductGroupsElem.errIdEmpty"), idField);
         else if (!id.matches("^[-_\\.\\w]+$"))
            result.addError(I18n.getMessage("ProductGroupsElem.errIdInvalidChars"), idField);

         found = project.getProductGroup(id);
         if (found != null && found != group)
            result.addError(I18n.getMessage("ProductGroupsElem.errIdNotUnique"), idField);

         String name = nameField.getText();
         if (name.isEmpty())
            result.addError(I18n.getMessage("ProductGroupsElem.errNameEmpty"), nameField);

         found = project.getProductGroupByName(name);
         if (found != null && found != group)
            result.addError(I18n.getMessage("ProductGroupsElem.errNameNotUnique"), idField);

         return result;
      }
   }
}

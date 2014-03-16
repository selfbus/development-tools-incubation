package org.selfbus.sbtools.prodedit.tabs.prodgroup.general;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInListAction;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.global.FunctionalEntity;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.CatalogEntry;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
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
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * An element that displays a {@link VirtualDevice virtual device}.
 */
public class VirtualDeviceElem implements CategoryElem
{
   private final ProductGroup group;
   private final SelectionInList<VirtualDevice> selectionInList;

   private final Validator<VirtualDevice> validator = new DetailsFormValidator();
   private final ListValidationHandler<VirtualDevice> validationHandler;

   private final JPanel detailsPanel;
   private final JToolBar toolBar = new JToolBar();
   
   private final JLabel idField;
   private final JTextComponent nameField;
   private final JTextArea descArea;
   private final JTextComponent numberField;

   private SelectionInList<FunctionalEntity> selectionInFunctionalEntities = new SelectionInList<FunctionalEntity>(new LinkedList<FunctionalEntity>());
   @SuppressWarnings("unchecked")
   private final JComboBox<FunctionalEntity> functionalEntityCombo = BasicComponentFactory.createComboBox(selectionInFunctionalEntities);
   
   //private final MappingListCellRenderer catalogEntryRenderer = new MappingListCellRenderer();
   private SelectionInList<CatalogEntry> selectionInCatalogEntries = new SelectionInList<CatalogEntry>(new LinkedList<CatalogEntry>());
   @SuppressWarnings("unchecked")
   private final JComboBox<CatalogEntry> catalogEntryCombo = BasicComponentFactory.createComboBox(selectionInCatalogEntries);

   /**
    * Converter that sets the name of the dependent objects (ApplicationProgram, ...)
    * when the name of the device is changed
    */
   private final BindingConverter<String, String> nameSyncConverter = new BindingConverter<String, String>()
   {
      @Override
      public String targetValue(String sourceValue)
      {
         return sourceValue;
      }

      @Override
      public String sourceValue(String targetValue)
      {
         VirtualDevice device = selectionInList.getSelection();
         if (device != null)
         {
            ApplicationProgram program = group.getProgram(device.getProgramId());
            if (program != null)
               program.setName(targetValue);
         }

         return targetValue;
      }
   };

   /**
    * Create a {@link VirtualDevice virtual device} display element.
    * 
    * @param group - the product group
    * @param selectionInList - the selection in list of the virtual devices
    */
   public VirtualDeviceElem(ProductGroup group, SelectionInList<VirtualDevice> selectionInList)
   {
      this.group = group;
      this.selectionInList = selectionInList;

      PresentationModel<VirtualDevice> detailsModel = new PresentationModel<VirtualDevice>(selectionInList);
      validationHandler = new ListValidationHandler<VirtualDevice>(detailsModel, validator);

      ConverterValueModel nameValueModel = new ConverterValueModel(validationHandler.getModel("name"), nameSyncConverter);
      nameField = BasicComponentFactory.createTextField(nameValueModel, false);

      idField = BasicComponentFactory.createLabel(detailsModel.getModel("idStr"));
      descArea = BasicComponentFactory.createTextArea(detailsModel.getModel("description"), false);
      numberField = BasicComponentFactory.createIntegerField(detailsModel.getModel("number"), 0);

      FormLayout layout = new FormLayout("6dlu,l:p,4dlu,f:p:g,6dlu", 
         "8dlu,p,8dlu,p,4dlu,p,p,4dlu,p,4dlu,p,4dlu,p,4dlu,p,4dlu,p,4dlu,p,4dlu,f:p:g,p,4dlu");
      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();
      int row = 2;
      
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.caption"), cc.rcw(row, 2, 3))
         .setFont(FontUtils.getCaptionFont());

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.name"), cc.rc(row, 2));
      builder.add(nameField, cc.rc(row, 4));
      ValidationComponentUtils.setMandatoryBackground(nameField);

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.description"), cc.rcw(row, 2, 3));
      builder.add(descArea, cc.rcw(++row, 2, 3));
      descArea.setRows(5);

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.number"), cc.rc(row, 2));
      builder.add(numberField, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.productType"), cc.rc(row, 2));
//      builder.add(productTypeField, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.catalogEntry"), cc.rc(row, 2));
      builder.add(catalogEntryCombo, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.functionalEntity"), cc.rc(row, 2));
      builder.add(functionalEntityCombo, cc.rc(row, 4));

      row += 2;
      builder.addLabel(I18n.getMessage("VirtualDeviceElem.id"), cc.rc(row, 2));
      builder.add(idField, cc.rc(row, 4));

      builder.add(Box.createVerticalGlue(), cc.rcw(++row, 2, 3));
      
      JComponent reportPane = ValidationResultViewFactory.createReportList(validationHandler.getValidationResultModel());
      builder.add(reportPane, cc.rcw(++row, 2, 3));

      detailsPanel = builder.build();

      validationHandler.setValidatedContainer(detailsPanel);
      validationHandler.observeSelectionChange(selectionInList);

//      ProdEdit.getInstance().getProjectService().addListener(projectListener);
      setupToolBar();

      updateContents();
   }

   /**
    * Setup the tool bar.
    */
   private void setupToolBar()
   {
      //
      //  Action: add a virtual device
      //
      toolBar.add(new BasicAction("add", I18n.getMessage("VirtualDeviceElem.addTip"), ImageCache.getIcon("icons/add"))
      {
         private static final long serialVersionUID = 1;

         @Override
         public void actionEvent(ActionEvent event)
         {
            // TODO
         }
      });

      //
      //  Action: remove the current virtual device
      //
      toolBar.add(new RemoveSelectionInListAction(selectionInList, I18n.getMessage("VirtualDeviceElem.removeTip"))); 
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("VirtualDeviceElem.title");
   }
   
   /**
    * @return Null as there is no list panel.
    */
   @Override
   public JComponent getListPanel()
   {
      return null;
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
   @Override
   public JToolBar getToolBar()
   {
      return toolBar;
   }

   /**
    * Update the contents.
    */
   protected void updateContents()
   {
      final Project project = ProdEdit.getInstance().getProjectService().getProject();
      if (project != null)
      {
         selectionInFunctionalEntities.setList(project.getFunctionalEntities());
//         selectionInCatalogEntries.setList(project.getCatalogEntries())
      }
   }

   /**
    * Private validator for details form input.
    */
   private class DetailsFormValidator implements Validator<VirtualDevice>
   {
      @Override
      public ValidationResult validate(VirtualDevice device)
      {
         ValidationResult result = new ValidationResult();

         return result;
      }
   }
}

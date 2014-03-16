package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.components.ToolBarButton;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.binding.IdValueConverter;
import org.selfbus.sbtools.prodedit.binding.IntegerValueConverter;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.enums.ObjectType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.utils.FontUtils;
import org.selfbus.sbtools.prodedit.utils.MultiLingualTextUtil;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationResultViewFactory;


/**
 * A panel for editing a {@link CommunicationObject}.
 */
public class CommunicationObjectPanel extends JPanel implements CloseableComponent
{
   private static final long serialVersionUID = -964401895866149706L;

   protected ApplicationProgram program;
   protected CommunicationObject comObject;

   private final IntegerValueConverter intValueConverter = new IntegerValueConverter();
   private final IntegerValueConverter hexAddrIntValueConverter = new IntegerValueConverter(16, 4);
   private final IdValueConverter idValueConverter = new IdValueConverter();
   
   private final PresentationModel<CommunicationObject> detailsModel = new PresentationModel<CommunicationObject>();
   private final DetailsFormValidator validator = new DetailsFormValidator();
   private final ListValidationHandler<CommunicationObject> validationHandler = new ListValidationHandler<CommunicationObject>(detailsModel, validator);

   private final ValueModel idValue = new ConverterValueModel(detailsModel.getModel("id"), idValueConverter); //detailsModel.getModel("idStr");
   private final JLabel idField = BasicComponentFactory.createLabel(idValue);

   private final ValueModel nameValue = detailsModel.getModel("name");
   private Map<String, PropertyAdapter<MultiLingualText.Element>> nameElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element> >();

   private final ValueModel funcValue = detailsModel.getModel("function");
   private Map<String, PropertyAdapter<MultiLingualText.Element>> funcElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element> >();

   private final ValueModel descValue = detailsModel.getModel("description");
   private Map<String, PropertyAdapter<MultiLingualText.Element>> descElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element> >();

   private final ValueModel parentIdValue = new ConverterValueModel(detailsModel.getModel("parentId"), intValueConverter);
   private final JTextComponent parentIdField = BasicComponentFactory.createTextField(parentIdValue);

   private final JButton gotoParentButton = new JButton(ImageCache.getIcon("icons/forward3"));

   private final ValueModel parentValueValue = new ConverterValueModel(detailsModel.getModel("parentValue"), intValueConverter);
   private final JTextComponent parentValueField = BasicComponentFactory.createTextField(parentValueValue);

   private final ValueModel typeValue = detailsModel.getModel("type");
   private SelectionInList<ObjectType> selectionInType = new SelectionInList<ObjectType>(ObjectType.values(), typeValue);
   @SuppressWarnings("unchecked")
   private final JComboBox<ObjectType> typeCombo = BasicComponentFactory.createComboBox(selectionInType);

   private final ValueModel numberValue = new ConverterValueModel(detailsModel.getModel("number"), intValueConverter);
   private final JTextComponent numberValueField = BasicComponentFactory.createTextField(numberValue);

   private final ValueModel addressValue = new ConverterValueModel(detailsModel.getModel("address"), hexAddrIntValueConverter);
   private final JTextComponent addressValueField = BasicComponentFactory.createTextField(addressValue);
   
   private final ValueModel commEnabledValue = detailsModel.getModel("commEnabled");
   private final JComponent commEnabledField =  BasicComponentFactory.createCheckBox(commEnabledValue, I18n.getMessage("CommunicationObjectPanel.commEnabled"));

   private final ValueModel readEnabledValue = detailsModel.getModel("readEnabled");
   private final JComponent readEnabledField =  BasicComponentFactory.createCheckBox(readEnabledValue, I18n.getMessage("CommunicationObjectPanel.readEnabled"));

   private final ValueModel writeEnabledValue = detailsModel.getModel("writeEnabled");
   private final JComponent writeEnabledField =  BasicComponentFactory.createCheckBox(writeEnabledValue, I18n.getMessage("CommunicationObjectPanel.writeEnabled"));

   private final ValueModel transEnabledValue = detailsModel.getModel("transEnabled");
   private final JComponent transEnabledField =  BasicComponentFactory.createCheckBox(transEnabledValue, I18n.getMessage("CommunicationObjectPanel.transEnabled"));

   private final ValueModel orderValue = detailsModel.getModel("order");
   private final JTextComponent orderValueField = BasicComponentFactory.createTextField(new ConverterValueModel(orderValue, intValueConverter), true);

   /**
    * Create a panel for editing a {@link CommunicationObject}.
    */
   public CommunicationObjectPanel(final ParametersElem parent)
   {
      setLayout(new BorderLayout(0, 2));

      FormLayout layout = new FormLayout("6dlu, l:p, 4dlu, f:p:g, 2dlu, l:p, 6dlu", 
         "8dlu, p, 6dlu, p, 4dlu, p, 4dlu, p, 12dlu, p, " +
         "4dlu, p, 4dlu, p, 4dlu, p, 12dlu, p, 4dlu, p, " +
         "4dlu, p, 4dlu, p, 12dlu, p, 4dlu, p, 4dlu, p, " +
         "4dlu, " +
         "f:p:g, p, 4dlu");

      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();

      int row = 2;
      JLabel lbl = builder.addLabel(I18n.getMessage("CommunicationObjectPanel.caption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getCaptionFont());
      lbl.setOpaque(false);
      idField.setHorizontalAlignment(SwingConstants.RIGHT);
      idField.setOpaque(false);
      builder.add(idField, cc.rc(row, 4));

      row = 4;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.name") + ':', cc.rc(row, 2));

      row = 5;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 6;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.function") + ':', cc.rc(row, 2));

      row = 7;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 8;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.description") + ':', cc.rc(row, 2));

      row = 9;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 10;
      lbl = builder.addLabel(I18n.getMessage("CommunicationObjectPanel.typeCaption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getSubCaptionFont());
      lbl.setOpaque(false);      

      row = 12;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.number"), cc.rc(row, 2));
      builder.add(numberValueField, cc.rc(row, 4));

      row = 14;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.type"), cc.rc(row, 2));
      builder.add(typeCombo, cc.rc(row, 4));

      row = 16;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.address"), cc.rc(row, 2));
      builder.add(addressValueField, cc.rc(row, 4));

      row = 17;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 18;
      lbl = builder.addLabel(I18n.getMessage("CommunicationObjectPanel.displayCaption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getSubCaptionFont());
      lbl.setOpaque(false);      

      row = 20;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.parentId"), cc.rc(row, 2));
      builder.add(parentIdField, cc.rc(row, 4));
      builder.add(gotoParentButton, cc.rc(row, 6));
      ToolBarButton.useToolBarStyle(gotoParentButton);
      gotoParentButton.setToolTipText(I18n.getMessage("ParameterPanel.gotoParentToolTip"));
      gotoParentButton.setPreferredSize(new Dimension(gotoParentButton.getPreferredSize().width, parentValueField.getPreferredSize().height));

      row = 22;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.parentValue"), cc.rc(row, 2));
      builder.add(parentValueField, cc.rc(row, 4));

      row = 24;
      builder.addLabel(I18n.getMessage("CommunicationObjectPanel.order"), cc.rc(row, 2));
      builder.add(orderValueField, cc.rc(row, 4));

      row = 25;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 26;
      lbl = builder.addLabel(I18n.getMessage("CommunicationObjectPanel.defaultValues"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getSubCaptionFont());
      lbl.setOpaque(false);      

      row = 28;
      builder.add(readEnabledField, cc.rc(row, 2));
      builder.add(commEnabledField, cc.rc(row, 4));

      row = 30;
      builder.add(writeEnabledField, cc.rc(row, 2));
      builder.add(transEnabledField, cc.rc(row, 4));


      row = 33;
      builder.add(Box.createVerticalGlue(), cc.rcw(row, 2, 3));

      nameElems = MultiLingualTextUtil.createFormElements(builder, 5);
      funcElems = MultiLingualTextUtil.createFormElements(builder, 7 + nameElems.size() * 2);
      descElems = MultiLingualTextUtil.createFormElements(builder, 9 + nameElems.size() * 2 + funcElems.size() * 2);

      add(builder.build(), BorderLayout.CENTER);

      validationHandler.setValidatedContainer(builder.getPanel());
      JComponent reportPane = ValidationResultViewFactory.createReportList(validationHandler.getValidationResultModel());
      add(reportPane, BorderLayout.SOUTH);

      ProdEdit.getInstance().getProject().getLanguages().addListDataListener(languagesListener);

      gotoParentButton.setEnabled(false);
      parentIdValue.addValueChangeListener(new PropertyChangeListener()
      {
         @Override
         public void propertyChange(PropertyChangeEvent evt)
         {
            String val = (String) evt.getNewValue();
            gotoParentButton.setEnabled(val != null && !val.isEmpty());
         }
      });

      gotoParentButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            String val = (String) parentIdValue.getValue();
            if (!val.isEmpty())
               parent.setSelectedParam(Integer.parseInt(val));
         }
      });
   }

   /**
    * Set the communication object that is edited.
    *
    * @param comObject - the communication object to set
    */
   public void setCommunicationObject(CommunicationObject comObject)
   {
      this.comObject = comObject;
      detailsModel.setBean(comObject);

      if (comObject != null)
      {
         MultiLingualText nameText = (MultiLingualText) nameValue.getValue();
         MultiLingualText funcText = (MultiLingualText) funcValue.getValue();
         MultiLingualText descText = (MultiLingualText) descValue.getValue();

         for (Entry<String, PropertyAdapter<MultiLingualText.Element>> entry : nameElems.entrySet())
            entry.getValue().setBean(nameText.getElement(entry.getKey()));

         for (Entry<String, PropertyAdapter<MultiLingualText.Element>> entry : funcElems.entrySet())
            entry.getValue().setBean(funcText.getElement(entry.getKey()));

         for (Entry<String, PropertyAdapter<MultiLingualText.Element>> entry : descElems.entrySet())
            entry.getValue().setBean(descText.getElement(entry.getKey()));
      }
   }

   /**
    * Set the application program.
    *
    * @param program - the program to set
    */
   public void setProgram(ApplicationProgram program)
   {
      this.program = program;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      detailsModel.release();
      ProdEdit.getInstance().getProject().getLanguages().removeListDataListener(languagesListener);
   }

   /**
    * Update or recreate the details panel.
    */
   protected void updateDetailsPanel()
   {
      // TODO recreate the text elements when the language-list changes
      // setupDetails();

      CommunicationObject current = comObject;
      setCommunicationObject(null);
      setCommunicationObject(current);
   }

   /**
    * A listener for project language changes 
    */
   private final ListDataListener languagesListener = new ListDataListener()
   {
      @Override
      public void intervalRemoved(ListDataEvent e)
      {
         updateDetailsPanel();
      }
      
      @Override
      public void intervalAdded(ListDataEvent e)
      {
         updateDetailsPanel();
      }
      
      @Override
      public void contentsChanged(ListDataEvent e)
      {
         updateDetailsPanel();
      }
   };

   /**
    * Private validator for details form input.
    */
   private class DetailsFormValidator implements Validator<CommunicationObject>
   {
      @Override
      public ValidationResult validate(CommunicationObject comObject)
      {
         ValidationResult result = new ValidationResult();

         return result;
      }
   }
}

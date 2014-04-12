package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.selfbus.sbtools.prodedit.binding.IdentifiableConverter;
import org.selfbus.sbtools.prodedit.binding.IntegerValueConverter;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.binding.ParameterTypeSizeConverter;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.renderer.ParameterCategoryComboBoxRenderer;
import org.selfbus.sbtools.prodedit.renderer.ParameterValueListCellRenderer;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.ProductGroupTab;
import org.selfbus.sbtools.prodedit.utils.FontUtils;
import org.selfbus.sbtools.prodedit.utils.MultiLingualTextUtil;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * A panel for editing a {@link Parameter}.
 */
public class ParameterPanel extends JPanel implements CloseableComponent
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(ParameterPanel.class);
   private static final long serialVersionUID = -1335749489001109671L;

   protected ApplicationProgram program;
   protected Parameter parameter;

   private final IntegerValueConverter hexAddrIntValueConverter = new IntegerValueConverter(16, 4);
   private final IntegerValueConverter intValueConverter = new IntegerValueConverter();
   private final IdValueConverter idValueConverter = new IdValueConverter();
   private final ParameterTypeSizeConverter paramTypeSizeConverter = new ParameterTypeSizeConverter();

   private final PresentationModel<Parameter> detailsModel = new PresentationModel<Parameter>();
   private final DetailsFormValidator validator = new DetailsFormValidator();
   private final ListValidationHandler<Parameter> validationHandler = new ListValidationHandler<Parameter>(detailsModel, validator);

   private final ValueModel nameValue = detailsModel.getModel("name");
   private final JTextComponent nameField = BasicComponentFactory.createTextField(nameValue, false);

   private final ValueModel idValue = new ConverterValueModel(detailsModel.getModel("id"), idValueConverter);
   private final JLabel idField = BasicComponentFactory.createLabel(idValue);

   private final ValueModel labelValue = detailsModel.getModel("description");
   private Map<String, PropertyAdapter<MultiLingualText.Element>> labelElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element>>();

   private final ValueModel parentIdValue = new ConverterValueModel(detailsModel.getModel("parentId"), intValueConverter);
   private final JTextComponent parentIdField = BasicComponentFactory.createTextField(parentIdValue);

   private final ValueModel parentValueValue = new ConverterValueModel(detailsModel.getModel("parentValue"), intValueConverter);
   private final JTextComponent parentValueField = BasicComponentFactory.createTextField(parentValueValue);

   private final ValueModel categoryValue = detailsModel.getModel("category");
   private SelectionInList<ParameterCategory> selectionInCategory = new SelectionInList<ParameterCategory>( ParameterCategory.values(), categoryValue);
   @SuppressWarnings("unchecked")
   private final JComboBox<ParameterCategory> categoryCombo = BasicComponentFactory.createComboBox(selectionInCategory);

   private final IdentifiableConverter typeConverter = new IdentifiableConverter();
   private final ValueModel typeValue = new ConverterValueModel(detailsModel.getModel("typeId"), typeConverter);
   private SelectionInList<ParameterType> selectionInType = new SelectionInList<ParameterType>(new LinkedList<ParameterType>(), typeValue);
   @SuppressWarnings("unchecked")
   private final JComboBox<ParameterType> typeCombo = BasicComponentFactory.createComboBox(selectionInType);

   private final ValueModel defaultIntValue = detailsModel.getModel("defaultInt");
   private final JTextComponent defaultIntField = BasicComponentFactory.createTextField(new ConverterValueModel(defaultIntValue, intValueConverter), false);

   private final ValueModel defaultEnumValue = new ValueHolder();
   private SelectionInList<ParameterValue> selectionInEnum = new SelectionInList<ParameterValue>(new ParameterValue[0], defaultEnumValue);
   @SuppressWarnings("unchecked")
   private final JComboBox<ParameterValue> defaultEnumField = BasicComponentFactory.createComboBox(selectionInEnum);

   private final ValueModel addressValue = new ConverterValueModel(detailsModel.getModel("address"), hexAddrIntValueConverter);
   private final JTextComponent addressValueField = BasicComponentFactory.createTextField(addressValue);

   private final ValueModel bitOffsetValue = new ConverterValueModel(detailsModel.getModel("bitOffset"), intValueConverter);
   private final JTextComponent bitOffsetValueField = BasicComponentFactory.createTextField(bitOffsetValue);

   private final ValueModel sizeValue = new ConverterValueModel(typeValue, paramTypeSizeConverter);
   private final JLabel sizeField = BasicComponentFactory.createLabel(sizeValue);

   private final ValueModel defaultStringValue = detailsModel.getModel("defaultString");
   private final JTextComponent defaultStringField = BasicComponentFactory.createTextField(defaultStringValue, false);

   private final JLabel defaultNoneField = new JLabel(" ");

   private final ValueModel defaultDoubleValue = detailsModel.getModel("defaultDouble");
   private final JTextComponent defaultDoubleField = BasicComponentFactory.createTextField(defaultDoubleValue, false);

   private final ValueModel visibleValue = detailsModel.getModel("visible");
   private final JCheckBox visibleValueField = BasicComponentFactory.createCheckBox(visibleValue, "");

   private final ValueModel orderValue = detailsModel.getModel("order");
   private final JTextComponent orderValueField = BasicComponentFactory.createTextField(new ConverterValueModel(orderValue, intValueConverter), true);

   private final JButton gotoParentButton = new JButton(ImageCache.getIcon("icons/forward3"));
   private final JButton gotoParamTypeButton = new JButton(ImageCache.getIcon("icons/forward3"));

   /**
    * Create a panel for editing a {@link Parameter}.
    * 
    * @param parent - the parent element
    */
   public ParameterPanel(final ParametersElem parent)
   {
      setLayout(new BorderLayout(0, 2));

      ParameterValueListCellRenderer defaultEnumRenderer = new ParameterValueListCellRenderer(defaultEnumField.getRenderer());
      defaultEnumRenderer.setShowValue(true);
      defaultEnumField.setRenderer(defaultEnumRenderer);

      FormLayout layout = new FormLayout("6dlu, l:p, 3dlu, f:p:g, 2dlu, l:p, 6dlu",
         "8dlu, p, 6dlu, p, 3dlu, p, 12dlu, p, 12dlu, p, " +
         "3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, " +
         "12dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, " +
         "3dlu, " +
         "f:p:g, p, 3dlu");

      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();
      JLabel lbl;

      int row = 2;
      lbl = builder.addLabel(I18n.getMessage("ParameterPanel.caption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getCaptionFont());
      lbl.setOpaque(false);
      idField.setHorizontalAlignment(SwingConstants.RIGHT);
      idField.setOpaque(false);
      builder.add(idField, cc.rc(row, 4));

      row = 4;
      builder.addLabel(I18n.getMessage("ParameterPanel.name"), cc.rc(row, 2));
      builder.add(nameField, cc.rc(row, 4));
      ValidationComponentUtils.setMandatoryBackground(nameField);

      row = 6;
      builder.addLabel(I18n.getMessage("ParameterPanel.category"), cc.rc(row, 2));
      builder.add(categoryCombo, cc.rc(row, 4));
      categoryCombo.setRenderer(new ParameterCategoryComboBoxRenderer(categoryCombo.getRenderer()));

      row = 7;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 8;
      builder.addLabel(I18n.getMessage("ParameterPanel.label") + ':', cc.rc(row, 2));

      row = 9;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 10;
      lbl = builder.addLabel(I18n.getMessage("ParameterPanel.typeCaption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getSubCaptionFont());
      lbl.setOpaque(false);      

      row = 12;
      builder.addLabel(I18n.getMessage("ParameterPanel.type"), cc.rc(row, 2));
      builder.add(typeCombo, cc.rc(row, 4));
      builder.add(gotoParamTypeButton, cc.rc(row, 6));
      ToolBarButton.useToolBarStyle(gotoParamTypeButton);
      gotoParamTypeButton.setToolTipText(I18n.getMessage("ParameterPanel.gotoParamTypeToolTip"));

      row = 14;
      builder.addLabel(I18n.getMessage("ParameterPanel.defaultValue"), cc.rc(row, 2));
      builder.add(defaultIntField, cc.rc(row, 4));
      builder.add(defaultDoubleField, cc.rc(row, 4));
      builder.add(defaultStringField, cc.rc(row, 4));
      builder.add(defaultEnumField, cc.rc(row, 4));
      builder.add(defaultNoneField, cc.rc(row, 4));
      defaultNoneField.setPreferredSize(defaultEnumField.getPreferredSize());

      row = 16;
      builder.addLabel(I18n.getMessage("ParameterPanel.address"), cc.rc(row, 2));
      builder.add(addressValueField, cc.rc(row, 4));

      row = 18;
      builder.addLabel(I18n.getMessage("ParameterPanel.bitOffset"), cc.rc(row, 2));
      builder.add(bitOffsetValueField, cc.rc(row, 4));

      row = 20;
      builder.addLabel(I18n.getMessage("ParameterPanel.size"), cc.rc(row, 2));
      builder.add(sizeField, cc.rc(row, 4));

      row = 21;
      builder.add(new JSeparator(), cc.rcw(row, 2, 3));

      row = 22;
      lbl = builder.addLabel(I18n.getMessage("ParameterPanel.displayCaption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getSubCaptionFont());
      lbl.setOpaque(false);      

      row = 24;
      builder.addLabel(I18n.getMessage("ParameterPanel.parentId"), cc.rc(row, 2));
      builder.add(parentIdField, cc.rc(row, 4));
      builder.add(gotoParentButton, cc.rc(row, 6));
      ToolBarButton.useToolBarStyle(gotoParentButton);
      gotoParentButton.setToolTipText(I18n.getMessage("ParameterPanel.gotoParentToolTip"));

      row = 26;
      builder.addLabel(I18n.getMessage("ParameterPanel.parentValue"), cc.rc(row, 2));
      builder.add(parentValueField, cc.rc(row, 4));

      row = 28;
      builder.addLabel(I18n.getMessage("ParameterPanel.visible"), cc.rc(row, 2));
      builder.add(visibleValueField, cc.rc(row, 4));

      row = 30;
      builder.addLabel(I18n.getMessage("ParameterPanel.order"), cc.rc(row, 2));
      builder.add(orderValueField, cc.rc(row, 4));


      row = 31;
      builder.add(Box.createVerticalGlue(), cc.rcw(row, 2, 3));

      labelElems = MultiLingualTextUtil.createFormElements(builder, 9);
      add(builder.build(), BorderLayout.CENTER);

      validationHandler.setValidatedContainer(builder.getPanel());
      JComponent reportPane = ValidationResultViewFactory
         .createReportList(validationHandler.getValidationResultModel());
      add(reportPane, BorderLayout.SOUTH);

      ProdEdit.getInstance().getProject().getLanguages().addListDataListener(languagesListener);

      selectionInType.addValueChangeListener(new PropertyChangeListener()
      {
         @Override
         public void propertyChange(final PropertyChangeEvent e)
         {
            parameterTypeChanged();
         }
      });

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

      gotoParamTypeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            ParameterType type = (ParameterType) typeValue.getValue();

            JPanel currentPanel = ProdEdit.getInstance().getCurrentTabPanel();
            if (currentPanel instanceof ProductGroupTab)
            {
               ((ProductGroupTab) currentPanel).showParameterType(type);
            }
         }
      });
   }

   /**
    * The parameter type was changed.
    */
   public void parameterTypeChanged()
   {
      final ParameterType paramType = (ParameterType) typeValue.getValue();
      if (paramType == null)
      {
         defaultNoneField.setVisible(true);
         defaultIntField.setVisible(false);
         defaultDoubleField.setVisible(false);
         defaultStringField.setVisible(false);
         defaultEnumField.setVisible(false);
      }
      else
      {
         ParameterAtomicType atomicType = paramType.getAtomicType();

         defaultNoneField.setVisible(atomicType == ParameterAtomicType.NONE);
         defaultIntField.setVisible(atomicType == ParameterAtomicType.SIGNED || atomicType == ParameterAtomicType.UNSIGNED);
         defaultDoubleField.setVisible(atomicType == ParameterAtomicType.FLOAT);
         defaultStringField.setVisible(atomicType == ParameterAtomicType.STRING);

         if (atomicType == ParameterAtomicType.ENUM || atomicType == ParameterAtomicType.LONG_ENUM ||
            atomicType == ParameterAtomicType.FLOAT_ENUM)
         {
            defaultEnumField.setVisible(true);
            selectionInEnum.setList(paramType.getValues());
            defaultEnumValue.setValue(paramType.findValueByInt(parameter.getDefaultInt()));
         }
         else
         {
            defaultEnumField.setVisible(false);
            selectionInEnum.setList(null);
         }
      }
   }

   /**
    * Set the parameter that is edited.
    * 
    * @param parameter - the parameter to set
    */
   public void setParameter(Parameter parameter)
   {
      this.parameter = parameter;
      detailsModel.setBean(parameter);

      if (parameter == null)
         return;

      MultiLingualText label = (MultiLingualText) labelValue.getValue();
      for (Entry<String, PropertyAdapter<MultiLingualText.Element>> entry : labelElems.entrySet())
         entry.getValue().setBean(label.getElement(entry.getKey()));

      parameterTypeChanged();
   }

   /**
    * Set the application program.
    * 
    * @param program - the program to set
    */
   public void setProgram(ApplicationProgram program)
   {
      this.program = program;

      if (program == null)
      {
         typeConverter.setList(null);
         selectionInType.setList(new LinkedList<ParameterType>());
      }
      else
      {
         typeConverter.setList(program.getParameterTypes());
         selectionInType.setList(program.getParameterTypes());
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      detailsModel.release();
      selectionInEnum.release();
      ProdEdit.getInstance().getProject().getLanguages().removeListDataListener(languagesListener);
   }

   /**
    * Update or recreate the details panel.
    */
   protected void updateDetailsPanel()
   {
      // TODO recreate the text elements when the language-list changes
      // setupDetails();

      Parameter currentParam = parameter;
      setParameter(null);
      setParameter(currentParam);
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
   private class DetailsFormValidator implements Validator<Parameter>
   {
      @Override
      public ValidationResult validate(Parameter parameter)
      {
         ValidationResult result = new ValidationResult();

         return result;
      }
   }
}

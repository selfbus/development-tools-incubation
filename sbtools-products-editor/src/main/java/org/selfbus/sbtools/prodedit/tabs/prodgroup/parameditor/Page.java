package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameditor;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * A tab page of the parameter editor.
 * 
 * @see ParameterEditor
 */
public class Page extends JPanel
{
   private static final long serialVersionUID = -1L;

   private final Set<ChangeListener> changeListeners = new CopyOnWriteArraySet<ChangeListener>();
   private final Vector<DeviceParameter> pageDevParams = new Vector<DeviceParameter>();
   private final Vector<DeviceParameter> devParams = new Vector<DeviceParameter>(8000);
   private ApplicationProgram program;
   private final int displayOrder;

   private final JLabel title = new JLabel("<title>");
   private final JPanel contents = new JPanel(new GridBagLayout());

   /**
    * Create a tab page for the parameter editor.
    *
    * @param program - the application program of the device
    * @param devParam - the device-parameter of the parameter which represents
    *           the page.
    */
   public Page(ApplicationProgram program, DeviceParameter devParam)
   {
      super(new GridBagLayout());
      this.program = program;

      pageDevParams.add(devParam);
      this.displayOrder = devParam.getParameter().getOrder();

      // setBackground(new Color((pageParam.getId() & 15) << 4,
      // (pageParam.getId() >> 4 & 15) << 4,
      // (pageParam.getId() >> 8 & 15) << 4));

      final Insets insets = new Insets(8, 8, 8, 8);
      final Insets separatorInsets = new Insets(0, 8, 0, 8);

      title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(title.getFont().getSize() * 1.1f));
      add(title, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            insets, 0, 0));

      add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, separatorInsets, 0, 0));

      add(contents, new GridBagConstraints(0, 2, 1, 1, 1, 10, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            insets, 0, 0));

      add(new JPanel(), new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            insets, 0, 0));

      updateName();
   }

   /**
    * @return the display order of the page.
    */
   public int getDisplayOrder()
   {
      return displayOrder;
   }

   /**
    * @return True if the page is visible.
    */
   /*
    * WARNING Do NOT rename this method to isVisible(), it will break the
    * underlying Swing component!
    */
   public boolean isPageVisible()
   {
      for (final DeviceParameter devParam: pageDevParams)
      {
         if (devParam.isVisible())
            return true;
      }

      return false;
   }

   /**
    * @return The first visible page device-parameter, or null if none is visible.
    */
   public DeviceParameter getVisibleDevParameter()
   {
      for (final DeviceParameter devParam: pageDevParams)
      {
         if (devParam.isVisible())
            return devParam;
      }

      return null;
   }

   /**
    * Add a device parameter to the page.
    * 
    * @param devParam - the device parameter to add.
    */
   public void addParam(DeviceParameter devParam)
   {
      devParams.add(devParam);
   }

   /**
    * Add a page device parameter.
    * 
    * @param devParam - the page device parameter to add.
    */
   public void addPageParam(DeviceParameter devParam)
   {
      pageDevParams.add(devParam);
   }

   /**
    * Update the name of the page.
    */
   public void updateName()
   {
      final Parameter pageParam = pageDevParams.get(0).getParameter();

      String name = pageParam.getDescription().getDefaultText();
      if (name == null || name.isEmpty())
         name = pageParam.getName();

      setName(name);
      title.setText(name);
   }

   /**
    * Add a listener that gets called when a parameter value is changed.
    * 
    * @param listener the listener to be added.
    */
   public void addChangeListener(ChangeListener listener)
   {
      changeListeners.add(listener);
   }

   /**
    * Remove a change listener.
    * 
    * @param listener the listener to be removed.
    */
   public void removeChangeListener(ChangeListener listener)
   {
      changeListeners.remove(listener);
   }

   /**
    * Update the contents of the page. Called when the page gets visible.
    */
   public void updateContents()
   {
//      Logger.getLogger(getClass()).debug(
//            "Page " + pageDevParam.getParameter() + " (" + pageDevParam.getParameter().getDescription() + ") - updateContents");

      updateName();
      contents.removeAll();

      int gridRow = -1;
      for (final DeviceParameter devParam : devParams)
      {
//         Logger.getLogger(getClass()).debug("   " + devParam.getParameter() + " visible " + devParam.isVisible());
         if (devParam.isVisible())
            createParamComponent(devParam, ++gridRow);
      }
   }

   /**
    * Create a component that can be used to edit the value of the
    * parameter-data <code>data</code>.
    */
   public void createParamComponent(final DeviceParameter devParam, int gridRow)
   {
      final Parameter param = devParam.getParameter();
      final ParameterType paramType = program.getParameterType(param.getTypeId());
      final ParameterAtomicType atomicType = paramType.getAtomicType();
      Component valueComp = null;

      if (atomicType == ParameterAtomicType.NONE)
      {
         createParamNoneComponent(devParam, gridRow);
      }
      else if (atomicType == ParameterAtomicType.STRING)
      {
         valueComp = createParamStringComponent(devParam, gridRow);
      }
      else if (atomicType == ParameterAtomicType.SIGNED || atomicType == ParameterAtomicType.UNSIGNED)
      {
         valueComp = createParamNumberComponent(devParam, gridRow);
      }
      else if (atomicType == ParameterAtomicType.ENUM || atomicType == ParameterAtomicType.LONG_ENUM)
      {
         valueComp = createParamEnumComponent(devParam, gridRow);
      }
      else
      {
         createParamLabel(param, gridRow, 1);
         contentAddComponent(new JLabel("Unsupported atomic type " + atomicType), gridRow);
      }

      if (valueComp != null && param.getCategory() != ParameterCategory.VALUE)
         valueComp.setEnabled(false);

      // final Integer addr = param.getAddress();
      // if (valueComp != null && (addr == null || addr == 0) &&
      // param.getBitOffset() == 0)
      // valueComp.setEnabled(false);
   }

   /**
    * Create a {@link JComponent} for a parameter of the atomic type
    * {@link ParameterAtomicType#ENUM}.
    * 
    * @param devParam the parameter-data to process.
    * @param gridRow the row of the contents grid in which the component(s) are
    *           to be added.
    */
   public Component createParamEnumComponent(final DeviceParameter devParam, int gridRow)
   {
      final Parameter param = devParam.getParameter();
      final ArrayListModel<ParameterValue> valuesList = program.getParameterType(param.getTypeId()).getValues(); 
      final ParameterValue[] values = new ParameterValue[valuesList.size()];
      valuesList.toArray(values);

      createParamLabel(param, gridRow, 1);

      if (values.length <= 1)
      {
         if (values.length == 1)
         {
            final JLabel lbl = new JLabel(values[0].getDisplayedValue());
            lbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            contentAddComponent(lbl, gridRow);
            return lbl;
         }
         return null;
      }

      Arrays.sort(values, new Comparator<ParameterValue>()
      {
         @Override
         public int compare(ParameterValue a, ParameterValue b)
         {
            return a.getOrder() - b.getOrder();
         }
      });

      final JComboBox<Object> combo = new JComboBox<Object>(new DefaultComboBoxModel<Object>(values));
      combo.setName("param-" + param.getId());

      @SuppressWarnings("unchecked")
      final ListCellRenderer<Object> origRenderer = (ListCellRenderer<Object>) combo.getRenderer();

      combo.setRenderer(new ListCellRenderer<Object>()
      {
         @Override
         public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
               boolean cellHasFocus)
         {
            if (value instanceof ParameterValue)
               value = ((ParameterValue) value).getDisplayedValue();

            return origRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         }
      });

      final int value = param.getCategory() == ParameterCategory.VALUE ? devParam.getIntValue() : param.getDefaultInt();
      for (final ParameterValue val : values)
      {
         if (val.getIntValue() == value)
         {
            combo.setSelectedItem(val);
            break;
         }
      }

      combo.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            final ParameterValue val = (ParameterValue) combo.getSelectedItem();
            devParam.setValue(val == null ? null : val.getIntValue());
            fireStateChanged(devParam);
         }
      });

      contentAddComponent(combo, gridRow);
      return combo;
   }

   /**
    * Create a {@link JComponent} for a parameter of the atomic type
    * {@link ParameterAtomicType#SIGNED} or {@link ParameterAtomicType#UNSIGNED}
    * .
    * 
    * @param devParam - the parameter-data to process.
    * @param gridRow - the row of the contents grid in which the component(s)
    *           are to be added.
    */
   public Component createParamNumberComponent(final DeviceParameter devParam, int gridRow)
   {
      final Parameter param = devParam.getParameter();
      final ParameterType paramType = program.getParameterType(param.getTypeId());

      final int min = paramType.getMinValue();
      final int max = paramType.getMaxValue();

      int value = devParam.getIntValue();
      if (value < min)
         value = min;
      else if (value > max)
         value = max;

      final SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, 1);
      final JSpinner spinner = new JSpinner(model);
      spinner.setName("param-" + param.getId());

      model.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent e)
         {
            devParam.setValue(model.getValue());
            fireStateChanged(devParam);
         }
      });

      createParamLabel(param, gridRow, 1);
      contentAddComponent(spinner, gridRow);
      return spinner;
   }

   /**
    * Create a {@link JComponent} for a parameter of the atomic type
    * {@link ParameterAtomicType#STRING}.
    * 
    * @param devParam - the parameter-data to process.
    * @param gridRow - the row of the contents grid in which the component(s)
    *           are to be added.
    */
   public Component createParamStringComponent(final DeviceParameter devParam, int gridRow)
   {
      final Parameter param = devParam.getParameter();
      createParamLabel(param, gridRow, 1);

      final JTextArea input = new JTextArea();
      input.setText((String) devParam.getValue());
      input.setName("param-" + param.getId());

      contentAddComponent(input, gridRow);

      input.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void removeUpdate(DocumentEvent e)
         {
            devParam.setValue(input.getText());
            fireStateChanged(devParam);
         }

         @Override
         public void insertUpdate(DocumentEvent e)
         {
            devParam.setValue(input.getText());
            fireStateChanged(devParam);
         }

         @Override
         public void changedUpdate(DocumentEvent e)
         {
         }
      });

      return input;
   }

   /**
    * Create a {@link JComponent} for a parameter of the atomic type
    * {@link ParameterAtomicType#NONE}.
    * 
    * @param data - the parameter-data to process.
    * @param gridRow - the row of the contents grid in which the component(s)
    *           are to be added.
    */
   public void createParamNoneComponent(final DeviceParameter data, int gridRow)
   {
      final Parameter param = data.getParameter();
      final JLabel lbl = createParamLabel(param, gridRow, 2);
      lbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
   }

   /**
    * Create a label for the parameter. The label is added to the contents grid
    * in column zero, spanning one or more columns.
    * 
    * @param param - the parameter whose label shall be used.
    * @param gridRow - the row of the contents grid in which the label is
    *           placed.
    * @param gridWidth - the number of grid columns the label shall use (usually
    *           1 or 2).
    * 
    * @return The created label.
    */
   public JLabel createParamLabel(final Parameter param, int gridRow, int gridWidth)
   {
      final String str = "<html>" + param.getDescription().getDefaultText().replace("\\r", "").replace("\\n", "<br>") + "</html>";
      final JLabel lbl = new JLabel(str);
      lbl.setToolTipText("Debug: parameter-id is " + param.getId());
      lbl.setName("param-" + param.getId());

      contents.add(lbl, new GridBagConstraints(0, gridRow, gridWidth, 1, 1, 0, GridBagConstraints.WEST,
            GridBagConstraints.NONE, new Insets(2, 0, 2, 4), 0, 0));

      return lbl;
   }

   /**
    * Add the component to the grid in column zero, row <code>gridRow</code>,
    * using suitable grid bag constraints.
    * 
    * @param comp - the component to be added.
    * @param gridRow - the row of the contents grid in which the component is
    *           added.
    */
   public void contentAddComponent(final JComponent comp, int gridRow)
   {
      contents.add(comp, new GridBagConstraints(1, gridRow, 1, 1, 4, 0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(6, 4, 6, 4), 0, 0));
   }

   /**
    * Inform all change listeners that a device parameter was changed.
    * 
    * @param devParam - the device parameter that was changed.
    */
   public void fireStateChanged(final DeviceParameter devParam)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            final ChangeEvent e = new ChangeEvent(devParam);

            for (ChangeListener listener : changeListeners)
               listener.stateChanged(e);
         }
      });
   }
}

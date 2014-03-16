package org.selfbus.sbtools.prodedit.renderer;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;

/**
 * A combo box renderer proxy for {@link ParameterAtomicType}s that uses the original renderer for
 * rendering.
 */
public class ParameterAtomicTypeComboBoxRenderer implements ListCellRenderer<ParameterAtomicType>, Serializable
{
   private static final long serialVersionUID = 2673987195780972272L;

   @SuppressWarnings("rawtypes")
   private final ListCellRenderer originalRenderer;

   /**
    * Create a combo box renderer proxy for {@link ParameterAtomicType}s.
    * 
    * @param originalRenderer - the original renderer of the combo box.
    */
   public ParameterAtomicTypeComboBoxRenderer(ListCellRenderer<?> originalRenderer)
   {
      this.originalRenderer = originalRenderer;
   }

   @SuppressWarnings("unchecked")
   public Component getListCellRendererComponent(JList<? extends ParameterAtomicType> list, ParameterAtomicType value,
      int index, boolean isSelected, boolean cellHasFocus)
   {
      String label = value == null ? null : value.getLabel();
      return originalRenderer.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
   }
}

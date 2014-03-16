package org.selfbus.sbtools.prodedit.renderer;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;

/**
 * A combo box renderer proxy for {@link ParameterCategory} that uses the original renderer for
 * rendering.
 */
public class ParameterCategoryComboBoxRenderer implements ListCellRenderer<ParameterCategory>, Serializable
{
   private static final long serialVersionUID = -8933913700858315456L;

   @SuppressWarnings("rawtypes")
   private final ListCellRenderer originalRenderer;

   /**
    * Create a combo box renderer proxy for {@link ParameterAtomicType}s.
    * 
    * @param originalRenderer - the original renderer of the combo box.
    */
   public ParameterCategoryComboBoxRenderer(ListCellRenderer<?> originalRenderer)
   {
      this.originalRenderer = originalRenderer;
   }

   @SuppressWarnings("unchecked")
   public Component getListCellRendererComponent(JList<? extends ParameterCategory> list, ParameterCategory value,
      int index, boolean isSelected, boolean cellHasFocus)
   {
      String label = value == null ? null : value.getLabel();
      return originalRenderer.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
   }
}

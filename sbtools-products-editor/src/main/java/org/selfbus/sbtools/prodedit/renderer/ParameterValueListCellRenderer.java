package org.selfbus.sbtools.prodedit.renderer;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;

/**
 * A list cell renderer proxy for {@link ParameterValue}s that uses the original renderer for
 * rendering.
 */
public class ParameterValueListCellRenderer implements ListCellRenderer<ParameterValue>, Serializable
{
   private static final long serialVersionUID = 81744871957809722L;

   private boolean showValue;

   @SuppressWarnings("rawtypes")
   private final ListCellRenderer originalRenderer;

   /**
    * Create a list cell renderer proxy for {@link ParameterValue}s.
    * 
    * @param originalRenderer - the original renderer of the list cell.
    */
   public ParameterValueListCellRenderer(ListCellRenderer<?> originalRenderer)
   {
      this.originalRenderer = originalRenderer;
   }

   /**
    * Show / hide the parameter value.
    * 
    * @param enable - enable the display of the value
    */
   public void setShowValue(boolean enable)
   {
      this.showValue = enable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   public Component getListCellRendererComponent(JList<? extends ParameterValue> list, ParameterValue value, int index,
      boolean isSelected, boolean cellHasFocus)
   {
      String label;
      if (value != null)
      {
         label = value.getLabel().getDefaultText();
         if (showValue) label += " [" + value.getIntValue() + "]";
      }
      else label = "";
 
      return originalRenderer.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
   }
}

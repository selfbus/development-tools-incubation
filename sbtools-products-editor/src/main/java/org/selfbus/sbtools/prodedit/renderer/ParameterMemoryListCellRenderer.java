package org.selfbus.sbtools.prodedit.renderer;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.utils.ParamUtils;

/**
 * A list cell renderer proxy for {@link Parameter}s and {@link ComObject}s that uses
 * the original renderer for rendering.
 */
public class ParameterMemoryListCellRenderer implements ListCellRenderer<AbstractParameterNode>, Serializable
{
   private static final long serialVersionUID = 3418062447420387527L;

   @SuppressWarnings("rawtypes")
   private final ListCellRenderer originalRenderer;

   /**
    * Create a list cell renderer proxy for {@link Parameter}s and {@link ComObject}s.
    * 
    * @param originalRenderer - the original renderer of the list cell.
    */
   public ParameterMemoryListCellRenderer(ListCellRenderer<?> originalRenderer)
   {
      this.originalRenderer = originalRenderer;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   public Component getListCellRendererComponent(JList<? extends AbstractParameterNode> list,
      AbstractParameterNode value, int index, boolean isSelected, boolean cellHasFocus)
   {
      String label;
      if (value instanceof Parameter)
      {
         Parameter param = (Parameter) value;

         label = String.format("$%1$04X P: %3$s [#%2$d ID:%4$d]",
            new Object[] { param.getAddress(), param.getNumber(),
                           ParamUtils.getLabel(param),
                           param.getId() });
      }
      else if (value instanceof CommunicationObject)
      {
         CommunicationObject comObj = (CommunicationObject) value;

         label = String.format("$%1$04X C: %3$s [#%2$d ID:%4$d]",
            new Object[] { comObj.getAddress(), comObj.getNumber(),
                           ParamUtils.getLabel(comObj),
                           comObj.getId() });
      }
      else label = "";
 
      return originalRenderer.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
   }
}

package org.selfbus.sbtools.prodedit.renderer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.apache.commons.lang3.Validate;

/**
 * A list cell renderer that gets an ID and displays the corresponding label.
 */
@SuppressWarnings("rawtypes")
public class MappingListCellRenderer extends DefaultListCellRenderer
{
   private static final long serialVersionUID = -4921442875788791901L;

   private Map<Object, String> labelsMap;

   /**
    * Create a list cell renderer that gets an ID and displays the corresponding label.
    * 
    * @see #setLabelsMap(Map)
    */
   public MappingListCellRenderer()
   {
      this.labelsMap = new HashMap<Object, String>();
   }

   /**
    * Create a list cell renderer that gets an ID and displays the corresponding label.
    * 
    * @param labelsMap - mapping from IDs to labels
    */
   public MappingListCellRenderer(Map<Object, String> labelsMap)
   {
      this.labelsMap = labelsMap;
   }

   /**
    * Set the map for mapping IDs to labels
    * 
    * @param map - the map to set
    */
   public void setLabelsMap(Map<Object, String> labelsMap)
   {
      Validate.notNull(labelsMap);
      this.labelsMap = labelsMap;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
      boolean cellHasFocus)
   {
      Object label = labelsMap.get(value);
      if (label == null) label = value;

      return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
   }
}

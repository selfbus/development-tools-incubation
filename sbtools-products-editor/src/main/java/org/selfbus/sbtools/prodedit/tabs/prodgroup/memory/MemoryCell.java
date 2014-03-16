package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.utils.ParamUtils;

/**
 * A memory cell of the {@link MemoryTableModel}.
 */
public class MemoryCell
{
   private int value = -1;
   private boolean modified;
   private MemoryRange range;
   private String label;
   private final Map<String, Object> objs = new TreeMap<String, Object>();

   /**
    * Create an empty memory cell.
    */
   public MemoryCell()
   {
   }

   /**
    * @return The value of the memory cell (0..255), or -1 if the value was
    *         never set.
    */
   public int getValue()
   {
      return value;
   }

   /**
    * Set the value of the memory cell
    * 
    * @param value - the value to set (0..255)
    */
   public void setValue(int value)
   {
      if (value < 0 || value > 255)
         throw new IllegalArgumentException("value is not in the range 0..255: " + value);

      this.value = value;
      modified = true;
   }

   /**
    * @return The optional label of the memory cell. May be null.
    */
   public String getLabel()
   {
      if (label == null)
      {
         Set<String> labels = new TreeSet<String>();
         for (Object obj : objs.values())
         {
            if (obj instanceof AbstractParameterNode)
               labels.add(ParamUtils.getLabel((AbstractParameterNode) obj));
            else labels.add(obj.toString());
         }

         label = "";
         for (String str : labels)
            label += str + "<br>";
      }
      return label;
   }

   /**
    * Set the optional label of the memory cell.
    * 
    * @param label - the label to set. May be null.
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

   /**
    * @return The objects that use the memory cell.
    */
   public Collection<Object> getObjects()
   {
      return objs.values();
   }

   /**
    * Add an object to the memory cell.
    * 
    * @param obj - the object to add, may be null.
    * @param order - an integer key for sorting.
    */
   public void addObject(Object obj, String order)
   {
      label = null;
      objs.put(order, obj);
   }

   /**
    * @return The memory range that the memory cell belongs to.
    */
   public MemoryRange getRange()
   {
      return range;
   }

   /**
    * Set the memory range that the memory cell belongs to.
    * 
    * @param range - the memory range to set.
    */
   public void setRange(MemoryRange range)
   {
      this.range = range;
   }

   /**
    * @return True if the memory cell was modified.
    */
   public boolean isModified()
   {
      return modified;
   }

   /**
    * Set the modified flag.
    * 
    * @param modified - the modified flag to set.
    */
   public void setModified(boolean modified)
   {
      this.modified = modified;
   }

   /**
    * Clear the contents of the memory cell.
    */
   public void clear()
   {
      value = -1;
      label = null;
      modified = false;
      objs.clear();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      if (value == -1)
         return "";
      return String.format("%1$02X", new Object[] { value });
   }
}

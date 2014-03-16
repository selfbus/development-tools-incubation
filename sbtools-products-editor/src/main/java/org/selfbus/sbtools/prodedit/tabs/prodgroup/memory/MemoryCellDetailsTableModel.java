package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.utils.ParamUtils;

/**
 * A table model for the details of a memory cell.
 */
public class MemoryCellDetailsTableModel extends AbstractTableModel
{
   private static final long serialVersionUID = -339465419801960088L;

   private static final Icon RANGE_ICON = ImageCache.getIcon("icons/memory_range");

   public static final int ICON_COL = 0;
   public static final int NAME_COL = 1;
   public static final int MEMORY_COL = 2;
   public static final int ID_COL = 3;

   private static final int OBJ_COL = 4;  // this column is not visible
   private static final int NUM_COLS = 4; // yes, the same as OBJ_COL

   private final Vector<Object[]> rows = new Vector<Object[]>();

   @Override
   public int getRowCount()
   {
      return rows.size();
   }

   @Override
   public int getColumnCount()
   {
      return NUM_COLS;
   }

   @Override
   public Class<?> getColumnClass(int columnIndex)
   {
      if (columnIndex == ICON_COL)
         return ImageIcon.class;
      return String.class;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      return rows.get(rowIndex)[columnIndex];
   }

   /**
    * Get the object in the specified row.
    *
    * @param rowIndex - the row to get the object for
    * @return The object
    */
   public Object getValueAt(int rowIndex)
   {
      return rows.get(rowIndex)[OBJ_COL];
   }

   /**
    * Add an object to the end of the table model.
    * 
    * @param obj - the object to add.
    */
   public void add(Object obj)
   {
      final Object[] row = new Object[NUM_COLS + 1];

      row[OBJ_COL] = obj;
      
      if (obj instanceof AbstractParameterNode)
      {
         AbstractParameterNode node = (AbstractParameterNode) obj;
         
         row[ICON_COL] = ParamUtils.getIcon(node);
         row[NAME_COL] = ParamUtils.getLabel(node);
         row[MEMORY_COL] = ParamUtils.getSizeOffsetLabel(node);

         row[ID_COL] = "#" + node.getNumber() + ", " +
            I18n.formatMessage("MemoryCellDetailsTableModel.id", Integer.toString(node.getId()));
      }
      else if (obj instanceof MemoryRange)
      {
         MemoryRange range = (MemoryRange) obj;

         row[ICON_COL] = RANGE_ICON;
         row[NAME_COL] = range.getName();
         row[MEMORY_COL] = I18n.formatMessage("MemoryCellDetailsTableModel.range",
            String.format("%1$04x", range.getStart()), Integer.toString(range.getSize()));
      }
      else
      {
         row[NAME_COL] = obj.toString();
         row[ID_COL] = "";
      }

      rows.add(row);
      fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
   }

   /**
    * Clear the table model.
    */
   public void clear()
   {
      rows.clear();
      fireTableDataChanged();
   }
}

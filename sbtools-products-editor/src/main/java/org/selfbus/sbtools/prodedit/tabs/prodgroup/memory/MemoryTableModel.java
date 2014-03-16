package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * A table model that represents a memory range. Each memory cell can have a
 * value, a name, and an area type.
 */
public class MemoryTableModel implements TableModel
{
   private final int startAddr;
   private final Vector<MemoryCell> data;
   private final Set<TableModelListener> listeners = new CopyOnWriteArraySet<TableModelListener>();

   /**
    * Create a memory table model.
    *
    * @param startAddr - the start address of the memory range.
    * @param size - the size of the shown memory range, in bytes.
    */
   public MemoryTableModel(int startAddr, int size)
   {
      this.startAddr = startAddr;
      this.data = new Vector<MemoryCell>(size, 1024);

      setSize(size);
   }

   /**
    * @return The start address of the memory range.
    */
   public int getStartAddr()
   {
      return startAddr;
   }

   /**
    * @return The size of the memory range.
    */
   public int getSize()
   {
      return data.size();
   }

   /**
    * Set the size of the memory range.
    *
    * @param newSize - the new size.
    */
   public void setSize(int newSize)
   {
      int oldSize = data.size();
      data.setSize(newSize);

      for (int i = oldSize; i < newSize; ++i)
         data.add(new MemoryCell());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getRowCount()
   {
      return data.size() >> 4;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getColumnCount()
   {
      return 17;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getColumnName(int columnIndex)
   {
      if (columnIndex == 0)
         return ""; //I18n.getMessage("MemoryTableModel.Address");
      return String.format("+%1$02X", new Object[] { columnIndex - 1 });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Class<?> getColumnClass(int columnIndex)
   {
      return columnIndex == 0 ? String.class : MemoryCell.class;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex)
   {
      return false;
   }

   /**
    * Returns the memory cell value at rowIndex / columnIndex.
    *
    * @param rowIndex - the index of the row
    * @param columnIndex - the index of the column (1..16)
    *
    * @return the cell value.
    */
   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      if (columnIndex == 0)
         return String.format("$%1$04X", new Object[] { startAddr + (rowIndex << 4) });

      int idx = (rowIndex << 4) + columnIndex - 1;
      MemoryCell cell = data.get(idx);
      if (cell == null)
      {
         cell = new MemoryCell();
         data.set(idx, cell);
      }
      return cell;
   }

   /**
    * @return the memory cell at a specific address.
    *
    * @param addr - the requested address
    * @return The memory cell at the address.
    */
   public MemoryCell getValueAt(int addr)
   {
      MemoryCell cell = data.get(addr - startAddr);
      if (cell == null)
      {
         cell = new MemoryCell();
         data.set(addr - startAddr, cell);
      }
      return cell;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setValueAt(Object aValue, int rowIndex, int columnIndex)
   {
      if (!(aValue instanceof MemoryCell))
         throw new IllegalArgumentException("object must be of type MemoryCell");

      if (columnIndex == 0)
         throw new IllegalArgumentException("cannot set data in column 0");

      data.set((rowIndex << 4) + columnIndex - 1, (MemoryCell) aValue);
      fireTableChanged(rowIndex);
   }

   /**
    * Clear all memory cells.
    */
   public void clear()
   {
      MemoryCell cell;
      for (int i = data.size() - 1; i >= 0; --i)
      {
         cell = data.get(i);
         if (cell != null)
            cell.clear();
      }
   }

   /**
    * Reset the modified flag for all memory cells.
    */
   public void unsetModified()
   {
      for (int i = data.size() - 1; i >= 0; --i)
         data.get(i).setModified(false);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addTableModelListener(TableModelListener l)
   {
      listeners.add(l);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void removeTableModelListener(TableModelListener l)
   {
      listeners.remove(l);
   }

   /**
    * Notify the listeners that the table has changed in a specific row.
    *
    * @param row - the row that has changed.
    */
   public void fireTableChanged(int row)
   {
      fireTableChanged(row, row);
   }

   /**
    * Notify the listeners that the table has changed in a specific row.
    *
    * @param firstRow - the first row that has changed.
    * @param lastRow - the last row that has changed.
    */
   public void fireTableChanged(int firstRow, int lastRow)
   {
      if (listeners.isEmpty())
         return;

      final TableModelEvent e = new TableModelEvent(this, firstRow, lastRow);

      for (TableModelListener listener : listeners)
         listener.tableChanged(e);
   }
}

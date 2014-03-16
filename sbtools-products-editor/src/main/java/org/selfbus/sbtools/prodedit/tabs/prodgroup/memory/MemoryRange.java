package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.awt.Color;

/**
 * A memory range for the {@link MemoryTableModel}.
 */
public final class MemoryRange implements Comparable<MemoryRange>
{
   private final int start, size;
   private final String name;
   private final Color backgroundColor;

   /**
    * Create a memory range.
    * 
    * @param start - the start address of the range.
    * @param size - the size of the range.
    * @param name - the name of the range.
    * @param backgroundColor - the background color for memory cells.
    */
   public MemoryRange(int start, int size, String name, Color backgroundColor)
   {
      this.start = start;
      this.size = size;
      this.name = name;
      this.backgroundColor = backgroundColor;
   }

   /**
    * @return The start address of the memory range.
    */
   public int getStart()
   {
      return start;
   }

   /**
    * @return The size of the memory range.
    */
   public int getSize()
   {
      return size;
   }

   /**
    * @return The name of the memory range.
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return The background color for memory cells.
    */
   public Color getBackground()
   {
      return backgroundColor;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(MemoryRange o)
   {
      if (o.start == start)
         return size - o.size;
      return o.start - start;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof MemoryRange))
         return false;
      final MemoryRange oo = (MemoryRange) o;
      return start == oo.start && size == oo.size;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return (start << 8) | size;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name + " start " + start + " size " + size;
   }
}

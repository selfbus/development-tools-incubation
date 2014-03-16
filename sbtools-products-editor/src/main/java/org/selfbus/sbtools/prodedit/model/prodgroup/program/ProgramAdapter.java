package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import org.selfbus.sbtools.common.address.Address;
import org.selfbus.sbtools.prodedit.model.global.Mask;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * Interface for adapters that encapsulate accessing an application program and mask for different
 * BCU types.
 */
public interface ProgramAdapter
{
   /**
    * Apply the changes to the application program. Call this method e.g. when a table was changed.
    */
   public void apply();

   /**
    * Update the tables. Call this method when application program or mask were changed.
    */
   public void update();

   /**
    * @return The program
    */
   public ApplicationProgram getProgram();

   /**
    * @return The mask
    */
   public Mask getMask();

   /**
    * @return The address table. The first entry is the device's physical address. The other entries
    *         are group addresses.
    */
   public ArrayListModel<Address> getAddressTab();

   /**
    * @return The communication objects table.
    */
   public ArrayListModel<CommsEntry> getCommsTab();

   /**
    * @return The address of the communication objects' RAM flags table.
    */
   public int getRamFlagTabAddr();

   /**
    * Set the pointer to the RAM flags table of the communication objects. Call {@link #apply()}
    * to apply the change.
    * 
    * @param ptr - the pointer to set
    */
   public void setRamFlagTabAddr(int ptr);

   /**
    * @return The association table that connects group addresses and communication objects.
    */
   public ArrayListModel<AssocEntry> getAssocTab();

   /**
    * Get the maximum available space for the tables (address, comms, assoc). The tables
    * need this space:
    * <ul>
    * <li> The address table's size is number of group addresses * 2 + 3
    * <li> The communication objects table's size is number of com-objects * 3 + 2
    * <li> The association table's size is number of associations * 2 + 1
    * </ul>
    * 
    * @return The maximum size of all tables in bytes.
    */
   public int getMaxTablesSize();
}

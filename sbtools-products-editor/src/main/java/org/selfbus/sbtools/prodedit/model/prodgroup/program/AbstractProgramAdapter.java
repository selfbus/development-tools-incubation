package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import org.selfbus.sbtools.common.address.Address;
import org.selfbus.sbtools.common.address.GroupAddress;
import org.selfbus.sbtools.common.address.PhysicalAddress;
import org.selfbus.sbtools.prodedit.model.global.Mask;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * An adapter that encapsulates accessing an application program and mask for different BCU types.
 */
public abstract class AbstractProgramAdapter implements ProgramAdapter
{
   protected Mask mask;
   protected ApplicationProgram program;
   protected ArrayListModel<Address> addressTab;
   protected ArrayListModel<CommsEntry> commsTab;
   protected ArrayListModel<AssocEntry> assocTab;

   /**
    * The address of the communication objects' RAM flags table.
    */
   protected Integer ramFlagTabAddr;

   /**
    * Create an adapter that encapsulates accessing an application program and mask for different
    * BCU types.
    */
   public AbstractProgramAdapter()
   {
      this(null, null);
   }

   /**
    * Create an adapter that encapsulates accessing an application program and mask for different
    * BCU types.
    * 
    * @param program - the application program
    * @param mask - the mask
    */
   public AbstractProgramAdapter(ApplicationProgram program, Mask mask)
   {
      this.program = program;
      this.mask = mask;
   }

   @Override
   public ApplicationProgram getProgram()
   {
      return program;
   }

   /**
    * @param program the program to set
    */
   public void setProgram(ApplicationProgram program)
   {
      this.program = program;
   }

   @Override
   public Mask getMask()
   {
      return mask;
   }

   /**
    * @param mask the mask to set
    */
   public void setMask(Mask mask)
   {
      this.mask = mask;
   }

   /**
    * @return The data of the address table.
    */
   protected abstract byte[] getAddressTabData();

   /**
    * @return The data of the communications table.
    */
   protected abstract byte[] getCommsTabData();

   /**
    * @return The data of the association table.
    */
   protected abstract byte[] getAssocTabData();

   @Override
   public synchronized int getRamFlagTabAddr()
   {
      if (commsTab == null)
         getCommsTab();

      return ramFlagTabAddr == null ? 0 : ramFlagTabAddr;
   }

   @Override
   public void setRamFlagTabAddr(int addr)
   {
      if (ramFlagTabAddr < 0)
         getCommsTab(); // loading the comms-tab overwrites the ramFlagTabPtr

      this.ramFlagTabAddr = addr;
   }

   @Override
   public synchronized ArrayListModel<Address> getAddressTab()
   {
      if (addressTab == null)
      {
         addressTab = new ArrayListModel<Address>(256);
         byte[] data = getAddressTabData();

         for (int idx = 1; idx < data.length - 1; idx += 2)
         {
            if (idx < 2)
               addressTab.add(new PhysicalAddress(data[idx] & 255, data[idx + 1] & 255));
            else addressTab.add(new GroupAddress(data[idx] & 255, data[idx + 1] & 255));
         }
      }

      return addressTab;
   }

   @Override
   public synchronized ArrayListModel<CommsEntry> getCommsTab()
   {
      if (commsTab == null)
      {
         commsTab = new ArrayListModel<CommsEntry>(256);
         byte[] data = getCommsTabData();

         if (data == null || data.length == 0)
            return commsTab;

         ramFlagTabAddr = data[1] & 255;

         for (int idx = 2; idx < data.length - 2; idx += 3)
         {
            CommsEntry e = new CommsEntry(data[idx] & 255, data[idx + 1] & 255, data[idx + 2] & 255);
            fixCommsEntryAddr(e);
            commsTab.add(e);
         }
      }

      return commsTab;
   }

   /**
    * Correct the address of the com-object value pointer, if applicable.
    * 
    * @param entry - the comms-entry to process.
    */
   protected abstract void fixCommsEntryAddr(CommsEntry entry);

   @Override
   public synchronized ArrayListModel<AssocEntry> getAssocTab()
   {
      if (assocTab == null)
      {
         assocTab = new ArrayListModel<AssocEntry>(256);
         byte[] data = getAssocTabData();

         for (int idx = 1; idx < data.length - 1; idx += 2)
         {
            assocTab.add(new AssocEntry(data[idx] & 255, data[idx + 1] & 255));
         }
      }

      return assocTab;
   }

   @Override
   public synchronized void update()
   {
      ramFlagTabAddr = null;
      addressTab = null;
      commsTab = null;
      assocTab = null;
   }
}

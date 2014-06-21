package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.model.global.Mask;

/**
 * A program adapter for BCU2 style programs.
 */
public class Bcu2ProgramAdapter extends AbstractProgramAdapter
{
   /**
    * Create a program adapter for BCU2 style programs.
    */
   public Bcu2ProgramAdapter()
   {
      super();
   }

   /**
    * Create a program adapter for BCU2 style programs.
    *
    * @param program - the application program
    * @param mask - the mask
    */
   public Bcu2ProgramAdapter(ApplicationProgram program, Mask mask)
   {
      super(program, mask);
   }

   /**
    * Get the data for an address.
    *
    * @param addr - the address to get the block for.
    * @return The data block.
    */
   protected DataBlock getData(int addr)
   {
      DataBlock result = null;

      for (DataBlock block : program.getDataBlocks())
      {
         Integer segmentAddr = block.getSegmentAddr();
         Integer segmentLength = block.getSegmentLength();

         if (segmentAddr != null && segmentLength != null &&
             addr >= segmentAddr && addr < segmentAddr + segmentLength &&
             (result == null || block.getNumber() < result.getNumber()))
         {
            result = block;
         }
      }

      Validate.notNull(result, "no data block found for address 0x%1$04x", addr);
      return result;
   }

   @Override
   protected byte[] getAddressTabData()
   {
      Integer addr = mask.getAddressTabAddr();
      DataBlock block = getData(addr);
      addr -= block.getSegmentAddr();
      int sz = program.getAddrTabSize();

      return Arrays.copyOfRange(block.getData(), addr, addr + sz);
   }

   @Override
   protected byte[] getCommsTabData()
   {
      Integer addr = program.getCommsTabAddr();
      DataBlock block = getData(addr);
      addr -= block.getSegmentAddr();
      int sz = program.getCommsTabSize();

      return Arrays.copyOfRange(block.getData(), addr, addr + sz);
   }

   @Override
   protected byte[] getAssocTabData()
   {
      Integer addr = program.getAssocTabAddr();
      DataBlock block = getData(addr);
      addr -= block.getSegmentAddr();
      int sz = program.getAssocTabSize();

      return Arrays.copyOfRange(block.getData(), addr, addr + sz);
   }

   @Override
   public int getMaxTablesSize()
   {
      int addrTabAddr = mask.getAddressTabAddr();

      if (addrTabAddr >= mask.getUserEepromStart() && addrTabAddr <= mask.getUserEepromEnd())
         return mask.getUserEepromEnd() - addrTabAddr + 1;
      else return mask.getUserEepromEnd() - mask.getUserEepromStart() + 1;
   }

   @Override
   protected void fixCommsEntryAddr(CommsEntry entry)
   {
      // Nothing to be done, AFAIK
   }

   @Override
   public void apply()
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void initialize()
   {
      // TODO Auto-generated method stub
   }
}

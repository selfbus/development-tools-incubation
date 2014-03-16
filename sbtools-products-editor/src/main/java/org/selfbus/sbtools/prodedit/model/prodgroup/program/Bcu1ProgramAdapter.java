package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.model.global.Mask;

/**
 * A program adapter for BCU1 style programs.
 */
public class Bcu1ProgramAdapter extends AbstractProgramAdapter
{
   /**
    * Create a program adapter for BCU1 style programs.
    */
   public Bcu1ProgramAdapter()
   {
      super();
   }

   /**
    * Create a program adapter for BCU1 style programs.
    *
    * @param program - the application program
    * @param mask - the mask
    */
   public Bcu1ProgramAdapter(ApplicationProgram program, Mask mask)
   {
      super(program, mask);
   }

   protected byte[] getEepromData()
   {
      if (program.getEepromData() != null)
         return program.getEepromData();
      return mask.getData();
   }

   @Override
   protected byte[] getAddressTabData()
   {
      int addr = program.getAssocTabAddr() - 0x100;
      int sz = program.getAddrTabSize();

      return Arrays.copyOfRange(getEepromData(), addr, addr + sz);
   }

   @Override
   protected byte[] getCommsTabData()
   {
      int addr = program.getCommsTabAddr() - 0x100;
      int sz = program.getCommsTabSize();

      int tabSize = getEepromData()[addr] * 3 + 2;
      Validate.isTrue(sz == tabSize);

      return Arrays.copyOfRange(getEepromData(), addr, addr + sz);
   }

   @Override
   protected byte[] getAssocTabData()
   {
      int addr = program.getAssocTabAddr() - 0x100;
      int sz = program.getAssocTabSize();

      return Arrays.copyOfRange(getEepromData(), addr, addr + sz);
   }

   @Override
   public void apply()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   protected void fixCommsEntryAddr(CommsEntry entry)
   {
      if (entry.memSegment)
         entry.valuePtr |= 0x100;
   }

   @Override
   public int getMaxTablesSize()
   {
      return 255;
   }
}

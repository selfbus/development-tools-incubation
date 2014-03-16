package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.model.global.Mask;

/**
 * A factory for {@link ProgramAdapter program adapters}.
 */
public final class ProgramAdapterFactory
{
   /**
    * Create a program adapter for the given application program and mask.
    * 
    * @param program - the application program
    * @param mask - the program mask
    * 
    * @return The program adapter for the program and mask.
    */
   public static ProgramAdapter getProgramAdapter(ApplicationProgram program, Mask mask)
   {
      Validate.notNull(program);
      Validate.notNull(mask);

      if (mask.getVersion() >= 0x20)
         return new Bcu2ProgramAdapter(program, mask);
      else return new Bcu1ProgramAdapter(program, mask);
   }
}

package org.selfbus.sbtools.prodedit.model.prodgroup.program;

/**
 * An entry in the association table.
 */
public class AssocEntry
{
   /**
    * The index into the address table.
    */
   public int addrno;

   /**
    * The number of the com-object.
    */
   public int objno;

   /**
    * Create an entry in the association table.
    */
   public AssocEntry()
   {
   }

   /**
    * Create an entry in the association table.
    * 
    * @param addrno - the index into the address table.
    * @param objno - the number of the com-object.
    */
   public AssocEntry(int addrno, int objno)
   {
      this.addrno = addrno;
      this.objno = objno;
   }
}

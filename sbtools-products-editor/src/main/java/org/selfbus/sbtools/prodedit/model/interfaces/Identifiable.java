package org.selfbus.sbtools.prodedit.model.interfaces;


/**
 * Interface for classes that have a numeric ID.
 */
public interface Identifiable
{
   /**
    * @return The ID.
    */
   public int getId();

   /**
    * Set the ID.
    * 
    * @param id - the ID to set.
    */
   public void setId(int id);
}

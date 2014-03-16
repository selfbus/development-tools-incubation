package org.selfbus.sbtools.prodedit.model.interfaces;

/**
 * Interface for classes that have a numeric order.
 */
public interface Orderable
{
   /**
    * @return The order.
    */
   public int getOrder();

   /**
    * Set the order.
    * 
    * @param order - the order to set.
    */
   public void setOrder(int order);
}

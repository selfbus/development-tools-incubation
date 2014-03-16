package org.selfbus.sbtools.prodedit.model;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Abstract base class for {@link ListDataListener}s. The method implementations are empty.
 */
public abstract class AbstractListDataListener implements ListDataListener
{
   /**
    * {@inheritDoc}
    */
   @Override
   public void intervalAdded(ListDataEvent e)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void intervalRemoved(ListDataEvent e)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void contentsChanged(ListDataEvent e)
   {
   }
}

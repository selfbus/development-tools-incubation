package org.selfbus.sbtools.prodedit.model;

import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;

/**
 * Abstract base class for {@link ProjectListener project listeners}.
 */
public abstract class AbstractProjectListener implements ProjectListener
{
   /**
    * {@inheritDoc}
    */
   @Override
   public void projectLoaded(Project project)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void projectChanged(Project project)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void productGroupAdded(ProductGroup group)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void productGroupChanged(ProductGroup group)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void productGroupRemoved(ProductGroup group)
   {
   }
}

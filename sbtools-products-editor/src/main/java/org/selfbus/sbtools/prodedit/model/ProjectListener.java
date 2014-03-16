package org.selfbus.sbtools.prodedit.model;

import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;

/**
 * Interface for listeners that get informed when the project changes.
 */
public interface ProjectListener
{
   /**
    * The project was loaded.
    * 
    * @param project - the new project.
    */
   public void projectLoaded(Project project);

   /**
    * The project was changed.
    * 
    * @param project - the new or changed project.
    */
   public void projectChanged(Project project);

   /**
    * A product group was added.
    * 
    * @param group - the added product group.
    */
   public void productGroupAdded(ProductGroup group);

   /**
    * A product group was changed.
    * 
    * @param group - the new or changed product group.
    */
   public void productGroupChanged(ProductGroup group);

   /**
    * A product group was removed.
    * 
    * @param group - the removed product group.
    */
   public void productGroupRemoved(ProductGroup group);
}

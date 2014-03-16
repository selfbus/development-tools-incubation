package org.selfbus.sbtools.devtool.project;

import org.selfbus.sbtools.devtool.project.model.Project;

/**
 * Interface for listeners that get informed when the project changes.
 */
public interface ProjectListener
{
   /**
    * The project was changed.
    * 
    * @param project - the new or changed project.
    */
   public void projectChanged(Project project);
}

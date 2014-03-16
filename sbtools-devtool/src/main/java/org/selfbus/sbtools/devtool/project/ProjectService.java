package org.selfbus.sbtools.devtool.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A manager that holds the current project and notifies subscribers
 * about changes.
 */
public class ProjectService
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
   private final Set<ProjectListener> listeners = new HashSet<ProjectListener>();
   private Project project = new Project();

   /**
    * Load a project.
    * 
    * @param file - the file to load the project from.
    */
   public void loadProject(File file)
   {
      LOGGER.info("Loading project from {}", file);

      ProjectReader reader = new ProjectReader();
      try
      {
         project = reader.read(file);
         Config.getInstance().put("project.last", file.getAbsolutePath());

         fireProjectChanged();
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException("File not found: " + file, e);
      }
   }

   /**
    * Save the project.
    * 
    * @param file - the file to save the project to.
    */
   public void saveProject(File file)
   {
      if (!file.getName().toLowerCase().endsWith(".proj"))
         file = new File(file + ".proj");

      LOGGER.info("Saving project as {}", file);

      ProjectWriter writer = new ProjectWriter();
      writer.write(project, file);
      Config.getInstance().put("project.last", file.getAbsolutePath());

      fireProjectChanged();
   }

   /**
    * Start a new project.
    */
   public void newProject()
   {
      LOGGER.info("Creating new project");

      project = new Project();
      Config.getInstance().remove("project.last");

      fireProjectChanged();
   }

   /**
    * @return The project.
    */
   public Project getProject()
   {
      return project;
   }

   /**
    * Add a project listener.
    * 
    * @param listener - the listener to add.
    */
   public void addListener(ProjectListener listener)
   {
      synchronized (listeners)
      {
         listeners.add(listener);         
      }
   }

   /**
    * Remove a project listener.
    * 
    * @param listener - the listener to remove.
    */
   public void removeListener(ProjectListener listener)
   {
      synchronized (listeners)
      {
         listeners.remove(listener);
      }
   }

   /**
    * Inform all listeners that the project changed.
    */
   public void fireProjectChanged()
   {
      synchronized (listeners)
      {
         for (ProjectListener listener : listeners)
            listener.projectChanged(project);
      }
   }
}

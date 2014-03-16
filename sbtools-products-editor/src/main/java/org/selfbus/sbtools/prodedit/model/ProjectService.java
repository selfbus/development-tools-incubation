package org.selfbus.sbtools.prodedit.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.global.FunctionalEntity;
import org.selfbus.sbtools.prodedit.model.global.Language;
import org.selfbus.sbtools.prodedit.model.global.Manufacturer;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.vdio.ProductsExporter;
import org.selfbus.sbtools.prodedit.vdio.ProductsImporter;
import org.selfbus.sbtools.vdio.VdioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * A manager that holds the current project and notifies subscribers about changes.
 */
public class ProjectService
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
   private final Set<ProjectListener> listeners = new CopyOnWriteArraySet<ProjectListener>();
   private Project project = new Project();

   /**
    * Load a project.
    * 
    * @param file - the file to load the project from.
    * @throws FileNotFoundException 
    */
   public void loadProject(File file) throws FileNotFoundException
   {
      LOGGER.info("Loading project from {}", file);

      ProjectReader reader = new ProjectReader();

      project = reader.read(file);
      project.setProjectService(this);
      Config.getInstance().put("project.last", file.getAbsolutePath());

      String baseName = file.getName();
      int idx = baseName.lastIndexOf('.');
      if (idx > 0) baseName = baseName.substring(0, idx);
      baseName += '-';

      String[] files = file.getParentFile().list();
      Arrays.sort(files);
      for (String fileName : files)
      {
         if (fileName.toLowerCase().startsWith(baseName) && fileName.toLowerCase().endsWith(".prgr"))
         {
            File groupFile = new File(file.getParent() + File.separatorChar + fileName);
            LOGGER.info("Loading product group {}", groupFile);

            try
            {
               ProductGroupReader groupReader = new ProductGroupReader();
               ProductGroup group = groupReader.read(groupFile);
               if (project.getId().equals(group.getProjectId()))
               {
                  project.addProductGroup(group);
               }
               else
               {
                  LOGGER.info("Skipping product group (project IDs do not match)");
               }
            }
            catch (Exception e)
            {
               LOGGER.error("Failed to load product group {}", groupFile, e);
            }
         }
      }

      fireProjectChanged();
   }

   /**
    * Save the project, including all product groups.
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

      String baseName = file.getPath();
      int idx = baseName.lastIndexOf('.');
      if (idx > 0) baseName = baseName.substring(0, idx);
      baseName += '-';
      
      for (ProductGroup group : project.getProductGroups())
      {
         File groupFile = new File(baseName + group.getName() + ".prgr");

         LOGGER.info("Saving products group {} as {}", group.getName(), groupFile);
         ProductGroupWriter groupWriter = new ProductGroupWriter();
         groupWriter.write(group, groupFile);
      }

      fireProjectChanged();
   }

   /**
    * Import the project from a file.
    *
    * @param file - the file to import from
    * 
    * @return The imported project.
    *
    * @throws VdioException 
    * @throws FileNotFoundException 
    */
   public Project importProject(File file) throws FileNotFoundException, VdioException
   {
      ProductsImporter importer = new ProductsImporter(this);

      Project newProject = importer.read(file);
      if (newProject != null)
      {
         project = newProject;

         Config.getInstance().remove("project.last");
         fireProjectLoaded();
         fireProjectChanged();
      }

      return project;
   }

   /**
    * Export the project to a file.
    *
    * @param file - the file to export to
    *
    * @throws VdioException 
    * @throws FileNotFoundException 
    */
   public void exportProject(File file) throws FileNotFoundException, VdioException
   {
      ProductsExporter exporter = new ProductsExporter();
      exporter.write(project, file);
   }

   /**
    * Start a new project.
    * 
    * @return The created project.
    */
   public Project createProject()
   {
      LOGGER.info("Creating new example project");

      project = new Project();
      project.setProjectService(this);

      int manufacturerId = 76;
      project.getManufacturers().put(manufacturerId, new Manufacturer(manufacturerId, "Selfbus"));

      Config.getInstance().remove("project.last");
      fireProjectLoaded();
      fireProjectChanged();

      return project;
   }

   /**
    * Create an example project.
    * 
    * @return The created project.
    */
   public Project createExampleProject()
   {
      LOGGER.info("Creating new example project");

      project = new Project();
      project.setProjectService(this);

      int manufacturerId = 76;
      project.getManufacturers().put(manufacturerId, new Manufacturer(manufacturerId, "Selfbus"));

      FunctionalEntity feIn = project.createFunctionalEntity("Inputs", null);
      project.createFunctionalEntity("Outputs", null);
      project.createFunctionalEntity("Touch", feIn);
      project.createFunctionalEntity("Switches", feIn);

      project.getLanguages().add(new Language("de", I18n.getMessage("ProjectService.langDE")));
      project.getLanguages().add(new Language("en", I18n.getMessage("ProjectService.langEN")));
      project.getLanguages().add(new Language("fr", I18n.getMessage("ProjectService.langFR")));
      project.setDefaultLangId("de");

      ProductGroup group = new ProductGroup();
      group.setId("inputs");
      group.setName("Inputs");
      group.setManufacturer(project.getManufacturer(manufacturerId));
      project.addProductGroup(group);

      VirtualDevice device = group.createDevice();
      device.setNumber(1);
      device.setDescription("Binary Input");

      ApplicationProgram program = group.getProgram(device);
      ArrayListModel<ParameterType> paramTypes = program.getParameterTypes();

      ParameterType paramType = new ParameterType(ParameterAtomicType.ENUM, "Yes-No");
      paramType.setId(100);
      paramType.setMinValue(0);
      paramType.setMaxValue(1);
      paramType.setSize(1);
      paramTypes.add(paramType);

      ParameterValue paramValue = new ParameterValue();
      paramValue.setIntValue(0);
      paramValue.getLabel().setText("en", "No");
      paramValue.getLabel().setText("de", "Nein");
      paramValue.getLabel().setText("fr", "Non");
      paramType.addValue(paramValue);

      paramValue = new ParameterValue();
      paramValue.setIntValue(1);
      paramValue.getLabel().setText("en", "Yes");
      paramValue.getLabel().setText("de", "Ja");
      paramValue.getLabel().setText("fr", "Oui");
      paramType.addValue(paramValue);

      paramType = new ParameterType(ParameterAtomicType.UNSIGNED, "Percent");
      paramType.setId(200);
      paramType.setMinValue(1);
      paramType.setMaxValue(100);
      paramType.setSize(7);
      paramTypes.add(paramType);

      Config.getInstance().remove("project.last");
      fireProjectLoaded();
      fireProjectChanged();

      return project;
   }

   /**
    * @return The project.
    */
   public Project getProject()
   {
      return project;
   }

   /**
    * Set the project.
    * 
    * @param project - the project
    */
   public void setProject(Project project)
   {
      this.project = project;
      fireProjectLoaded();
      fireProjectChanged();
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
    * Inform all listeners that the project was loaded.
    */
   public void fireProjectLoaded()
   {
      synchronized (listeners)
      {
         for (ProjectListener listener : listeners)
            listener.projectLoaded(project);
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

   /**
    * Inform all listeners that a product group was added.
    * 
    * @param group - the product group
    */
   public void fireProductGroupAdded(ProductGroup group)
   {
      synchronized (listeners)
      {
         for (ProjectListener listener : listeners)
            listener.productGroupAdded(group);
      }
   }

   /**
    * Inform all listeners that a product group was changed.
    * 
    * @param group - the product group
    */
   public void fireProductGroupChanged(ProductGroup group)
   {
      synchronized (listeners)
      {
         for (ProjectListener listener : listeners)
            listener.productGroupChanged(group);
      }
   }

   /**
    * Inform all listeners that a product group was removed.
    * 
    * @param group - the product group
    */
   public void fireProductGroupRemoved(ProductGroup group)
   {
      synchronized (listeners)
      {
         for (ProjectListener listener : listeners)
            listener.productGroupRemoved(group);
      }
   }
}

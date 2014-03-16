package org.selfbus.sbtools.prodedit;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataEvent;

import org.jdesktop.application.Application;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.Environment;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.misc.LookAndFeelManager;
import org.selfbus.sbtools.prodedit.components.AbstractProdEdit;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.AbstractListDataListener;
import org.selfbus.sbtools.prodedit.model.AbstractProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.ProductGroupTab;
import org.selfbus.sbtools.prodedit.tabs.project.ProjectTab;
import org.selfbus.sbtools.prodedit.vdio.ProductsImporter;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * The application class.
 */
public class ProdEdit extends AbstractProdEdit
{
   //   private static final Logger LOGGER = LoggerFactory.getLogger(ProdEdit.class);

   private Config config = Config.getInstance();
   private String configFileName;
   private final ProjectService projectService = new ProjectService();
   /**
    * Start the application.
    * 
    * @param args - the command line arguments
    * @throws UnsupportedLookAndFeelException 
    */
   public static void main(String[] args) throws UnsupportedLookAndFeelException
   {
      LookAndFeelManager.setDefaultLookAndFeel();
      Application.launch(ProdEdit.class, args);
   }

   /**
    * @return The application object.
    */
   public static ProdEdit getInstance()
   {
      return getInstance(ProdEdit.class);
   }

   /**
    * @return The application's configuration.
    */
   public Config getConfig()
   {
      return config;
   }

   /**
    * Save the configuration.
    */
   public void saveConfig()
   {
      try
      {
         config.save(configFileName);
      }
      catch (IOException e)
      {
         Dialogs.showErrorDialog(I18n.formatMessage("Error.write", configFileName));
      }
   }

   /**
    * Load the configuration. Automatically called during application initialization.
    */
   protected void loadConfig()
   {
      configFileName = getContext().getLocalStorage().getDirectory().getPath() + File.separator + "user.config";
      try
      {
         config.load(configFileName);
      }
      catch (FileNotFoundException e)
      {
      }
      catch (IOException e)
      {
         Dialogs.showErrorDialog(I18n.formatMessage("Error.read", configFileName));
      }
   }

   /**
    * @return The project service.
    */
   public ProjectService getProjectService()
   {
      return projectService;
   }

   /**
    * @return The project. This is a shortcut for getProjectService().getProject().
    */
   public Project getProject()
   {
      return projectService.getProject();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void initialize(String[] args)
   {
      try
      {
         super.initialize(args);

         Environment.setAppName(getClass().getSimpleName());
         File appDir = new File(Environment.getAppDir());
         getContext().getLocalStorage().setDirectory(appDir);

         loadConfig();

         projectService.addListener(projectListener);
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("Error.startup"));
         System.exit(1);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void shutdown()
   {
      saveConfig();

      projectService.removeListener(projectListener);

      super.shutdown();
   }

   /**
    * Responsible for starting the application; for creating and showing the initial GUI.
    * 
    * This method is called by the static launch method, subclasses must override it. It runs on the
    * event dispatching thread.
    */
   @Override
   protected void startup()
   {
      try
      {
         final JFrame mainFrame = getMainView().getFrame();
         mainFrame.setName("sbtools-prodedit");
         mainFrame.setMinimumSize(new Dimension(800, 600));
         mainFrame.setTitle(I18n.getMessage("ProdEdit.title"));
         setupMainFrame();        

         showProjectTab();
         show(getMainView());
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("Error.startup"));
         System.exit(1);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void ready()
   {
      try
      {
         getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

         String lastProjectPath = Config.getInstance().getStringValue("project.last");
         if (lastProjectPath != null && !lastProjectPath.isEmpty() && (new File(lastProjectPath)).exists())
         {
            projectService.loadProject(new File(lastProjectPath));
         }
         else
         {
            ProductsImporter importer = new ProductsImporter(projectService);
            String fileName = "Bosch-Freebus12.vd_";
            Project project = importer.read(getClass().getClassLoader().getResourceAsStream(fileName));
            project.setName(fileName);
            projectService.setProject(project);
         }

         setStatusMessage(I18n.formatMessage("Project.loaded", projectService.getProject().getName()));
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("Error.startup"));
      }
      finally
      {
         getMainFrame().setCursor(Cursor.getDefaultCursor());
      }
   }

   /**
    * Update the title of the main application window.
    */
   protected void updateMainTitle()
   {
      final JFrame mainFrame = getMainView().getFrame();

      Project project = projectService.getProject();
      File projFile = project == null ? null : project.getFile();

      if (projFile != null)
      {
         mainFrame.setTitle(I18n.formatMessage("ProdEdit.titleLoaded", projFile.getName()));
         
      }
      else if (project != null && !project.getName().isEmpty())
      {
         mainFrame.setTitle(I18n.formatMessage("ProdEdit.titleLoaded", project.getName()));
      }
      else
      {
         mainFrame.setTitle(I18n.getMessage("ProdEdit.title"));
      }
   }

   /**
    * Show the tab of the specified product group. Creates a tab for the product
    * group if none exists.
    *
    * @param group - the product group to show.
    */
   public void showProductGroupTab(ProductGroup group)
   {
      String name = getTabPanelName(group);
      ProductGroupTab tab = (ProductGroupTab) findTabPanel(name);
      if (tab == null)
      {
         tab = new ProductGroupTab(group);
         tab.setName(name);
         addTabPanel(tab, tab.getTitle());
      }
      else
      {
         showTabPanel(tab);
      }
   }

   /**
    * Shows the project tab. Creates the project tab if it does not exist.
    */
   public ProjectTab showProjectTab()
   {
      ProjectTab tab = (ProjectTab) findTabPanel("project");
      if (tab == null)
      {
         tab = new ProjectTab();
         tab.setName("project");
         addTabPanel(tab, tab.getTitle());
      }
      else
      {
         showTabPanel(tab);
      }

      return tab;
   }

   /**
    * Get the name of the tab panel for the given product group.
    * 
    * @param group - the product group
    * @return The name of the tab panel for the product group.
    */
   static public String getTabPanelName(ProductGroup group)
   {
      return "product-group:" + group.getId();
   }

   /**
    * Private project listener
    */
   private final ProjectListener projectListener = new AbstractProjectListener()
   {
      @Override
      public void projectLoaded(Project project)
      {
         closeAllCloseableTabPanels();
         showProjectTab();
      }

      @Override
      public void projectChanged(final Project project)
      {
         updateMainTitle();

         project.getProductGroups().addListDataListener(new AbstractListDataListener()
         {
            @Override
            public void intervalRemoved(ListDataEvent e)
            {
            }

            @Override
            public void contentsChanged(ListDataEvent e)
            {
               ArrayListModel<ProductGroup> groups = project.getProductGroups();
               for (int listIndex = e.getIndex0(); listIndex <= e.getIndex1(); ++listIndex)
               {
                  ProductGroup group = groups.get(listIndex);

                  int idx = tabbedPane.indexOfComponent(findTabPanel(getTabPanelName(group)));
                  if (idx >= 0)
                  {
                     tabbedPane.setTitleAt(idx, group.getName());
                     tabbedPane.getTabComponentAt(idx).setName(group.toString());
                     updateViewMenu();
                  }
               }
            }
         });
      }

      @Override
      public void productGroupRemoved(ProductGroup group)
      {
         closeTabPanel(findTabPanel(getTabPanelName(group)));
      }
   };
}

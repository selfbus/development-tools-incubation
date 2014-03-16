package org.selfbus.sbtools.devtool;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.lang3.Validate;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.Environment;
import org.selfbus.sbtools.common.exception.SbToolsRuntimeException;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.components.ExtTabbedPane;
import org.selfbus.sbtools.common.gui.misc.LookAndFeelManager;
import org.selfbus.sbtools.common.gui.window.XmlMenuFactory;
import org.selfbus.sbtools.common.gui.window.XmlToolBarFactory;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.ProjectListener;
import org.selfbus.sbtools.devtool.project.ProjectService;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.selfbus.sbtools.devtool.tabs.BusMonitor;
import org.selfbus.sbtools.knxcom.BusInterfaceFactory;

/**
 * The designer's application class.
 */
public class DevTool extends SingleFrameApplication
{
   //   private static final Logger LOGGER = LoggerFactory.getLogger(DevTool.class);

   private Config config = Config.getInstance();
   private String configFileName;
   private final ProjectService projectService = new ProjectService();
   private final Map<Class<? extends JComponent>, JComponent> tabPanels = new HashMap<Class<? extends JComponent>, JComponent>();
   private final ExtTabbedPane tabbedPane = new ExtTabbedPane();

   /**
    * Start the application.
    * 
    * @param args - the command line arguments
    */
   public static void main(String[] args)
   {
      //ActionFactory.getInstance().setPackageNames("", "org.selfbus.sbtools.devtool.actions");
      LookAndFeelManager.setDefaultLookAndFeel();
      Application.launch(DevTool.class, args);
   }

   /**
    * @return The application object.
    */
   public static DevTool getInstance()
   {
      return getInstance(DevTool.class);
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
    * Get a specific tab panel. The tab panel is created if it does not exist.
    * 
    * @param panelClass - the class of the tab panel.
    */
   public JComponent getPanel(final Class<? extends JComponent> panelClass)
   {
      synchronized (tabPanels)
      {
         JComponent tabPanel = tabPanels.get(panelClass);
         if (tabPanel == null)
         {
            try
            {
               tabPanel = panelClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e)
            {
               Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.newInstance", panelClass.getSimpleName()));
               throw new SbToolsRuntimeException(e);
            }

            tabPanels.put(panelClass, tabPanel);
            JMenuItem menuItem = new JMenuItem(tabPanel.getName());
            menuItem.addActionListener(new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  showPanel(panelClass);
               }
            });

            JMenu menu = getMenu("View");
            if (menu != null)
               menu.add(menuItem);
         }

         return tabPanel;
      }
   }

   /**
    * Get a specific menu of the main menu bar.
    * 
    * @param name - the name of the menu.
    * 
    * @return The requested menu or null if not found.
    */
   protected JMenu getMenu(String name)
   {
      JMenuBar menuBar = getMainView().getMenuBar();
      for (int i = menuBar.getMenuCount() - 1; i >= 0; --i)
      {
         JMenu menu = menuBar.getMenu(i);

         if (name.equalsIgnoreCase(menu.getName()))
            return menu;
      }

      return null;
   }

   /**
    * Ensure that a specific tab panel exists and is shown.
    * 
    * @param panelClass - the class of the tab panel.
    */
   public JComponent showPanel(Class<? extends JComponent> panelClass)
   {
      synchronized (tabPanels)
      {
         JComponent tabPanel = getPanel(panelClass);
         // TODO ensure that the panel is not yet in the tabbed pane
         tabbedPane.add(tabPanel);
         tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

         return tabPanel;
      }
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
         FrameView mainView = getMainView();
         final JFrame mainFrame = mainView.getFrame();

         mainFrame.setName("sbtools-devtool");
         mainFrame.setMinimumSize(new Dimension(800, 600));
         mainFrame.setTitle(I18n.getMessage("DevTool.title"));

         String fileName = "org/selfbus/sbtools/devtool/main-menubar.xml";
         InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
         Validate.notNull(in, "menubar configuration not found: " + fileName);
         mainView.setMenuBar(new XmlMenuFactory(I18n.BUNDLE).createMenuBar(in));

         fileName = "org/selfbus/sbtools/devtool/main-toolbar.xml";
         in = getClass().getClassLoader().getResourceAsStream(fileName);
         Validate.notNull(in, "toolbar configuration not found: " + fileName);
         mainView.setToolBar(new XmlToolBarFactory().createToolBar(in));

         try
         {
            BusInterfaceFactory.getBusInterface();
         }
         catch (Exception e)
         {
            Dialogs.showExceptionDialog(e, I18n.getMessage("Error.openBusInterface"));
         }

         mainFrame.add(tabbedPane);

         showPanel(BusMonitor.class);
//         showPanel(GroupsPanel.class);
//         showPanel(TestsPanel.class);
         tabbedPane.setSelectedIndex(0);

         show(mainView);

         projectService.addListener(new ProjectListener()
         {
            @Override
            public void projectChanged(Project project)
            {
               updateMainTitle();
            }
         });
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
         String lastProjectPath = Config.getInstance().getStringValue("project.last");
         if (lastProjectPath != null && !lastProjectPath.isEmpty())
            projectService.loadProject(new File(lastProjectPath));
         else projectService.fireProjectChanged();
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("Error.startup"));
      }
   }

   /**
    * Update the title of the main application window.
    */
   protected void updateMainTitle()
   {
      final JFrame mainFrame = getMainView().getFrame();

      File projFile = projectService.getProject().getFile();

      if (projFile == null)
      {
         mainFrame.setTitle(I18n.getMessage("DevTool.title"));
      }
      else
      {
         String projName = projFile.getName();
         mainFrame.setTitle(I18n.formatMessage("DevTool.titleLoaded", projName));
      }
   }
}

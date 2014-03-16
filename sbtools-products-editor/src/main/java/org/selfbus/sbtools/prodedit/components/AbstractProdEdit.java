package org.selfbus.sbtools.prodedit.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.lang3.Validate;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.components.ExtTabbedPane;
import org.selfbus.sbtools.common.gui.window.XmlMenuFactory;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.tabs.internal.AbstractAccordionDetailsTab;

/**
 * Abstract base application class. This class contains the basic application
 * window layout, status-bar, and tab-handling.
 */
public abstract class AbstractProdEdit extends SingleFrameApplication
{
   protected final ExtTabbedPane tabbedPane = new ExtTabbedPane();

   private final StatusBar statusBar = new StatusBar();
   private final JLabel statusMessagePanel = new JLabel(" ");
   private Timer clearStatusTimer;

   /**
    * Create an abstract application object.
    */
   public AbstractProdEdit()
   {
      super();
   }

   /**
    * Setup the main frame. To be called from the application's setup().
    */
   protected void setupMainFrame()
   {
      FrameView mainView = getMainView();

      final JFrame mainFrame = mainView.getFrame();
      mainFrame.setMinimumSize(new Dimension(800, 600));

      final String resourceDir = getClass().getPackage().getName().replace('.', '/');
      
      String fileName = resourceDir + "/main-menubar.xml";
      InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
      Validate.notNull(in, "menubar configuration not found: " + fileName);
      mainView.setMenuBar(new XmlMenuFactory(I18n.BUNDLE).createMenuBar(in));

//    fileName = resourceDir + "/main-toolbar.xml";
//    in = getClass().getClassLoader().getResourceAsStream(fileName);
//    Validate.notNull(in, "toolbar configuration not found: " + fileName);
//    mainView.setToolBar(new XmlToolBarFactory().createToolBar(in));

      statusBar.add(Box.createHorizontalStrut(2), 0, false);
      statusBar.add(statusMessagePanel, 100, true);
      mainView.setStatusBar(statusBar);

      clearStatusTimer = new Timer(5000, clearStatusHandler);
      clearStatusTimer.setRepeats(false);

      mainFrame.add(tabbedPane);
   }

   /**
    * @return The tab panel that is currently shown.
    */
   public JPanel getCurrentTabPanel()
   {
      return (JPanel) tabbedPane.getSelectedComponent();
   }
   
   /**
    * Add a tab panel.
    * 
    * @param panel - the panel to add.
    * @param title - the title of the tab.
    */
   public void addTabPanel(JPanel panel, String title)
   {
      tabbedPane.add(title, panel);
      tabbedPane.setSelectedComponent(panel);

      updateViewMenu();
   }

   /**
    * Find a tab panel by name.
    * 
    * @param name - the name of the panel to find.
    * @return The panel or null if not found.
    */
   public JPanel findTabPanel(String name)
   {
      for (int idx = tabbedPane.getTabCount() - 1; idx >= 0; --idx)
      {
         JPanel panel  = (JPanel) tabbedPane.getComponentAt(idx);

         if (name.equals(panel.getName()))
            return panel;
      }

      return null;
   }

   /**
    * Ensure that the tab panel is visible. Does nothing if the given panel
    * is null or not found.
    * 
    * @param panel - the tab panel to show.
    */
   public void showTabPanel(JPanel panel)
   {
      if (panel != null)
         tabbedPane.setSelectedComponent(panel);
   }

   /**
    * Close a tab panel.
    *
    * @param panel - the tab panel to close.
    */
   public void closeTabPanel(JPanel panel)
   {
      tabbedPane.remove(panel);
      panel.setVisible(false);

      updateViewMenu();
   }

   /**
    * Close all tab panels.
    */
   public void closeAllTabPanels()
   {
      while (tabbedPane.getTabCount() > 0)
         tabbedPane.remove(0);

      updateViewMenu();
   }

   /**
    * Close all tab panels that implement {@link CloseableComponent}.
    */
   public void closeAllCloseableTabPanels()
   {
      List<Component> obsoletes = new LinkedList<Component>();
      
      for (int idx = tabbedPane.getTabCount() - 1; idx >= 0; --idx)
      {
         Component comp = tabbedPane.getComponentAt(idx);
         if (comp instanceof CloseableComponent)
            obsoletes.add(comp);
      }

      if (obsoletes.isEmpty())
         return;

      for (Component comp : obsoletes)
      {
         tabbedPane.remove(comp);
         comp.setVisible(false);
      }

      updateViewMenu();
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
    * Set a message that is displayed in the status line.
    * 
    * @param message - the status message
    */
   public void setStatusMessage(String message)
   {
      statusMessagePanel.setText(message);
   
      clearStatusTimer.stop();
      clearStatusTimer.start();
   }

   /**
    * Lazily update the view menu.
    * Does nothing if no menu with the name "View" exists.
    */
   public void updateViewMenu()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            final JMenu viewMenu = getMenu("View");
            if (viewMenu == null)
               return;

            viewMenu.removeAll();
            for (int idx = 0; idx < tabbedPane.getTabCount(); ++idx)
            {
               final Component comp = tabbedPane.getComponentAt(idx);
               String title;

               if (comp instanceof AbstractAccordionDetailsTab)
                  title = ((AbstractAccordionDetailsTab) comp).getTitle();
               else title = comp.getName();

               JMenuItem menuItem = new JMenuItem(title);
               menuItem.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                     tabbedPane.setSelectedComponent(comp);
                  }
               });

               viewMenu.add(menuItem);
            }
         }
      });
   }

   /**
    * Clear the status message after a delay.
    */
   private final ActionListener clearStatusHandler = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         statusMessagePanel.setText(" ");
      }
   };
}

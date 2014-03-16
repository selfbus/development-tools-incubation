package org.selfbus.sbtools.prodedit.tabs.internal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.selfbus.sbtools.common.gui.components.Accordion;

/**
 * A tab page that has an {@link Accordion accordion} on the left side and a details view on the
 * right side.
 */
public class AbstractAccordionDetailsTab extends JPanel
{
   // private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAccordionDetailsTab.class);
   private static final long serialVersionUID = -4926187137109981959L;

   private final JPanel emptyDetails = new JPanel();
   private final JSplitPane basePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
   private final JPanel leftPanel = new JPanel();
   private final Accordion accordion = new Accordion();
   private JToolBar leftToolBar;
   private String title;

   /**
    * Create an accordion details tab.
    */
   public AbstractAccordionDetailsTab()
   {
      super();
      setLayout(new BorderLayout());

      basePane.setDividerLocation(300);
      add(basePane, BorderLayout.CENTER);

      leftPanel.setLayout(new BorderLayout());
      leftPanel.add(accordion, BorderLayout.CENTER);

      basePane.setLeftComponent(leftPanel);
      basePane.setRightComponent(emptyDetails);
   }

   /**
    * @return The title of the tab.
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * Set the title of the tab.
    *
    * @param title - the title to set
    */
   public void setTitle(String title)
   {
      this.title = title;
   }

   /**
    * Add a category to the tab page. The category is added to the accordion on the
    * left side of the tab page.
    * 
    * @param category - the category to add
    */
   public void addCategory(final CategoryElem category)
   {
      if (accordion.isEmpty())
      {
         basePane.setRightComponent(category.getDetailsPanel());

         leftToolBar = category.getToolBar();
         if (leftToolBar != null)
            leftPanel.add(leftToolBar, BorderLayout.NORTH);
      }

      String name = category.getName();
      accordion.addBar(name, category.getListPanel());
      accordion.addBarListener(name, new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            int loc = basePane.getDividerLocation();

            basePane.setRightComponent(category.getDetailsPanel());

            if (leftToolBar != null)
               leftPanel.remove(leftToolBar);

            leftToolBar = category.getToolBar();

            if (leftToolBar != null)
               leftPanel.add(leftToolBar, BorderLayout.NORTH);

            basePane.setDividerLocation(loc);
         }
      });
   }

   /**
    * Set the category element that is visible.
    * 
    * @param category - the category to make visible.
    */
   public void setVisibleCategory(CategoryElem category)
   {
      accordion.setVisibleBar(category.getName());
   }

   /**
    * @return The base splitter pane.
    */
   public JSplitPane getBasePane()
   {
      return basePane;
   }
}

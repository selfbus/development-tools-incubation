package org.selfbus.sbtools.prodedit.tabs.internal;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A category element that contains a list where each list entry has it's
 * own details panel.
 */
public class MixedCategoryElem implements CategoryElem
{
   private final DefaultListModel<String> entriesListModel = new DefaultListModel<String>();
   private final Vector<CategoryElem> elems = new Vector<CategoryElem>();
   private final JList<String> entriesList = new JList<String>(entriesListModel);
   private final JScrollPane entriesScrollPane = new JScrollPane(entriesList);
   private final JPanel detailsPanel = new JPanel();
   private final String name;

   /**
    * Create a category element that contains a list where each list entry has it's
    * own details panel.
    * 
    * @param name - the localized name of the category.
    */
   public MixedCategoryElem(String name)
   {
      this.name = name;

      detailsPanel.setLayout(new BorderLayout());

      entriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      entriesList.addListSelectionListener(new ListSelectionListener()
      {
         @Override
         public void valueChanged(ListSelectionEvent e)
         {
            if (!e.getValueIsAdjusting())
            {
               final CategoryElem elem = elems.get(entriesList.getSelectedIndex());
   
               detailsPanel.removeAll();
               detailsPanel.add(elem.getDetailsPanel(), BorderLayout.CENTER);

               detailsPanel.revalidate();
               detailsPanel.repaint();
            }
         }
      });

      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            if (!entriesListModel.isEmpty())
               entriesList.setSelectedIndex(0);
         }
      });
   }

   /**
    * Add a category to the list.
    * 
    * @param category - the category to add
    */
   public void addCategory(final CategoryElem category)
   {
      entriesListModel.addElement(category.getName());
      elems.add(category);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getDetailsPanel()
   {
      return detailsPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getListPanel()
   {
      return entriesScrollPane;
   }

   /**
    * @return null, as there is no tool bar.
    */
   @Override
   public JToolBar getToolBar()
   {
      return null;
   }
}

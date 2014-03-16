package org.selfbus.sbtools.prodedit.tabs.internal;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 * Abstract base class for {@link CategoryElem category elements}.
 */
public abstract class AbstractCategoryElem implements CategoryElem
{
   protected JScrollPane listScrollPane;
   protected JPanel detailsPanel;
   protected JToolBar toolBar;

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
      return listScrollPane;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JToolBar getToolBar()
   {
      return toolBar;
   }
}

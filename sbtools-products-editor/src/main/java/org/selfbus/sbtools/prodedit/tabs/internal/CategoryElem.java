package org.selfbus.sbtools.prodedit.tabs.internal;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/**
 * Interface for category elements.
 */
public interface CategoryElem
{
   /**
    * @return The localized name of the element.
    */
   public String getName();
   
   /**
    * @return The details panel.
    */
   public JComponent getDetailsPanel();
   
   /**
    * @return The list panel.
    */
   public JComponent getListPanel();

   /**
    * @return The tool bar. May be null.
    */
   public JToolBar getToolBar();
}

package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTree;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.common.gui.utils.TreeUtils;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Expand all elements of the tree. This is a specialized action that
 * is meant for master-details views.
 */
public final class ExpandAllTreeAction extends BasicAction
{
   private static final long serialVersionUID = 7493687992091135896L;

   private final JTree tree;

   /**
    * Create an action object.
    * 
    * @param tree - the tree to work on
    */
   public ExpandAllTreeAction(JTree tree)
   {
      super(I18n.getMessage("ExpandAllTreeAction.name"),  I18n.getMessage("ExpandAllTreeAction.toolTip"),
         ImageCache.getIcon("icons/expand-all"));

      this.tree = tree;
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      TreeUtils.expandAll(tree);
   }
}

package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTree;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.common.gui.utils.TreeUtils;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Collapse all elements of the tree. This is a specialized action that
 * is meant for master-details views.
 */
public final class CollapseAllTreeAction extends BasicAction
{
   private static final long serialVersionUID = 1259955475102915688L;

   private final JTree tree;

   /**
    * Create an action object.
    * 
    * @param tree - the tree to work on
    */
   public CollapseAllTreeAction(JTree tree)
   {
      super(I18n.getMessage("CollapseAllTreeAction.name"),  I18n.getMessage("CollapseAllTreeAction.toolTip"),
         ImageCache.getIcon("icons/collapse-all"));

      this.tree = tree;
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      TreeUtils.collapseAll(tree);
      tree.expandRow(0);
   }
}

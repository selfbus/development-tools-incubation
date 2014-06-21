package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.binding.SelectionInTree;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Remove the currently selected object in the tree. This is a specialized action that
 * is meant for master-details views.
 */
public final class RemoveSelectionInTreeAction extends BasicAction
{
   private static final long serialVersionUID = 82734366934557299L;
   
   private final SelectionInTree selectionInTree;
   private boolean confirm = false;

   /**
    * Create an action object.
    * 
    * @param selectionInTree - the selection-in-list to work on
    * @param toolTip - the text for the tool tip
    */
   public RemoveSelectionInTreeAction(SelectionInTree selectionInTree, String toolTip)
   {
      this(selectionInTree, toolTip, ImageCache.getIcon("icons/edit-remove"));
   }

   /**
    * Create an action object.
    * 
    * @param selectionInList - the selection-in-list to work on
    * @param toolTip - the text for the tool tip
    * @param icon - the icon
    */
   public RemoveSelectionInTreeAction(SelectionInTree selectionInTree, String toolTip, Icon icon)
   {
      super(I18n.getMessage("RemoveSelectionInTreeAction.name"), toolTip, icon);
      this.selectionInTree = selectionInTree;
   }

   /**
    * Shall executing the action be confirmed? Default: false.
    * 
    * @param confirm - true to ask for confirmation
    */
   public void setConfirmAction(boolean confirm)
   {
      this.confirm = confirm;
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
	   // TODO implement removing of selectionInTree

//      List<?> list = selectionInTree.getList();
//
//      Object selectedObject = selectionInTree.getSelection();
//      if (selectedObject == null) return;
//
//      if (confirm)
//      {
//         if (JOptionPane.showConfirmDialog(ProdEdit.getInstance().getMainFrame(),
//               I18n.formatMessage("RemoveSelectionInListAction.confirmQuestion", selectedObject.toString()),
//               I18n.getMessage("RemoveSelectionInListAction.confirmTitle"),
//               JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
//         {
//            return;
//         }
//      }
//
//      int selectedIndex = Math.min(selectionInTree.getSelectionIndex(), list.size() - 2);
//
//      list.remove(selectedObject);
//
//      if (selectedIndex >= 0)
//         selectionInTree.setSelectionIndex(selectedIndex);
   }
}

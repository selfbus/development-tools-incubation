package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.internal.I18n;

import com.jgoodies.binding.list.SelectionInList;

/**
 * Remove the currently selected object in the list. This is a specialized action that
 * is meant for master-details views.
 */
public final class RemoveSelectionInListAction extends BasicAction
{
   private static final long serialVersionUID = 7311898366957557210L;
   
   private final SelectionInList<?> selectionInList;
   private boolean confirm = false;

   /**
    * Create an action object.
    * 
    * @param selectionInList - the selection-in-list to work on
    * @param toolTip - the text for the tool tip
    */
   public RemoveSelectionInListAction(SelectionInList<?> selectionInList, String toolTip)
   {
      this(selectionInList, toolTip, ImageCache.getIcon("icons/edit-remove"));
   }

   /**
    * Create an action object.
    * 
    * @param selectionInList - the selection-in-list to work on
    * @param toolTip - the text for the tool tip
    * @param icon - the icon
    */
   public RemoveSelectionInListAction(SelectionInList<?> selectionInList, String toolTip, Icon icon)
   {
      super(I18n.getMessage("RemoveSelectionInListAction.name"), toolTip, icon);
      this.selectionInList = selectionInList;
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
      List<?> list = selectionInList.getList();

      Object selectedObject = selectionInList.getSelection();
      if (selectedObject == null) return;

      if (confirm)
      {
         if (JOptionPane.showConfirmDialog(ProdEdit.getInstance().getMainFrame(),
               I18n.formatMessage("RemoveSelectionInListAction.confirmQuestion", selectedObject.toString()),
               I18n.getMessage("RemoveSelectionInListAction.confirmTitle"),
               JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
         {
            return;
         }
      }

      int selectedIndex = Math.min(selectionInList.getSelectionIndex(), list.size() - 2);

      list.remove(selectedObject);

      if (selectedIndex >= 0)
         selectionInList.setSelectionIndex(selectedIndex);
   }
}

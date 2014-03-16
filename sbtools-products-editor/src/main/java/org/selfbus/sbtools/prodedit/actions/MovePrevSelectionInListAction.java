package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.internal.I18n;

import com.jgoodies.binding.list.SelectionInList;

/**
 * Move the currently selected object one step towards the beginning of the list.
 */
public final class MovePrevSelectionInListAction extends BasicAction
{
   private static final long serialVersionUID = -3500983128313898054L;

   private final SelectionInList<?> selectionInList;

   /**
    * Create an action object.
    * 
    * @param selectionInList - the selection-in-list to work on
    */
   public MovePrevSelectionInListAction(SelectionInList<?> selectionInList)
   {
      super(I18n.getMessage("MovePrevSelectionInListAction.name"),
         I18n.getMessage("MovePrevSelectionInListAction.toolTip"),
         ImageCache.getIcon("icons/1uparrow"));

      this.selectionInList = selectionInList;
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      @SuppressWarnings("unchecked")
      List<Object> list = (List<Object>) selectionInList.getList();

      int selectedIndex = selectionInList.getSelectionIndex();
      if (selectedIndex < 1) return;

      Object selectedObject = selectionInList.getSelection();
      list.remove(selectedIndex);
      list.add(selectedIndex - 1, selectedObject);

      selectionInList.setSelectionIndex(selectedIndex - 1);
   }
}

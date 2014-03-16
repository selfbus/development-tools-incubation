package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import org.jdesktop.application.Application;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Exit the application.
 */
public final class ExitAction extends BasicAction
{
   private static final long serialVersionUID = 2249213534706980310L;

   /**
    * Create an action object.
    */
   public ExitAction()
   {
      super(I18n.getMessage("ExitAction.name"), I18n.getMessage("ExitAction.toolTip"), ImageCache.getIcon("icons/exit"));

      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      Application.getInstance().exit();
   }
}

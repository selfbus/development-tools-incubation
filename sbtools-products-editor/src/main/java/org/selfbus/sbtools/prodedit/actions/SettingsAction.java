package org.selfbus.sbtools.prodedit.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.dialogs.settings.Settings;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Open the settings dialog.
 */
public final class SettingsAction extends BasicAction
{
   private static final long serialVersionUID = 5649188834706980695L;

   /**
    * Create an action object.
    */
   public SettingsAction()
   {
      super(I18n.getMessage("SettingsAction.name"), I18n.getMessage("SettingsAction.toolTip"), ImageCache.getIcon("icons/configure"));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      SingleFrameApplication app = (SingleFrameApplication) SingleFrameApplication.getInstance();
      final JFrame mainWin = app.getMainFrame();
      try
      {
         mainWin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         final Settings dlg = new Settings(mainWin);
         dlg.setVisible(true);
      }
      finally
      {
         mainWin.setCursor(Cursor.getDefaultCursor());
      }
   }
}

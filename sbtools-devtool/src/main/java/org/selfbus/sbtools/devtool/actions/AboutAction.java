package org.selfbus.sbtools.devtool.actions;

import java.awt.event.ActionEvent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.dialogs.About;
import org.selfbus.sbtools.devtool.internal.I18n;

/**
 * Show the "about" dialog.
 */
public final class AboutAction extends BasicAction
{
   private static final long serialVersionUID = 8511540400150309373L;

   /**
    * Create an action object.
    */
   public AboutAction()
   {
      super(I18n.getMessage("AboutAction.name"), null, null);
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      final About dlg = new About(DevTool.getInstance().getMainFrame());
      dlg.center();
      dlg.setVisible(true);
   }
}

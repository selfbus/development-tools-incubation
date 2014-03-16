package org.selfbus.sbtools.prodedit.actions;

import java.awt.event.ActionEvent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.dialogs.About;
import org.selfbus.sbtools.prodedit.internal.I18n;

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
      final About dlg = new About(ProdEdit.getInstance().getMainFrame());
      dlg.center();
      dlg.setVisible(true);
   }
}

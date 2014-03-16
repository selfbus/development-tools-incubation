package org.selfbus.sbtools.devtool.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;

/**
 * Create a new project.
 */
public final class NewProjectAction extends BasicAction
{
   private static final long serialVersionUID = 4126480576934633506L;

   /**
    * Create an action object.
    */
   public NewProjectAction()
   {
      super(I18n.getMessage("NewProjectAction.name"), I18n.getMessage("NewProjectAction.toolTip"), ImageCache
            .getIcon("icons/filenew"));
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      DevTool app = (DevTool) SingleFrameApplication.getInstance();
//      final JFrame mainWin = app.getMainFrame();

      app.getProjectService().newProject();
   }
}

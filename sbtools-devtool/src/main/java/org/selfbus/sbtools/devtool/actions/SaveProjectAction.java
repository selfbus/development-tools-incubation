package org.selfbus.sbtools.devtool.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.ProjectService;

/**
 * Save the current project.
 */
public final class SaveProjectAction extends SaveProjectAsAction
{
   private static final long serialVersionUID = 1119367485810122303L;

   /**
    * Create an action object.
    */
   public SaveProjectAction()
   {
      super(I18n.getMessage("SaveProjectAction.name"), I18n.getMessage("SaveProjectAction.toolTip"), ImageCache
            .getIcon("icons/filesave"));
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent event)
   {
      DevTool app = (DevTool) SingleFrameApplication.getInstance();
      ProjectService projService = app.getProjectService();
      File file = projService.getProject().getFile();

      if (file == null)
      {
         super.actionEvent(event);
         return;
      }

      final JFrame mainWin = app.getMainFrame();
      try
      {
         mainWin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         projService.saveProject(file);
      }
      finally
      {
         mainWin.setCursor(Cursor.getDefaultCursor());
      }
   }
}

package org.selfbus.sbtools.devtool.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.filter.ProjectFileFilter;
import org.selfbus.sbtools.devtool.internal.I18n;

/**
 * Open a project.
 */
public final class OpenProjectAction extends BasicAction
{
   private static final long serialVersionUID = -3511750143932514022L;

   /**
    * Create an action object.
    */
   public OpenProjectAction()
   {
      super(I18n.getMessage("OpenProjectAction.name"), I18n.getMessage("OpenProjectAction.toolTip"), ImageCache
            .getIcon("icons/fileopen"));
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      SingleFrameApplication app = (SingleFrameApplication) SingleFrameApplication.getInstance();
      final JFrame mainWin = app.getMainFrame();

      final Config cfg = Config.getInstance();
      String lastDir = cfg.getStringValue("project.lastDir");

      final JFileChooser dlg = new JFileChooser();
      dlg.setCurrentDirectory(new File(lastDir));
      final FileFilter fileFilter = new ProjectFileFilter(true);
      dlg.addChoosableFileFilter(fileFilter);
      dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
      dlg.setFileFilter(fileFilter);
      dlg.setDialogTitle(I18n.getMessage("OpenProjectAction.title"));

      if (dlg.showOpenDialog(mainWin) != JFileChooser.APPROVE_OPTION)
         return;

      final File file = dlg.getSelectedFile();
      if (file == null) return;

      cfg.put("project.lastDir", file.getParent());

      try
      {
         mainWin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         DevTool.getInstance().getProjectService().loadProject(file);
      }
      finally
      {
         mainWin.setCursor(Cursor.getDefaultCursor());
      }
   }
}

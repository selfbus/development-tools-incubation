package org.selfbus.sbtools.prodedit.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.filter.ProjectFileFilter;
import org.selfbus.sbtools.prodedit.internal.I18n;

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
   public void actionEvent(ActionEvent ev)
   {
      ProdEdit app = ProdEdit.getInstance();
      final JFrame mainWin = app.getMainFrame();

      final Config cfg = Config.getInstance();
      String lastDir = cfg.getStringValue("project.lastDir");

      final JFileChooser dlg = new JFileChooser();
      dlg.setCurrentDirectory(new File(lastDir));
      final FileFilter fileFilter = new ProjectFileFilter();
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
         ProdEdit.getInstance().getProjectService().loadProject(file);
         app.setStatusMessage(I18n.formatMessage("Project.loaded", app.getProjectService().getProject().getName()));
      }
      catch (FileNotFoundException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.read", file.getName()));
      }
      finally
      {
         mainWin.setCursor(Cursor.getDefaultCursor());
      }
   }
}

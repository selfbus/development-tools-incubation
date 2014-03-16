package org.selfbus.sbtools.prodedit.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.filter.ProjectFileFilter;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Save the project in a custom file.
 */
public class SaveProjectAsAction extends BasicAction
{
   private static final long serialVersionUID = -81975014312514022L;

   /**
    * Create an action object.
    */
   public SaveProjectAsAction()
   {
      super(I18n.getMessage("SaveProjectAsAction.name"), I18n.getMessage("SaveProjectAsAction.toolTip"), ImageCache
            .getIcon("icons/filesaveas"));
   }

   /**
    * Create an action object.
    *
    * @param name - The name of the action. A "&" marks the mnemonic.
    * @param toolTipText - The text for the tool-tip.
    * @param icon - The icon for the action.
    */
   SaveProjectAsAction(String name, String toolTipText, Icon icon)
   {
      super(name, toolTipText, icon);
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent e)
   {
      ProdEdit app = (ProdEdit) SingleFrameApplication.getInstance();
      final JFrame mainWin = app.getMainFrame();

      final Config cfg = Config.getInstance();
      String lastDir = cfg.getStringValue("project.lastDir");

      final JFileChooser dlg = new JFileChooser();
      dlg.setCurrentDirectory(new File(lastDir));
      final FileFilter fileFilter = new ProjectFileFilter();
      dlg.addChoosableFileFilter(fileFilter);
      dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
      dlg.setFileFilter(fileFilter);
      dlg.setDialogTitle(I18n.getMessage("SaveProjectAsAction.title"));

      if (dlg.showOpenDialog(mainWin) != JFileChooser.APPROVE_OPTION)
         return;

      File file = dlg.getSelectedFile();
      if (file == null) return;

      cfg.put("project.lastDir", file.getParent());

      try
      {
         mainWin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         app.getProjectService().saveProject(file);
         app.setStatusMessage(I18n.formatMessage("Project.saved", app.getProjectService().getProject().getName()));
      }
      finally
      {
         mainWin.setCursor(Cursor.getDefaultCursor());
      }
   }
}

package org.selfbus.sbtools.devtool.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.tabs.BusTraceViewer;
import org.selfbus.sbtools.knxcom.gui.busmonitor.BusTraceFileFilter;

/**
 * Ask the user for a bus trace file and open a {@link BusTraceViewer bus trace
 * viewer} page with the contents of the file.
 */
public final class OpenBusTraceAction extends BasicAction
{
   private static final long serialVersionUID = 737842394413863495L;

   /**
    * Create an action object.
    */
   public OpenBusTraceAction()
   {
      super(I18n.getMessage("OpenBusTraceAction.name"), I18n.getMessage("OpenBusTraceAction.toolTip"),
            ImageCache.getIcon("icons/view-bus-trace"));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionEvent(ActionEvent event)
   {
      DevTool app = DevTool.getInstance();
      JFrame mainWin = app.getMainFrame();

      try
      {
         final Config cfg = Config.getInstance();
         String lastDir = cfg.getStringValue("busTrace.lastDir");

         final JFileChooser dlg = new JFileChooser();
         dlg.setSelectedFile(new File(lastDir));
         final FileFilter fileFilter = new BusTraceFileFilter();
         dlg.addChoosableFileFilter(fileFilter);
         dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
         dlg.setFileFilter(fileFilter);
         dlg.setDialogTitle(I18n.getMessage("OpenBusTraceAction.title"));

         if (dlg.showOpenDialog(mainWin) != JFileChooser.APPROVE_OPTION)
            return;

         final File file = dlg.getSelectedFile();
         if (file == null) return;

         cfg.put("busTrace.lastDir", file.getAbsolutePath());

         BusTraceViewer viewer = (BusTraceViewer) app.showPanel(BusTraceViewer.class);
         viewer.loadBusTrace(file);
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("OpenBusTraceAction.errShow"));
      }
   }
}

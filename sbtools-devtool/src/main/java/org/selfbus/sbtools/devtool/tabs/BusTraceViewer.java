package org.selfbus.sbtools.devtool.tabs;

import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.knxcom.gui.busmonitor.BusMonitorPanel;

/**
 * A panel that displays the contents of a bus trace file.
 */
public class BusTraceViewer extends BusMonitorPanel
{
   private static final long serialVersionUID = 1396873554372571138L;

   private JLabel fileNameLabel;

   /**
    * Create a bus trace viewer.
    * 
    * @see {@link #loadBusTrace(File)}.
    */
   public BusTraceViewer()
   {
      super();
      setName(I18n.getMessage("BusTraceViewer.title"));

      addDefaultToolBarButtons();

      toolBar.addSeparator();

      fileNameLabel = new JLabel();
      toolBar.add(fileNameLabel);
   }

   /**
    * Load the bus trace file and display it's contents.
    * 
    * @param file - the file to open
    * @throws IOException if reading the file failed
    */
   public void loadBusTrace(File file) throws IOException
   {
      super.loadBusTrace(file);
      fileNameLabel.setText(file.getName());
   }
}

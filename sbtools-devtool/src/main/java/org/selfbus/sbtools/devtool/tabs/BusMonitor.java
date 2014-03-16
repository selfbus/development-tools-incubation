package org.selfbus.sbtools.devtool.tabs;

import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.ProjectListener;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.selfbus.sbtools.knxcom.BusInterface;
import org.selfbus.sbtools.knxcom.BusInterfaceFactory;
import org.selfbus.sbtools.knxcom.emi.L_Data_con;
import org.selfbus.sbtools.knxcom.emi.L_Data_ind;
import org.selfbus.sbtools.knxcom.emi.types.EmiFrameType;
import org.selfbus.sbtools.knxcom.gui.busmonitor.BusMonitorPanel;
import org.selfbus.sbtools.knxcom.telegram.Telegram;
import org.selfbus.sbtools.knxcom.telegram.TelegramListener;

/**
 * An EIB bus monitor.
 */
public class BusMonitor extends BusMonitorPanel implements TelegramListener, ProjectListener
{
   private static final long serialVersionUID = -2293191694623284469L;

   private final BusInterface bus;

   /**
    * Create a bus monitor widget.
    */
   public BusMonitor()
   {
      super();
      setName(I18n.getMessage("BusMonitor.title"));

      bus = BusInterfaceFactory.getBusInterface();
      bus.addListener(this);

      DevTool.getInstance().getProjectService().addListener(this);

      toolBar.add(saveAction);
      toolBar.add(eraseAction);

      toolBar.addSeparator();
      addDefaultToolBarButtons();

      toolBar.addSeparator();
   }

   /**
    * Callback: a telegram was received.
    */
   @Override
   public void telegramReceived(Telegram telegram)
   {
      addFrame(new L_Data_ind(EmiFrameType.EMI2_L_DATA_IND, telegram));
   }

   /**
    * Callback: a telegram was sent.
    */
   @Override
   public void telegramSent(Telegram telegram)
   {
      addFrame(new L_Data_con(EmiFrameType.EMI2_L_DATA_CON, telegram));
   }

   /**
    * Callback: the project changed.
    */
   @Override
   public void projectChanged(Project project)
   {
   }
}

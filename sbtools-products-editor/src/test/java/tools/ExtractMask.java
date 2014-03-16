package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.misc.LookAndFeelManager;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.filter.ProductsFileFilter;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.vdio.ProductsReader;
import org.selfbus.sbtools.vdio.VdioException;
import org.selfbus.sbtools.vdio.model.VD;
import org.selfbus.sbtools.vdio.model.VdMask;
import org.selfbus.sbtools.vdio.model.VdMaskEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A small (and somewhat hacky) tool to extract mask and mask entry from a VD file.
 */
public class ExtractMask
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ExtractMask.class);
   private static final String homeDir = System.getProperty("user.home");

   private final JFrame mainWin = new JFrame();

   public ExtractMask()
   {
      mainWin.setSize(300, 200);
   }

   public File getVdFile()
   {
      final Config cfg = Config.getInstance();
      String lastDir = cfg.getStringValue("project.lastImportDir");

      final FileFilter fileFilter = new ProductsFileFilter();

      final JFileChooser dlg = new JFileChooser();
      dlg.setCurrentDirectory(new File(lastDir));
      dlg.addChoosableFileFilter(fileFilter);
      dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
      dlg.setFileFilter(fileFilter);
      dlg.setDialogTitle(I18n.getMessage("ImportProjectAction.title"));

      if (dlg.showOpenDialog(mainWin) != JFileChooser.APPROVE_OPTION)
         return null;

      File file = dlg.getSelectedFile();
      if (file != null)
         cfg.put("project.lastImportDir", file.getParent());

      return file;
   }

   public void putProp(Properties props, String key, Object value)
   {
      props.setProperty(key, value == null ? "" : value.toString());
   }

   public void writeMask(OutputStream out, VdMask m) throws IOException
   {
      Validate.notNull(out);
      Validate.notNull(m);

      Properties props = new Properties();

      putProp(props, "mask_version", m.getVersion());
      putProp(props, "user_ram_start", m.getUserRamStart());
      putProp(props, "user_ram_end", m.getUserRamEnd());
      putProp(props, "user_eeprom_start", m.getUserEepromStart());
      putProp(props, "user_eeprom_end", m.getUserEepromEnd());
      putProp(props, "run_error_address", m.getRunErrorAddress());
      putProp(props, "address_tab_address", m.getAddressTabAddress());
      putProp(props, "assoctabptr_address", m.getAssocTabPtrAddress());
      putProp(props, "commstabptr_address", m.getCommsTabPtrAddress());
      putProp(props, "manufacturer_data_address", m.getManufacturerDataAddress());
      putProp(props, "manufacturer_data_size", m.getManufacturerDataSize());
      putProp(props, "manufacturer_id_address", m.getManufacturerIdAddress());
      putProp(props, "routecnt_address", m.getRouteCountAddress());
      putProp(props, "manufacturer_id_protected", m.isManufacturerIdProtected());
      putProp(props, "mask_version_name", m.getMaskVersionName());
      putProp(props, "mask_eeprom_data", m.getData() == null ? "" : DatatypeConverter.printHexBinary(m.getData()));
      putProp(props, "mask_data_length", m.getData() == null ? 0 : m.getData().length);
      putProp(props, "address_tab_lcs", m.getAddressTabLCS());
      putProp(props, "assoc_tab_lcs", m.getAssocTabLCS());
      putProp(props, "application_program_lcs", m.getApplicationProgramLCS());
      putProp(props, "pei_program_lcs", m.getPeiProgramLCS());
      putProp(props, "load_control_address", m.getLoadControlAddress());
      putProp(props, "run_control_address", m.getRunControlAddress());
      putProp(props, "external_memory_start", m.getExternalMemoryStart());
      putProp(props, "external_memory_end", m.getExternalMemoryEnd());
      putProp(props, "application_prgroam_rcs", m.getApplicationProgramRCS());
      putProp(props, "pei_program_rcs", m.getPeiProgramRCS());
      putProp(props, "port_a_ddr", m.getPortADdr());
      putProp(props, "port_address_protected", m.isPortAddressProtected());

      props.store(out, "Mask version 0x" + Integer.toHexString(m.getVersion()));
   }

   public void writeMaskEntries(OutputStream out, VdMask mask, List<VdMaskEntry> entries) throws IOException
   {
      Validate.notNull(out);
      Validate.notNull(mask);
      Validate.notNull(entries, "The VD contains no mask entries");

      Properties props = new Properties();
      int maskId = mask.getId();

      for (VdMaskEntry entry : entries)
      {
         if (entry.getMaskId() == maskId)
            putProp(props, entry.getName(), entry.getAddress());
      }

      props.store(out, "Mask entries for mask version 0x" + Integer.toHexString(mask.getVersion()));
   }

   public void run() throws VdioException, IOException
   {
      File entriesFile, maskFile;

      File vdFile = getVdFile();
      if (vdFile == null) return;

      ProductsReader reader = new ProductsReader(ProdEdit.getInstance().getMainFrame());
      reader.setZipPassword(Config.getInstance().get("zipPassword"));

      VD vd = reader.read(vdFile);
      if (vd == null) return;
      
      Validate.notNull(vd.masks, "The VD contains no masks");
      for (VdMask mask : vd.masks)
      {
         String maskVersion = Integer.toHexString(mask.getVersion());

         maskFile = new File(homeDir + "/mask_" + maskVersion + ".properties");
         LOGGER.info("Writing mask 0x{} to {}", maskVersion, maskFile);
         writeMask(new FileOutputStream(maskFile), mask);

         try
         {
            entriesFile = new File(homeDir + "/mask_entries_" + maskVersion + ".properties");
            LOGGER.info("Writing mask 0x{} entries to {}", maskVersion, entriesFile);
            writeMaskEntries(new FileOutputStream(entriesFile), mask, vd.maskEntries);
         }
         catch (RuntimeException e)
         {
            Dialogs.showExceptionDialog(e, "Failed to create mask entries file");
            entriesFile = null;
         }

         JOptionPane.showMessageDialog(null, "Wrote files for mask 0x" + maskVersion +
            ":\n\nMask: " + maskFile + "\nMask entries: " + entriesFile);
      }
   }

   public static void main(String[] args)
   {
      try
      {
         LookAndFeelManager.setDefaultLookAndFeel();
   
         ExtractMask main = new ExtractMask();
         main.run();
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, "Program failed");
      }

      System.exit(0);
   }
}

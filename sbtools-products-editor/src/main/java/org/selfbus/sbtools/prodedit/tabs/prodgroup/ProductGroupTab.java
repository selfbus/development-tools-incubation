package org.selfbus.sbtools.prodedit.tabs.prodgroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInListAction;
import org.selfbus.sbtools.prodedit.filter.HeaderFileFilter;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.AbstractProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectListener;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.tabs.internal.AbstractCloseableAccordionDetailsTab;
import org.selfbus.sbtools.prodedit.tabs.internal.MixedCategoryElem;
import org.selfbus.sbtools.prodedit.tabs.internal.ObjectActivatedListener;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.general.ApplicationProgramElem;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.general.VirtualDeviceElem;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.memory.MemoryElem;
import org.selfbus.sbtools.prodedit.tabs.prodgroup.parameter.ParametersElem;
import org.selfbus.sbtools.prodedit.utils.FontUtils;
import org.selfbus.sbtools.prodedit.utils.HeaderFileGenerator;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;

/**
 * A tab panel for editing a {@link ProductGroup products group}.
 */
public class ProductGroupTab extends AbstractCloseableAccordionDetailsTab
{
   private static final long serialVersionUID = -7440697803186717305L;

   // The product group that the tab displays
   private final ProductGroup group;

   private SelectionInList<VirtualDevice> selectionInList = new SelectionInList<VirtualDevice>(new LinkedList<VirtualDevice>());
   @SuppressWarnings("unchecked")
   private final JComboBox<VirtualDevice> currentDeviceCombo = BasicComponentFactory.createComboBox(selectionInList, new VirtualDeviceListCellRenderer()); 

   private JButton addDeviceButton, duplicateDeviceButton, removeDeviceButton, generateHeaderFileButton;

   private final VirtualDeviceElem virtualDeviceElem;
   private final ApplicationProgramElem applicationProgramElem;
   private final ParameterTypesElem parameterTypesElem;
   private final ParametersElem parametersElem;
   private final MemoryElem memoryElem;

   /**
    * Create a tab panel for editing a {@link ProductGroup}.
    * 
    * @param group - the product group to edit.
    */
   public ProductGroupTab(ProductGroup group)
   {
      super();

      setupTopToolBar();

      this.group = group;
      setTitle(group.getName());

      MixedCategoryElem mixed = new MixedCategoryElem(I18n.getMessage("ProductGroupTab.generalName"));
      addCategory(mixed);

      virtualDeviceElem = new VirtualDeviceElem(group, selectionInList); 
      mixed.addCategory(virtualDeviceElem);

      applicationProgramElem = new ApplicationProgramElem(group);
      mixed.addCategory(applicationProgramElem);

      parameterTypesElem = new ParameterTypesElem(group);
      addCategory(parameterTypesElem);

      parametersElem = new ParametersElem(group);
      addCategory(parametersElem);

      memoryElem = new MemoryElem(group);
      addCategory(memoryElem);

      ProdEdit.getInstance().getProjectService().addListener(projectListener);

      selectionInList.getSelectionHolder().addValueChangeListener(new PropertyChangeListener()
      {
         @Override
         public void propertyChange(PropertyChangeEvent e)
         {
            VirtualDevice device = selectionInList.getSelection();

            applicationProgramElem.setDevice(device);
            parameterTypesElem.setDevice(device);
            parametersElem.setDevice(device);
            memoryElem.setDevice(device);
         }
      });

      memoryElem.setParamActivatedListener(objectActivatedListener);

      updateContents();

      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            if (currentDeviceCombo.getItemCount() > 0)
               currentDeviceCombo.setSelectedIndex(0);
         }
      });
   }

   /**
    * A renderer for the list cells in the virtual-device selection combo box.
    */
   class VirtualDeviceListCellRenderer extends BasicComboBoxRenderer
   {
      private static final long serialVersionUID = 7640233499086879012L;

      @Override
      public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list,
         Object value, int index, boolean isSelected, boolean cellHasFocus)
      {
         if (value instanceof VirtualDevice)
         {
            VirtualDevice dev = (VirtualDevice) value;
            value = dev.getName();

            ApplicationProgram program = group.getProgram(dev.getProgramId());
            if (program != null)
               value += ": " + program.getName();
         }

         return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
   };

   /**
    * A listener for object activation.
    */
   private final ObjectActivatedListener objectActivatedListener = new ObjectActivatedListener()
   {
      @Override
      public void objectActivated(Object o)
      {
         if (o instanceof AbstractParameterNode)
         {
            setVisibleCategory(parametersElem);
            parametersElem.setSelected((AbstractParameterNode) o);
         }
//         else if (o instanceof ParameterType)
//         {
//            setVisibleCategory(parameterTypesElem);
//            parameterTypesElem.setSelected((ParameterType) o);
//         }
      }
   };
   
   /**
    * Setup the top tool bar.
    */
   protected void setupTopToolBar()
   {
      JToolBar toolBar = new JToolBar();
      add(toolBar, BorderLayout.NORTH);

      JLabel lbl = new JLabel(I18n.getMessage("ProductGroupTab.currentDevice") + ':');
      lbl.setFont(FontUtils.getSubCaptionFont());
      toolBar.add(lbl);
      toolBar.add(Box.createHorizontalStrut(8));

      currentDeviceCombo.setPreferredSize(new Dimension(400, currentDeviceCombo.getPreferredSize().height));
      toolBar.add(currentDeviceCombo);

      addDeviceButton = new JButton(addDeviceAction);
      addDeviceButton.setText(null);
      toolBar.add(addDeviceButton);

      duplicateDeviceButton = new JButton(duplicateDeviceAction);
      duplicateDeviceButton.setText(null);
      toolBar.add(duplicateDeviceButton);

      removeDeviceButton = new JButton(new RemoveSelectionInListAction(selectionInList, I18n.getMessage("ProductGroupTab.removeDeviceTip"), ImageCache.getIcon("icons/editdelete")));
      removeDeviceButton.setText(null);
      toolBar.add(removeDeviceButton);

      toolBar.addSeparator();

      generateHeaderFileButton = new JButton(generateHeaderFileAction);
      generateHeaderFileButton.setText(null);
      toolBar.add(generateHeaderFileButton);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      ProdEdit.getInstance().getProjectService().removeListener(projectListener);
      super.close();
   }

   /**
    * Update the contents.
    */
   protected void updateContents()
   {
      selectionInList.setList(group.getDevices());
   }

   /**
    * Switch to the parameter types category and show the given parameter type.
    *
    * @param type - the parameter type to make visible.
    */
   public void showParameterType(ParameterType type)
   {
      setVisibleCategory(parameterTypesElem);
      parameterTypesElem.setSelected(type);
   }

   /**
    * Action: add a new device.
    */
   private final BasicAction addDeviceAction = new BasicAction("add", I18n.getMessage("ProductGroupTab.addDeviceTip"), ImageCache.getIcon("icons/filenew"))
   {
      private static final long serialVersionUID = 1;

      @Override
      public void actionEvent(ActionEvent event)
      {
         Project project = ProdEdit.getInstance().getProject();
         if (project != null)
            selectionInList.setSelection(group.createDevice());
      }
   };

   /**
    * Action: duplicate a device.
    */
   private final BasicAction duplicateDeviceAction = new BasicAction("duplicate", I18n.getMessage("ProductGroupTab.duplicateDeviceTip"), ImageCache.getIcon("icons/editcopy"))
   {
      private static final long serialVersionUID = 1;

      @Override
      public void actionEvent(ActionEvent event)
      {
         Project project = ProdEdit.getInstance().getProject();
         if (project != null)
         {
            VirtualDevice device = group.createDevice();
            selectionInList.setSelection(device);
         }
      }
   };

   /**
    * Action: Generates a C/C++ header file from the virtual device.
    */
   private final BasicAction generateHeaderFileAction = new BasicAction("generateHeaderFile", I18n.getMessage("ProductGroupTab.generateHeaderFileTip"), ImageCache.getIcon("icons/filesaveas"))
   {
      private static final long serialVersionUID = 1;

      @Override
      public void actionEvent(ActionEvent event)
      {
         final Config cfg = Config.getInstance();
         String last = cfg.getStringValue("generateHeaderFile.last");

         final JFileChooser dlg = new JFileChooser();
         dlg.setCurrentDirectory(new File(last).getParentFile());
         dlg.setSelectedFile(new File(last));
         final FileFilter fileFilter = new HeaderFileFilter();
         dlg.addChoosableFileFilter(fileFilter);
         dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
         dlg.setFileFilter(fileFilter);
         dlg.setDialogTitle(I18n.getMessage("ProductGroupTab.generateHeaderFileTitle"));

         if (dlg.showOpenDialog(ProdEdit.getInstance().getMainFrame()) != JFileChooser.APPROVE_OPTION)
            return;

         File file = dlg.getSelectedFile();
         if (file == null) return;

         cfg.put("generateHeaderFile.last", file.getAbsolutePath());

         HeaderFileGenerator generator = new HeaderFileGenerator();
         try
         {
            generator.write(group, selectionInList.getSelection(), file);
            ProdEdit.getInstance().setStatusMessage(I18n.formatMessage("ProductGroupTab.generateHeaderFileDone", file.getName()));
         }
         catch (IOException e)
         {
            Dialogs.showExceptionDialog(e, I18n.getMessage("ProductGroupTab.generateHeaderFailed"));
         }
      }
   };

   /**
    * Private project listener
    */
   private final ProjectListener projectListener = new AbstractProjectListener()
   {
      @Override
      public void productGroupChanged(ProductGroup group)
      {
         if (ProductGroupTab.this.group == group)
         {
            setName(group.getName());
         }
      }
   };
}

package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.selfbus.sbtools.common.gui.utils.TableUtils;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.global.Mask;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.DataBlock;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ProgramAdapter;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ProgramAdapterFactory;
import org.selfbus.sbtools.prodedit.renderer.ParameterMemoryListCellRenderer;
import org.selfbus.sbtools.prodedit.tabs.internal.AbstractCategoryElem;
import org.selfbus.sbtools.prodedit.tabs.internal.ObjectActivatedListener;
import org.selfbus.sbtools.prodedit.utils.FontUtils;

/**
 * An element that displays the memory layout of a device.
 */
public class MemoryElem extends AbstractCategoryElem
{
   private final ProductGroup group;
   private VirtualDevice device;
   private Integer selectedAddress;

   private final DefaultListModel<AbstractParameterNode> paramListModel = new DefaultListModel<AbstractParameterNode>();
   private final JList<AbstractParameterNode> paramList = new JList<AbstractParameterNode>(paramListModel);
   private final Set<MemoryRange> ranges = new TreeSet<MemoryRange>();
   private final MemoryTableModel tableModel = new MemoryTableModel(0, 512);
   private final JTable table = new JTable(tableModel);
   private final JScrollPane tableScrollPane = new JScrollPane(table);
   private final MemoryCellDetailsTableModel cellDetailsModel = new MemoryCellDetailsTableModel();
   private final JTable cellDetailsTable = new JTable(cellDetailsModel);
   private final JScrollPane cellDetailsPane = new JScrollPane(cellDetailsTable);
   private final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
   private final JLabel cellDetailsCaption = new JLabel();
   private Color backgroundColor;

   private ObjectActivatedListener paramActivatedListener;

   /**
    * Create an element that displays the memory layout of a device.
    * 
    * @param group - the product group
    */
   public MemoryElem(final ProductGroup group)
   {
      this.group = group;

      ParameterMemoryListCellRenderer paramListCellRenderer =
         new ParameterMemoryListCellRenderer(paramList.getCellRenderer());
      paramList.setCellRenderer(paramListCellRenderer);
      listScrollPane = new JScrollPane(paramList);

      table.setDefaultRenderer(MemoryCell.class, new MemoryCellTableRenderer());
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.setCellSelectionEnabled(true);
      table.setDoubleBuffered(false);
      TableUtils.pack(table, 4);

      JPanel memoryPanel = new JPanel(new BorderLayout());

      JLabel lbl = new JLabel(I18n.getMessage("MemoryElem.caption"));
      lbl.setFont(FontUtils.getCaptionFont());
      memoryPanel.add(lbl, BorderLayout.NORTH);
      memoryPanel.add(tableScrollPane, BorderLayout.CENTER);

      detailsPanel = new JPanel(new BorderLayout());
      detailsPanel.add(splitPane, BorderLayout.CENTER);

      JPanel cellDetailsPanel = new JPanel(new BorderLayout());
      cellDetailsPanel.add(cellDetailsCaption, BorderLayout.NORTH);
      cellDetailsPanel.add(cellDetailsPane, BorderLayout.CENTER);
      cellDetailsCaption.setFont(FontUtils.getCaptionFont());

      cellDetailsTable.setBorder(null);
      cellDetailsTable.setTableHeader(null);
      cellDetailsTable.setShowGrid(false);
      cellDetailsTable.setOpaque(false);;
      cellDetailsTable.setIntercellSpacing(new Dimension(0, 0));
      cellDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
//      cellDetailsTable.setBackground(cellDetailsPanel.getBackground());
//      cellDetailsPane.getViewport().setOpaque(false);

      splitPane.setLeftComponent(memoryPanel);
      splitPane.setRightComponent(cellDetailsPanel);
      splitPane.setContinuousLayout(true);

      detailsPanel.addComponentListener(new ComponentAdapter()
      {
         private boolean init = true;

         public void componentResized(ComponentEvent e)
         {
            if (init)
            {
               init = false;
               splitPane.setDividerLocation(0.7);
            }
         }
      });

      backgroundColor = table.getBackground();

      updateContents();

      cellDetailsTable.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2 && selectedAddress != null)
            {
               Object obj = cellDetailsModel.getValueAt(cellDetailsTable.getSelectedRow()); 
               if (obj instanceof AbstractParameterNode)
                  paramActivatedListener.objectActivated(obj);
            }
         }
      });

      paramList.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2)
               paramActivatedListener.objectActivated(paramList.getSelectedValue());
         }
      });

      paramList.addListSelectionListener(new ListSelectionListener()
      {
         @Override
         public void valueChanged(ListSelectionEvent e)
         {
            AbstractParameterNode node = paramList.getSelectedValue();
            if (node != null && !e.getValueIsAdjusting())
            {
               selectedAddress = node.getAddress();
               updateCellDetails();

               int row = node.getAddress() >> 4;
               int col = (node.getAddress() & 15) + 1;

               table.setRowSelectionInterval(row, row);
               table.setColumnSelectionInterval(col, col);
            }
         }
      });

      listScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

      table.getColumnModel().getSelectionModel().addListSelectionListener(tableSelectionListener);
      table.getSelectionModel().addListSelectionListener(tableSelectionListener);
   }

   /**
    * A cell of the table was clicked.
    */
   private final ListSelectionListener tableSelectionListener = new ListSelectionListener()
   {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
         if (e.getValueIsAdjusting())
            return;

         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               int row = table.getSelectionModel().getMinSelectionIndex();
               int col = table.getColumnModel().getSelectionModel().getMinSelectionIndex() - 1;
               Integer addr = null;

               if (row >= 0 && col >= 0)
                  addr = (row << 4) + col;

               if (addr != selectedAddress)
               {
                  selectedAddress = addr;
                  updateCellDetails();
               }
            }
         });
      }
   };

   /**
    * Set the listener that is called when a parameter or com-object is double clicked.
    * 
    * @param listener - the listener to set
    */
   public void setParamActivatedListener(ObjectActivatedListener listener)
   {
      this.paramActivatedListener = listener;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("MemoryElem.title");
   }

   /**
    * Set the virtual device.
    *
    * @param device - the device to set
    */
   public void setDevice(VirtualDevice device)
   {
      this.device = device;
      updateContents();
      tableModel.unsetModified();
   }

   /**
    * Update the cell details view.
    */
   protected void updateCellDetails()
   {
      cellDetailsModel.clear();
      if (selectedAddress == null)
      {
         cellDetailsCaption.setText("");
         return;
      }
      
      MemoryCell cell = tableModel.getValueAt(selectedAddress);

      cellDetailsCaption.setText(I18n.formatMessage("MemoryElem.cellDetailsCaption",
         String.format("$%0$04x", selectedAddress)));

      for (Object obj : cell.getObjects())
         cellDetailsModel.add(obj);

      TableUtils.pack(cellDetailsTable, MemoryCellDetailsTableModel.ICON_COL, 4, true);
      TableUtils.pack(cellDetailsTable, MemoryCellDetailsTableModel.ID_COL, 4, true);
      TableUtils.pack(cellDetailsTable, 4);
   }

   /**
    * Update the contents.
    */
   protected void updateContents()
   {
      ranges.clear();
      tableModel.clear();
      paramListModel.removeAllElements();
      selectedAddress = null;

      Project project = ProdEdit.getInstance().getProjectService().getProject();

      if (project == null || device == null)
         return;

      ApplicationProgram program = group.getProgram(device);
      Mask mask = project.getMask(program.getMaskVersion());
      ProgramAdapter adapter = ProgramAdapterFactory.getProgramAdapter(program, mask);

      int maxAddr = Math.max(mask.getUserRamEnd(), mask.getUserEepromEnd());
      if (maxAddr == 0) maxAddr = 65535;

      tableModel.setSize((maxAddr + 15) & ~15);

      int start;

      start = mask.getUserRamStart();
      createRange(start, mask.getUserRamEnd() - start + 1, I18n.getMessage("MemoryElem.userRam"));

      start = mask.getUserEepromStart();
      createRange(start, mask.getUserEepromEnd() - start + 1, I18n.getMessage("MemoryElem.userEeprom"));

      start = program.getAssocTabAddr();
      createRange(start, program.getAssocTabSize(), I18n.getMessage("MemoryElem.assocTab"));

      start = program.getCommsTabAddr();
      createRange(start, program.getCommsTabSize(), I18n.getMessage("MemoryElem.commsTab"));

      start = mask.getAddressTabAddr();
      createRange(start, program.getAddrTabSize(), I18n.getMessage("MemoryElem.addrTab"));

      start = adapter.getRamFlagTabAddr();
      createRange(start, adapter.getCommsTab().size() >> 1, I18n.getMessage("MemoryElem.ramFlagsTab"));

      updateMemoryCellRanges();
      setMemoryCellValues(program, mask);
      addParameterRanges();

      sortParamList();
      updateCellDetails();
   }

   /**
    * Set the memory cell values.
    */
   protected void setMemoryCellValues(ApplicationProgram program, Mask mask)
   {
      setMemoryCellValues(mask.getUserEepromStart(), mask.getData()); 
      setMemoryCellValues(mask.getUserEepromStart(), program.getEepromData()); 

      for (DataBlock block : program.getDataBlocks())
      {
         setMemoryCellValues(block.getSegmentAddr(), block.getData());
      }
   }

   /**
    * Set the memory cell values.
    * 
    * @param addr - the start address of the data
    * @param data - the data bytes
    */
   protected void setMemoryCellValues(Integer addr, byte[] data)
   {
      if (addr == null || data == null)
         return;

      int count = data.length;
      if (addr + count >= tableModel.getStartAddr() + tableModel.getSize())
         count = tableModel.getSize() - (addr - tableModel.getStartAddr());

      for (int idx = 0; idx < count; ++idx)
         tableModel.getValueAt(addr + idx).setValue(data[idx] & 255);
   }

   /**
    * Sort the list of parameters.
    */
   protected void sortParamList()
   {
      AbstractParameterNode[] arr = new AbstractParameterNode[paramListModel.size()];
      paramListModel.copyInto(arr);

      Arrays.sort(arr, new Comparator<AbstractParameterNode>()
      {
         @Override
         public int compare(AbstractParameterNode a, AbstractParameterNode b)
         {
            return a.getAddress() - b.getAddress();
         }
      });

      paramListModel.removeAllElements();
      for (AbstractParameterNode elem : arr)
         paramListModel.addElement(elem);
   }

   /**
    * Add memory ranges for parameters and com-objects.
    */
   protected void addParameterRanges()
   {
      MemoryRange paramRange = createRange(0, 0, I18n.getMessage("MemoryElem.param"));
      MemoryRange comObjRange = createRange(0, 0, I18n.getMessage("MemoryElem.comObject"));

      ApplicationProgram program = group.getProgram(device);
      addParameterRanges(program.getParameterTreeModel().getRoot(), paramRange, comObjRange);
   }

   /**
    * Add memory ranges for parameters and com-objects of params and its children.
    * 
    * @param params - the parameter container to process
    * @param paramRange - the memory range for parameters
    * @param comObjRange - the memory range for com-objects
    */
   protected void addParameterRanges(AbstractParameterNode parentNode, MemoryRange paramRange, MemoryRange comObjRange)
   {
      Enumeration<AbstractParameterNode> it = parentNode.children();
      while (it.hasMoreElements())
      {
         AbstractParameterNode node = it.nextElement();

         if (!node.isLeaf())
            addParameterRanges(node, paramRange, comObjRange);

         Integer addr = node.getAddress();
         if (addr == null || addr == 0)
            continue;

         MemoryRange range = null;
         int size = 1;
         String key;

         if (node instanceof Parameter)
         {
            Parameter param = (Parameter) node;

            range = paramRange;
            size = (param.getSize() + 7) >> 3;
            key = createKey(node.getId(), param.getBitOffset(), param.getSize());

            paramListModel.addElement(node);
         }
         else if (node instanceof CommunicationObject)
         {
            CommunicationObject comObj = (CommunicationObject) node;

            range = comObjRange;
            size = (comObj.getType().getBitSize() + 7) >> 3;
            key = createKey(node.getId(), 0, comObj.getType().getBitSize());

            paramListModel.addElement(node);
         }
         else key = node.toString();

         if (range != null)
         {
            for (int i = 0; i < size; ++i)
            {
               MemoryCell cell = tableModel.getValueAt(addr + i);
               cell.setRange(range);
               cell.addObject(node, key);
            }
         }
      }
   }
   
   /**
    * Update the memory ranges of all memory cells. Call when the memory ranges
    * have changed.
    */
   public void updateMemoryCellRanges()
   {
      final int addr0 = tableModel.getStartAddr();
      final int sz = tableModel.getSize();

      // Clear the memory range of all memory cells
      for (int i = sz - 1; i >= 0; --i)
         tableModel.getValueAt(addr0 + i).setRange(null);

      // Apply the memory ranges to the corresponding memory cells
      for (MemoryRange range : ranges)
      {
         final int start = range.getStart();
         for (int j = range.getSize() - 1; j >= 0; --j)
         {
            MemoryCell cell = tableModel.getValueAt(start + j);

            if (cell.getRange() == null)
            {
               cell.setRange(range);
               cell.addObject(range, createKey(range.getStart(), -1, range.getSize()));
            }
         }
      }
   }

   /**
    * Create a key for {@link MemoryCell#addObject}
    * 
    * @param id - the ID
    * @param offs - the bit offset
    * @param size - the size
    * @return The key.
    */
   protected String createKey(int id, int offs, int size)
   {
      if (size > 7 && offs == 0)
         offs = 8;
         
      return String.format("%1$d-%2$04d-%3$06x", offs, size, id);
   }

   /**
    * Create a memory range and add it to the memory ranges.
    *
    * @param start - the start address of the range.
    * @param size - the size of the range.
    * @param name - the name of the range.
    * @return The created memory range.
    */
   public MemoryRange createRange(int start, int size, String name)
   {
      int idx = ranges.size();

      // avoid light grey
      if (idx >= 7)
         ++idx;

      int r = (idx & 1) | ((idx & 8) >> 2) | ((idx & 64) >> 4);
      int g = ((idx & 2) >> 1) | ((idx & 16) >> 3) | ((idx & 128) >> 5);
      int b = ((idx & 4) >> 2) | ((idx & 32) >> 4) | ((idx & 256) >> 6);
      int offs = -36;

      r = (int) (backgroundColor.getRed() * 0.8f) + (r << 6) + offs;
      if (r > 255)
         r -= 255;
      else if (r < 0)
         r = 0;

      g = (int) (backgroundColor.getGreen() * 0.8f) + (g << 6) + offs;
      if (g > 255)
         g = 255;
      else if (g < 0)
         g = 0;

      b = (int) (backgroundColor.getBlue() * 0.8f) + (b << 6) + offs;
      if (b > 255)
         b = 255;
      else if (b < 0)
         b = 0;

      final MemoryRange range = new MemoryRange(start, size, name, new Color(r, g, b));
      ranges.add(range);
      return range;
   }
}

package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameditor;

import java.awt.Component;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterContainer;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.tabs.internal.CategoryElem;
import org.selfbus.sbtools.prodedit.utils.FontUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An element that displays a parameters editor that simulates editing parameters like in ETS.
 */
public class ParamEditorElem implements CategoryElem
{
   private final ProductGroup group;
   private VirtualDevice device;

   private final JPanel detailsPanel;
   private final DefaultListModel<Parameter> pagesListModel = new DefaultListModel<Parameter>();
   private final JList<Parameter> pagesList = new JList<Parameter>(pagesListModel);
   private final Map<Parameter,Object> paramValues = new HashMap<Parameter,Object>();

   /**
    * Create a {@link VirtualDevice virtual device} display element.
    * 
    * @param group - the product group
    */
   public ParamEditorElem(ProductGroup group)
   {
      this.group = group;

      FormLayout layout = new FormLayout("6dlu, l:p, 3dlu, f:p:g, 6dlu",
         "8dlu, p, 6dlu, f:p:g, 3dlu, f:p:g, 3dlu");

      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();
      JLabel lbl;

      int row = 2;
      lbl = builder.addLabel(I18n.getMessage("ParamEditorTester.caption"), cc.rcw(row, 2, 3));
      lbl.setFont(FontUtils.getCaptionFont());
      lbl.setOpaque(false);

      row = 4;
      pagesList.setCellRenderer(new ParameterListCellRenderer());
      builder.add(new JScrollPane(pagesList), cc.rc(row, 2));

      detailsPanel = builder.build();
      updateContents();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("ParamEditorTester.title");
   }

   /**
    * @return Null as there is no list panel.
    */
   @Override
   public JComponent getListPanel()
   {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getDetailsPanel()
   {
      return detailsPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JToolBar getToolBar()
   {
      return null;
   }

   /**
    * Process a parameter node and it's children
    * 
    * @param node - the parameter node to process
    */
   void processParameter(AbstractParameterNode node)
   {
      if (node instanceof Parameter)
      {
         Parameter param = (Parameter) node;
         if (param.getCategory() == ParameterCategory.PAGE)
            pagesListModel.addElement(param);
      }

      if (node instanceof AbstractParameterContainer)
      {
         Enumeration<AbstractParameterNode> childs = ((AbstractParameterContainer) node).children();
         while (childs.hasMoreElements())
         {
            processParameter(childs.nextElement());
         }
      }
   }

   /**
    * Update the contents.
    */
   protected void updateContents()
   {
      pagesListModel.clear();
      paramValues.clear();

      ApplicationProgram program = device == null ? null : group.getProgram(device);
      if (program != null)
      {
         processParameter(program.getParameterRoot());
      }
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
   }

   /**
    * List cell renderer for a JList of parameters.
    */
   static class ParameterListCellRenderer extends DefaultListCellRenderer
   {
      private static final long serialVersionUID = 1321779246000913731L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
      {
         if (value instanceof Parameter)
         {
            Parameter param = (Parameter) value;
            value = param.getDescription().getDefaultText();
         }

         return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
   }
}

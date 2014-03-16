package org.selfbus.sbtools.prodedit.tabs.prodgroup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.MoveNextSelectionInListAction;
import org.selfbus.sbtools.prodedit.actions.MovePrevSelectionInListAction;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInListAction;
import org.selfbus.sbtools.prodedit.binding.IntegerValueConverter;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;
import org.selfbus.sbtools.prodedit.renderer.ParameterValueListCellRenderer;
import org.selfbus.sbtools.prodedit.utils.MultiLingualTextUtil;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A panel that displays the values of a parameter type.
 */
public class ParameterTypeValues extends JPanel implements CloseableComponent
{
   private static final long serialVersionUID = 8942156798705140445L;

   private ParameterType paramType;
   private Map<String, PropertyAdapter<MultiLingualText.Element>> labelElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element> >();

   private final JSplitPane splitPane;
   private final JToolBar toolBar = new JToolBar();

   private SelectionInList<ParameterValue> selectionInList = new SelectionInList<ParameterValue>(new LinkedList<ParameterValue>());
   @SuppressWarnings("unchecked")
   private final JList<ParameterValue> valuesList = BasicComponentFactory.createList(selectionInList);
   private final JScrollPane valuesScrollPane = new JScrollPane(valuesList);
   private ArrayListModel<ParameterValue> values;

   private final PresentationModel<ParameterValue> detailsModel = new PresentationModel<ParameterValue>(selectionInList);
   private final IntegerValueConverter intValueConverter = new IntegerValueConverter();

   private final AbstractValueModel orderValue =  detailsModel.getModel("order");
   private final JLabel orderField = BasicComponentFactory.createLabel(new ConverterValueModel(orderValue, intValueConverter));

   private final AbstractValueModel valueValue =  detailsModel.getModel("intValue");
   private final JTextComponent valueField = BasicComponentFactory.createTextField(new ConverterValueModel(valueValue, intValueConverter), false);

   /**
    * Create a panel that displays the values of a parameter type.
    * 
    * @param paramTypeModel - the model of the parameter type of which the values
    *           are displayed.
    */
   public ParameterTypeValues(final ValueModel paramTypeModel)
   {
      super();
      setLayout(new BorderLayout(0, 0));

      ParameterValueListCellRenderer valuesListRenderer = new ParameterValueListCellRenderer(valuesList.getCellRenderer());
      valuesListRenderer.setShowValue(true);
      valuesList.setCellRenderer(valuesListRenderer);

      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      add(splitPane, BorderLayout.CENTER);

      JPanel leftPanel = new JPanel();
      leftPanel.setLayout(new BorderLayout());
      leftPanel.add(toolBar, BorderLayout.NORTH);
      leftPanel.add(valuesScrollPane, BorderLayout.CENTER);
      splitPane.setLeftComponent(leftPanel);

      setupToolBar();
      setupDetails();

      selectionInList.addValueChangeListener(new PropertyChangeListener()
      {
         @Override
         public void propertyChange(PropertyChangeEvent e)
         {
            ParameterValue paramValue = (ParameterValue) e.getNewValue();
            if (paramValue == null) return;

            MultiLingualText label = paramValue.getLabel();
            for (Entry<String, PropertyAdapter<MultiLingualText.Element>> entry : labelElems.entrySet())
            {
               entry.getValue().setBean(label.getElement(entry.getKey()));
            }
         }
      });

      paramTypeModel.addValueChangeListener(new PropertyChangeListener()
      {
         @Override
         public void propertyChange(PropertyChangeEvent evt)
         {
            paramType = (ParameterType) paramTypeModel.getValue();

            splitPane.setDividerLocation(0.4);

            if (values != null)
               values.removeListDataListener(valuesListener);

            values = paramType.getValues();
            selectionInList.setList(values);

            if (values != null && !values.isEmpty())
               selectionInList.setSelection(values.get(0));

            if (values != null)
               values.addListDataListener(valuesListener);
         }
      });

      ProdEdit.getInstance().getProject().getLanguages().addListDataListener(languagesListener);
   }
   
   protected void setupDetails()
   {
      FormLayout layout = new FormLayout("2dlu, l:p, 4dlu, f:p:g, 2dlu", 
         "4dlu, p, 8dlu, p, 8dlu, p, 4dlu, p, 4dlu");
      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();

      int row = 2;
      builder.addLabel(I18n.getMessage("ParameterTypeValue.name") + ':', cc.rcw(row, 2, 3));

      row = 4;
      builder.addLabel(I18n.getMessage("ParameterTypeValue.value"), cc.rc(row, 2));
      builder.add(valueField, cc.rc(row, 4));

      row = 6;
      builder.addLabel(I18n.getMessage("ParameterTypeValue.order"), cc.rc(row, 2));
      builder.add(orderField, cc.rc(row, 4));

      splitPane.setRightComponent(new JScrollPane(builder.build()));

      labelElems = MultiLingualTextUtil.createFormElements(builder, 3);
   }

   /**
    * Setup the tool bar.
    */
   private void setupToolBar()
   {
      //
      //  Action: add a value
      //
      toolBar.add(new BasicAction("add", I18n.getMessage("ParameterTypeValues.addTip"), ImageCache.getIcon("icons/add"))
      {
         private static final long serialVersionUID = 1;

         @Override
         public void actionEvent(ActionEvent event)
         {
            if (paramType != null)
               selectionInList.setSelection(paramType.createValue());
         }
      });

      //
      //  Action: remove the current value
      //
      toolBar.add(new RemoveSelectionInListAction(selectionInList, I18n.getMessage("ParameterTypeValues.removeTip"))); 

      //
      //  Action: move the current value one step towards the beginning of the list.
      //
      toolBar.add(new MovePrevSelectionInListAction(selectionInList)); 

      //
      //  Action: move the current value one step towards the end of the list.
      //
      toolBar.add(new MoveNextSelectionInListAction(selectionInList)); 
   }

   /**
    * Update the display order of the values.
    */
   private void updateOrder()
   {
      if (values == null)
         return;

      int order = 0;
      for (ParameterValue paramValue : values)
      {
         paramValue.setOrder(++order);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      ProdEdit.getInstance().getProject().getLanguages().removeListDataListener(languagesListener);
   }

   /**
    * Update or recreate the details panel.
    */
   protected void updateDetailsPanel()
   {
      setupDetails();

      ParameterValue paramValue = selectionInList.getSelection();
      selectionInList.setSelection(null);
      selectionInList.setSelection(paramValue);
   }

   /**
    * A listener for project language changes 
    */
   private final ListDataListener languagesListener = new ListDataListener()
   {
      @Override
      public void intervalRemoved(ListDataEvent e)
      {
         updateDetailsPanel();
      }
      
      @Override
      public void intervalAdded(ListDataEvent e)
      {
         updateDetailsPanel();
      }
      
      @Override
      public void contentsChanged(ListDataEvent e)
      {
         updateDetailsPanel();
      }
   };

   /**
    * A list data listener that reorders the display order when the list changes.
    */
   private final ListDataListener valuesListener = new ListDataListener()
   {
      private boolean updatePending;

      private void lazyUpdateOrder()
      {
         if (!updatePending)
         {
            updatePending = true;

            SwingUtilities.invokeLater(new Runnable()
            {
               @Override
               public void run()
               {
                  updateOrder();
                  updatePending = false;

                  orderValue.fireValueChange(-1, selectionInList.getSelection().getOrder());
               }
            });
         }
      }
      
      @Override
      public void intervalRemoved(ListDataEvent e)
      {
         lazyUpdateOrder();
      }
      
      @Override
      public void intervalAdded(ListDataEvent e)
      {
         lazyUpdateOrder();
      }
      
      @Override
      public void contentsChanged(ListDataEvent e)
      {
         lazyUpdateOrder();
      }
   };
}

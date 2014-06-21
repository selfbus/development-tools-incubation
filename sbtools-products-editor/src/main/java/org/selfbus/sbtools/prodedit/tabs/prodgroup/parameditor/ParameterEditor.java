package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterContainer;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.model.project.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel that allows to edit the parameters of a program.
 */
public class ParameterEditor extends JPanel
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ParameterEditor.class);
   private static final long serialVersionUID = 1163429348377518472L;

   private JTabbedPane paramTabs = new JTabbedPane(JTabbedPane.LEFT);

   private VirtualDevice device;
   private ApplicationProgram program;
   private Device testDevice;

   private final Vector<Page> paramPages = new Vector<Page>();
   private final EventListenerList listenerList = new EventListenerList();
   private transient ChangeEvent changeEvent = null;
   private boolean updateContentsEnabled, inStateChanged;

   /**
    * Create a parameter editor.
    */
   public ParameterEditor()
   {
      super(new BorderLayout());

      add(paramTabs, BorderLayout.CENTER);
      paramTabs.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent e)
         {
            final Component comp = paramTabs.getSelectedComponent();
            if (updateContentsEnabled && comp instanceof Page)
               ((Page) comp).updateContents();
         }
      });
   }

   /**
    * Set the device whose parameters are edited. Calls
    * {@link #updateContents()}.
    * 
    * @param device - the virtual device.
    * @param program - the application program to be used.
    */
   public void setVirtualDevice(VirtualDevice device, ApplicationProgram program)
   {
      this.device = device;
      this.program = program;
      this.testDevice = new Device(device);

      updateContents();
   }

   /**
    * @return the device whose parameters are edited, or <code>null</code> if
    *         none.
    */
   public VirtualDevice getVirtualDevice()
   {
      return device;
   }

   /**
    * Adds a <code>ChangeListener</code> to this object.
    * 
    * @param l the <code>ChangeListener</code> to add
    * 
    * @see #fireStateChanged
    * @see #removeChangeListener
    */
   public void addChangeListener(ChangeListener l)
   {
      listenerList.add(ChangeListener.class, l);
   }

   /**
    * Removes a <code>ChangeListener</code> from this object.
    * 
    * @param l the <code>ChangeListener</code> to remove
    * @see #fireStateChanged
    * @see #addChangeListener
    */
   public void removeChangeListener(ChangeListener l)
   {
      listenerList.remove(ChangeListener.class, l);
   }

   /**
    * Sends a {@code ChangeEvent}, with this {@code ParameterEditor} as the
    * source, to each registered listener. This method is called each time there
    * is a change of a parameter value.
    */
   protected void fireStateChanged()
   {
      Object[] listeners = listenerList.getListenerList();

      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
         if (listeners[i] == ChangeListener.class)
         {
            // Lazily create the event
            if (changeEvent == null)
               changeEvent = new ChangeEvent(this);

            ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
         }
      }
   }

   /**
    * @return All parameters of the program in a collection.
    */
   protected Collection<Parameter> getParameters()
   {
      ArrayList<Parameter> params = new ArrayList<Parameter>(8000);
      collectParameters(params, program.getParameterRoot());
      return params;
   }

   void collectParameters(ArrayList<Parameter> params, AbstractParameterNode node)
   {
      if (node instanceof Parameter)
         params.add((Parameter) node);

      if (node instanceof AbstractParameterContainer)
      {
         for (AbstractParameterNode child : ((AbstractParameterContainer) node).getChildrenModel())
            collectParameters(params, child);
      }
   }

   /**
    * Update the contents of the editor.
    */
   public void updateContents()
   {
      updateContentsEnabled = false;

      paramPages.clear();
      paramTabs.removeAll();

      if (device == null || program == null)
      {
         updateContentsEnabled = true;
         return;
      }
      
      //
      // Get the parameters and sort them by display order
      //
      final Collection<Parameter> paramsSet = getParameters();
      final Parameter[] paramsSorted = new Parameter[paramsSet.size()];
      paramsSet.toArray(paramsSorted);
      Arrays.sort(paramsSorted, new Comparator<Parameter>()
      {
         @Override
         public int compare(Parameter a, Parameter b)
         {
            return a.getOrder() - b.getOrder();
         }
      });

      //
      // Create the parameter pages.
      // Fill the "normal" parameters into the parameter pages.
      //
      Page page = null;
      for (final Parameter param : paramsSorted)
      {
         final DeviceParameter devParam = new DeviceParameter(testDevice, param, null);
         if (!devParam.isVisible())
            continue;

         if (param.getCategory() == ParameterCategory.PAGE)
         {
            if (page != null && page.getDisplayOrder() == param.getOrder())
               page.addPageParam(devParam);
            else
            {
               page = new Page(program, devParam);
               page.addChangeListener(paramValueChangedListener);
               paramPages.add(page);
               paramTabs.add(page.getName(), page);
               paramTabs.setToolTipTextAt(paramTabs.getTabCount() - 1, "Debug: parameter #"
                     + page.getVisibleDevParameter().getParameter().getNumber());
            }
         }
         else if (page != null)
         {
            page.addParam(devParam);
         }
         else
         {
            // Parameter does not belong to a page
            LOGGER.debug("Parameter does not belong to a page: " + param);
         }
      }

      // updatePagesVisibility();

      if (paramTabs.getComponentCount() > 0)
      {
         updateContentsEnabled = false;
         ((Page) paramTabs.getComponentAt(0)).updateContents();
         paramTabs.setSelectedIndex(0);
         updateContentsEnabled = true;
      }

      updateContentsEnabled = true;
   }

   /**
    * Update the visibility of the parameter pages.
    */
   public void updatePagesVisibility()
   {
      // get selected tab title
      int selectedIndex = paramTabs.getSelectedIndex();
      String selectedTitle = paramTabs.getTitleAt(selectedIndex);

      // process changes
      updateContents();

      // set selected tab
      for (int i = paramTabs.getTabCount() - 1; i >= 0; --i)
         if (selectedTitle.equals(paramTabs.getTitleAt(i)))
            paramTabs.setSelectedIndex(i);
   }

   /**
    * Apply the parameter values and visibility to the device.
    */
   public void apply()
   {
      // TODO implement or remove this
//      if (device != null)
//         ProjectManager.getController().parametersChanged(device);
   }

   /**
    * Called when a parameter value was changed.
    */
   private final ChangeListener paramValueChangedListener = new ChangeListener()
   {
      @Override
      public void stateChanged(final ChangeEvent e)
      {
         if (!updateContentsEnabled)
            return;

         fireStateChanged();

         if (!inStateChanged)
         {
            SwingUtilities.invokeLater(new Runnable()
            {
               @Override
               public void run()
               {
                  try
                  {
                     inStateChanged = true;
                     updatePagesVisibility();

                     final Component currentPageComp = paramTabs.getSelectedComponent();
                     if (currentPageComp instanceof Page)
                        ((Page) currentPageComp).updateContents();
                  }
                  finally
                  {
                     inStateChanged = false;
                  }
               }
            });
         }

      }
   };
}

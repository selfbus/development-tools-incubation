package org.selfbus.sbtools.prodedit.binding;
/* Copyright (c) 2013, Stefan Taferner
 * Copyright (c) 2005, Felix Leipold
   All rights reserved.

   Redistribution and use in source and binary forms, with or without modification,
   are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Felix Leipold nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
   IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
   INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
   OF SUCH DAMAGE.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.selfbus.sbtools.common.gui.utils.TreeUtils;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Represents a selection in a tree of objects. Provides bound bean properties for the tree, the
 * selection, and the selection empty state. The SelectionInTree implements ValueModel with the
 * selection as value. Selection changes fire an event only if the old and new value are not equal.
 * If you need to compare the identity you can use and observe the selection index instead of the
 * selection or value.
 * <p>
 */
public class SelectionInTree extends Model implements TreeSelectionListener, TreeModel, ValueModel
{
   private static final long serialVersionUID = 3988520999895337122L;

   /**
    * The name of the bound read-write <em>selection</em> property.
    */
   public static final String PROPERTY_SELECTION = "selection";

   // Holds the selection, an instance of {@code Object}.
   private ValueModel selectionHolder;

   private TreeModel model;
   private TreeSelectionModel selectionModel;

   private final SelectionChangeHandler selectionChangeHandler = new SelectionChangeHandler();
   private final TreeSelectionListener treeSelectionHandler = new TreeSelectionHandler();
   private final Map<Object, Object> node2parent = new HashMap<Object, Object>();

   /**
    * Constructs a SelectionInTree on the given tree model using defaults for the selection holder.
    * 
    * @param model - the initial tree model
    */
   public SelectionInTree(TreeModel model)
   {
      this(model, new ValueHolder());
   }

   /**
    * Constructs a SelectionInTree on the given tree model.
    * 
    * @param model - the initial tree model
    * @param selectionHolder - the selection holder
    */
   public SelectionInTree(TreeModel model, ValueModel selectionHolder)
   {
      this.selectionHolder = selectionHolder;
      this.selectionModel = new DefaultTreeSelectionModel();

      setTree(model);

      selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      selectionModel.addTreeSelectionListener(treeSelectionHandler);
      selectionHolder.addValueChangeListener(selectionChangeHandler);
   }

   /**
    * Set the tree model.
    *
    * @param model - the tree model
    */
   public void setTree(TreeModel model)
   {
      this.model = model;
      fillHash(model.getRoot());
      
   }
   
   /**
    * Recursively fill the internal hash with the tree model's objects.
    *
    * @param base - the base tree node to process
    */
   private void fillHash(Object base)
   {
      node2parent.clear();

      for (int i = 0; i < model.getChildCount(base); i++)
      {
         Object child = model.getChild(base, i);
         node2parent.put(child, base);
         fillHash(child);
      }
   }

   /**
    * Binds a JTree to the given SelectionInTree.
    * <p>
    * 
    * @param tree - the tree to be bound
    * @param selectionInList provides the list and selection; if the selection holder is a
    *           ComponentValueModel, it is synchronized with the list properties <em>visible</em>
    *           and <em>enabled</em>
    * @param <E> the type of the list items
    * 
    * @throws NullPointerException if the list or the selectionInList is {@code null}
    */
   public void bindTo(JTree tree)
   {
      tree.setSelectionModel(selectionModel);
   }

   /**
    * Returns the selected object.
    *
    * @return the selected object, or null if nothing is selected.
    */
   public Object getSelection()
   {
      return selectionHolder.getValue();
   }

   /**
    * Returns the selection holder.
    *
    * @return the selection holder
    */
   public ValueModel getSelectionHolder()
   {
      return selectionHolder;
   }

   public void setSelectionFromValueHolder()
   {
      final Object currentCategory = selectionHolder.getValue();
      if (currentCategory != null)
      {
         setSelection(currentCategory);
      }
   }

   /**
    * Removes the internal listeners from the selection holder.
    */
   public void release()
   {
      selectionHolder.removeValueChangeListener(selectionChangeHandler);
   }

   /**
    * Sets the selection index to the index of the tree element that equals {@code newSelection}. If
    * {@code newSelection} is {@code null}, it is interpreted as <em>unspecified</em> and the
    * selection index is set to -1, and this SelectionInList has no selection. Does nothing if the
    * list is empty or {@code null}.
    * 
    * @param newSelection the object to be set as new selection, or {@code null} to set the
    *           selection index to -1
    */
   public void setSelection(Object newSelection)
   {
      if (newSelection == null)
      {
         selectionModel.clearSelection();
         selectionHolder.setValue(null);
         return;
      }

      for (TreeNode treeNode : TreeUtils.getChildTreeNodes((TreeNode) model.getRoot()))
      {
         if (treeNode instanceof DefaultMutableTreeNode)
         {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
            if (node.getUserObject() == newSelection)
            {
               // selectionHolder.setValue(newSelection);
               selectionModel.setSelectionPath(new TreePath(node.getPath()));
               return;
            }
         }
         else if (treeNode == newSelection)
         {
            selectionModel.setSelectionPath(createTreePath(treeNode));
            return;
         }
      }

      selectionHolder.setValue(null);
      selectionModel.clearSelection();
   }

   private TreePath createTreePath(TreeNode treeNode)
   {
      List<TreeNode> nodes = new LinkedList<TreeNode>();

      while (treeNode != null)
      {
         nodes.add(0, treeNode);
         treeNode = treeNode.getParent();
      }

      return new TreePath(nodes.toArray());
   }

   public TreeSelectionModel getSelectionModel()
   {
      return selectionModel;
   }

   @Override
   public void valueChanged(TreeSelectionEvent e)
   {
      selectionHolder.removeValueChangeListener(selectionChangeHandler);

      final TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
      if (newLeadSelectionPath != null)
      {
         selectionHolder.setValue(newLeadSelectionPath.getLastPathComponent());

      }
      else
      {
         selectionHolder.setValue(null);

      }

      selectionHolder.addValueChangeListener(selectionChangeHandler);
   }

   /**
    * @return The root of the tree. Returns null only if the tree has no nodes.
    */
   @Override
   public Object getRoot()
   {
      return model.getRoot();
   }

   /**
    * Returns the number of children of parent. Returns 0 if the node is a leaf or if it has no
    * children. parent must be a node previously obtained from this data source.
    * 
    * @param parent - a node in the tree, obtained from this data source
    * @return The number of children of the node parent
    */
   @Override
   public int getChildCount(Object parent)
   {
      return model.getChildCount(parent);
   }

   @Override
   public boolean isLeaf(Object node)
   {
      return model.isLeaf(node);
   }

   @Override
   public void addTreeModelListener(TreeModelListener l)
   {
      model.addTreeModelListener(l);
   }

   @Override
   public void removeTreeModelListener(TreeModelListener l)
   {
      model.removeTreeModelListener(l);
   }

   @Override
   public Object getChild(Object parent, int index)
   {
      return model.getChild(parent, index);
   }

   @Override
   public int getIndexOfChild(Object parent, Object child)
   {
      return model.getIndexOfChild(parent, child);
   }

   @Override
   public void valueForPathChanged(TreePath path, Object newValue)
   {
      model.valueForPathChanged(path, newValue);
   }

   @Override
   public Object getValue()
   {
      TreePath path = selectionModel.getSelectionPath();
      if (path == null) return null;

      DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
      return node == null ? null : node.getUserObject();
   }

   /**
    * Sets the selection index to the index of the first list element that equals {@code newValue}.
    * If {@code newValue} is {@code null}, it is interpreted as <em>unspecified</em> and the
    * selection index is set to -1, and this SelectionInTree has no selection. Does nothing if the
    * tree is empty or {@code null}.
    * 
    * @param newValue the object to be set as new selection, or {@code null} to set the selection
    *           index to -1
    */
   @Override
   public void setValue(Object newValue)
   {
      setSelection(newValue);
   }

   /**
    * Registers the given PropertyChangeListener with this model. The listener will be notified if
    * the value has changed.
    * <p>
    * 
    * The PropertyChangeEvents delivered to the listener have the name set to "value". In other
    * words, the listeners won't get notified when a PropertyChangeEvent is fired that has a null
    * object as the name to indicate an arbitrary set of the event source's properties have changed.
    * <p>
    * 
    * In the rare case, where you want to notify a PropertyChangeListener even with
    * PropertyChangeEvents that have no property name set, you can register the listener with
    * #addPropertyChangeListener, not #addValueChangeListener.
    * 
    * @param l - the listener to add
    */
   @Override
   public void addValueChangeListener(PropertyChangeListener l)
   {
      addPropertyChangeListener(PROPERTY_VALUE, l);
   }

   /**
    * Removes the given PropertyChangeListener from the model.
    * 
    * @param l - the listener to remove
    */
   @Override
   public void removeValueChangeListener(PropertyChangeListener l)
   {
      removePropertyChangeListener(PROPERTY_VALUE, l);
   }

   /**
    * Notifies all listeners that have registered interest for notification on this event type. The
    * event instance is lazily created using the parameters passed into the fire method.
    * 
    * @param oldValue the value before the change
    * @param newValue the value after the change
    * 
    * @see java.beans.PropertyChangeSupport
    */
   void fireValueChange(Object oldValue, Object newValue)
   {
      firePropertyChange(PROPERTY_VALUE, oldValue, newValue);
   }

   private class SelectionChangeHandler implements PropertyChangeListener
   {
      @Override
      public void propertyChange(PropertyChangeEvent e)
      {
         Object currentSelection = e.getNewValue();

         setSelection(currentSelection);
         System.out.println("Selection Value Model changed. Source: " + e.getSource());
         System.out.println("Oldvalue: " + e.getOldValue() + "  NewValue: " + e.getNewValue());
      }
   }

   /**
    * Private handler that handles tree selection changes.
    */
   private class TreeSelectionHandler implements TreeSelectionListener
   {
      @Override
      public void valueChanged(TreeSelectionEvent e)
      {
         Object oldSelection = selectionHolder.getValue();

         TreePath newPath = e.getPath();
         Object newSelection = null;

         if (newPath != null)
         {
            Object pathComponent = newPath.getLastPathComponent();
            if (pathComponent instanceof DefaultMutableTreeNode)
               newSelection = ((DefaultMutableTreeNode) pathComponent).getUserObject();
            else newSelection = pathComponent;
         }

         if (e.getOldLeadSelectionPath() != null && model instanceof DefaultTreeModel)
         {
            ((DefaultTreeModel) model).nodeChanged((DefaultMutableTreeNode) e.getOldLeadSelectionPath().getLastPathComponent());
         }

         if (oldSelection != newSelection)
         {
            selectionHolder.removeValueChangeListener(selectionChangeHandler);
            selectionHolder.setValue(newSelection);
            selectionHolder.addValueChangeListener(selectionChangeHandler);
         }

         firePropertyChange(PROPERTY_SELECTION, oldSelection, newSelection);
         fireValueChange(oldSelection, newSelection);
      }
   }
}

package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;

/**
 * Tree model for {@link Parameter}s and {@link CommunicationObject}s of an
 * {@link ApplicationProgram}.
 */
public class ParameterTreeModel implements TreeModel
{
   protected final EventListenerList listenerList = new EventListenerList();
   protected final ParameterRoot root;

   public ParameterTreeModel()
   {
      this.root = new ParameterRoot();
   }

   public ParameterTreeModel(ParameterRoot root)
   {
      this.root = root;
   }

   @Override
   public ParameterRoot getRoot()
   {
      return root;
   }

   @Override
   public AbstractParameterNode getChild(Object parent, int index)
   {
      ParameterContainer parentCont = (ParameterContainer) parent;
      return parentCont.getChildAt(index);
   }

   @Override
   public int getChildCount(Object parent)
   {
      if (parent instanceof TreeNode)
         return ((TreeNode) parent).getChildCount();

      return 0;
   }

   @Override
   public boolean isLeaf(Object node)
   {
      return !(node instanceof TreeNode) || ((TreeNode) node).isLeaf();
   }

   @Override
   public void valueForPathChanged(TreePath path, Object newValue)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public int getIndexOfChild(Object parent, Object child)
   {
      if (parent == null || !(child instanceof TreeNode))
         return -1;

      TreeNode parentNode = (TreeNode) parent;
      return parentNode.getIndex((TreeNode) child);
   }

   /**
    * Recursively search the tree for a parameter with the given ID.
    * 
    * @param id - the ID of the parameter to find.
    * 
    * @return The parameter, or null if not found.
    */
   public AbstractParameterNode findById(int id)
   {
      return findById(root, id);
   }

   /**
    * Recursively search the tree for a parameter with the given ID. The search is started with the
    * children of the given parent parameter.
    * 
    * @param parent - search the parent node and it's children
    * @param id - the ID of the parameter to find.
    * 
    * @return The parameter, or null if not found.
    */
   public AbstractParameterNode findById(AbstractParameterNode parent, int id)
   {
      Enumeration<AbstractParameterNode> it = parent.children();
      while (it.hasMoreElements())
      {
         AbstractParameterNode node = it.nextElement();

         if (node.id == id)
            return node;

         if (!node.isLeaf())
         {
            node = findById(node, id);
            if (node != null)
               return node;
         }
      }

      return null;
   }

   /**
    * Builds the parents of node up to and including the root node, where the
    * original node is the last element in the returned array. The length of the
    * returned array gives the node's depth in the tree.
    * 
    * @param node - the TreeNode to get the path for
    * @return an array of TreeNodes giving the path from the root to the
    *         specified node
    */
   public AbstractParameterNode[] getPathToRoot(AbstractParameterNode node)
   {
      LinkedList<AbstractParameterNode> nodes = new LinkedList<AbstractParameterNode>();
      for (; node != null; node = node.getParent())
      {
         nodes.addFirst(node);
      }

      AbstractParameterNode[] result = new AbstractParameterNode[nodes.size()];
      nodes.toArray(result);
      return result;
   }

   /**
    * Adds a listener for the TreeModelEvent posted after the tree changes.
    * 
    * @param l - the listener to add
    * @see #removeTreeModelListener
    */
   public void addTreeModelListener(TreeModelListener l)
   {
      listenerList.add(TreeModelListener.class, l);
   }

   /**
    * Removes a listener previously added with <B>addTreeModelListener()</B>.
    * 
    * @param l - the listener to remove
    * @see #addTreeModelListener
    */
   public void removeTreeModelListener(TreeModelListener l)
   {
      listenerList.remove(TreeModelListener.class, l);
   }

   /**
    * Invoke this method after you've inserted some TreeNodes into node.
    * childIndices should be the index of the new elements and must be sorted in
    * ascending order.
    */
   public void nodesWereInserted(AbstractParameterNode node, int[] childIndices)
   {
      if (listenerList != null && node != null && childIndices != null && childIndices.length > 0)
      {
         int cCount = childIndices.length;
         Object[] newChildren = new Object[cCount];

         for (int counter = 0; counter < cCount; counter++)
            newChildren[counter] = node.getChildAt(childIndices[counter]);
         fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
      }
   }

   /**
    * Invoke this method after you've removed some TreeNodes from node.
    * childIndices should be the index of the removed elements and must be
    * sorted in ascending order. And removedChildren should be the array of the
    * children objects that were removed.
    */
   public void nodesWereRemoved(AbstractParameterNode node, int[] childIndices, Object[] removedChildren)
   {
      if (node != null && childIndices != null)
      {
         fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
      }
   }

   /**
    * Invoke this method if you've totally changed the children of node and its
    * childrens children... This will post a treeStructureChanged event.
    */
   public void nodeStructureChanged(AbstractParameterNode node)
   {
      if (node != null)
      {
         fireTreeStructureChanged(this, getPathToRoot(node), null, null);
      }
   }

   /**
    * Notifies all listeners that have registered interest for notification on
    * this event type. The event instance is lazily created using the parameters
    * passed into the fire method.
    * 
    * @param source the source of the {@code TreeModelEvent}; typically
    *           {@code this}
    * @param path the path to the parent the nodes were added to
    * @param childIndices the indices of the new elements
    * @param children the new elements
    */
   protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children)
   {
      // Guaranteed to return a non-null array
      Object[] listeners = listenerList.getListenerList();
      TreeModelEvent e = null;
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
         if (listeners[i] == TreeModelListener.class)
         {
            // Lazily create the event:
            if (e == null)
               e = new TreeModelEvent(source, path, childIndices, children);
            ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
         }
      }
   }

   /**
    * Notifies all listeners that have registered interest for notification on
    * this event type. The event instance is lazily created using the parameters
    * passed into the fire method.
    * 
    * @param source the source of the {@code TreeModelEvent}; typically
    *           {@code this}
    * @param path the path to the parent the nodes were removed from
    * @param childIndices the indices of the removed elements
    * @param children the removed elements
    */
   protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children)
   {
      // Guaranteed to return a non-null array
      Object[] listeners = listenerList.getListenerList();
      TreeModelEvent e = null;
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
         if (listeners[i] == TreeModelListener.class)
         {
            // Lazily create the event:
            if (e == null)
               e = new TreeModelEvent(source, path, childIndices, children);
            ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
         }
      }
   }

   /**
    * Notifies all listeners that have registered interest for notification on
    * this event type. The event instance is lazily created using the parameters
    * passed into the fire method.
    * 
    * @param source - the source of the {@code TreeModelEvent}; typically
    *           {@code this}
    * @param path - the path to the parent of the structure that has changed;
    *           use {@code null} to identify the root has changed
    * @param childIndices - the indices of the affected elements
    * @param children - the affected elements
    */
   protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children)
   {
      // Guaranteed to return a non-null array
      Object[] listeners = listenerList.getListenerList();
      TreeModelEvent e = null;

      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
         if (listeners[i] == TreeModelListener.class)
         {
            // Lazily create the event:
            if (e == null)
               e = new TreeModelEvent(source, path, childIndices, children);
            ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
         }
      }
   }
}

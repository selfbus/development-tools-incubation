package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import java.util.Enumeration;

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
   private final ParameterRoot root;

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

   @Override
   public void addTreeModelListener(TreeModelListener l)
   {
//      Validate.isTrue(false, "not implemented");
   }

   @Override
   public void removeTreeModelListener(TreeModelListener l)
   {
//      Validate.isTrue(false, "not implemented");
   }
}

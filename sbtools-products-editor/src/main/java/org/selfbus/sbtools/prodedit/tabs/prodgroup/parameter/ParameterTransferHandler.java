package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameter;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import org.selfbus.sbtools.prodedit.binding.SelectionInTree;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterContainer;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterRoot;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle transfer of parameters and communication objects when doing drag & drop in a JTree.
 */
public class ParameterTransferHandler extends TransferHandler
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ParameterTransferHandler.class);
   private static final long serialVersionUID = -8228071117274907012L;

   private final SelectionInTree selectionInTree;
   private final DataFlavor[] flavors = new DataFlavor[1];
   private final DataFlavor flavor;

   public ParameterTransferHandler(SelectionInTree selectionInTree)
   {
      this.selectionInTree = selectionInTree;

      try
      {
         String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + AbstractParameterNode.class.getName() + "\"";

         flavor = new DataFlavor(mimeType);
         flavors[0] = flavor;
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public boolean canImport(TransferSupport support)
   {
      if (!support.isDrop())
         return false;

      JTree tree = (JTree) support.getComponent();
      JTree.DropLocation location = (JTree.DropLocation) support.getDropLocation();
      Object targetObject = location.getPath().getLastPathComponent();

      if (!(targetObject instanceof AbstractParameterContainer))
         return false;

      // Do not allow a drop on or within the drag source
      if (isChildOfSelection(tree, (AbstractParameterNode) targetObject))
         return false;

      return true;
   }

   @Override
   public boolean importData(TransferSupport support)
   {
      if (!canImport(support))
      {
         LOGGER.warn("cannot drop this here");
         return false;
      }

      // Extract transfer data.
      AbstractParameterNode node;
      try
      {
         Transferable t = support.getTransferable();
         node = (AbstractParameterNode) t.getTransferData(flavor);
      }
      catch (Exception e)
      {
         LOGGER.error("Cannot drop this here: {}", e.getMessage());
         return false;
      }

      JTree.DropLocation location = (JTree.DropLocation) support.getDropLocation();
      TreePath dest = location.getPath();
      AbstractParameterContainer target = (AbstractParameterContainer) dest.getLastPathComponent();

      JTree tree = (JTree) support.getComponent();
      ParameterTreeModel model = (ParameterTreeModel) tree.getModel();

      AbstractParameterContainer parent = (AbstractParameterContainer) node.getParent();
      if (parent != null)
      {
         int idx = parent.getIndex(node);
         parent.removeChild(node);
         model.nodesWereRemoved(parent, new int[] { idx }, new Object[] { node });
      }

      target.addChild(node);
      model.nodesWereInserted(target, new int[] { target.getChildCount() - 1 });
      model.nodeStructureChanged(target);

      selectionInTree.setSelection(null);
      selectionInTree.setSelection(node);
      tree.scrollRowToVisible(tree.getSelectionRows()[0]);

      return true;
   }   

   @Override
   public Transferable createTransferable(JComponent c)
   {
      JTree tree = (JTree) c;

      TreePath[] paths = tree.getSelectionPaths();
      if (paths == null)
         return null;

      AbstractParameterNode node = (AbstractParameterNode) paths[0].getLastPathComponent();
      if (node instanceof ParameterRoot)
         return null;

      return new AbstractParameterNodeTransferable(node);
   }

   @Override
   public int getSourceActions(JComponent c)
   {
      return MOVE;
   }

   /**
    * Test if the node is a child of any selected node of the tree
    * 
    * @param tree - the tree to process
    * @param search - the node to search for
    * @return True if the node is a child, false if not.
    */
   boolean isChildOfSelection(JTree tree, AbstractParameterNode search)
   {
      AbstractParameterNode selectedNode, node;

      for (TreePath path: tree.getSelectionPaths())
      {
         selectedNode = (AbstractParameterNode) path.getLastPathComponent();
         for (node = search; node != null; node = node.getParent())
         {
            if (selectedNode == node)
               return true;
         }
      }

      return false;
   }

   /**
    * Transferable for doing drag & drop.
    */
   class AbstractParameterNodeTransferable implements Transferable
   {
      final AbstractParameterNode node;

      public AbstractParameterNodeTransferable(AbstractParameterNode node)
      {
         this.node = node;
      }

      @Override
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
      {
         if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);

         return node;
      }

      @Override
      public DataFlavor[] getTransferDataFlavors()
      {
         return flavors;
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor f)
      {
         return flavor.equals(f);
      }
   }
}

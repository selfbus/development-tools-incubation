package org.selfbus.sbtools.prodedit.dragdrop;

import java.awt.datatransfer.Transferable;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * A handler that creates {@link Transferable} objects for draging.
 */
public class ObjectTransferHandler extends TransferHandler
{
   private static final long serialVersionUID = -3207689686622973309L;

   private final int defaultSourceActions;

   /**
    * Create a transfer handler with {@link TransferHandler#COPY} as default
    * drag&drop action.
    */
   public ObjectTransferHandler()
   {
      defaultSourceActions = TransferHandler.COPY;
   }

   /**
    * Create a transfer handler.
    * 
    * @param defaultSourceActions - the default drag&drop actions.
    */
   public ObjectTransferHandler(int defaultSourceActions)
   {
      this.defaultSourceActions = defaultSourceActions;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Transferable createTransferable(JComponent c)
   {
      List<Object> objs;

      if (c instanceof JList)
      {
         @SuppressWarnings("unchecked")
         JList<Object> lst = (JList<Object>) c;

         objs = lst.getSelectedValuesList();
      }
      else if (c instanceof JTree)
      {
         final JTree tree = (JTree) c;
         objs = new Vector<Object>();
         for (TreePath path : tree.getSelectionPaths())
         {
            final Object n = path.getLastPathComponent();
            if (n instanceof DefaultMutableTreeNode)
            {
               final Object obj = ((DefaultMutableTreeNode) n).getUserObject();
               if (isDragable(obj))
                  objs.add(obj);
            }
         }
      }
      else throw new RuntimeException("Component type not supported: " + c.getClass().getName());

      return objs.size() == 0 ? null : new TransferableObject(objs);
   }

   /**
    * Test if the object can be dragged.
    * 
    * @param obj - the object to test.
    * @return True if the object can be dragged.
    */
   public boolean isDragable(Object obj)
   {
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getSourceActions(JComponent c)
   {
      return defaultSourceActions;
   }
}

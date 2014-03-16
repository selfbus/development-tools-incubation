package org.selfbus.sbtools.prodedit.dragdrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class TransferableObject implements Transferable
{
   public static DataFlavor objectFlavor = new DataFlavor("application/x-java-object-list", null);
   
   private static final DataFlavor[] flavors = new DataFlavor[] { objectFlavor };
   private final List<Object> objects;

   public TransferableObject(List<Object> objects)
   {
      this.objects = objects;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors()
   {
      return flavors;
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor)
   {
      for (final DataFlavor f : flavors)
      {
         if (f.equals(flavor))
            return true;
      }

      return false;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
   {
      if (objectFlavor.equals(flavor))
         return objects;

      throw new UnsupportedFlavorException(flavor);
   }
}

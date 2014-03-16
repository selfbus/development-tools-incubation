package org.selfbus.sbtools.prodedit.filter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * A {@link JFileChooser} file-filter for products files.
 */
public final class ProductsFileFilter extends FileFilter
{
   @Override
   public boolean accept(File file)
   {
      if (file.isDirectory()) return true;
      return file.getName().toLowerCase().matches(".*\\.vd[_0-9]");
   }

   @Override
   public String getDescription()
   {
      return I18n.getMessage("ProductsFileFilter.description");
   }
}

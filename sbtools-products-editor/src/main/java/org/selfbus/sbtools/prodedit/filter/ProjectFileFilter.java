package org.selfbus.sbtools.prodedit.filter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * A {@link JFileChooser} file-filter for project files.
 */
public final class ProjectFileFilter extends FileFilter
{
   @Override
   public boolean accept(File file)
   {
      if (file.isDirectory()) return true;

      String name = file.getName().toLowerCase();
      return name.endsWith(".proj");
   }

   @Override
   public String getDescription()
   {
      return I18n.getMessage("ProjectFileFilter.description");
   }
}

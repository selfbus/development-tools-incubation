package org.selfbus.sbtools.devtool.filter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.selfbus.sbtools.common.gui.utils.FileUtils;
import org.selfbus.sbtools.devtool.internal.I18n;

/**
 * A {@link JFileChooser} file-filter for project files.
 */
public final class ProjectFileFilter extends FileFilter
{
   private final boolean showImports;

   /**
    * Create a file-filter for project files.
    *
    * @param showImports - shall ETS project files be shown?
    */
   public ProjectFileFilter(boolean showImports)
   {
      this.showImports = showImports;
   }

   @Override
   public boolean accept(File file)
   {
      if (file.isDirectory()) return true;

      final String ext = FileUtils.getExtension(file);
      if ("proj".equalsIgnoreCase(ext)) return true;
      if (!showImports) return false;
      return "pr_".equalsIgnoreCase(ext);
   }

   @Override
   public String getDescription()
   {
      return I18n.getMessage("ProjectFileFilter.description");
   }
}

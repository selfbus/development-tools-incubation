package org.selfbus.sbtools.prodedit.vdio;

import java.io.File;
import java.io.FileNotFoundException;

import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.vdio.VdioException;

/**
 * A small test program that writes a VD to a file in the user's home directory.
 */
public class ExampleProductsExporter
{
   public static void main(String[] args) throws FileNotFoundException, VdioException
   {
      ProjectService projectService = new ProjectService();
      ProductsExporter target = new ProductsExporter();
      Project project = projectService.createExampleProject();

      String home = System.getProperty("user.home");
      target.write(project, new File(home + "/TestProductsExporter.vd_"));
   }
}

package org.selfbus.sbtools.prodedit.vdio;

import java.io.File;
import java.io.FileNotFoundException;

import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.vdio.VdioException;

/**
 * A small test program that reads a VD and writes a VD to a file in the user's home directory.
 */
public class ExampleProductsInExport
{
   public static void main(String[] args) throws FileNotFoundException, VdioException
   {
      String home = System.getProperty("user.home");

      ProjectService projectService = ProdEdit.getInstance().getProjectService();

//      projectService.importProject(new File("src/main/resources/Bosch-Freebus12.vd_"));
      projectService.importProject(new File("src/test/resources/test-device.vd_"));

      projectService.exportProject(new File(home + "/TestProductsInExport.vd_"));
   }
}

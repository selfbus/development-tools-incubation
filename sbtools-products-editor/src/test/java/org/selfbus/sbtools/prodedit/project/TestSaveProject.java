package org.selfbus.sbtools.prodedit.project;

import java.io.File;
import java.io.FileNotFoundException;

import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.vdio.VdioException;

/**
 * Import products and save the project.
 */
public class TestSaveProject
{
   public static void main(String[] args) throws FileNotFoundException, VdioException
   {
      String home = System.getProperty("user.home");

      ProjectService projectService = ProdEdit.getInstance().getProjectService();

      projectService.importProject(new File("src/main/resources/Bosch-Freebus12.vd_"));
      projectService.saveProject(new File(home + "/TestSaveProject"));
   }
}

package org.selfbus.sbtools.prodedit.vdio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.vdio.VdioException;
import org.selfbus.sbtools.vdio.model.VD;

public class TestProductsExporter
{
   @Test
   public void createVD() throws VdioException
   {
      ProjectService projectService = new ProjectService();
      ProductsExporter target = new ProductsExporter();
      Project project = projectService.createExampleProject();

      VD vd = target.createVD(project);
      assertNotNull(vd);

      assertEquals(1, vd.manufacturers.size());
   }
}

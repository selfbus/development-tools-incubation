package org.selfbus.sbtools.prodedit.vdio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.vdio.VdioException;

public class TestProductsImporter
{
   @Test
   public void read() throws FileNotFoundException, VdioException
   {
      ProductsImporter importer = new ProductsImporter(new ProjectService());
      Project project = importer.read(new File("src/test/resources/test-device.vd_"));
//      Project project = importer.read(new File("src/test/resources/Bosch-Freebus12.vd_"));
      assertNotNull(project);

      assertEquals(4, project.getLanguages().size());
      assertEquals(2, project.getManufacturers().size());
      assertNotNull(project.getManufacturer(72));
   }

   @Test
   public void readFreebus() throws FileNotFoundException, VdioException
   {
      ProductsImporter importer = new ProductsImporter(new ProjectService());
      Project project = importer.read(new File("src/test/resources/Bosch-Freebus12.vd_"));
      assertNotNull(project);

      assertEquals(2, project.getLanguages().size());
      assertEquals(6, project.getManufacturers().size());
      assertNotNull(project.getManufacturer(61));

      ProductGroup productGroup = project.getProductGroupByName("Kontroller");
      assertNotNull(productGroup);

      VirtualDevice device = productGroup.getDeviceByName("Verknüpfung, 8-fach");
      assertNotNull(device);

      ApplicationProgram program = productGroup.getProgram(device.getProgramId());
      assertNotNull(program);

      // The parent of parameter #1 is the root node. We want the parent ID to be null then.
      AbstractParameterNode node = program.getParameter(1);
      assertNotNull(node);
      assertNull(node.getParentId());
   }

   @Test
   public void readBcu2() throws FileNotFoundException, VdioException
   {
      ProductsImporter importer = new ProductsImporter(new ProjectService());
      Project project = importer.read(new File("src/test/resources/bcu2_abb_usu_2.4.vd_"));
      assertNotNull(project);

      assertEquals(4, project.getLanguages().size());
      assertEquals(1, project.getManufacturers().size());
      assertNotNull(project.getManufacturer(2));
   }
}

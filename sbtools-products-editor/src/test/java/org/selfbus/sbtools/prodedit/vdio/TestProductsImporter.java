package org.selfbus.sbtools.prodedit.vdio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
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

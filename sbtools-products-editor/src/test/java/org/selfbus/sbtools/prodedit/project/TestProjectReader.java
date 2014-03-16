package org.selfbus.sbtools.prodedit.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProjectReader;
import org.selfbus.sbtools.prodedit.model.global.Manufacturer;
import org.selfbus.sbtools.prodedit.model.global.Project;


public class TestProjectReader
{
   private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

   @Test
   public void readProject() throws FileNotFoundException
   {
      String data = XML_HEADER + 
         "<project name=\"project-1\">\n" +
         " <manufacturers>\n" +
         "  <manufacturer id=\"11\" name=\"manu-11\" />\n" +
         "  <manufacturer id=\"23\" name=\"manu-23\" />\n" +
         " </manufacturers>\n" +
         " <functional_entities>\n" +
         "  <functional_entity id=\"100\" name=\"Input\" manufacturer_id=\"11\">\n" +
         "   <functional_entity id=\"110\" name=\"Touch\" manufacturer_id=\"11\" />\n" +
         "  </functional_entity>\n" +
         "  <functional_entity id=\"200\" name=\"Output\" manufacturer_id=\"11\" />\n" +
         " </functional_entities>\n" +
         "</project>";
      InputStream in = new ByteArrayInputStream(data.getBytes());

      ProjectReader reader = new ProjectReader();
      Project project = reader.read(in); 
      assertNotNull(project);
      assertEquals("project-1", project.getName());

      Manufacturer manufacturer = project.getManufacturer(11);
      assertNotNull(manufacturer);
      assertEquals(11, manufacturer.id);

      assertEquals(2, project.getFunctionalEntities().size());
      assertEquals(200, project.getFunctionalEntity(200).getId());
      assertEquals(110, project.getFunctionalEntity(110).getId());
   }
}

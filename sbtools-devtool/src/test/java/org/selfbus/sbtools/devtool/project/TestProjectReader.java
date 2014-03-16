package org.selfbus.sbtools.devtool.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.selfbus.sbtools.devtool.project.model.group.MainGroup;
import org.selfbus.sbtools.devtool.project.model.group.MidGroup;


public class TestProjectReader
{
   private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

   @Test
   public void readProject() throws FileNotFoundException
   {
      String data = XML_HEADER + 
         "<project name=\"project-1\">\n" +
         " <groups>\n" +
         "  <group name=\"group-1.2.1\" address=\"1/2/1\" />\n" +
         "  <group name=\"group-2.1.1\" address=\"2/1/1\" />\n" +
         " </groups>\n" +
         " <mainGroups>\n" +
         "  <mainGroup name=\"main-1\" address=\"1\">\n" +
         "   <midGroup name=\"mid-1.1\" address=\"1\" />\n" +
         "   <midGroup name=\"mid-1.2\" address=\"2\" />\n" +
         "   <midGroup name=\"mid-1.3\" address=\"3\" />\n" +
         "   <midGroup name=\"mid-1.4\" address=\"4\" />\n" +
         "  </mainGroup>\n" +
         "  <mainGroup name=\"main-2\" address=\"2\">\n" +
         "   <midGroup name=\"mid-2.1\" address=\"1\" />\n" +
         "  </mainGroup>\n" +
         "  <mainGroup name=\"main-3\" address=\"3\" />\n" +
         " </mainGroups>\n" +
         "</project>";
      InputStream in = new ByteArrayInputStream(data.getBytes());

      ProjectReader reader = new ProjectReader();
      Project project = reader.read(in); 
      assertNotNull(project);
      assertEquals("project-1", project.getName());

      List<MainGroup> mainGroups = project.getMainGroups();
      assertNotNull(mainGroups);
      assertEquals(3, mainGroups.size());

      MainGroup mainGroup = mainGroups.get(0);
      assertEquals("main-1", mainGroup.getName());
      assertEquals(1, mainGroup.getAddress());

      List<MidGroup> midGroups = mainGroup.getMidGroups();
      assertNotNull(midGroups);
      assertEquals(4, midGroups.size());

      MidGroup midGroup = midGroups.get(2);
      assertEquals("mid-1.3", midGroup.getName());
      assertEquals(3, midGroup.getAddress());
   }
}

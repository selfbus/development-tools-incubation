package org.selfbus.sbtools.prodedit.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProductGroupReader;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;


public class TestProductGroupReader
{
   private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

   @Test
   public void readProductGroup() throws FileNotFoundException
   {
      String data = XML_HEADER + 
         "<product_group id=\"inputs-1\" name=\"Inputs\">\n" +
         " <programs>\n" +
         "  <program program_id=\"100\" program_name=\"Input\" manufacturer_id=\"11\" "+
         "device_type=\"1\" program_type=\"1010\">\n" +
         "   <description>\n" +
         "    <text id=\"en\" value=\"Input devices\" />\n" +
         "    <text id=\"de\" value=\"Eingänge\" />\n" +
         "   </description>\n" +
         "  </program>\n" +
         " </programs>\n" +
         "</product_group>";
      InputStream in = new ByteArrayInputStream(data.getBytes());

      ProductGroupReader reader = new ProductGroupReader();
      ProductGroup group = reader.read(in); 
      assertNotNull(group);
      assertEquals("Inputs", group.getName());
      assertEquals("inputs-1", group.getId());

      ApplicationProgram program = group.getProgram(100);
      assertNotNull(program);
      assertEquals("Input devices", program.getDescription().getText("en"));
   }
}

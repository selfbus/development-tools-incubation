package org.selfbus.sbtools.prodedit.project;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.ProjectWriter;

public class TestProjectWriter
{
   @Test
   public void writeExampleProject() throws IOException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      ProjectService projService = new ProjectService();
      projService.createExampleProject();

      ProjectWriter writer = new ProjectWriter();
      writer.write(projService.getProject(), out);

      out.close();

      assertTrue(out.toByteArray().length > 0);
//      assertEquals("", new String(out.toByteArray()));
   }
}

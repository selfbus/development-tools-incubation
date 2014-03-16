package org.selfbus.sbtools.prodedit.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.ProductGroupWriter;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;

public class TestProductGroupWriter
{
   @Test
   public void writeProductGroup() throws IOException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      ProjectService projService = new ProjectService();
      Project project = projService.createProject();
      ProductGroup group = project.createProductGroup();
      assertNotNull(group);

      ProductGroupWriter writer = new ProductGroupWriter();
      writer.write(group, out);

      out.close();

      assertTrue(out.toByteArray().length > 0);
   }

   // @Test
   public void writeExampleProductGroup() throws IOException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      ProjectService projService = new ProjectService();
      ProductGroup group = projService.createExampleProject().getProductGroups().get(0);
      assertNotNull(group);

      ProductGroupWriter writer = new ProductGroupWriter();
      writer.write(group, out);

      out.close();

      assertTrue(out.toByteArray().length > 0);
      assertEquals("", new String(out.toByteArray()));
   }
}

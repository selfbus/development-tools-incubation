package org.selfbus.sbtools.prodedit.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.xml.sax.SAXException;

/**
 * A writer for projects.
 */
public class ProjectWriter
{
   /**
    * Write the project to the file. Sets the {@link Project#getFile() project's file}
    * to the given file.
    * 
    * @param project - the project to write.
    * @param file - the file to write to.
    */
   public void write(Project project, File file)
   {
      try
      {
         write(project, new FileOutputStream(file));
         project.setFile(file);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   /**
    * Write the project to the output stream.
    * 
    * @param project - the project to write.
    * @param out - the stream to write to.
    */
   public void write(Project project, OutputStream out)
   {
      Validate.notNull(project, "project is null");
      Validate.notNull(out, "output stream is null");

      try
      {
         final JAXBContext context = JAXBContext.newInstance("org.selfbus.sbtools.prodedit.model.global");

         final String schemaFileName = "org/selfbus/sbtools/prodedit/project.xsd";
         final URL schemaUrl = getClass().getClassLoader().getResource(schemaFileName);
         if (schemaUrl == null)
            throw new RuntimeException("Schema file not found in class path: " + schemaFileName);

         Marshaller marshaller = context.createMarshaller();
         marshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl));
         marshaller.setProperty("jaxb.formatted.output", true);
         marshaller.marshal(project, out);
      }
      catch (JAXBException e)
      {
         throw new RuntimeException(e);
      }
      catch (SAXException e)
      {
         throw new RuntimeException(e);
      }
   }
}

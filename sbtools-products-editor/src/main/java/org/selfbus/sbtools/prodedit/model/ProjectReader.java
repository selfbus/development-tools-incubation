package org.selfbus.sbtools.prodedit.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.xml.sax.SAXException;

/**
 * A reader for projects.
 */
public class ProjectReader
{
   /**
    * Read the project from the file.
    * 
    * @param file - the file to read from.
    * @return The read project.
    *
    * @throws FileNotFoundException if the file does not exist
    */
   public Project read(File file) throws FileNotFoundException
   {
      Project project = read(new FileInputStream(file));
      project.setFile(file);
      return project;
   }

   /**
    * Read the project from the input stream.
    * 
    * @param in - the stream to read from.
    * @return The read project.
    */
   public Project read(InputStream in)
   {
      Validate.notNull(in, "input stream is null");

      try
      {
         final JAXBContext context = JAXBContext.newInstance("org.selfbus.sbtools.prodedit.model.global");

         final String schemaFileName = "org/selfbus/sbtools/prodedit/project.xsd";
         final URL schemaUrl = getClass().getClassLoader().getResource(schemaFileName);
         if (schemaUrl == null)
            throw new RuntimeException("Schema file not found in class path: " + schemaFileName);

         Unmarshaller unmarshaller = context.createUnmarshaller();
         unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl));

         @SuppressWarnings("unchecked")
         JAXBElement<Project> root = (JAXBElement<Project>) unmarshaller.unmarshal(in);

         return root.getValue();
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

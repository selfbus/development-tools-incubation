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
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.xml.sax.SAXException;

/**
 * A writer for {@link ProductGroup product groups}.
 */
public class ProductGroupWriter
{
   /**
    * Write the product group to the file. Set's the product group's file
    * to the file.
    * 
    * @param group - the product group to write.
    * @param file - the file to write to.
    */
   public void write(ProductGroup group, File file)
   {
      try
      {
         write(group, new FileOutputStream(file));
         group.setFile(file);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   /**
    * Write the product group to the output stream.
    * 
    * @param group - the product group to write.
    * @param out - the stream to write to.
    */
   public void write(ProductGroup group, OutputStream out)
   {
      Validate.notNull(group, "product group is null");
      Validate.notNull(out, "output stream is null");

      try
      {
         final JAXBContext context = JAXBContext.newInstance("org.selfbus.sbtools.prodedit.model");

         final String schemaFileName = "org/selfbus/sbtools/prodedit/product-group.xsd";
         final URL schemaUrl = getClass().getClassLoader().getResource(schemaFileName);
         if (schemaUrl == null)
            throw new RuntimeException("Schema file not found in class path: " + schemaFileName);

         Marshaller marshaller = context.createMarshaller();
         marshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl));
         marshaller.setProperty("jaxb.formatted.output", true);
         marshaller.marshal(group, out);
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

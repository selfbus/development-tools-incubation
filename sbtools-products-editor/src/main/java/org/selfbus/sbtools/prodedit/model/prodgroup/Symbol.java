package org.selfbus.sbtools.prodedit.model.prodgroup;

import java.awt.image.BufferedImage;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.vdio.SymbolUtil;
import org.selfbus.sbtools.vdio.VdioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A symbol.
 */
@XmlType(name = "Symbol", propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Symbol implements Identifiable
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Symbol.class);

   @XmlAttribute(name = "id", required = true)
   private int id;

   @XmlAttribute(name = "name", required = true)
   private String name;

   @XmlAttribute(name = "filename")
   private String fileName;

   private byte[] data;

   /**
    * Create a symbol.
    */
   public Symbol()
   {
   }

   /**
    * Create a symbol.
    * 
    * @param id - the ID of the symbol
    * @param name - the name of the symbol
    */
   public Symbol(int id, String name)
   {
      this.id = id;
      this.name = name;
   }

   /**
    * @return the ID
    */
   @Override
   public int getId()
   {
      return id;
   }

   /**
    * Set the ID
    *
    * @param id - the id to set
    */
   @Override
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return The name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name.
    *
    * @param name - the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the fileName
    */
   public String getFileName()
   {
      return fileName;
   }

   /**
    * @param fileName the fileName to set
    */
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }

   /**
    * @return The symbol as image, or null if the symbol contains no valid image.
    */
   public BufferedImage getImage()
   {
      try
      {
         return data == null ? null : SymbolUtil.toImage(data);
      }
      catch (VdioException e)
      {
         LOGGER.error("Failed to create symbol image", e);
         return null;
      }
   }

   /**
    * @return the data
    */
   public byte[] getData()
   {
      return data;
   }

   /**
    * @param data the data to set
    */
   public void setData(byte[] data)
   {
      this.data = data;
   }

   @XmlAttribute(name = "data")
   String getDataStr()
   {
      return DatatypeConverter.printBase64Binary(data);
   }

   void setDataStr(String str)
   {
      data = DatatypeConverter.parseBase64Binary(str);
   }
}

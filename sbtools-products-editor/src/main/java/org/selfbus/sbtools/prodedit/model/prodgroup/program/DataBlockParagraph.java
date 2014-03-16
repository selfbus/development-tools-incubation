package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;

import com.jgoodies.binding.beans.Model;

/**
 * An data block paragraph. In the ETS language it is called a "S19 block paragraph".
 */
@XmlType(name = "DataBlockParagraph", propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class DataBlockParagraph extends Model implements Identifiable
{
   private static final long serialVersionUID = -4426715147692224166L;

   @XmlAttribute(name = "id", required = true)
   private int id;

   @XmlAttribute(name = "pt_column_id")
   private Integer ptColumnId;

   @XmlAttribute(name = "data_long")
   private Integer dataLong;

   //@XmlAttribute(name = "data_binary")
   private byte[] dataBinary;

   /**
    * @return the id
    */
   @Override
   public int getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   @Override
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the ptColumnId
    */
   public Integer getPtColumnId()
   {
      return ptColumnId;
   }

   /**
    * @param ptColumnId the ptColumnId to set
    */
   public void setPtColumnId(Integer ptColumnId)
   {
      this.ptColumnId = ptColumnId;
   }

   /**
    * @return the dataLong
    */
   public Integer getDataLong()
   {
      return dataLong;
   }

   /**
    * @param dataLong the dataLong to set
    */
   public void setDataLong(Integer dataLong)
   {
      this.dataLong = dataLong;
   }

   /**
    * @return the dataBinary
    */
   public byte[] getDataBinary()
   {
      return dataBinary;
   }

   /**
    * @param dataBinary the dataBinary to set
    */
   public void setDataBinary(byte[] dataBinary)
   {
      this.dataBinary = dataBinary;
   }

   @XmlAttribute(name = "data_binary")
   String getDataBase64Str()
   {
      return DatatypeConverter.printBase64Binary(dataBinary).toLowerCase();
   }

   void setDataBase64Str(String str)
   {
      dataBinary = DatatypeConverter.parseBase64Binary(str);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (o == this)
         return true;
      if (!(o instanceof DataBlockParagraph))
         return false;
      final DataBlockParagraph oo = (DataBlockParagraph) o;
      return id == oo.id;
   }
}

package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.prodedit.utils.IdentifiableUtils;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * A block of data of an application program. In the ETS language it is called a "S19 block".
 */
@XmlType(name = "DataBlock", propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class DataBlock extends Model implements Identifiable
{
   private static final long serialVersionUID = -5439425520464411238L;

   @XmlAttribute(name = "id", required = true)
   private int id;

   @XmlAttribute(name = "block_number", required = true)
   private int number;

   @XmlAttribute(name = "block_name", required = true)
   private String name;

   @XmlAttribute(name = "block_type")
   private Integer type;

   @XmlAttribute(name = "control_code")
   private Integer controlCode;

   @XmlAttribute(name = "segment_type")
   private Integer segmentType;

   @XmlAttribute(name = "segment_id")
   private Integer segmentId;

   @XmlAttribute(name = "segment_address")
   private Integer segmentAddress;

   @XmlAttribute(name = "segment_length")
   private Integer segmentLength;

   @XmlAttribute(name = "access_attributes")
   private Integer accessAttributes;

   @XmlAttribute(name = "memory_type")
   private Integer memoryType;

   @XmlAttribute(name = "memory_attributes")
   private Integer memoryAttributes;

   // @XmlAttribute(name = "data")
   private byte[] data;

   // @XmlAttribute(name = "mask")
   private byte[] mask;

   @XmlElementWrapper(name = "paragraphs")
   @XmlElement(name = "paragraph")
   private ArrayListModel<DataBlockParagraph> paragraphs = new ArrayListModel<DataBlockParagraph>();

   /**
    * Create a program data block.
    */
   public DataBlock()
   {
   }

   /**
    * Create a data block paragraph. The created data block is added to the data block paragraphs.
    * 
    * @return The created data block paragraph.
    */
   public DataBlockParagraph createParagraph()
   {
      DataBlockParagraph paragraph = new DataBlockParagraph();
      paragraph.setId(IdentifiableUtils.createUniqueId(paragraphs));
      paragraphs.add(paragraph);

      return paragraph;
   }

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
    * @return the number
    */
   public int getNumber()
   {
      return number;
   }

   /**
    * @param number the number to set
    */
   public void setNumber(int number)
   {
      this.number = number;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the type
    */
   public Integer getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(Integer type)
   {
      this.type = type;
   }

   /**
    * @return the controlCode
    */
   public Integer getControlCode()
   {
      return controlCode;
   }

   /**
    * @param controlCode the controlCode to set
    */
   public void setControlCode(Integer controlCode)
   {
      this.controlCode = controlCode;
   }

   /**
    * @return the segmentType
    */
   public Integer getSegmentType()
   {
      return segmentType;
   }

   /**
    * @param segmentType the segmentType to set
    */
   public void setSegmentType(Integer segmentType)
   {
      this.segmentType = segmentType;
   }

   /**
    * @return the segmentId
    */
   public Integer getSegmentId()
   {
      return segmentId;
   }

   /**
    * @param segmentId the segmentId to set
    */
   public void setSegmentId(Integer segmentId)
   {
      this.segmentId = segmentId;
   }

   /**
    * @return the segmentAddress
    */
   public Integer getSegmentAddr()
   {
      return segmentAddress;
   }

   /**
    * @param segmentAddress the segmentAddress to set
    */
   public void setSegmentAddr(Integer segmentAddress)
   {
      this.segmentAddress = segmentAddress;
   }

   /**
    * @return the segmentLength
    */
   public Integer getSegmentLength()
   {
      return segmentLength;
   }

   /**
    * @param segmentLength the segmentLength to set
    */
   public void setSegmentLength(Integer segmentLength)
   {
      this.segmentLength = segmentLength;
   }

   /**
    * @return the accessAttributes
    */
   public Integer getAccessAttributes()
   {
      return accessAttributes;
   }

   /**
    * @param accessAttributes the accessAttributes to set
    */
   public void setAccessAttributes(Integer accessAttributes)
   {
      this.accessAttributes = accessAttributes;
   }

   /**
    * @return the memoryType
    */
   public Integer getMemoryType()
   {
      return memoryType;
   }

   /**
    * @param memoryType the memoryType to set
    */
   public void setMemoryType(Integer memoryType)
   {
      this.memoryType = memoryType;
   }

   /**
    * @return the memoryAttributes
    */
   public Integer getMemoryAttributes()
   {
      return memoryAttributes;
   }

   /**
    * @param memoryAttributes the memoryAttributes to set
    */
   public void setMemoryAttributes(Integer memoryAttributes)
   {
      this.memoryAttributes = memoryAttributes;
   }

   /**
    * @return the block data
    */
   public byte[] getData()
   {
      return data;
   }

   /**
    * @param data the block data to set
    */
   public void setData(byte[] data)
   {
      this.data = data;
   }

   @XmlAttribute(name = "data")
   String getDataBase64Str()
   {
      return DatatypeConverter.printBase64Binary(data).toLowerCase();
   }

   void setDataBase64Str(String str)
   {
      data = DatatypeConverter.parseBase64Binary(str);
   }

   /**
    * @return the block mask
    */
   public byte[] getMask()
   {
      return mask;
   }

   /**
    * @param mask the block mask to set
    */
   public void setBlockMask(byte[] mask)
   {
      this.mask = mask;
   }

   @XmlAttribute(name = "mask")
   String getMaskStr()
   {
      return DatatypeConverter.printBase64Binary(mask).toLowerCase();
   }

   void setMaskStr(String str)
   {
      mask = DatatypeConverter.parseBase64Binary(str);
   }

   /**
    * Set the data block paragraphs.
    *
    * @param paragraphs - the data block paragraphs.
    */
   public void setParagraphs(ArrayListModel<DataBlockParagraph> paragraphs)
   {
      this.paragraphs = paragraphs;
   }

   /**
    * @return The data block paragraphs.
    */
   public ArrayListModel<DataBlockParagraph> getParagraphs()
   {
      if (paragraphs == null)
         paragraphs = new ArrayListModel<DataBlockParagraph>();

      return paragraphs;
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
      if (!(o instanceof DataBlock))
         return false;
      final DataBlock oo = (DataBlock) o;
      return id == oo.id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name;
   }
}

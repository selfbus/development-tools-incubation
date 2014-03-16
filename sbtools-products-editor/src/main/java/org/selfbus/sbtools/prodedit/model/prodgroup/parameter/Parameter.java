package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A parameter of a program.
 */
@XmlRootElement
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Parameter extends AbstractParameterContainer
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(Parameter.class);
   private static final long serialVersionUID = -4919375775576501382L;

   @XmlAttribute(name = "name", required = true)
   private String name;

   @XmlAttribute(name = "category", required = true)
   private ParameterCategory category;

   @XmlAttribute(name = "visible")
   private boolean visible = true;

   @XmlAttribute(name = "type_id", required = true)
   private int typeId;

   @XmlAttribute(name = "size", required = true)
   private int size;

   @XmlAttribute(name = "bit_offset")
   private Integer bitOffset;

   @XmlAttribute(name = "default_int")
   private Integer defaultInt;

   @XmlAttribute(name = "default_double")
   private Double defaultDouble;

   @XmlAttribute(name = "default_string")
   private String defaultString;

   /**
    * Create an empty parameter object.
    */
   public Parameter()
   {
   }

   /**
    * Create a parameter object with a parameter type ID.
    * 
    * @param typeId - the parameter type ID.
    */
   public Parameter(int typeId)
   {
      this.typeId = typeId;
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
    * Get the ID of the parameter type.
    * 
    * @return The parameter type ID.
    */
   public int getTypeId()
   {
      return typeId;
   }

   /**
    * Set the parameter type ID.
    * 
    * @param typeId - the parameter type ID to set.
    */
   public void setTypeId(int typeId)
   {
      this.typeId = typeId;
   }

   /**
    * @return the category
    */
   public ParameterCategory getCategory()
   {
      return category;
   }

   /**
    * Set the category of the parameter.
    *
    * @param category the category to set
    */
   public void setCategory(ParameterCategory category)
   {
      this.category = category;
   }

   /**
    * @return the visible flag
    */
   public boolean isVisible()
   {
      return visible;
   }

   /**
    * Set if the parameter is visible in the parameter dialog.
    *
    * @param visible the visible to set
    */
   public void setVisible(boolean visible)
   {
      this.visible = visible;
   }

   /**
    * @return the size
    */
   public int getSize()
   {
      return size;
   }

   /**
    * @param size the size to set
    */
   public void setSize(int size)
   {
      this.size = size;
   }

   /**
    * @return the bitOffset
    */
   public Integer getBitOffset()
   {
      return bitOffset;
   }

   /**
    * @param bitOffset the bitOffset to set
    */
   public void setBitOffset(Integer bitOffset)
   {
      this.bitOffset = bitOffset;
   }

   /**
    * @return the default integer value
    */
   public Integer getDefaultInt()
   {
      return defaultInt;
   }

   /**
    * @param defaultInt the default to set
    */
   public void setDefaultInt(Integer defaultInt)
   {
      //LOGGER.debug("Param #{} defaultInt={}", id, defaultInt);
      this.defaultInt = defaultInt;
   }

   /**
    * @return the defaultDouble
    */
   public Double getDefaultDouble()
   {
      return defaultDouble;
   }

   /**
    * @param defaultDouble the defaultDouble to set
    */
   public void setDefaultDouble(Double defaultDouble)
   {
      this.defaultDouble = defaultDouble;
   }

   /**
    * @return the defaultString
    */
   public String getDefaultString()
   {
      return defaultString;
   }

   /**
    * @param defaultString the defaultString to set
    */
   public void setDefaultString(String defaultString)
   {
      this.defaultString = defaultString;
   }
}

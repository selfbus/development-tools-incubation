package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;

import com.jgoodies.binding.beans.Model;

/**
 * Values of an enum parameter type.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ParameterValue extends Model
{
   private static final long serialVersionUID = -6983752023306872120L;

   @XmlAttribute(name = "order", required = true)
   private int order;

   @XmlAttribute(name = "int_value")
   private Integer intValue;

   @XmlAttribute(name = "binary_value")
   private byte[] binaryValue;

   @XmlAttribute(name = "double_value")
   private Double doubleValue;

   @XmlElement(name = "label")
   private MultiLingualText label = new MultiLingualText();

   /**
    * Create an empty parameter value.
    */
   public ParameterValue()
   {
   }

   /**
    * @return the label
    */
   public MultiLingualText getLabel()
   {
      if (label == null)
         label = new MultiLingualText();

      return label;
   }

   /**
    * Set the label.
    *
    * @param label - the label to set
    */
   public void setLabel(MultiLingualText label)
   {
      this.label = label;
   }

   /**
    * @return the displayOrder
    */
   public int getOrder()
   {
      return order;
   }

   /**
    * @param order the display order to set
    */
   public void setOrder(int order)
   {
      this.order = order;
   }

   /**
    * @return The integer value that will be loaded into the BCU's memory.
    */
   public Integer getIntValue()
   {
      return intValue;
   }

   /**
    * Set the integer value that will be loaded into the BCU's memory.
    *
    * @param intValue the intValue to set
    */
   public void setIntValue(Integer intValue)
   {
      this.intValue = intValue;
   }

   /**
    * @return The binary value that will be loaded into the BCU's memory. May be
    *         null.
    */
   public byte[] getBinaryValue()
   {
      return binaryValue;
   }

   /**
    * Set the binary value that will be loaded into the BCU's memory. May be
    * null.
    *
    * @param binaryValue - the binary value to set
    */
   public void setBinaryValue(byte[] binaryValue)
   {
      this.binaryValue = binaryValue;
   }

   /**
    * @return The double value that will be loaded into the BCU's memory.
    */
   public Double getDoubleValue()
   {
      return doubleValue;
   }

   /**
    * Set the double value that will be loaded into the BCU's memory.
    *
    * @param doubleValue - the double value to set.
    */
   public void setDoubleValue(Double doubleValue)
   {
      this.doubleValue = doubleValue;
   }

   /**
    * @return The value that is displayed when showing the parameter.
    */
   public String getDisplayedValue()
   {
      // TODO Auto-generated method stub
      return null;
   }
}

package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * The type of a program's parameter. The parameter type is used to group parameters of the same
 * type that can have the same range of values.
 * 
 * The parameter type holds e.g. the possible values for a parameter and minimum/maximum values for
 * numbers.
 * 
 * What one would expect here, which type a parameter is, contains the class
 * {@link ParameterAtomicType}, which can be access with {@link #getAtomicType()}.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ParameterType extends Model implements Identifiable
{
   private static final long serialVersionUID = -6354471736343630279L;

   @XmlAttribute(name = "id", required = true)
   private int id;

   //@XmlAttribute(name = "atomic_type", required = true)
   private ParameterAtomicType atomicType;

   @XmlAttribute(name = "name", required = true)
   private String name;

   @XmlAttribute(name = "min_value")
   private Integer minValue;

   @XmlAttribute(name = "max_value")
   private Integer maxValue;

   @XmlAttribute(name = "min_double_value")
   private Double minDoubleValue;

   @XmlAttribute(name = "max_double_value")
   private Double maxDoubleValue;

   @XmlAttribute(name = "size")
   private int size;

   @XmlElement(name = "value")
   private ArrayListModel<ParameterValue> values;

   /**
    * Create an empty parameter type.
    */
   public ParameterType()
   {
   }

   /**
    * Create a parameter type.
    * 
    * @param atomicType - the parameter's atomic type.
    */
   public ParameterType(ParameterAtomicType atomicType)
   {
      this.atomicType = atomicType;
   }

   /**
    * Create a parameter type.
    * 
    * @param atomicType - the parameter's atomic type.
    * @param name - the name
    */
   public ParameterType(ParameterAtomicType atomicType, String name)
   {
      this.atomicType = atomicType;
      this.name = name;
   }

   /**
    * @return the id
    */
   public int getId()
   {
      return id;
   }

   /**
    * @return the id as string
    */
   public String getIdStr()
   {
      return Integer.toString(id);
   }

   /**
    * Set the id. Use {@link ProductGroup#getNextUniqueId()} to get a unique ID.
    * 
    * @param id - the id to set.
    */
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the atomic type of the parameter.
    */
   public ParameterAtomicType getAtomicType()
   {
      return atomicType;
   }

   /**
    * Set the atomic type of the parameter.
    * 
    * @param atomicType - the atomic type to set.
    */
   public void setAtomicType(ParameterAtomicType atomicType)
   {
      this.atomicType = atomicType;
   }

   /**
    * @return the atomic type of the parameter as string.
    */
   @XmlAttribute(name = "atomic_type", required = true)
   String getAtomicTypeStr()
   {
      return atomicType.toString();
   }

   /**
    * Set the atomic type of the parameter as String.
    * 
    * @param str - the atomic type to set.
    */
   void setAtomicTypeStr(String str)
   {
      this.atomicType = ParameterAtomicType.valueOf(str);
   }

   /**
    * @return the name of the parameter type.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the parameter type.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      String oldName = this.name;
      this.name = name;
      firePropertyChange("name", oldName, name);
   }

   /**
    * @return the minimum value.
    */
   public Integer getMinValue()
   {
      return minValue;
   }

   /**
    * Set the minimum value.
    * 
    * @param minValue - the minimum value to set.
    */
   public void setMinValue(Integer minValue)
   {
      this.minValue = minValue;
   }

   /**
    * @return the maximum value.
    */
   public Integer getMaxValue()
   {
      return maxValue;
   }

   /**
    * Set the maximum value.
    * 
    * @param maxValue - the maximum value to set.
    */
   public void setMaxValue(Integer maxValue)
   {
      this.maxValue = maxValue;
   }

   /**
    * @return the minimum double value.
    */
   public Double getMinDoubleValue()
   {
      return minDoubleValue;
   }

   /**
    * Set the minimum double value.
    * 
    * @param minValue - the minimum value to set.
    */
   public void setMinDoubleValue(Double minValue)
   {
      this.minDoubleValue = minValue;
   }

   /**
    * @return the maximum double value.
    */
   public Double getMaxDoubleValue()
   {
      return maxDoubleValue;
   }

   /**
    * Set the maximum double value.
    * 
    * @param maxValue - the maximum value to set.
    */
   public void setMaxDoubleValue(Double maxValue)
   {
      this.maxDoubleValue = maxValue;
   }

   /**
    * @return the allowed values or null if no specific allowed values are set.
    */
   public ArrayListModel<ParameterValue> getValues()
   {
      return values;
   }

   /**
    * Create a new parameter value.
    * 
    * @return The created value.
    */
   public ParameterValue createValue()
   {
      int id = values == null ? 1 : values.size() + 1;

      String langId = I18n.getMessage("Project.languageId");
      String label = I18n.formatMessage("ParameterType.newValue", Integer.toString(id));

      ParameterValue value = new ParameterValue();
      value.getLabel().setText(langId, label);
      value.setIntValue(0);

      addValue(value);
      reorderValues();

      return value;
   }

   /**
    * Add a parameter value to the parameter type's values
    * 
    * @param value - the value to add
    */
   public void addValue(ParameterValue value)
   {
      if (values == null)
         values = new ArrayListModel<ParameterValue>();

      values.add(value);
   }

   /**
    * Update the order of the parameter values.
    */
   public void reorderValues()
   {
      if (values != null)
      {
         int order = 1;
         for (ParameterValue value : values)
         {
            value.setOrder(order++);
         }
      }
   }

   /**
    * Sort the parameter values by {@link ParameterValue#getOrder() order}.
    */
   public void sortValues()
   {
      if (values == null)
         return;

      ParameterValue[] arr = new ParameterValue[values.size()];
      values.toArray(arr);

      Arrays.sort(arr, new Comparator<ParameterValue>()
      {
         @Override
         public int compare(ParameterValue a, ParameterValue b)
         {
            return a.getOrder() - b.getOrder();
         }
      });

      values.clear();
      Collections.addAll(values, arr);
   }

   /**
    * Set the allowed values or null if no specific allowed values are set.
    * 
    * @param values - the allowed values to set.
    */
   public void setValues(ArrayListModel<ParameterValue> values)
   {
      this.values = values;
   }

   /**
    * Find the parameter value with the given {@link ParameterValue#getIntValue() integer value}.
    * 
    * @param val - the integer value to find.
    * @return The first matching parameter value, or null if not found.
    */
   public ParameterValue findValueByInt(Integer val)
   {
      if (values == null || val == null)
         return null;

      for (ParameterValue value : values)
      {
         if (val.equals(value.getIntValue()))
            return value;
      }

      return null;
   }

   /**
    * @return the size of the parameter in bits.
    */
   public int getSize()
   {
      return size;
   }

   /**
    * Set the size of the parameter in bits.
    * 
    * @param size the size to set
    */
   public void setSize(int size)
   {
      this.size = size;
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
   public String toString()
   {
      return name;
   }
}

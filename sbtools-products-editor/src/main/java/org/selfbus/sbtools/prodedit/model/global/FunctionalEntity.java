package org.selfbus.sbtools.prodedit.model.global;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * A functional entity.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class FunctionalEntity extends Model implements Identifiable, Comparable<FunctionalEntity>
{
   private static final long serialVersionUID = 1262854512380193005L;

   @XmlElement(name = "functional_entity")
   private final ArrayListModel<FunctionalEntity> childs = new ArrayListModel<FunctionalEntity>();

   @XmlAttribute
   private int id;

   @XmlAttribute
   private String name;

   @XmlAttribute
   private int number;

   @XmlAttribute
   private String description;

   @XmlAttribute(name = "manufacturer_id")
   private int manufacturerId;

   /**
    * Create an empty functional entity object.
    */
   public FunctionalEntity()
   {
   }

   /**
    * Create a functional entity object.
    * 
    * @param id - the ID
    * @param name - the name
    */
   public FunctionalEntity(int id, String name)
   {
      this.id = id;
      this.name = name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getId()
   {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the ID as string
    */
   public String getIdStr()
   {
      return Integer.toString(id);
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
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * @return the manufacturerId
    */
   public int getManufacturerId()
   {
      return manufacturerId;
   }

   /**
    * @param manufacturerId the manufacturerId to set
    */
   public void setManufacturerId(int manufacturerId)
   {
      this.manufacturerId = manufacturerId;
   }

   /**
    * @return the childs
    */
   public ArrayListModel<FunctionalEntity> getChilds()
   {
      return childs;
   }

   /**
    * Add a functional entity child.
    * 
    * @param child - the child to add
    */
   public void add(FunctionalEntity child)
   {
      childs.add(child);
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
   public int compareTo(FunctionalEntity o)
   {
      return name.compareTo(o.name);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof FunctionalEntity))
         return false;

      FunctionalEntity oo = (FunctionalEntity) o;
      return id == oo.id && name.equals(oo.name);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name == null || name.isEmpty() ? I18n.getMessage("unnamed") : name;
   }
}

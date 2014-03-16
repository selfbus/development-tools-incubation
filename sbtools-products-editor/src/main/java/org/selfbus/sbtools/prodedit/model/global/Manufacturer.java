package org.selfbus.sbtools.prodedit.model.global;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;

import com.jgoodies.binding.beans.Model;

/**
 * A manufacturer.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Manufacturer extends Model implements Identifiable
{
   private static final long serialVersionUID = -6758441893017604232L;

   /**
    * The "unknown" manufacturer
    */
   public static final Manufacturer NONE = new Manufacturer(0, "unknown");

   @XmlAttribute
   public String name;

   @XmlAttribute
   public int id;

   /**
    * Create a manufacturer.
    */
   public Manufacturer()
   {
   }

   /**
    * Create a manufacturer.
    * 
    * @param id - the ID
    * @param name - the name
    */
   public Manufacturer(int id, String name)
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
}

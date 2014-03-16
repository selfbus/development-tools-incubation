package org.selfbus.sbtools.devtool.project.model.group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * A mid group.
 */
@XmlType(name = "MidGroup", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class MidGroup
{
   @XmlAttribute
   protected String name;

   @XmlAttribute
   protected int address;

   /**
    * @return The name of the mid group.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the mid group.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return The address of the mid group.
    */
   public int getAddress()
   {
      return address;
   }

   /**
    * Set the address of the mid group.
    * 
    * @param address - the address to set
    */
   public void setAddress(int address)
   {
      this.address = address;
   }
}

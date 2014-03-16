package org.selfbus.sbtools.devtool.project.model.group;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * A main group.
 */
@XmlType(name = "MainGroup", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class MainGroup
{
   @XmlAttribute
   protected String name;

   @XmlAttribute
   protected int address;

   @XmlElement(name = "midGroup")
   protected List<MidGroup> midGroups;

   /**
    * @return The name of the group.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the group.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return The address of the main group.
    */
   public int getAddress()
   {
      return address;
   }

   /**
    * Set the address of the main group.
    *
    * @param address - the address to set
    */
   public void setAddress(int address)
   {
      this.address = address;
   }

   /**
    * @return The mid groups.
    */
   public List<MidGroup> getMidGroups()
   {
      return midGroups;
   }
}

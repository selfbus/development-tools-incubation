package org.selfbus.sbtools.devtool.project.model.group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.common.address.GroupAddress;
import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * A group.
 */
@XmlType(name = "Group", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Group implements Comparable<Group>
{
   @XmlAttribute
   protected String name;

   protected GroupAddress address;
   
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
    * @return The address of the group.
    */
   public GroupAddress getAddress()
   {
      return address;
   }

   /**
    * Set the address of the group.
    *
    * @param address - the address to set
    */
   public void setAddress(GroupAddress address)
   {
      this.address = address;
   }

   /**
    * @return The address of the group as string.
    */
   @XmlAttribute(name = "address")
   public String getAddressStr()
   {
      return address.toString();
   }

   /**
    * Set the address of the group.
    *
    * @param address - the address to set
    */
   public void setAddressStr(String address)
   {
      this.address = GroupAddress.valueOf(address);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return address + " " + name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof Group)) return false;
      return address.getAddr() == ((Group) o).address.getAddr();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(Group o)
   {
      return address.getAddr() - o.address.getAddr();
   }
}

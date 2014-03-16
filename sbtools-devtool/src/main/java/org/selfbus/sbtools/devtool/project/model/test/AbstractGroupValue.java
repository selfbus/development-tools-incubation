package org.selfbus.sbtools.devtool.project.model.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.common.address.GroupAddress;
import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * Abstract test step for group value telegrams.
 */
@XmlType(name = "AbstractGroupValue", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractGroupValue extends AbstractTestStep
{
   protected GroupAddress address;

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
}

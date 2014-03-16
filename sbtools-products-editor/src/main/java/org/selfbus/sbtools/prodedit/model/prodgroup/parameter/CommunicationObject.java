package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.enums.ObjectPriority;
import org.selfbus.sbtools.prodedit.model.enums.ObjectType;

/**
 * A communication object of a program. Communication objects are the interface
 * to other devices on the bus. Group data telegrams send and receive the data
 * of the communication objects.
 */
@XmlRootElement
@XmlType(name = "communication_object", propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class CommunicationObject extends AbstractParameterNode
{
   private static final long serialVersionUID = 8221705107829888090L;

   @XmlElement(name = "name")
   private MultiLingualText name;

   @XmlElement(name = "function")
   protected MultiLingualText function;

   @XmlAttribute(name = "read_enabled")
   protected boolean readEnabled;

   @XmlAttribute(name = "write_enabled")
   protected boolean writeEnabled;

   @XmlAttribute(name = "comm_enabled")
   protected boolean commEnabled = true;

   @XmlAttribute(name = "trans_enabled")
   protected boolean transEnabled = true;

   @XmlAttribute(name = "priority")
   protected ObjectPriority priority;

   @XmlAttribute(name = "type")
   protected ObjectType type;

   /**
    * @return the name
    */
   public MultiLingualText getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(MultiLingualText name)
   {
      this.name = name;
   }

   /**
    * @return the function
    */
   public MultiLingualText getFunction()
   {
      return function;
   }

   /**
    * @param function the function to set
    */
   public void setFunction(MultiLingualText function)
   {
      this.function = function;
   }

   /**
    * @return the readEnabled
    */
   public boolean isReadEnabled()
   {
      return readEnabled;
   }

   /**
    * @param readEnabled the readEnabled to set
    */
   public void setReadEnabled(boolean readEnabled)
   {
      this.readEnabled = readEnabled;
   }

   /**
    * @return the writeEnabled
    */
   public boolean isWriteEnabled()
   {
      return writeEnabled;
   }

   /**
    * @param writeEnabled the writeEnabled to set
    */
   public void setWriteEnabled(boolean writeEnabled)
   {
      this.writeEnabled = writeEnabled;
   }

   /**
    * @return the commEnabled
    */
   public boolean isCommEnabled()
   {
      return commEnabled;
   }

   /**
    * @param commEnabled the commEnabled to set
    */
   public void setCommEnabled(boolean commEnabled)
   {
      this.commEnabled = commEnabled;
   }

   /**
    * @return the transEnabled
    */
   public boolean isTransEnabled()
   {
      return transEnabled;
   }

   /**
    * @param transEnabled the transEnabled to set
    */
   public void setTransEnabled(boolean transEnabled)
   {
      this.transEnabled = transEnabled;
   }

   /**
    * @return the priority
    */
   public ObjectPriority getPriority()
   {
      return priority;
   }

   /**
    * @param priority the priority to set
    */
   public void setPriority(ObjectPriority priority)
   {
      this.priority = priority;
   }

   /**
    * @return the type
    */
   public ObjectType getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(ObjectType type)
   {
      this.type = type;
   }
}

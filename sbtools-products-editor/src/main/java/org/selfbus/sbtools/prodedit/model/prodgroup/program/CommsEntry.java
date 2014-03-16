package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import org.selfbus.sbtools.prodedit.model.enums.ObjectPriority;
import org.selfbus.sbtools.prodedit.model.enums.ObjectType;

/**
 * An entry of the device's communications table.
 */
public class CommsEntry
{
   /**
    * The pointer to the com-object's value.
    */
   public int valuePtr;

   /**
    * The com-object type.
    */
   public ObjectType type;

   public ObjectPriority priority;
   public boolean readEnabled;
   public boolean writeEnabled;
   public boolean commEnabled;
   public boolean transEnabled;
   public boolean responseOnUpdate;
   public boolean memSegment;

   /**
    * Create a communications entry.
    */
   public CommsEntry()
   {
   }

   /**
    * Create a communications entry.
    *
    * @param valuePtr - the pointer to the com-object's value
    * @param config - the configuration
    * @param type - the type byte
    */
   public CommsEntry(int valuePtr, int config, int type)
   {
      this.valuePtr = valuePtr;

      this.priority = ObjectPriority.valueOf(config & 3);
      this.commEnabled = (config & 0x04) != 0;
      this.readEnabled = (config & 0x08) != 0;
      this.writeEnabled = (config & 0x10) != 0;
      this.memSegment = (config & 0x20) != 0;
      this.transEnabled = (config & 0x40) != 0;
      this.responseOnUpdate = (config & 0x80) != 0;

      this.type = ObjectType.valueOf(type);
   }
}

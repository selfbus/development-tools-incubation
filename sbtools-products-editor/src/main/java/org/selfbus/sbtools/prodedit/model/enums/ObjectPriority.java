package org.selfbus.sbtools.prodedit.model.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Priority of communication objects.
 * 
 * The {@link #ordinal() ordinal} gives the numeric value of the priority as it
 * is used in communication and the KNX devices.
 */
@XmlType
@XmlEnum
public enum ObjectPriority
{
   /**
    * System priority. This is the highest priority.
    */
   SYSTEM,

   /**
    * Alarm / urgent priority.
    */
   ALARM,

   /**
    * High priority.
    */
   HIGH,

   /**
    * Low / normal priority. This is the lowest priority.
    */
   LOW;

   private final String name;

   /**
    * @return the id
    */
   public int getId()
   {
      return ordinal();
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return the name
    */
   @Override
   public String toString()
   {
      return name;
   }

   /**
    * Constructor.
    */
   private ObjectPriority()
   {
      this.name = I18n.getMessage("ObjectPriority." + name());
   }

   /**
    * @param ordinal - the ordinal to lookup.
    * @return The object for the given ordinal, or null if not found.
    */
   public static ObjectPriority valueOf(int ordinal)
   {
      for (ObjectPriority o : values())
      {
         if (o.ordinal() == ordinal)
            return o;
      }

      return null;
   }
}

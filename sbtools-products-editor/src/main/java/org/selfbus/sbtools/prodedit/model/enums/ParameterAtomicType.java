package org.selfbus.sbtools.prodedit.model.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * The atomic type of a parameter: none, unsigned, signed, string, enum, long enum.
 */
@XmlType
@XmlEnum
public enum ParameterAtomicType
{
   // WARNING: do not change the order of the enum values.
   // They must come in the same order as in ETS.

   /**
    * No parameter type. Used for labels and pages in the parameter editor.
    */
   NONE(' ', "none", null),

   /**
    * Unsigned integer.
    */
   UNSIGNED('+', "unsigned", Integer.class),

   /**
    * Signed integer.
    */
   SIGNED('-', "signed", Integer.class),

   /**
    * String.
    */
   STRING('$', "string", String.class),

   /**
    * Enumeration.
    */
   ENUM('Y', "enum", Integer.class),

   /**
    * Long enumeration.
    */
   LONG_ENUM('Z', "long enum", Integer.class),

   /**
    * Floating point.
    */
   FLOAT('B', "floating point", Float.class),

   /**
    * Floating point enumeration.
    */
   FLOAT_ENUM('C', "floating point enum", Float.class);

   private final char dispAttr;
   private final Class<?> parameterClass;
   private final String name;

   /*
    * Internal constructor.
    */
   private ParameterAtomicType(char dispAttr, String name, Class<?> parameterClass)
   {
      this.dispAttr = dispAttr;
      this.name = name;
      this.parameterClass = parameterClass;
   }

   /**
    * @return the display-attribute character.
    */
   public char getDispAttr()
   {
      return dispAttr;
   }

   /**
    * @return The name.
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return the translated name of the type.
    */
   public String getLabel()
   {
      return I18n.getMessage("ParameterAtomicType." + name());
   }

   /**
    * @return the class that is used for parameters of this type.
    */
   public Class<?> getParameterClass()
   {
      return parameterClass;
   }

   /**
    * Get the {@link ParameterAtomicType} for a specific ordinal.
    * 
    * @param ordinal - the ordinal to lookup.
    * 
    * @return the object for the given ordinal, or null if the ordinal
    *         is unknown.
    */
   public static ParameterAtomicType valueOf(int ordinal)
   {
      for (ParameterAtomicType o : values())
         if (o.ordinal() == ordinal)
            return o;
      return null;
   }
}

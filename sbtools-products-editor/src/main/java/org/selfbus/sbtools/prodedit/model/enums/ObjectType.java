package org.selfbus.sbtools.prodedit.model.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Types of communication objects.
 * 
 * The {@link #ordinal() ordinal} of the communication object gives the ID for KNX devices, e.g. in
 * the EEPROM communication table.
 */
@XmlType
@XmlEnum
public enum ObjectType
{
   /**
    * 1 bit.
    */
   @XmlEnumValue("1")
   BITS_1(1),

   /**
    * 2 bits.
    */
   @XmlEnumValue("1")
   BITS_2(2),

   /**
    * 3 bits.
    */
   @XmlEnumValue("1")
   BITS_3(3),

   /**
    * 4 bits.
    */
   @XmlEnumValue("1")
   BITS_4(4),

   /**
    * 5 bits.
    */
   BITS_5(5),

   /**
    * 6 bits.
    */
   BITS_6(6),

   /**
    * 7 bits.
    */
   BITS_7(7),

   /**
    * 1 byte.
    */
   BYTES_1(8),

   /**
    * 1 byte.
    */
   BYTES_2(16),

   /**
    * 3 bytes.
    */
   BYTES_3(24),

   /**
    * 4 bytes float.
    */
   BYTES_4(32),

   /**
    * 6 bytes.
    */
   BYTES_6(48),

   /**
    * 8 bytes.
    */
   BYTES_8(64),

   /**
    * 10 bytes.
    */
   BYTES_10(80),

   /**
    * 14 bytes.
    */
   BYTES_14(112),

   /**
    * 1..14 bytes.
    */
   VARBYTES_14(112, "0..14 Bytes");

   private final String label, vdName;
   private final int bitLength;

   /**
    * @return the id
    */
   public int getId()
   {
      return ordinal();
   }

   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @return the name
    */
   public String getVdName()
   {
      return vdName;
   }

   /**
    * @return the size in bits
    */
   public int getBitSize()
   {
      return bitLength;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return label;
   }

   /**
    * Get the object type from the object type's ordinal number.
    * 
    * @param ordinal - the ordinal number to process.
    * @return The object type.
    */
   static public ObjectType valueOf(int ordinal)
   {
      for (ObjectType t : values())
      {
         if (t.ordinal() == ordinal)
            return t;
      }

      return null;
   }

   /**
    * Constructor.
    * 
    * @param bitLength - the number of bits of the object type.
    */
   private ObjectType(int bitLength)
   {
      this.label = I18n.getMessage("ObjectType." + name());
      this.vdName = bitLength > 7 ? Integer.toString(bitLength >> 3) + " Byte" : Integer.toString(bitLength) + " Bit";
      this.bitLength = bitLength;
   }

   /**
    * Constructor.
    * 
    * @param bitLength - the number of bits of the object type.
    */
   private ObjectType(int bitLength, String vdName)
   {
      this.label = I18n.getMessage("ObjectType." + name());
      this.vdName = vdName;
      this.bitLength = bitLength;
   }
}

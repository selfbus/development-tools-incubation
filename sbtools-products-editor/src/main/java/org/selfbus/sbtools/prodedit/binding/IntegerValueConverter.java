package org.selfbus.sbtools.prodedit.binding;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.binding.value.BindingConverter;

/**
 * A converter that converts an integer value to a string value.
 */
public class IntegerValueConverter implements BindingConverter<Integer, String>
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(IntegerValueConverter.class);

   private final int radix;
   private final int digits;
   
   /**
    * Create an integer value converter for decimal values.
    */
   public IntegerValueConverter()
   {
      this(10, 0);
   }

   /**
    * Create an integer value converter.
    * 
    * @param radix - the radix of the values.
    */
   public IntegerValueConverter(int radix)
   {
      this(radix, 0);
   }

   /**
    * Create an integer value converter.
    * 
    * @param radix - the radix of the values.
    * @param digits - the number of digits to display.
    */
   public IntegerValueConverter(int radix, int digits)
   {
      this.radix = radix;
      this.digits = digits;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String targetValue(Integer sourceValue)
   {
      if (sourceValue == null)
         return "";

      try
      {
         String str = Integer.toString((Integer) sourceValue, radix);
         if (digits > 0 && str.length() < digits)
            return StringUtils.repeat('0', digits - str.length()) + str;
         return str;
      }
      catch (Exception e)
      {
         return "";
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Integer sourceValue(String targetValue)
   {
      if (targetValue == null || ((String) targetValue).isEmpty())
         return null;

      try
      {
         return Integer.parseInt((String) targetValue, radix);
      }
      catch (NumberFormatException e)
      {
         return null;
      }
   }
}

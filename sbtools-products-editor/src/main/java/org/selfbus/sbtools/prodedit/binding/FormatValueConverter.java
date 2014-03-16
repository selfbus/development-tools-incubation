package org.selfbus.sbtools.prodedit.binding;

import java.text.MessageFormat;

import com.jgoodies.binding.value.BindingConverter;

/**
 * A read-only converter that converts an integer value to a string containing the value.
 */
public class FormatValueConverter implements BindingConverter<Integer, String>
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(IntegerValueConverter.class);
   private final String format;

   /**
    * Create a read-only converter that converts an integer value to a string containing the value.
    * The placeholder "{0}" in the format is replaced with the actual value. If the value is null,
    * an empty string is returned.
    *
    * @param format - the format to use
    */
   public FormatValueConverter(String format)
   {
      this.format = format;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String targetValue(Integer sourceValue)
   {
      if (sourceValue == null)
         return "";

      return MessageFormat.format(format, sourceValue.toString());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Integer sourceValue(String targetValue)
   {
      throw new IllegalAccessError("this converter is read only");
   }
}

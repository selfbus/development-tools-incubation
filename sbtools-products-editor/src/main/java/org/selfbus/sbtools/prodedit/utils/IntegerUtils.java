package org.selfbus.sbtools.prodedit.utils;

/**
 * Utility methods for integer handling.
 */
public final class IntegerUtils
{
   /**
    * Parse the string, handle empty strings as null.
    *
    * @param str - the string to parse
    * @return An integer of the string, null if the string is empty or null.
    * @throws NumberFormatException - if the string does not contain a parsable integer.
    */
   public static Integer parseIntNull(String str)
   {
      if (str == null || str.trim().isEmpty())
         return null;

      return Integer.parseInt(str);
   }
}

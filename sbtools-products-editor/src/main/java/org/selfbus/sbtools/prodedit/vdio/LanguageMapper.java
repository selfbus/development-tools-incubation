package org.selfbus.sbtools.prodedit.vdio;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.Validate;

/**
 * A mapper for mapping to/from from 2 characters (ISO 3166-1-Alpha-2) short language codes and ETS
 * language IDs.
 */
public final class LanguageMapper
{
   private static final String BUNDLE_NAME = "org.selfbus.sbtools.prodedit/lang2ets";
   private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

   private static final Map<Integer,String> ets2lang = new HashMap<Integer,String>();
   private static final Map<String,Integer> lang2ets = new HashMap<String,Integer>();

   static
   {
      for (String langId : BUNDLE.keySet())
      {
         Integer etsId = Integer.parseInt(BUNDLE.getString(langId));

         ets2lang.put(etsId, langId);
         lang2ets.put(langId, etsId);
      }
   }

   /**
    * Map the internal language ID to the ETS language ID
    *
    * @param langId - the language ID
    * @return The ETS language ID.
    * 
    * @throws IllegalArgumentException if no mapping could be found
    */
   public static int getEtsId(String langId)
   {
      Integer etsId = lang2ets.get(langId);
      Validate.isTrue(etsId != null, "No mapping found in lang2ets.properties for language \"" + langId + "\"");
      return etsId;
   }

   /**
    * Map the ETS language ID to the internal language ID
    *
    * @param etsId - the ETS language ID
    * @return The internal language ID.
    * 
    * @throws IllegalArgumentException if no mapping could be found
    */
   public static String getLangId(int etsId)
   {
      String langId = ets2lang.get(etsId);
      Validate.isTrue(langId != null, "No mapping found in lang2ets.properties for ETS language " + etsId);
      return langId;
   }
}

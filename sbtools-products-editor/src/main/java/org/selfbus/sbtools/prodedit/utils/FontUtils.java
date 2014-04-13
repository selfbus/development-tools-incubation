package org.selfbus.sbtools.prodedit.utils;

import java.awt.Font;

import javax.swing.UIManager;

/**
 * Font utility methods.
 */
public final class FontUtils
{
   private static Font captionFont, subCaptionFont;

   /**
    * @return The default caption font for forms
    */
   public static Font getCaptionFont()
   {
      if (captionFont == null)
      {
         captionFont = UIManager.getDefaults().getFont("Label.font");
         captionFont = captionFont.deriveFont(Font.BOLD, captionFont.getSize() * 1.15f);
      }

      return captionFont;
   }

   /**
    * @return The default secondary caption font for forms
    */
   public static Font getSubCaptionFont()
   {
      if (subCaptionFont == null)
      {
         subCaptionFont = UIManager.getDefaults().getFont("Label.font");
         subCaptionFont = subCaptionFont.deriveFont(Font.BOLD, subCaptionFont.getSize() * 1.02f);
      }

      return subCaptionFont;
   }
}

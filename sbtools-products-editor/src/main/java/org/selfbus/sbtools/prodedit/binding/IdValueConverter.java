package org.selfbus.sbtools.prodedit.binding;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * A {@link FormatValueConverter} with the localized format "ID {0}".
 */
public class IdValueConverter extends FormatValueConverter
{
   public IdValueConverter()
   {
      super(I18n.getMessage("IdValueConverter.label"));
   }
}

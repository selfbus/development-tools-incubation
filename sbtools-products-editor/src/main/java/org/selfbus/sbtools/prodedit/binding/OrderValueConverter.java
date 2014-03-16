package org.selfbus.sbtools.prodedit.binding;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * A {@link FormatValueConverter} with the localized format "Order {0}".
 */
public class OrderValueConverter extends FormatValueConverter
{
   public OrderValueConverter()
   {
      super(I18n.getMessage("OrderValueConverter.label"));
   }
}

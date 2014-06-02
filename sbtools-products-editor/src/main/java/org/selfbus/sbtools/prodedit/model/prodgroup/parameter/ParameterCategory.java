package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * The Category of the parameter: page, label, value.
 */
@XmlType
@XmlEnum
public enum ParameterCategory
{
   /**
    * A (tab) page in the parameter configuration dialog.
    */
   PAGE,

   /**
    * A label text line.
    */
   LABEL,

   /**
    * A normal parameter input field / combo box.
    */
   VALUE;

   /**
    * @return the translated name of the type.
    */
   public String getLabel()
   {
      return I18n.getMessage("ParameterCategory." + name());
   }
}

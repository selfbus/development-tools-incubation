package org.selfbus.sbtools.prodedit.model.global;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.internal.I18n;

import com.jgoodies.binding.beans.Model;

/**
 * A language.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Language extends Model implements Comparable<Language>
{
   private static final long serialVersionUID = -5078671369479853568L;

   @XmlAttribute(required = true)
   private String id;

   @XmlAttribute(required = true)
   private String name;

   /**
    * Create a language.
    */
   public Language()
   {
      id = "??";
   }

   /**
    * Create a language.
    * 
    * @param id - the ID
    * @param name - the name
    */
   public Language(String id, String name)
   {
      this.id = id;
      this.name = name;
   }

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      Validate.notNull(name);
      this.name = name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      if (name != null && !name.isEmpty())
         return name;
      return I18n.getMessage("unnamed");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(Language o)
   {
      return name.compareTo(o.name);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof Language))
         return false;

      Language oo = (Language) o;

      if (name == null && oo.name != null)
         return false;

      return name == oo.name || name.equals(oo.name);
   }
}

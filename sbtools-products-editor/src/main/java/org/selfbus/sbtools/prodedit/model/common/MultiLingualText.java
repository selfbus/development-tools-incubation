package org.selfbus.sbtools.prodedit.model.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.ProdEdit;

import com.jgoodies.common.collect.ArrayListModel;


/**
 * A text / string in multiple languages.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class MultiLingualText
{
   @XmlElement(name = "text")
   private ArrayListModel<Element> texts = new ArrayListModel<Element>();
   
   @XmlType(propOrder = {})
   @XmlAccessorType(XmlAccessType.NONE)
   public static class Element
   {
     @XmlAttribute(name = "id")
     public String langId;

     @XmlAttribute(name = "value")
     public String value;
    
     public Element()
     {
     }
    
     public Element(String langId, String text)
     {
       this.langId = langId;
       this.value = text;
     }

     public String getText()
     {
        return value;
     }
     
     public void setText(String text)
     {
        this.value = text;
     }
   }

   /**
    * Create a multi lingual text element.
    */
   public MultiLingualText()
   {
   }

   /**
    * Create a multi lingual text element with a text in the default language.
    * 
    * @param defaultText - the default text
    */
   public MultiLingualText(String defaultText)
   {
	   setText(ProdEdit.getInstance().getProject().getDefaultLangId(), defaultText);
   }
   
   /**
    * Get the text in the default language.
    * @return The text in the default language.
    */
   public String getDefaultText()
   {
      return getText(ProdEdit.getInstance().getProject().getDefaultLangId());
   }

   /**
    * Get the text for a specific language.
    * 
    * @param lang - the language to get the text for.
    * @return The text or null if not found
    */
   public String getText(String langId)
   {
      return getElement(langId).value;
   }

   /**
    * Set the text for a language.
    *
    * @param langId - the language ID
    * @param text - the text
    */
   public void setText(String langId, String text)
   {
      for (Element e : texts)
      {
         if (e.langId.equals(langId))
         {
            e.value = text;
            return;
         }
      }

      texts.add(new Element(langId, text));
   }


   /**
    * Get the text element for a specific language.
    * 
    * @param langId - the language to get the text for.
    * @return The text element
    */
   public Element getElement(String langId)
   {
      for (Element e : texts)
      {
         if (e.langId.equals(langId))
            return e;
      }

      Element e = new Element(langId, null);
      texts.add(e);

      return e;
   }

   /**
    * @return the texts
    */
   public ArrayListModel<Element> getTexts()
   {
      return texts;
   }

   /**
    * @param texts the texts to set
    */
   public void setTexts(ArrayListModel<Element> texts)
   {
      this.texts = texts;
   }
}

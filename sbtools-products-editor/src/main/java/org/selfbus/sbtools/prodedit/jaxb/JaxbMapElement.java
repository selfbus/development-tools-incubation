package org.selfbus.sbtools.prodedit.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class JaxbMapElement<K, V>
{
   @XmlElement
   public K key;

   @XmlElement
   public V value;

   protected JaxbMapElement()
   { //Required by JAXB
   }

   public JaxbMapElement(K key, V value)
   {
      this.key = key;
      this.value = value;
   }
}

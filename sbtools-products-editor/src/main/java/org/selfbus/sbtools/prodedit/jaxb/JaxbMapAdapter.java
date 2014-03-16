package org.selfbus.sbtools.prodedit.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbMapAdapter<K, V> extends XmlAdapter<JaxbMapElement<K, V>[], Map<K, V>>
{
   @Override
   public JaxbMapElement<K, V>[] marshal(Map<K, V> arg0) throws Exception
   {
      @SuppressWarnings("unchecked")
      JaxbMapElement<K, V>[] mapElements = new JaxbMapElement[arg0.size()];

      int i = 0;
      for (Map.Entry<K, V> entry : arg0.entrySet())
         mapElements[i++] = new JaxbMapElement<K, V>(entry.getKey(), entry.getValue());

      return mapElements;
   }

   @Override
   public Map<K, V> unmarshal(JaxbMapElement<K, V>[] arg0) throws Exception
   {
      Map<K, V> r = new HashMap<K, V>();
      for (JaxbMapElement<K, V> mapelement : arg0)
         r.put(mapelement.key, mapelement.value);
      return r;
   }
}

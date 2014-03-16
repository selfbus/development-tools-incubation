package org.selfbus.sbtools.prodedit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.selfbus.sbtools.vdio.VdioException;

/**
 * A properties file loader. The properties file is searched in the class path.
 */
public final class ClassPathPropertiesLoader
{
   /**
    * Load a properties file that is searched in the directory "org/selfbus/sbtools/prodedit"
    * in the class path.
    * 
    * @param name - name of the properties file.
    * @return The properties
    * @throws VdioException if the properties file could not be loaded
    */
   static public Properties getProperties(String name) throws VdioException
   {
      String fileName = "org/selfbus/sbtools/prodedit/" + name;
      Properties prop = new Properties();
      try
      {
         InputStream in = ClassPathPropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
         if (in == null)
            throw new VdioException("property file not found in class path: " + fileName);

         prop.load(in);
      }
      catch (IOException e)
      {
         throw new VdioException("failed to read property file: " + fileName, e);
      }
      return prop;
   }
}

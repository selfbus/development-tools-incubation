package org.selfbus.sbtools.prodedit.vdio;

import java.util.Properties;

import org.selfbus.sbtools.prodedit.utils.ClassPathPropertiesLoader;
import org.selfbus.sbtools.vdio.VdioException;

/**
 * Abstract base class for products exporter and importer.
 */
abstract public class AbstractProductsExpImp
{
   /**
    * Load a properties file that is searched in the directory "org.selfbus.sbtools.prodedit"
    * in the class path.
    * 
    * @param name - name of the properties file.
    * @return The properties
    * @throws VdioException if the properties file could not be loaded
    */
   Properties getProperties(String name) throws VdioException
   {
      return ClassPathPropertiesLoader.getProperties(name);
   }
}

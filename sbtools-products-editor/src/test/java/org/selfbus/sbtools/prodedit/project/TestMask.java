package org.selfbus.sbtools.prodedit.project;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.selfbus.sbtools.prodedit.model.global.Mask;
import org.selfbus.sbtools.prodedit.utils.ClassPathPropertiesLoader;
import org.selfbus.sbtools.vdio.VdioException;

public class TestMask
{
   @Test
   public void maskFromProps() throws VdioException
   {
      Properties props = ClassPathPropertiesLoader.getProperties("mask_20.properties");
      Mask mask = new Mask(0x20, props);
      
      assertEquals(0x20, mask.getVersion());
      assertEquals(278, mask.getAddressTabAddr());
   }
}

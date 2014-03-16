package org.selfbus.sbtools.prodedit.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.model.global.Mask;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;

import com.jgoodies.common.base.Strings;

/**
 * Generates a C/C++ header file for a virtual device.
 */
public class HeaderFileGenerator
{
   private final Map<String, String> params = new TreeMap<String, String>();
   private final Map<String, String> comObjects = new TreeMap<String, String>();
   private ApplicationProgram program;
   private Mask mask;

   /**
    * Generates a C/C++ header file for the virtual device.
    *
    * @param group - the product group of the device.
    * @param device - the device to process.
    * @param file - the file to write.
    * @throws IOException in case of IO problems. 
    */
   public void write(ProductGroup group, VirtualDevice device, File file) throws IOException
   {
      String guardName = file.getName().replaceAll("[^\\d\\w]", "_");

      OutputStream outs = new FileOutputStream(file);
      try
      {
         write(group, device, outs, guardName);
      }
      finally
      {
         outs.close();
      }
   }

   /**
    * Generates a C/C++ header file for the virtual device.
    *
    * @param group - the product group of the device.
    * @param device - the device to process.
    * @param outs - the output stream to write.
    * @param guardName - the name of the define to guard the file.
    * 
    * @throws IOException in case of IO problems. 
    */
   public void write(ProductGroup group, VirtualDevice device, OutputStream outs, String guardName) throws FileNotFoundException
   {
      program = group.getProgram(device.getProgramId());
      mask = ProdEdit.getInstance().getProject().getMask(program);
      
      comObjects.clear();
      params.clear();
      createDefines(program.getParameterRoot());

      PrintWriter out = new PrintWriter(outs);

      out.println("/*");
      out.println(" * EIB Product Definitions");
      out.println(" *");
      out.println(" * Device:  " + device.getName());
      out.println(" * Program: " + program.getName());
      out.println(" * Version: " + program.getVersion());
      out.println(" *");
      out.println(" * Created at " + new Date().toString() + " by the Selfbus Products Editor");
      out.println(" */");
      out.println("#ifndef " + guardName);
      out.println("#define " + guardName);
      out.println("");
      out.println("// Use this define to enable absolute addresses in the _ADDR defines below");
      out.println("// The default is to have relative addresses for user EEPROM addresses");
      out.println("// #define USE_ABSOLUTE_ADDRESSES");
      out.println("");
      out.println("#ifdef USE_ABSOLUTE_ADDRESSES");
      out.println("#  define USER_EEPROM_ADDR(x)  x");
      out.println("#else");
      out.println(String.format("#  define USER_EEPROM_ADDR(x) (x - 0x%1$x)", mask.getUserEepromStart()));
      out.println("#endif");
      out.println("");

      out.println("");
      out.println("");
      out.println("//------------------------------------------------------------------------------");
      out.println("// Device and Program");
      out.println("//------------------------------------------------------------------------------");
      out.println("");
      out.println("// The ID of the manufacturer");
      out.println("#define MANUFACTURER_ID  " + group.getManufacturer().getId());
      out.println("");
      out.println("// The device type");
      out.println("#define DEVICE_TYPE  0x" + Integer.toHexString(program.getDeviceType()));
      out.println("");
      out.println("// The version of the application program");
      out.println("#define PROGRAM_VERSION  " + program.getVersion());

      out.println("");
      out.println("");
      out.println("//------------------------------------------------------------------------------");
      out.println("// Parameters");
      out.println("//------------------------------------------------------------------------------");
      out.println("");
      for (String val : params.values())
         out.println(val);

      out.println("");
      out.println("");
      out.println("//------------------------------------------------------------------------------");
      out.println("// Communication objects");
      out.println("//------------------------------------------------------------------------------");
      out.println("");
      for (String val : comObjects.values())
         out.println(val);

      out.println("");
      out.println("#undef USER_EEPROM_ADDR");
      out.println("");    
      out.println("#endif /*" + guardName + "*/");

      out.flush();
   }

   /**
    * Create the defines for the parameters and com-objects.
    */
   protected void createDefines(AbstractParameterNode node)
   {
      if (node instanceof Parameter)
         createDefines((Parameter) node);
      else if (node instanceof CommunicationObject)
         createDefines((CommunicationObject) node);

      Enumeration<AbstractParameterNode> it = node.children();
      while (it.hasMoreElements())
         createDefines(it.nextElement());
   }

   /**
    * Create defines for a parameter.
    * 
    * @param param - the parameter to process.
    */
   protected void createDefines(Parameter param)
   {
      Integer addr = param.getAddress();
      int size = param.getSize();

      if (addr == null || size <= 0)
         return;

//      ParameterType paramType = program.getParameterType(param.getTypeId());
      StringBuilder builder = new StringBuilder();

      String name = param.getName();
      if (Strings.isEmpty(name))
         name = "ID_" + param.getId();

      String defineName = "PARAM_" + getDefineName(name);

      String commentName = param.getDescription().getText("en");
      if (Strings.isEmpty(commentName))
         commentName = param.getDescription().getDefaultText();
      
      commentName = "parameter \"" + commentName + "\""; 

      builder.append("// Address of ").append(commentName).append('\n');

      builder.append("// ");
      int offset = param.getBitOffset();
      if (size > 7 || size == 0)
         builder.append("Size ");
      else
      {
         if (size > 1)
            builder.append("Bits ").append(offset).append("..").append(offset + size - 1);
         else builder.append("Bit ").append(offset);

         builder.append(", size ");
      }
      builder.append(getSizeStr(size)).append('\n');

      if (!param.isVisible())
         builder.append("// (this parameter is invisible)\n");

      builder.append(formatDefine(defineName, "ADDR"))
             .append(getRelativeAddrStr(addr)).append('\n');

      if (size < 8)
      {
         builder.append("\n// Bit offset of ").append(commentName).append('\n');
         builder.append(formatDefine(defineName, "OFFS")).append(offset).append('\n');

         int mask = ((1 << size) - 1) << offset;
         builder.append("\n// Bit mask for accessing ").append(commentName).append('\n');
         builder.append(formatDefine(defineName, "MASK")).append(String.format("0x%1$02x", mask)).append('\n');
      }

      params.put(defineName, builder.toString());
   }

   /**
    * Create defines for a communication object.
    * 
    * @param comObject - the communication object to process.
    */
   protected void createDefines(CommunicationObject comObject)
   {
      Integer addr = comObject.getAddress();
      if (addr == null)
         return;

      StringBuilder builder = new StringBuilder();

      String name = comObject.getName().getText("en");
      if (Strings.isEmpty(name))
         name = comObject.getName().getDefaultText();
      if (Strings.isEmpty(name))
         name = "ID_" + comObject.getId();

      String func = comObject.getFunction().getText("en");
      if (Strings.isEmpty(func))
         func = comObject.getFunction().getDefaultText();

      String defineName = "COMOBJ_";
      if (Strings.isEmpty(func))
         defineName += getDefineName(name);
      else defineName += getDefineName(name + '_' + func);

      String commentName = "communication object \"" + name + "\" function \"" + func + "\""; 
      builder.append("// Address of ").append(commentName).append('\n');
      builder.append("// Size ").append(getSizeStr(comObject.getType().getBitSize())).append('\n');
      builder.append(formatDefine(defineName, "ADDR")).append(getRelativeAddrStr(addr)).append('\n');

      comObjects.put(defineName, builder.toString());
   }

   /**
    * Format the define constant by adding the extension to the name and padding it to
    * the standard string length with spaces.
    * 
    * @param name - the name of the constant
    * @param ext - the extension
    * @return The formatted constant
    */
   protected String formatDefine(String name, String ext)
   {
      return String.format("#define %1$-58s  ", name + "_" + ext);
   }

   /**
    * Translate the numeric address into a define with the relative address for an user EEPROM
    * or user RAM address.
    * 
    * @param addr - the address
    * @return A define with the relative address.
    */
   protected String getRelativeAddrStr(int addr)
   {
      if (addr >= mask.getUserEepromStart() && addr < mask.getUserEepromEnd())
         return String.format("USER_EEPROM_ADDR(0x%1$x)", addr);
      return String.format("0x%1$x", addr);
   }

   /**
    * Translate the size in bits into "x bits" or "x bytes" text.
    * 
    * @param bitSize - the size in bits
    * @return The size as human readable text
    */
   protected String getSizeStr(int bitSize)
   {
      if (bitSize < 8 || (bitSize & 7) != 0)
         return bitSize + " bits";
      return (bitSize >> 3) + " bytes";
   }

   /**
    * Create a define constant from the given name.
    * 
    * @param name - the name to process.
    * @return The name of a define constant for the given name.
    */
   protected String getDefineName(String name)
   {
      return name.toUpperCase().replaceAll("[^\\w\\d]", "_").replaceAll("___*", "_");
   }
}

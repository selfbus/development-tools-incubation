package org.selfbus.sbtools.prodedit.utils;

import javax.swing.Icon;

import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterRoot;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;

import com.jgoodies.common.base.Strings;

/**
 * Utility methods for parameters.
 */
public final class ParamUtils
{
   private static final Icon COM_OBJECT_ICON = ImageCache.getIcon("icons/connect");
   private static final Icon PARAM_ROOT_ICON = ImageCache.getIcon("icons/param_root");
   private static final Icon PARAM_PAGE_ICON = ImageCache.getIcon("icons/parameter");
   private static final Icon PARAM_LABEL_ICON = ImageCache.getIcon("icons/param_label");
   private static final Icon PARAM_FIELD_ICON = ImageCache.getIcon("icons/param_field");
   private static final Icon PARAM_ENUM_ICON = ImageCache.getIcon("icons/param_enum");

   private static final Icon PARAM_HIDDEN_PAGE_ICON = ImageCache.getIcon("icons/parameter", "icons/param_invisible_overlay");
   private static final Icon PARAM_HIDDEN_LABEL_ICON = ImageCache.getIcon("icons/param_label", "icons/param_invisible_overlay");
   private static final Icon PARAM_HIDDEN_FIELD_ICON = ImageCache.getIcon("icons/param_field", "icons/param_invisible_overlay");
   private static final Icon PARAM_HIDDEN_ENUM_ICON = ImageCache.getIcon("icons/param_enum", "icons/param_invisible_overlay");

   /**
    * Fix the text for display in a list. Leading whitespaces are removed,
    * CR and LF are replaced with "/".
    * 
    * @param text - the text to fix
    * @return The fixed text.
    */
   public static String fixText(String text)
   {
      if (text == null)
         return "";

      return text.replaceAll("\\\\n", " / ").replaceAll("\\\\r", "").replaceAll("^ / ", "")
         .replaceFirst("^  *", "");
   }

   /**
    * Get a label for the parameter node.
    * 
    * @param node - the parameter node to process.
    * @return The label
    */
   public static String getLabel(AbstractParameterNode node)
   {
      if (node instanceof Parameter)
      {
         Parameter param = (Parameter) node;

         String lbl = param.getDescription().getDefaultText();
         if (Strings.isEmpty(lbl))
            lbl = param.getName();

         return ParamUtils.fixText(lbl);
      }
      else if (node instanceof CommunicationObject)
      {
         CommunicationObject comObject = (CommunicationObject) node;

         String lbl = comObject.getName().getDefaultText();
         String func = comObject.getFunction().getDefaultText();
         if (Strings.isNotEmpty(func))
            lbl += ": " + func;

         return lbl;
      }

      return node.getDescription().getDefaultText();
   }

   /**
    * Get the size in human readable form.
    * 
    * @param bits - the number of bits
    * @return The size as label. "x bit" or "x byte"
    */
   public static String getSizeLabel(int bits)
   {
      if (bits > 7 && (bits & 7) == 0)
         return I18n.formatMessage("ParamUtils.byte", Integer.toString(bits >> 3));
      return I18n.formatMessage("ParamUtils.bit", Integer.toString(bits));
   }

   /**
    * Get the size and bit offset in human readable form.
    * 
    * @param bits - the number of bits
    * @param offs - the bit offset.
    * @return The size and bit offset as label. "x bit, offset y", "x byte, offset y", "x bit", "x byte"
    */
   public static String getSizeOffsetLabel(int bits, int offs)
   {
      if (offs == 0 && bits > 7 && (bits & 7) == 0)
         return getSizeLabel(bits);

      if (bits == 1)
         return I18n.formatMessage("ParamUtils.1bit", Integer.toString(offs));

      return I18n.formatMessage("ParamUtils.bitOffs", Integer.toString(offs),
         Integer.toString(offs + bits - 1));
   }

   /**
    * Get the size, bit offset and optionally starting address in human readable form.
    * 
    * @param node - the parameter node to process
    * @return The size, bit offset and optionally starting address in human readable form.
    */
   public static String getSizeOffsetLabel(AbstractParameterNode node)
   {
      Integer addr = node.getAddress();
      int size = 0, offs = 0;

      if (node instanceof CommunicationObject)
      {
         CommunicationObject comObject = (CommunicationObject) node;
         size = comObject.getType().getBitSize(); 
      }
      else if (node instanceof Parameter)
      {
         Parameter param = (Parameter) node;
         size = param.getSize();
         offs = param.getBitOffset();
      }

      String label = getSizeOffsetLabel(size, offs);

      if (node instanceof CommunicationObject && size < 8)
         return I18n.formatMessage("ParamUtils.bit", Integer.toString(size));

      if (size > 8 && addr != null)
         return I18n.formatMessage("ParamUtils.sizeAtPos", label, String.format("%1$04x", addr));
      return label;
   }

   /**
    * Get the icon for a parameter node.
    *
    * @param node - the parameter node.
    * @param program - the application program owning the parameter, may be null
    *
    * @return The icon for the node.
    */
   public static Icon getIcon(AbstractParameterNode node, ApplicationProgram program)
   {
      if (node instanceof CommunicationObject)
      {
         return COM_OBJECT_ICON;
      }
      else if (node instanceof ParameterRoot)
      {
         return PARAM_ROOT_ICON;
      }
      else if (node instanceof Parameter)
      {
         Parameter param = (Parameter) node;
         ParameterCategory category = param.getCategory();
         boolean visible = param.isVisible();

         if (category == ParameterCategory.LABEL)
            return visible ? PARAM_LABEL_ICON : PARAM_HIDDEN_LABEL_ICON;
         else if (category == ParameterCategory.PAGE)
            return visible ? PARAM_PAGE_ICON : PARAM_HIDDEN_PAGE_ICON;
         else if (program != null)
         {
            ParameterType paramType = program.getParameterType(param.getTypeId());
            ParameterAtomicType atomicType = paramType.getAtomicType();

            if (atomicType == ParameterAtomicType.ENUM || atomicType == ParameterAtomicType.LONG_ENUM)
               return visible ? PARAM_ENUM_ICON : PARAM_HIDDEN_ENUM_ICON;
         }

         return visible ? PARAM_FIELD_ICON : PARAM_HIDDEN_FIELD_ICON;
         
      }

      return null;
   }

   /**
    * Get the icon for a parameter node.
    *
    * @param node - the parameter node.
    *
    * @return The icon for the node.
    */
   public static Icon getIcon(AbstractParameterNode node)
   {
      return getIcon(node, null);
   }
}

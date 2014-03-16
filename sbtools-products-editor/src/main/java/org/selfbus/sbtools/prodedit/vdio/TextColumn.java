package org.selfbus.sbtools.prodedit.vdio;

/**
 * IDs for text columns in VD table text_attribute
 */
public enum TextColumn
{
   /**
    * ID for communication_object name
    */
   COM_OBJECT_NAME(20),

   /**
    * ID for communication_object function
    */
   COM_OBJECT_FUNCTION(22),

   /**
    * ID for communication_object description
    */
   COM_OBJECT_DESCRIPTION(21),

   /**
    * ID for parameter description
    */
   PARAM_DESCRIPTION(10),

   /**
    * ID for parameter_list_of_values
    */
   PARAM_VALUE(11);

   /**
    * The column id;
    */
   public final int column;

   /*
    * Internal constructor.
    */
   private TextColumn(int column)
   {
      this.column = column;
   }
}

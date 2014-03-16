package org.selfbus.sbtools.prodedit.binding;

import org.selfbus.sbtools.prodedit.model.enums.ObjectType;

import com.jgoodies.binding.value.BindingConverter;

/**
 * A converter that converts between the numeric ID in the model and 
 * the {@link ObjectType}.
 */
public class ObjectTypeConverter implements BindingConverter<ObjectType, String>
{
   /**
    * {@inheritDoc}
    */
   @Override
   public String targetValue(ObjectType sourceValue)
   {
      if (sourceValue == null)
         return null;

      return ((ObjectType) sourceValue).getLabel();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ObjectType sourceValue(String targetValue)
   {
      throw new IllegalAccessError("This converter is read only");
   }
}

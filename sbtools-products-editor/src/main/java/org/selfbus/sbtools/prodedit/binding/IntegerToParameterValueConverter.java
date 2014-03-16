package org.selfbus.sbtools.prodedit.binding;

import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;

import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * A converter that converts an integer to a {@link ParameterValue} object.
 */
public class IntegerToParameterValueConverter implements BindingConverter<Integer, ParameterValue>
{
   private ArrayListModel<ParameterValue> paramValues;

   /**
    * Set the list of parameter values to be used for conversion.
    *
    * @param paramValues - the list of values to set
    */
   public void setParamValues(ArrayListModel<ParameterValue> paramValues)
   {
      this.paramValues = paramValues;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ParameterValue targetValue(Integer sourceValue)
   {
      if (paramValues == null || !(sourceValue instanceof Integer))
         return null;

      int value = (Integer) sourceValue;

      for (ParameterValue paramValue : paramValues)
      {
         if (paramValue.getIntValue() == value)
            return paramValue;
      }

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Integer sourceValue(ParameterValue targetValue)
   {
      if (paramValues == null || !(targetValue instanceof ParameterValue))
         return null;

      return ((ParameterValue) targetValue).getIntValue();
   }
}

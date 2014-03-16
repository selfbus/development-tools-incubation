package org.selfbus.sbtools.prodedit.binding;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.utils.IdentifiableUtils;

import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * A converter that converts between the numeric ID in the model and 
 * the {@link Identifiable} in the presentation.
 */
public class IdentifiableConverter implements BindingConverter<Integer,Identifiable>
{
   private ArrayListModel<ParameterType> list;

   /**
    * Set the list of the {@link Identifiable}s.
    *
    * @param arrayListModel - the list to set
    */
   public void setList(ArrayListModel<ParameterType> arrayListModel)
   {
      this.list = arrayListModel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Identifiable targetValue(Integer sourceValue)
   {
      if (list == null)
         return null;

      return IdentifiableUtils.findById(list, (Integer) sourceValue);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Integer sourceValue(Identifiable targetValue)
   {
      return Integer.valueOf(((Identifiable) targetValue).getId());
   }
}

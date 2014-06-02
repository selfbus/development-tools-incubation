package org.selfbus.sbtools.prodedit.tabs.prodgroup.parameditor;

import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.project.Device;

/**
 * The parameter of a specific {@link Device} device, including the current
 * parameter value. The parameter values are set by the parameter editor when
 * the user parameterizes a device.
 */
public class DeviceParameter
{
   private Device device;
   private Parameter parameter;
   private Object value;
   private String stringValue;

   /**
    * Create an empty device parameter object.
    */
   public DeviceParameter()
   {
   }

   /**
    * Create an initialized device parameter object.
    */
   public DeviceParameter(Device device, Parameter parameter, Object value)
   {
      this.device = device;
      this.parameter = parameter;
      this.value = value;
   }

   /**
    * @return the device
    */
   public Device getDevice()
   {
      return device;
   }

   /**
    * @param device the device to set
    */
   public void setDevice(Device device)
   {
      this.device = device;
   }

   /**
    * @return The parameter
    */
   public Parameter getParameter()
   {
      return parameter;
   }

   /**
    * @return The parent device parameter, or null if the parameter has no parent.
    */
   public DeviceParameter getParent()
   {
      if (parameter == null)
         return null;
//
//      final Parameter parentParam = parameter.getParent();
//      if (parentParam == null)
//         return null;
//
//      return  device.getDeviceParameter(parentParam);

      // TODO implement this
      return null;
   }

   /**
    * @return the value as string.
    */
   public String getValue()
   {
      if (value == null)
      {
         if (stringValue == null)
            return null;

         value = stringValue;
      }

      return value.toString();
   }

   /**
    * @return the value as integer. Returns zero if the value is not set.
    *
    * @throws ClassCastException if the value cannot be cast to an
    *            {@link Integer}.
    */
   public Integer getIntValue()
   {
      if (value != null)
         return (Integer) value;

      if (stringValue == null)
         return null;

      value = stringValue.isEmpty() ? 0 : Integer.parseInt(stringValue);
      return (Integer) value;
   }

   /**
    * Set the value.
    */
   public void setValue(Object o)
   {
      value = o;

      if (value instanceof String)
         stringValue = (String) value;
      else if (value instanceof Integer)
         stringValue = ((Integer) value).toString();
      else
         throw new RuntimeException("Cannot serialize parameter value");
   }

   /**
    * Test if the parameter is visible.
    *
    * @return true if the parameter is visible.
    */
   public boolean isVisible()
   {
      if (!isEnabled())
         return false;

      // TODO implement this
      return true;
//      return getParameter().getHighAccess() != 0;
   }

   /**
    * @return True if the parameter is enabled.
    */
   public boolean isEnabled()
   {
      final DeviceParameter parent = getParent();

      if (parent == null)
         return true;

      if (!parent.isEnabled())
         return false;

      final Integer expectedParentValue = parameter.getParentValue();
      if (expectedParentValue == null)
         return true;

      return expectedParentValue.equals(parent.getIntValue());
   }

   /**
    * Test if the parameter shall be stored in the eeprom.
    *
    * @return true if the parameter shall be stored in the eeprom.
    */
   public boolean isUsed()
   {
      // TODO implement this
      return true;

//      // TODO verify the if-condition ... it is only a guess!
//      if (parameter.getLowAccess() == 0 && parameter.getHighAccess() == 0)
//         return false;
//
//      final DeviceParameter parent = getParent();
//      if (parent == null)
//         return true;
//
//      if (!parent.isUsed())
//         return false;
//
//      final Integer expectedParentValue = parameter.getParentValue();
//      if (expectedParentValue == null)
//         return true;
//
//      return expectedParentValue.equals(parent.getIntValue());
   }

   /**
    * Prepare the value for persisting.
    */
   protected void prePersist()
   {
      if (stringValue == null)
      {
         if (value == null)
            stringValue = "";
         else stringValue = value.toString();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return parameter == null ? 0 : parameter.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "#" + (parameter == null ? -1 : parameter.getId()) + " value " + value;
   }
}

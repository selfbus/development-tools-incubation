package org.selfbus.sbtools.prodedit.model.project;

import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;

/**
 * A {@link VirtualDevice} built into some place in a project.
 */
public class Device
{
   private final VirtualDevice virtualDevice;

   /**
    * Create a physical device.
    *
    * @param virtualDevice - the virtual device.
    */
   public Device(VirtualDevice virtualDevice)
   {
      this.virtualDevice = virtualDevice;
   }

   /**
    * @return The virtual device.
    */
   public VirtualDevice getVirtualDevice()
   {
      return virtualDevice;
   }
}

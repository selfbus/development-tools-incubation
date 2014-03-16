package org.selfbus.sbtools.devtool.project.model.test;

import java.awt.Component;
import java.io.IOException;

import org.selfbus.sbtools.knxcom.BusInterface;

/**
 * Interface for test steps.
 */
public interface TestStep
{
   /**
    * @return The name of the test step.
    */
   public String getName();

   /**
    * Execute the test step.
    * 
    * @param bus - the bus interface to use for EIB bus communication.
    * 
    * @return True if successful, false if the test step failed.
    * @throws IOException in case of EIB communication problems
    */
   public boolean perform(BusInterface bus) throws IOException;

   /**
    * Create a GUI for configuring the test step.
    */
   public Component createEditComponent();
}

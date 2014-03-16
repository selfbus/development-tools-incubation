package org.selfbus.sbtools.devtool.project.model.test;

import java.awt.Component;

import javax.swing.JLabel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * Abstract base class for test steps.
 */
@XmlType(name = "TestStep", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractTestStep implements TestStep
{
   @XmlAttribute
   protected String name;

   /**
    * @return The name of the test step.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the test step.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Component createEditComponent()
   {
      return new JLabel(name);
   }
}

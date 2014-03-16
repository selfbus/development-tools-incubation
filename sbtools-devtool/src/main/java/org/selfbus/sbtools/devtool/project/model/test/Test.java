package org.selfbus.sbtools.devtool.project.model.test;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.devtool.project.Namespaces;

/**
 * A test.
 */
@XmlType(name = "Test", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Test implements Comparable<Test>
{
   @XmlAttribute
   protected String name = "";

   @XmlAttribute
   protected String description = "";

   @XmlElements
   ({
      @XmlElement(name = "writeGroupValue", type = WriteGroupValue.class),
   })
   protected ArrayList<TestStep> steps = new ArrayList<TestStep>();

   /**
    * @return The name of the test.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the test.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return The description of the test.
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * Set the description of the test.
    * 
    * @param description - the description to set.
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * @return The test steps.
    */
   public ArrayList<TestStep> getSteps()
   {
      return steps;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof Test)) return false;
      return name.equals(((Test) o).name);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(Test o)
   {
      return name.compareTo(o.name);
   }
}

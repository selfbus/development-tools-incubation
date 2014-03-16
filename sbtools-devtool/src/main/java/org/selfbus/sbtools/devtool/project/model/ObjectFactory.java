package org.selfbus.sbtools.devtool.project.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.freebus.fbhome.domain.project package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory
{
   private final static QName _Project_QNAME = new QName("", "project");

   /**
    * Create a new ObjectFactory that can be used to create new instances of
    * schema derived classes for package: org.freebus.fbhome.domain.project
    * 
    */
   public ObjectFactory()
   {
   }

   /**
    * Create an instance of {@link Project }
    * 
    */
   public Project createProject()
   {
      return new Project();
   }

   /**
    * Create an instance of {@link JAXBElement }{@code <}{@link Project }{@code >}
    * 
    */
   @XmlElementDecl(namespace = "", name = "project")
   public JAXBElement<Project> createProject(Project value)
   {
      return new JAXBElement<Project>(_Project_QNAME, Project.class, null, value);
   }
}

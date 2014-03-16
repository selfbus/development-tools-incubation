package org.selfbus.sbtools.prodedit.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;


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
   private final static QName _ProductGroup_QNAME = new QName("", "product_group");

   /**
    * Create a new ObjectFactory that can be used to create new instances of
    * schema derived classes for package: org.freebus.fbhome.domain.project
    * 
    */
   public ObjectFactory()
   {
   }

   /**
    * Create an instance of {@link ProductGroup }
    * 
    */
   public ProductGroup createProductGroup()
   {
      return new ProductGroup();
   }

   /**
    * Create an instance of {@link JAXBElement }{@code <}{@link ProductGroup}{@code >}
    * 
    */
   @XmlElementDecl(namespace = "", name = "product_group")
   public JAXBElement<ProductGroup> createProductGroup(ProductGroup value)
   {
      return new JAXBElement<ProductGroup>(_ProductGroup_QNAME, ProductGroup.class, null, value);
   }
}

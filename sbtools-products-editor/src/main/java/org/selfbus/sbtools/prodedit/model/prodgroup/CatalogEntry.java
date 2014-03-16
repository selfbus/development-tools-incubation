package org.selfbus.sbtools.prodedit.model.prodgroup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Catalog entries name the variations of a virtual device, as it can be bought
 * from a catalog or web-shop. Catalog entries of the same virtual device
 * usually differ in things like housing color or maximum switching power.
 * 
 * @deprecated Not used - dynamically created upon export
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
@Deprecated
public class CatalogEntry
{
   @XmlAttribute(name = "catalog_entry_id", required = true)
   private int id;

   @XmlAttribute(name = "entry_name", required = true)
   private String name;

   @XmlAttribute(name = "manufacturer_id", required = true)
   private int manufacturerId;

   @XmlAttribute(name = "product_id")
   private int productId;

   @XmlAttribute(name = "entry_width_in_modules")
   private int widthModules;

   @XmlAttribute(name = "entry_width_in_millimeters")
   private int widthMM;

   @XmlAttribute(name = "din_flag", required = true)
   private boolean din;

   @XmlAttribute(name = "order_number")
   private String orderNumber;

   @XmlAttribute(name = "entry_colour")
   private String color;

   @XmlAttribute(name = "series")
   private String series;

//   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "catalogEntry")
//   private ProductDescription description;

   /**
    * Create an empty catalog-entry object.
    */
   public CatalogEntry()
   {
   }

   /**
    * Create an empty catalog-entry object with an id.
    * 
    * @param id - the database ID of the object.
    */
   public CatalogEntry(int id)
   {
      this.id = id;
   }

   /**
    * Create a catalog-entry object.
    * 
    * @param id - the database ID of the object.
    * @param name - the name of the object.
    * @param manufacturerId - the manufacturer.
    * @param productId - the product.
    */
   public CatalogEntry(int id, String name, int manufacturerId, int productId)
   {
      this.id = id;
      this.name = name;
      this.manufacturerId = manufacturerId;
      this.productId = productId;
   }

   /**
    * Create a catalog-entry object.
    *
    * @param name - the name of the object.
    * @param manufacturerId - the manufacturer ID.
    */
   public CatalogEntry(String name, int manufacturerId)
   {
      this(0, name, manufacturerId, 0);
   }

   /**
    * @return the id of the catalog-entry.
    */
   public int getId()
   {
      return id;
   }

   /**
    * Set the id of the catalog-entry. Use {@link ProductGroup#getNextUniqueId()}
    * to get a unique ID.
    * 
    * @param id - the ID to set.
    */
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the name of the catalog-entry.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the catalog-entry.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the manufacturer ID.
    */
   public int getManufacturerId()
   {
      return manufacturerId;
   }

   /**
    * Set the manufacturer ID.
    * 
    * @param manufacturerId - the manufacturer ID to set.
    */
   public void setManufacturerId(int manufacturerId)
   {
      this.manufacturerId = manufacturerId;
   }

   /**
    * @return the product ID.
    */
   public int getProductId()
   {
      return productId;
   }

   /**
    * Set the hardware product.
    * 
    * @param productId - the product to set.
    */
   public void setProductId(int productId)
   {
      this.productId = productId;
   }

   /**
    * @return the with in module-units.
    */
   public int getWidthModules()
   {
      return widthModules;
   }

   /**
    * Set the with in module-units.
    * 
    * @param widthModules - the width to set.
    */
   public void setWidthModules(int widthModules)
   {
      this.widthModules = widthModules;
   }

   /**
    * @return the width in millimeters.
    */
   public int getWidthMM()
   {
      return widthMM;
   }

   /**
    * Set the width in millimeters.
    * 
    * @param widthMM - the width to set.
    */
   public void setWidthMM(int widthMM)
   {
      this.widthMM = widthMM;
   }

   /**
    * Test if the device can be mounted on DIN rails.
    * 
    * @return True if the device is DIN mountable.
    */
   public boolean getDIN()
   {
      return din;
   }

   /**
    * Set if the device can be mounted on DIN rails.
    * 
    * @param din - true if the device is DIN mountable.
    */
   public void setDIN(boolean din)
   {
      this.din = din;
   }

   /**
    * @return the order-number.
    */
   public String getOrderNumber()
   {
      return orderNumber;
   }

   /**
    * Set the order-number.
    * 
    * @param orderNumber - the order number to set.
    */
   public void setOrderNumber(String orderNumber)
   {
      this.orderNumber = orderNumber;
   }

   /**
    * @return the color of the device.
    */
   public String getColor()
   {
      return color;
   }

   /**
    * Set the color of the device.
    * 
    * @param color - the color of the device.
    */
   public void setColor(String color)
   {
      this.color = color;
   }

   /**
    * @return the series.
    */
   public String getSeries()
   {
      return series;
   }

   /**
    * Set the series.
    * 
    * @param series - the series to set.
    */
   public void setSeries(String series)
   {
      this.series = series;
   }

//   /**
//    * Set the description.
//    * 
//    * @param description - the description to set.
//    */
//   public void setDescription(ProductDescription description)
//   {
//      this.description = description;
//   }
//
//   /**
//    * @return The description.
//    */
//   public ProductDescription getDescription()
//   {
//      return description;
//   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(final Object o)
   {
      if (o == this)
         return true;

      if (!(o instanceof CatalogEntry))
         return false;

      final CatalogEntry oo = (CatalogEntry) o;

      if (id != oo.id || !name.equals(oo.name))
         return false;

      return manufacturerId == oo.manufacturerId && productId == oo.productId;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name == null ? "#" + id : name;
   }
}

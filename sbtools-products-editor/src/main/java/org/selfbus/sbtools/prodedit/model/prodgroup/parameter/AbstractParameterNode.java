package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.prodedit.model.interfaces.Orderable;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * An abstract base class for {@link CommunicationObject} and {@link Parameter}.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class AbstractParameterNode extends Model implements Identifiable, Orderable, TreeNode
{
   private static final long serialVersionUID = 8492676566007169620L;

   @XmlAttribute(name = "id", required = true)
   protected Integer id;

   @XmlElement(name = "description")
   private MultiLingualText description;

   @XmlAttribute(name = "order", required = true)
   private int order;

   @XmlAttribute(name = "parent_id")
   private Integer parentId;

   @XmlAttribute(name = "parent_value")
   private Integer parentValue;

   // The parameter/com-object number is unique for the program and starts with 1
   @XmlAttribute(name = "number")
   private Integer number;

   @XmlAttribute(name = "address")
   private Integer address;

   protected ArrayListModel<AbstractParameterNode> childs;
   protected AbstractParameterNode parent;

   protected AbstractParameterNode()
   {
   }

   protected AbstractParameterNode(AbstractParameterNode parent)
   {
      this.parent = parent;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getId()
   {
      return id;
   }

   /**
    * @return The ID as string.
    */
   public String getIdStr()
   {
      return Integer.toString(id);
   }

   /**
    * Set the ID. Use {@link ProductGroup#getNextUniqueId()} to get a unique ID.
    * 
    * @param id - the ID to set.
    */
   @Override
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the description
    */
   public MultiLingualText getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(MultiLingualText description)
   {
      this.description = description;
   }

   /**
    * @return the order
    */
   @Override
   public int getOrder()
   {
      return order;
   }

   /**
    * @param order the order to set
    */
   @Override
   public void setOrder(int order)
   {
      this.order = order;
   }

   /**
    * @return the parentId
    */
   public Integer getParentId()
   {
      return parentId;
   }

   /**
    * @param parentId the parentId to set
    */
   public void setParentId(Integer parentId)
   {
      this.parentId = parentId;
   }

   /**
    * @return the parentValue
    */
   public Integer getParentValue()
   {
      return parentValue;
   }

   /**
    * @param parentValue the parentValue to set
    */
   public void setParentValue(Integer parentValue)
   {
      this.parentValue = parentValue;
   }

   /**
    * @return the number
    */
   public Integer getNumber()
   {
      return number;
   }

   /**
    * @param number the number to set
    */
   public void setNumber(Integer number)
   {
      this.number = number;
   }

   /**
    * @return the address
    */
   public Integer getAddress()
   {
      return address;
   }

   /**
    * @param address the address to set
    */
   public void setAddress(Integer address)
   {
      this.address = address;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public AbstractParameterNode getChildAt(int index)
   {
      if (childs == null)
         return null;
   
      return childs.get(index);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Enumeration<AbstractParameterNode> children()
   {
      if (childs == null)
         childs = new ArrayListModel<AbstractParameterNode>();
   
      return Collections.enumeration(childs);
   }

   @Override
   public int getChildCount()
   {
      if (childs == null)
         return 0;

      return childs.getSize();
   }

   /**
    * Set the parent node.
    * 
    * @param parent - the parent node.
    */
   public void setParent(AbstractParameterNode parent)
   {
      this.parent = parent;

      if (parent instanceof ParameterRoot || parent == null)
         this.parentId = null;
      else this.parentId = parent.getId();
   }

   @Override
   public AbstractParameterNode getParent()
   {
      return parent;
   }

   @Override
   public int getIndex(TreeNode node)
   {
      if (childs == null)
         return -1;

      return childs.indexOf(node);
   }

   @Override
   public boolean getAllowsChildren()
   {
      return false;
   }

   @Override
   public boolean isLeaf()
   {
      return childs == null || childs.isEmpty();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return id == null ? -1 : id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (o == this)
         return true;

      if (!getClass().isInstance(o))
         return false;

      final AbstractParameterNode oo = (AbstractParameterNode) o;
      return id == oo.id;
   }
}

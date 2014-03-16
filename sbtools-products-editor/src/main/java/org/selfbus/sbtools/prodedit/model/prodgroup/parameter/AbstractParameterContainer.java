package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.jgoodies.common.collect.ArrayListModel;

@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
@XmlSeeAlso({ Parameter.class, CommunicationObject.class })
public class AbstractParameterContainer extends AbstractParameterNode implements ParameterContainer
{
   private static final long serialVersionUID = 4327394838211456868L;

   protected AbstractParameterContainer()
   {
      super();
   }

   /**
    * @return The children as an ArrayListModel.
    */
   //@XmlMixed
   @XmlElementWrapper(name = "childs")
   @XmlElementRefs({
      @XmlElementRef(name = "parameter", type = Parameter.class),
      @XmlElementRef(name = "communication_object", type = CommunicationObject.class)
   })
   public ArrayListModel<AbstractParameterNode> getChildrenModel()
   {
      return childs;
   }

   /**
    * Set the children.
    *
    * @param childs - the new children
    */
   public void setChildrenModel(ArrayListModel<AbstractParameterNode> childs)
   {
      this.childs = childs;

      if (childs != null)
      {
         for (AbstractParameterNode child : childs)
            child.setParent(this);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addChild(AbstractParameterNode child)
   {
      if (childs == null)
         childs = new ArrayListModel<AbstractParameterNode>();
   
      child.setParent(this);
      childs.add(child);
   }

   /**
    * Remove a child.
    * 
    * @param child - the child to remove
    */
   public void removeChild(AbstractParameterNode child)
   {
      child.setParent(null);
      childs.remove(child);
   }

   /**
    * Remove all children.
    */
   public void removeChildren()
   {
      childs.clear();
   }

   @Override
   public boolean getAllowsChildren()
   {
      return true;
   }
}

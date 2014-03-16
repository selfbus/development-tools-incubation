package org.selfbus.sbtools.prodedit.model.prodgroup.parameter;

import java.util.Enumeration;

public interface ParameterContainer
{

   /**
    * Add a child parameter or com-object.
    *
    * @param child - the parameter or com-object to add
    */
   public void addChild(AbstractParameterNode child);

   /**
    * @return The child parameters / com-object.
    */
   public Enumeration<AbstractParameterNode> children();

   /**
    * Get a child.
    *
    * @param index - the index of the child to get
    * @return The child
    */
   public AbstractParameterNode getChildAt(int index);
}

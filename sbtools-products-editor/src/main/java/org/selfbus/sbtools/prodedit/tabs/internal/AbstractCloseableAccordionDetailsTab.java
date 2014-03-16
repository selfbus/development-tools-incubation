package org.selfbus.sbtools.prodedit.tabs.internal;

import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.prodedit.ProdEdit;

/**
 * A {@link AbstractAccordionDetailsTab} that can be closed.
 */
public abstract class AbstractCloseableAccordionDetailsTab extends AbstractAccordionDetailsTab implements CloseableComponent
{
   private static final long serialVersionUID = 2965985944006394432L;


   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      ProdEdit.getInstance().closeTabPanel(this);
   }
}

package org.selfbus.sbtools.prodedit.tabs.project;

import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.tabs.internal.AbstractAccordionDetailsTab;

/**
 * A tab page that shows the (global) project configurations. 
 */
public class ProjectTab extends AbstractAccordionDetailsTab
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTab.class);
   private static final long serialVersionUID = 8340644174897864258L;

   private final ProductGroupsElem prodGroupsElem = new ProductGroupsElem();
   private final LanguagesElem langElem = new LanguagesElem();
   private final FunctionalEntitiesElem funcEntitiesElem = new FunctionalEntitiesElem();

   /**
    * Create a project tab.
    */
   public ProjectTab()
   {
      super();
      setTitle(I18n.getMessage("ProjectTab.title"));

      addCategory(prodGroupsElem);
      addCategory(funcEntitiesElem);
      addCategory(langElem);
   }
}

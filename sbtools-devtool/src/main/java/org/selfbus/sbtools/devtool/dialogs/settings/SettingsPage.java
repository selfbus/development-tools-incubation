package org.selfbus.sbtools.devtool.dialogs.settings;

import javax.swing.JPanel;

/**
 * A page of the settings dialog.
 */
public abstract class SettingsPage extends JPanel
{
   private static final long serialVersionUID = 1003647755243052307L;
   private String title;

   /**
    * Create a settings page with a title. The title will be used for the tab.
    * 
    * @param title - the title of the page.
    */
   SettingsPage(String title)
   {
      this.setTitle(title);
   }

   /**
    * Set the title of the page.
    */
   public void setTitle(String title)
   {
      this.title = title;
   }

   /**
    * @return the title of the page.
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * Apply the widget's contents to the running application and the
    * configuration.
    */
   public void apply()
   {
   }

   /**
    * Revert the widget's contents changes, if any.
    */
   public void revert()
   {
   }

   /**
    * @return A hash code. Returns the hash-code of the title.
    */
   @Override
   public int hashCode()
   {
      return title.hashCode();
   }
}

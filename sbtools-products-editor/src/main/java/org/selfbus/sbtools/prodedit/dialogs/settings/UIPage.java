package org.selfbus.sbtools.prodedit.dialogs.settings;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.common.gui.components.Dialogs;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * Settings page for user interface settings.
 */
public final class UIPage extends SettingsPage
{
   private static final long serialVersionUID = 1393125890233412334L;
   private final LookAndFeel initialLookAndFeel = UIManager.getLookAndFeel();
   private final JComboBox<LookAndFeelItem> cboLookAndFeel;

   /**
    * Internal class for look-and-feel combo box entries
    */
   private static class LookAndFeelItem implements Comparable<LookAndFeelItem>
   {
      private final LookAndFeelInfo info;

      private LookAndFeelItem(LookAndFeelInfo info)
      {
         this.info = info;
      }

      @Override
      public int compareTo(LookAndFeelItem o)
      {
         return info.getName().compareTo(o.info.getName());
      }

      @Override
      public boolean equals(Object o)
      {
         if (o == this) return true;
         if (!(o instanceof LookAndFeelItem)) return false;
         final LookAndFeelItem oo = (LookAndFeelItem) o;
         return info.getName().equals(oo.info.getName());
      }

      @Override
      public int hashCode()
      {
         if (info == null) return 0;
         return info.hashCode();
      }

      @Override
      public String toString()
      {
         return info.getName();
      }
   }

   /**
    * Create a user interface settings page.
    */
   public UIPage()
   {
      super(I18n.getMessage("Settings.UIPage.title"));

      GridBagLayout layout = new GridBagLayout();
      setLayout(layout);

      final GridBagConstraints c = new GridBagConstraints();
      int gridY = -1;

      final JLabel lblCaption = new JLabel(I18n.getMessage("Settings.UIPage.caption"));
      lblCaption.setFont(getFont().deriveFont(Font.BOLD).deriveFont(getFont().getSize() * (float) 1.2));
      c.fill = GridBagConstraints.NONE;
      c.gridx = 0;
      c.gridy = ++gridY;
      c.gridwidth = 2;
      c.ipady = 20;
      add(lblCaption, c);
      c.gridwidth = 1;
      c.ipady = 2;

      c.fill = GridBagConstraints.NONE;
      c.weightx = 1;
      c.gridx = 0;
      c.gridy = ++gridY;
      add(new JLabel(I18n.getMessage("Settings.UIPage.lookAndFeel")), c);

      cboLookAndFeel = new JComboBox<LookAndFeelItem>();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 10;
      c.gridx = 1;
      c.gridy = gridY;
      add(cboLookAndFeel, c);

      final JPanel spacer = new JPanel();
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 10;
      c.gridx = 0;
      c.gridy = ++gridY;
      add(spacer, c);

      final String currentLFClassName = UIManager.getLookAndFeel().getClass().getCanonicalName();
      final LookAndFeelInfo[] lafInfos = UIManager.getInstalledLookAndFeels();
      final LookAndFeelItem[] lafItems = new LookAndFeelItem[lafInfos.length];

      for (int i = lafInfos.length - 1; i >= 0; --i)
         lafItems[i] = new LookAndFeelItem(lafInfos[i]);
      Arrays.sort(lafItems);

      for (LookAndFeelItem lafItem: lafItems)
      {
         cboLookAndFeel.addItem(lafItem);
         if (currentLFClassName.equals(lafItem.info.getClassName()))
            cboLookAndFeel.setSelectedIndex(cboLookAndFeel.getItemCount() - 1);
      }

      cboLookAndFeel.addItemListener(new ItemListener()
      {
         @Override
         public void itemStateChanged(ItemEvent event)
         {
            final LookAndFeelItem item = (LookAndFeelItem) event.getItem();
            if (item == null || item != cboLookAndFeel.getSelectedItem()) return;

            lookAndFeelSelected(item);
         }
      });
   }

   /**
    * Called when the user selects a look-and-feel.
    */
   private void lookAndFeelSelected(final LookAndFeelItem item)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            final LookAndFeel previousLookAndFeel = UIManager.getLookAndFeel();

            try
            {
               setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

               UIManager.setLookAndFeel(item.info.getClassName());
               SwingUtilities.updateComponentTreeUI(getParent().getParent());
            }
            catch (Exception e)
            {
               Dialogs.showExceptionDialog(e, I18n.formatMessage("Settings.UIPage.errChangeLookAndFeel",
                     item.info.getName()));

               if (previousLookAndFeel != null)
               {
                  try
                  {
                     UIManager.setLookAndFeel(previousLookAndFeel);
                  }
                  catch (UnsupportedLookAndFeelException e1)
                  {
                     e1.printStackTrace();
                  }
               }
            }
            finally
            {
               setCursor(Cursor.getDefaultCursor());
            }
         }
      });
   }

   /**
    * Apply the widget's contents to the running application and the configuration.
    */
   @Override
   public void apply()
   {
      final Config cfg = Config.getInstance();
      cfg.put("lookAndFeel", ((LookAndFeelItem) cboLookAndFeel.getSelectedItem()).info.getClassName());

      try
      {
         SwingUtilities.updateComponentTreeUI(ProdEdit.getInstance().getMainFrame());
      }
      catch (Exception e)
      {
         Dialogs.showExceptionDialog(e, I18n.getMessage("Settings.UIPage.errActivatingLookAndFeel"));
      }
   }

   /**
    * Revert the widget's contents changes, if any.
    */
   @Override
   public void revert()
   {
      if (UIManager.getLookAndFeel() != initialLookAndFeel) try
      {
         UIManager.setLookAndFeel(initialLookAndFeel);
         SwingUtilities.updateComponentTreeUI(ProdEdit.getInstance().getMainFrame());
      }
      catch (UnsupportedLookAndFeelException e)
      {
      }
   }
}

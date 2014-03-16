package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.utils.FontUtils;

/**
 * A {@link JPanel} that displays the allocation of a program's memory cell.
 */
public class MemoryCellPanel extends JPanel
{
   private static final long serialVersionUID = 8406649061081693928L;

   private MemoryCell cell;
   private JTextPane detailsPane = new JTextPane();

   /**
    * Create a memory cell panel.
    */
   public MemoryCellPanel()
   {
      super(new BorderLayout(4, 4));

      JLabel lbl = new JLabel(I18n.getMessage("MemoryCellPanel.caption"));
      lbl.setFont(FontUtils.getCaptionFont());
      add(lbl, BorderLayout.NORTH);

      detailsPane.setBackground(getBackground());
      add(detailsPane, BorderLayout.CENTER);
   }

   /**
    * @return The displayed memory cell.
    */
   public MemoryCell getCell()
   {
      return cell;
   }

   /**
    * Set the memory cell to display.
    *
    * @param cell - the memory cell to display.
    */
   public void setCell(MemoryCell cell)
   {
      this.cell = cell;
      updateContents();
   }

   /**
    * Update the panel's contents.
    */
   public void updateContents()
   {
      if (cell == null)
      {
         return;
      }

      
   }
}

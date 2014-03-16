package org.selfbus.sbtools.prodedit.tabs.prodgroup.memory;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.selfbus.sbtools.prodedit.internal.I18n;

/**
 * A {@link DefaultTableCellRenderer table cell renderer} that is suited to
 * render {@link MemoryCell memory cells} of a {@link MemoryCellTableModel}.
 */
public class MemoryCellTableRenderer extends DefaultTableCellRenderer
{
   private static final long serialVersionUID = -8985604131449863602L;
   private final String unknownToolTip = I18n.getMessage("DeviceMemoryTableModel.Unknown");
   private Border modifiedBorder;
   private Font stdFont, modifiedFont;

   /**
    * Create a {@link MemoryCell memory cell} table renderer.
    */
   public MemoryCellTableRenderer()
   {
      setHorizontalAlignment(SwingConstants.CENTER);
   }

   /**
    * {@inheritDoc}
    */
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
         int row, int column)
   {
      if (stdFont == null)
      {
         stdFont = table.getFont();
         modifiedFont = stdFont.deriveFont(Font.BOLD);
      }

      final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (c instanceof JComponent && value instanceof MemoryCell)
      {
         final JComponent jc = (JComponent) c;
         final MemoryCell cell = (MemoryCell) value;

         if (modifiedBorder == null)
            modifiedBorder = BorderFactory.createLineBorder(jc.getForeground());

         final MemoryRange range = cell.getRange();
         if (range == null)
            jc.setToolTipText(unknownToolTip);
         else
         {
            final StringBuilder sb = new StringBuilder();
            sb.append("<html><body>Type: ").append(range.getName());
            if (range.getStart() != 0 || range.getSize() != 0)
            {
               sb.append("<br>Start: ").append(range.getStart()).append(" (0x")
                 .append(Integer.toHexString(range.getStart()))
                 .append(")<br>Size: ").append(range.getSize());
            }
            if (cell.getLabel() != null)
               sb.append("<br>").append(cell.getLabel());
            sb.append("</body></html>");

            jc.setToolTipText(sb.toString());
         }

         if (cell.isModified())
         {
            jc.setFont(modifiedFont);
            jc.setBorder(modifiedBorder);
         }
         else
         {
            jc.setFont(stdFont);
            jc.setBorder(null);
         }

         if (!isSelected)
            jc.setBackground(range == null ? table.getBackground() : range.getBackground());
      }

      return c;
   }
}

package org.selfbus.sbtools.prodedit.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A {@link JPanel panel} with convenient methods for creating forms.
 */
public class FormPanel extends JPanel
{
   private static final long serialVersionUID = 2924359553389491530L;
   private final Font captionFont;
   private int currentRow = 0;
   private Insets captionInsets = new Insets(10, 10, 0, 10);
   private Insets defaultInsets = new Insets(10, 10, 0, 10);

   /**
    * Create a form panel;
    */
   public FormPanel()
   {
      super();
      setLayout(new GridBagLayout());

      Font defaultFont = getFont();
      captionFont = defaultFont.deriveFont(Font.BOLD, defaultFont.getSize() * 1.2f);
   }

   /**
    * Add a caption in the next row of the form.
    * 
    * @param label - the caption text
    */
   public void addCaption(String label)
   {
      ++currentRow;

      Insets ins = currentRow == 0 ? defaultInsets : captionInsets;
      
      JLabel lbl = new JLabel(label);
      lbl.setFont(captionFont);
      add(lbl, new GridBagConstraints(0, currentRow, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, ins, 0, 0));
   }

   /**
    * Add a labeled form edit line in the next row of the form.
    * 
    * @param label - the label text
    * @param comp - the component for editing, may be null
    */
   public void addFormLine(String label, Component comp)
   {
      ++currentRow;
      
      add(new JLabel(label), new GridBagConstraints(0, currentRow, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, defaultInsets, 0, 0));

      if (comp != null)
         add(comp, new GridBagConstraints(1, currentRow, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));
   }

   /**
    * Add a form edit line in the next row of the form.
    * 
    * @param comp - the component
    */
   public void addFormLine(Component comp)
   {
      addFormLine(comp, 1);
   }

   /**
    * Add a form edit line in the next row of the form.
    * 
    * @param comp - the component
    * @param weight - the grid weight of the component (default: 1)
    */
   public void addFormLine(Component comp, int weight)
   {
      ++currentRow;
      
      add(comp, new GridBagConstraints(0, currentRow, 2, 1, 1, weight, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));
   }

   /**
    * Add a stretch in the next row of the form.
    * The stretch has a grid weight of 100.
    */
   public void addStretch()
   {
      ++currentRow;

      add(Box.createVerticalGlue(), new GridBagConstraints(0, currentRow, 2, 1, 1, 100, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, defaultInsets, 0, 0));
   }

   /**
    * @return The current form row.
    */
   public int getCurrentRow()
   {
      return currentRow;
   }

   /**
    * @return The font for captions.
    */
   public Font getCaptionFont()
   {
      return captionFont;
   }

   /**
    * @return The default insets.
    */
   public Insets getDefaultInsets()
   {
      return defaultInsets;
   }

   /**
    * @return The caption insets.
    */
   public Insets getCaptionInsets()
   {
      return captionInsets;
   }
}

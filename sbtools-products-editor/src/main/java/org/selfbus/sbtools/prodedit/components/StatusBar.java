package org.selfbus.sbtools.prodedit.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * A status bar.
 */
public class StatusBar extends JPanel
{
   private static final long serialVersionUID = -2138294079859022118L;

   private Insets insets = new Insets(1, 1, 1, 1);
   private int padX = 0;
   private int padY = 0;

   private int gridX = 0;

   /**
    * Create a status bar widget.
    */
   public StatusBar()
   {
      super();
      setLayout(new GridBagLayout());
   }

   /**
    * Add a component to the status bar.
    * 
    * @param comp - the component to add.
    * @param weight - the weight of the component.
    * @param fill - if true the component will fill the available space
    */
   public void add(Component comp, int weight, boolean fill)
   {
      add(comp, new GridBagConstraints(gridX++, 0, 1, 1, weight, 1, GridBagConstraints.NORTHWEST,
         fill ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE, insets, padX, padY));
   }
}

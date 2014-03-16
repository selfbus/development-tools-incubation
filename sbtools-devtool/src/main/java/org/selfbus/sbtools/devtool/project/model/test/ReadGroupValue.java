package org.selfbus.sbtools.devtool.project.model.test;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.common.HexString;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.Namespaces;
import org.selfbus.sbtools.knxcom.BusInterface;
import org.selfbus.sbtools.knxcom.application.GroupValueRead;
import org.selfbus.sbtools.knxcom.telegram.Telegram;

/**
 * Test step that sends a read-value telegram to a group, receives the value-response
 * telegram, and compares it with something.
 */
@XmlType(name = "ReadGroupValue", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ReadGroupValue extends AbstractGroupValue
{
   private byte[] value;
   private Operator operator;

   /**
    * @return The value to compare with.
    */
   public byte[] getValue()
   {
      return value;
   }

   /**
    * Set the value to compare with.
    * 
    * @param value - the value to set
    */
   public void setValue(byte[] value)
   {
      this.value = value;
   }

   /**
    * @return The value as string.
    */
   @XmlAttribute(name = "value")
   public String getValueStr()
   {
      return HexString.toString(value);
   }

   /**
    * Set the value.
    *
    * @param value - the value to set
    */
   public void setValueStr(String value)
   {
      this.value = HexString.valueOf(value);
   }

   /**
    * @return The operator as string.
    */
   @XmlAttribute(name = "operator")
   public String getOperatorStr()
   {
      return operator.toString().toLowerCase();
   }

   /**
    * Set the operator.
    *
    * @param operator - the operator to set
    */
   public void setOperatorStr(String operator)
   {
      this.operator = Operator.valueOf(operator.toUpperCase());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean perform(BusInterface bus) throws IOException
   {
      Telegram telegram = new Telegram(new GroupValueRead());
      telegram.setDest(address);
      bus.send(telegram);

      //TODO

      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Component createEditComponent()
   {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

      panel.add(new JLabel(I18n.getMessage("ReadGroupValue.label")));
      panel.add(new JTextField(getAddressStr()));
      panel.add(new JLabel(operator.toString()));
      panel.add(new JTextField(getValueStr()));

      panel.setMaximumSize(new Dimension(32767, panel.getPreferredSize().height));
      
      return panel;
   }
}

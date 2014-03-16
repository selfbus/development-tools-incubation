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
import org.selfbus.sbtools.devtool.project.Namespaces;
import org.selfbus.sbtools.knxcom.BusInterface;
import org.selfbus.sbtools.knxcom.application.GroupValueWrite;
import org.selfbus.sbtools.knxcom.telegram.Telegram;
import org.selfbus.sbtools.devtool.internal.I18n;

/**
 * Test step that sends a write-value telegram to a group.
 */
@XmlType(name = "WriteGroupValue", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class WriteGroupValue extends AbstractGroupValue
{
   private byte[] value;

   /**
    * @return The value that is sent.
    */
   public byte[] getValue()
   {
      return value;
   }

   /**
    * Set the value to send.
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
    * {@inheritDoc}
    */
   @Override
   public boolean perform(BusInterface bus) throws IOException
   {
      Telegram telegram = new Telegram(new GroupValueWrite(value));
      telegram.setDest(address);

      bus.send(telegram);

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

      panel.add(new JLabel(I18n.getMessage("WriteGroupValue.label")));
      panel.add(new JTextField(getAddressStr()));
      panel.add(new JLabel(" = "));
      panel.add(new JTextField(getValueStr()));

      panel.setMaximumSize(new Dimension(32767, panel.getPreferredSize().height));
      
      return panel;
   }
}

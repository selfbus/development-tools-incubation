package org.selfbus.sbtools.prodedit.model.prodgroup.program;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;
import org.selfbus.sbtools.prodedit.model.interfaces.Symbolized;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterContainer;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterRoot;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterTreeModel;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.utils.IdentifiableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

/**
 * An application program.
 * 
 * Device type and program version identify the application program.
 */
@XmlType(propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ApplicationProgram extends Model implements Identifiable, Symbolized
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProgram.class);
   private static final long serialVersionUID = 2365607976221764138L;
   
   @XmlAttribute(name = "program_id", required = true)
   private int id;

   @XmlAttribute(name = "symbol_id")
   private Integer symbolId;

   @XmlAttribute(name = "mask_version", required = true)
   private int maskVersion;

   @XmlAttribute(name = "program_name", required = true)
   private String name = "";

   @XmlAttribute(name = "program_version")
   private String version;

   @XmlAttribute(name = "program_type", required = true)
   private int typeId;

   @XmlAttribute(name = "device_type", required = true)
   private int deviceType;

   @XmlAttribute(name = "address_tab_size")
   private int addrTabSize;

   @XmlAttribute(name = "assoctab_address")
   private int assocTabAddr;

   @XmlAttribute(name = "assoctab_size")
   private int assocTabSize;

   @XmlAttribute(name = "commstab_address")
   private int commsTabAddr;

   @XmlAttribute(name = "commstab_size")
   private int commsTabSize;

   // @XmlAttribute(name = "eeprom_data")
   private byte[] eepromData;

   @XmlAttribute(name = "data_length")
   private int eepromDataLength;

   @XmlAttribute(name = "ram_size")
   private int ramSize;

   @XmlAttribute(name = "original_manufacturer_id")
   private int originalManufacturerId;

   @XmlElementWrapper(name = "parameter_types")
   @XmlElement(name = "parameter_type")
   private ArrayListModel<ParameterType> parameterTypes = new ArrayListModel<ParameterType>();

   @XmlElement(name = "description")
   private MultiLingualText description = new MultiLingualText();

   @XmlElementWrapper(name = "data_blocks")
   @XmlElement(name = "data_block")
   private ArrayListModel<DataBlock> dataBlocks = new ArrayListModel<DataBlock>();

   private ParameterTreeModel parameterTree;

   // Next unique ID for parameters and communication objects
   private int uniqueParamId = 1;

   /**
    * Create an empty program object.
    */
   public ApplicationProgram()
   {
   }

   /**
    * Create a program object.
    * 
    * @param id - the database ID of the object.
    * @param name - the name of the object.
    * @param maskVersion - the mask version.
    */
   public ApplicationProgram(int id, String name, int maskVersion)
   {
      this.id = id;
      this.name = name;
      this.maskVersion = maskVersion;
   }

   /**
    * Create a parameter type.
    * 
    * @param atomicType - the atomic type of the parameter.
    * 
    * @return The created parameter type.
    */
   public ParameterType createParameterType(ParameterAtomicType atomicType)
   {
      Validate.notNull(atomicType);

      ParameterType paramType = new ParameterType(atomicType);
      paramType.setSize(1);
      paramType.setMinValue(0);
      paramType.setMaxValue(1);
      paramType.setId(IdentifiableUtils.createUniqueId(parameterTypes));
      paramType.setName(I18n.formatMessage("ParameterType.newName", Integer.toString(paramType.getId())));

      addParameterType(paramType);

      return paramType;
   }

   /**
    * Create a parameter.
    * 
    * @param paramType - the type of the parameter.
    * @param parent - the parent parameter, null if it is a top level parameter.
    * 
    * @return The created parameter.
    */
   public Parameter createParameter(ParameterType paramType, AbstractParameterContainer parent)
   {
      Parameter param = createParameter(paramType);

      if (parent == null)
         parent = getParameterRoot();

      parent.addChild(param);

      return param;
   }

   /**
    * Create a parameter. The parameter is not added to the program or any other parent.
    * 
    * @param paramType - the type of the parameter.
    * 
    * @return The created parameter.
    */
   public Parameter createParameter(ParameterType paramType)
   {
      Validate.notNull(paramType);

      final int id = uniqueParamId++;
      Parameter param = new Parameter(paramType.getId());
      param.setId(id);
      param.setName("param_" + id);
      param.setDescription(new MultiLingualText());

      return param;
   }

   /**
    * Create a communication object.
    * 
    * @param parent - the parent parameter, null if it is a top level communication object.
    * 
    * @return The created parameter.
    */
   public CommunicationObject createCommunicationObject(AbstractParameterContainer parent)
   {
      final int id = uniqueParamId++;
      CommunicationObject comObject = new CommunicationObject();
      comObject.setId(id);
      comObject.setName(new MultiLingualText(I18n.formatMessage("ApplicationProgram.newComObjectName", "" + id)));
      comObject.setFunction(new MultiLingualText(I18n.formatMessage("ApplicationProgram.newComObjectFunc", "" + id)));
      comObject.setDescription(new MultiLingualText());

      if (parent == null)
         parent = getParameterRoot();

      parent.addChild(comObject);

      return comObject;
   }

   /**
    * Create a data block. The created data block is added to the program's data blocks.
    * 
    * @return The created data block.
    */
   public DataBlock createDataBlock()
   {
      DataBlock block = new DataBlock();
      block.setId(IdentifiableUtils.createUniqueId(dataBlocks));
      dataBlocks.add(block);

      return block;
   }

   /**
    * @return the program id
    */
   @Override
   public int getId()
   {
      return id;
   }

   /**
    * Set the program ID.
    * 
    * @param id - the id to set.
    */
   @Override
   public void setId(int id)
   {
      this.id = id;
   }

   /**
    * @return the mask version.
    */
   public int getMaskVersion()
   {
      return maskVersion;
   }

   /**
    * Set the mask version.
    * 
    * @param maskVersion - the mask version to set.
    */
   public void setMaskVersion(int maskVersion)
   {
      this.maskVersion = maskVersion;
   }

   /**
    * @return the name of the program.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the program.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      firePropertyChange("name", this.name, name);
      this.name = name == null ? "" : name;
   }

   /**
    * Get the version of the program. The name is a bit misleading,
    * as higher version numbers do not mean newer revisions of the
    * program. The version is used to distinguish different application
    * program types for the same physical device.
    * 
    * @return The version
    */
   public String getVersion()
   {
      return version;
   }

   /**
    * Set the version. See {@link #getVersion()} for details about
    * a version.
    * 
    * @param version - the version to set.
    */
   public void setVersion(String version)
   {
      this.version = version;
   }

   /**
    * The device type. Is written to the KNX device also.
    * 
    * @return the device type.
    */
   public int getDeviceType()
   {
      return deviceType;
   }

   /**
    * Set the device type.
    * 
    * @param deviceType - the device type to set.
    */
   public void setDeviceType(int deviceType)
   {
      this.deviceType = deviceType;
   }

   /**
    * @return the address-tab size.
    */
   public int getAddrTabSize()
   {
      return addrTabSize;
   }

   /**
    * Set the address-tab size.
    * 
    * @param addrTabSize - the address-tab size to set.
    */
   public void setAddrTabSize(int addrTabSize)
   {
      this.addrTabSize = addrTabSize;
   }

   /**
    * @return the association-tab address.
    */
   public int getAssocTabAddr()
   {
      return assocTabAddr;
   }

   /**
    * Set the association-tab address.
    * 
    * @param assocTabAddr - the association-tab address to set.
    */
   public void setAssocTabAddr(int assocTabAddr)
   {
      this.assocTabAddr = assocTabAddr;
   }

   /**
    * @return the association-tab size.
    */
   public int getAssocTabSize()
   {
      return assocTabSize;
   }

   /**
    * Set the association-tab size.
    * 
    * @param assocTabSize - the association-tab size.
    */
   public void setAssocTabSize(int assocTabSize)
   {
      this.assocTabSize = assocTabSize;
   }

   /**
    * @return The address of the communications table.
    */
   public int getCommsTabAddr()
   {
      return commsTabAddr;
   }

   /**
    * Set the address of the communications table.
    * 
    * @param commsTabAddr - the address to set.
    */
   public void setCommsTabAddr(int commsTabAddr)
   {
      this.commsTabAddr = commsTabAddr;
   }

   /**
    * @return The size of the communications table.
    */
   public int getCommsTabSize()
   {
      return commsTabSize;
   }

   /**
    * Set the size of the communications table.
    * 
    * @param commsTabSize - the size to set
    */
   public void setCommsTabSize(int commsTabSize)
   {
      this.commsTabSize = commsTabSize;
   }

   /**
    * @return The parameter tree model.
    */
   public ParameterTreeModel getParameterTreeModel()
   {
      return parameterTree;
   }

   /**
    * @return The root of the parameter tree model.
    */
   @XmlElement(name = "parameterRoot")
   public ParameterRoot getParameterRoot()
   {
      if (parameterTree == null)
         parameterTree = new ParameterTreeModel();

      return parameterTree.getRoot();
   }

   void setParameterRoot(ParameterRoot root)
   {
      parameterTree = new ParameterTreeModel(root);
   }

   /**
    * Get a parameter by parameter-id.
    * 
    * @param id - the parameter id
    * 
    * @return the parameter, or null if not found.
    */
   public AbstractParameterNode getParameter(int id)
   {
      if (parameterTree == null)
         return null;

      return parameterTree.findById(id);
   }

   /**
    * Add a top-level parameter to the program.
    * 
    * @param param - the parameter to add.
    */
   public void addParameter(AbstractParameterNode param)
   {
      if (parameterTree == null)
         parameterTree = new ParameterTreeModel();

      parameterTree.getRoot().addChild(param);
   }

   /**
    * Add a parameter type to the program.
    * 
    * @param paramType - the parameter type to add.
    */
   public void addParameterType(ParameterType paramType)
   {
      if (parameterTypes == null)
         parameterTypes = new ArrayListModel<ParameterType>();

      parameterTypes.add(paramType);
   }

   /**
    * Remove a top-level parameter from the program.
    * 
    * @param param - the parameter to remove.
    */
   public void removeParameter(Parameter param)
   {
      if (parameterTree != null)
         parameterTree.getRoot().removeChild(param);
   }

   /**
    * Remove all parameter from the program.
    */
   public void removeAllParameters()
   {
      if (parameterTree != null)
         parameterTree.getRoot().removeChildren();
   }

   /**
    * @return the eepromData
    */
   public byte[] getEepromData()
   {
      return eepromData;
   }

   /**
    * @param eepromData the eepromData to set
    */
   public void setEepromData(byte[] eepromData)
   {
      this.eepromData = eepromData;
   }

   @XmlAttribute(name = "eeprom_data")
   String getEepromDataStr()
   {
      return DatatypeConverter.printBase64Binary(eepromData);
   }

   void setEepromDataStr(String str)
   {
      eepromData = DatatypeConverter.parseBase64Binary(str);
   }

   /**
    * Get the program type ID. The program type ID is a 16 bit number that is unique
    * for a specific manufacturer. It can be used to identify the program that
    * is running in a BCU.
    * 
    * @return The program type ID
    */
   public int getTypeId()
   {
      return typeId;
   }

   /**
    * Set the program type ID. The program type ID is a 16 bit number that is unique
    * for a specific manufacturer. It can be used to identify the program that
    * is running in a BCU.
    *
    * @param id - the program type ID to set
    */
   public void setTypeId(int id)
   {
      this.typeId = id;
   }

   /**
    * @return the ramSize
    */
   public int getRamSize()
   {
      return ramSize;
   }

   /**
    * @param ramSize the ramSize to set
    */
   public void setRamSize(int ramSize)
   {
      this.ramSize = ramSize;
   }

   /**
    * @param parameterTypes the parameterTypes to set
    */
   public void setParameterTypes(ArrayListModel<ParameterType> parameterTypes)
   {
      this.parameterTypes = parameterTypes;
   }

   /**
    * @return The parameter types.
    */
   public ArrayListModel<ParameterType> getParameterTypes()
   {
      if (parameterTypes == null)
         parameterTypes = new ArrayListModel<ParameterType>();

      return parameterTypes;
   }

   /**
    * Get a parameter type.
    *
    * @param id - the ID of the parameter type
    * @return The parameter type, or null if not found.
    */
   public ParameterType getParameterType(int id)
   {
      if (parameterTypes == null)
         return null;

      for (ParameterType paramType : parameterTypes)
      {
         if (paramType.getId() == id)
            return paramType;
      }

      LOGGER.info("Parameter type #{} not found", id);
      return null;
   }

   /**
    * @return The "empty" parameter type.
    */
   public ParameterType getEmptyParameterType()
   {
      for (ParameterType paramType : parameterTypes)
      {
         if (ParameterType.EMPTY_NAME.equals(paramType.getName()))
            return paramType;
      }

      ParameterType emptyType = createParameterType(ParameterAtomicType.NONE);
      emptyType.setName(ParameterType.EMPTY_NAME);
      return emptyType;
   }

   /**
    * Sort the parameter types by name.
    */
   public void sortParameterTypes()
   {
      ParameterType[] arr = new ParameterType[parameterTypes.size()];
      parameterTypes.toArray(arr);

      Arrays.sort(arr, new Comparator<ParameterType>()
      {
         @Override
         public int compare(ParameterType a, ParameterType b)
         {
            return a.getName().compareToIgnoreCase(b.getName());
         }
      });

      parameterTypes.clear();
      Collections.addAll(parameterTypes, arr);
   }

   /**
    * Set the data blocks.
    *
    * @param dataBlocks - the data blocks.
    */
   public void setDataBlocks(ArrayListModel<DataBlock> dataBlocks)
   {
      this.dataBlocks = dataBlocks;
   }

   /**
    * @return The data blocks.
    */
   public ArrayListModel<DataBlock> getDataBlocks()
   {
      if (dataBlocks == null)
         dataBlocks = new ArrayListModel<DataBlock>();

      return dataBlocks;
   }

   /**
    * Sort the data block by {@link DataBlock#getNumber() block number}.
    */
   public void sortDataBlocks()
   {
      if (dataBlocks == null)
         return;

      DataBlock[] arr = new DataBlock[dataBlocks.size()];
      dataBlocks.toArray(arr);

      Arrays.sort(arr, new Comparator<DataBlock>()
      {
         @Override
         public int compare(DataBlock a, DataBlock b)
         {
            return b.getNumber() - a.getNumber();
         }
      });

      dataBlocks.clear();
      Collections.addAll(dataBlocks, arr);
   }

   /**
    * Set the program description.
    * 
    * @param description - the program description to set.
    */
   public void setDescription(MultiLingualText description)
   {
      this.description = description;
   }

   /**
    * @return The program description.
    */
   public MultiLingualText getDescription()
   {
      return description;
   }

   /**
    * @return the symbolId
    */
   @Override
   public Integer getSymbolId()
   {
      return symbolId;
   }

   /**
    * @param symbolId the symbolId to set
    */
   public void setSymbolId(Integer symbolId)
   {
      this.symbolId = symbolId;
   }

   /**
    * Update the internal unique ID for parameters and communication objects.
    * <p>
    * Call this method once after adding parameters or communication objects that
    * were not created with the create methods of this class.
    */
   public void updateUniqueId()
   {
      uniqueParamId = 1;
      updateUniqueParamId(getParameterRoot());
   }

   /**
    * Recursive worker of {@link #updateUniqueId()}.
    */
   private void updateUniqueParamId(AbstractParameterContainer cont)
   {
      Enumeration<AbstractParameterNode> it = cont.children();
      while (it.hasMoreElements())
      {
         AbstractParameterNode param = it.nextElement();

         if (param.getId() >= uniqueParamId)
            uniqueParamId = param.getId() + 1;

         if (param instanceof AbstractParameterContainer)
            updateUniqueParamId((AbstractParameterContainer) param);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object o)
   {
      if (o == this)
         return true;

      if (!(o instanceof ApplicationProgram))
         return false;

      final ApplicationProgram oo = (ApplicationProgram) o;
      return id == oo.id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return name;
   }
}

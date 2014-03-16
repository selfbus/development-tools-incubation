package org.selfbus.sbtools.prodedit.vdio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.common.Config;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.model.ProjectService;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.enums.ObjectPriority;
import org.selfbus.sbtools.prodedit.model.enums.ObjectType;
import org.selfbus.sbtools.prodedit.model.enums.ParameterAtomicType;
import org.selfbus.sbtools.prodedit.model.global.FunctionalEntity;
import org.selfbus.sbtools.prodedit.model.global.Language;
import org.selfbus.sbtools.prodedit.model.global.Manufacturer;
import org.selfbus.sbtools.prodedit.model.global.Mask;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.model.prodgroup.ProductGroup;
import org.selfbus.sbtools.prodedit.model.prodgroup.Symbol;
import org.selfbus.sbtools.prodedit.model.prodgroup.VirtualDevice;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterContainer;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.AbstractParameterNode;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.CommunicationObject;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.Parameter;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterCategory;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterType;
import org.selfbus.sbtools.prodedit.model.prodgroup.parameter.ParameterValue;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ApplicationProgram;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.CommsEntry;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.DataBlock;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.DataBlockParagraph;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ProgramAdapter;
import org.selfbus.sbtools.prodedit.model.prodgroup.program.ProgramAdapterFactory;
import org.selfbus.sbtools.vdio.ProductsReader;
import org.selfbus.sbtools.vdio.VdioException;
import org.selfbus.sbtools.vdio.model.VD;
import org.selfbus.sbtools.vdio.model.VdApplicationProgram;
import org.selfbus.sbtools.vdio.model.VdCatalogEntry;
import org.selfbus.sbtools.vdio.model.VdCommunicationObject;
import org.selfbus.sbtools.vdio.model.VdFunctionalEntity;
import org.selfbus.sbtools.vdio.model.VdLanguage;
import org.selfbus.sbtools.vdio.model.VdManufacturer;
import org.selfbus.sbtools.vdio.model.VdMask;
import org.selfbus.sbtools.vdio.model.VdParameter;
import org.selfbus.sbtools.vdio.model.VdParameterType;
import org.selfbus.sbtools.vdio.model.VdParameterValue;
import org.selfbus.sbtools.vdio.model.VdProgramDescription;
import org.selfbus.sbtools.vdio.model.VdS19Block;
import org.selfbus.sbtools.vdio.model.VdS19BlockParagraph;
import org.selfbus.sbtools.vdio.model.VdSymbol;
import org.selfbus.sbtools.vdio.model.VdTextAttribute;
import org.selfbus.sbtools.vdio.model.VdVirtualDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * The products importer can import ETS .vd_ products files.
 */
public class ProductsImporter extends AbstractProductsExpImp
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ProductsImporter.class);

   private final ProjectService projectService;
   private String fallbackLangId = "de";

   private VD vd;
   private Project project;
   private ProductGroup group;
   private Map<FunctionalEntity, ProductGroup> groups = new HashMap<FunctionalEntity, ProductGroup>();
   private Map<Integer, FunctionalEntity> topEntities = new HashMap<Integer, FunctionalEntity>();

   // Map application programs from external ID to internal program.
   private Map<Integer, ApplicationProgram> programs;

   // Application programs map, one per group
   private final Map<ProductGroup, Map<Integer, ApplicationProgram>> groupPrograms = new HashMap<ProductGroup, Map<Integer, ApplicationProgram>>();

   // Map external mask ID to mask
   private final Map<Integer, Mask> masks = new HashMap<Integer, Mask>();

   // Map application program to mask.
   private final Map<ApplicationProgram, Mask> programMasks = new HashMap<ApplicationProgram, Mask>();

   // Map parameter types from external ID to internal parameter type.
   private final Map<Integer, ParameterType> paramTypes = new HashMap<Integer, ParameterType>();

   // Map parameters from external ID to internal parameter.
   private final Map<Integer, Parameter> params = new HashMap<Integer, Parameter>();

   // Map symbols from external ID to internal ID.
   private final Map<Integer, Integer> symbolIds = new HashMap<Integer, Integer>();

   // The VD parameters, sorted by order
   private VdParameter[] sortedParams;

   // The VD com-objects, sorted by order
   private VdCommunicationObject[] sortedComObjects;

   /**
    * Text lookup cache. Always use {@link #getText(int,int)} to access the texts.
    */
   private Map<Integer, MultiLingualText> texts = new HashMap<Integer, MultiLingualText>(8191);

   /**
    * Create a products importer.
    *
    * @param projectService - the project service to use
    */
   public ProductsImporter(ProjectService projectService)
   {
      this.projectService = projectService;
   }

   /**
    * Read the VD file and create a {@link Project} from it.
    *
    * @param file - the VD or ZIP file to read.
    * @return The created project, or null if the user canceled the import.
    *
    * @throws FileNotFoundException if the file does not exist
    * @throws VdioException in case of VD parse errors
    */
   public Project read(File file) throws FileNotFoundException, VdioException
   {
      long startTime = System.currentTimeMillis();

      ProductsReader reader = new ProductsReader(ProdEdit.getInstance().getMainFrame());
      reader.setZipPassword(Config.getInstance().get("zipPassword"));

      vd = reader.read(file);
      if (vd == null) return null;
      Config.getInstance().put("zipPassword", reader.getZipPassword());

      importVd();
      project.setFile(file);

      LOGGER.debug("Import done ({} seconds)", (System.currentTimeMillis() - startTime) * 0.001);
      return project;
   }

   /**
    * Read the VD products from the input stream. This method can only read VD
    * streams and not ZIP streams.
    * 
    * @param in - the VD stream to read from.
    * @return The read project, or null if the user canceled the import.
    *
    * @throws VdioException in case of VD parse errors
    */
   public Project read(InputStream in) throws VdioException
   {
      long startTime = System.currentTimeMillis();

      ProductsReader reader = new ProductsReader(ProdEdit.getInstance().getMainFrame());
      reader.setZipPassword(Config.getInstance().get("zipPassword"));

      vd = reader.read(in);
      if (vd == null) return null;
      Config.getInstance().put("zipPassword", reader.getZipPassword());

      importVd();

      LOGGER.debug("Import done ({} seconds)", (System.currentTimeMillis() - startTime) * 0.001);
      return project;
   }

   /**
    * Process the read VD in {@link #vd}.
    * The created project is stored in {@link #project}.
    *
    * @throws VdioException in case of VD parse errors
    */
   protected void importVd() 
   {
      Validate.notNull(vd, "input stream is null");

      sortedParams = null;
      sortedComObjects = null;

      groups.clear();
      groupPrograms.clear();
      params.clear();
      paramTypes.clear();
      symbolIds.clear();
      programMasks.clear();
      masks.clear();

      project = new Project();
      project.setProjectService(projectService);

      analyzeTexts();

      importManufacturers();
      importLanguages();
      importFunctionalEntities();
      importProducts();

      project.afterUnmarshal(null, null);
   }

   /**
    * Analyze the translated texts and put them into a map for faster access.
    */
   protected void analyzeTexts()
   {
      texts.clear();

      for (VdTextAttribute t : vd.textAttributes)
      {
         MultiLingualText text = texts.get(t.getEntityId());
         if (text == null)
         {
            Validate.isTrue(t.getColumnId() < 1024); // or the implementation below will fail

            text = new MultiLingualText();
            texts.put((t.getEntityId() << 10) + t.getColumnId(), text);
         }

         text.setText(LanguageMapper.getLangId(t.getLanguageId()), t.getText());
      }
   }

   /**
    * Get the translated text.
    * 
    * @param id - the ID of the text
    * @param col - the column
    */
   protected MultiLingualText getText(int id, TextColumn col)
   {
      return texts.get((id << 10) + col.column);
   }

   /**
    * Get the translated text. If no text is found, a default text is
    * created with the given default in the fallback language.
    * 
    * @param id - the ID of the text
    * @param col - the column
    * @param defaultValue - the default value
    */
   protected MultiLingualText getText(int id, TextColumn col, String defaultValue)
   {
      MultiLingualText text = getText(id, col);

      if (text == null)
         text = new MultiLingualText();

      if (text.getText(fallbackLangId) == null)
         text.setText(fallbackLangId, defaultValue);

      return text;
   }


   /**
    * Import the manufacturers.
    */
   protected void importManufacturers()
   {
      Map<Integer, Manufacturer> manufacturers = project.getManufacturers();
      for (VdManufacturer m : vd.manufacturers)
      {
         manufacturers.put(m.getId(), new Manufacturer(m.getId(), m.getName()));
      }

      LOGGER.debug("Imported {} manufacturers", manufacturers.size());
   }

   /**
    * Import the languages.
    */
   protected void importLanguages()
   {
      ArrayListModel<Language> langs = project.getLanguages();
      for (VdLanguage l : vd.languages)
      {
         String langId = LanguageMapper.getLangId(l.getId());

         langs.add(new Language(langId, l.getName()));

         if (l.getDatabaseLanguage() == 1)
            project.setDefaultLangId(langId);
      }

      LOGGER.debug("Imported {} languages", langs.size());
   }

   /**
    * Import the functional entities.
    */
   protected void importFunctionalEntities()
   {
      for (VdFunctionalEntity e : vd.functionalEntities)
      {
         FunctionalEntity entity = new FunctionalEntity(e.getId(), e.getName());
         FunctionalEntity parentEntity = null;

         Integer parentId = e.getParentId();
         if (parentId != null)
         {
            parentEntity = project.getFunctionalEntity(parentId);
            Validate.notNull(parentEntity, "functional entity #" + parentId + " not found");
            parentEntity.add(entity);
         }
         else
         {
            project.getFunctionalEntities().add(entity);
         }
      }

      LOGGER.debug("Imported {} functional entities", project.getFunctionalEntities().size());
   }

   /**
    * Analyze the functional entities for faster products import. 
    */
   protected void analyzeFunctionalEntities()
   {
      topEntities.clear();

      for (VdFunctionalEntity e : vd.functionalEntities)
      {
         FunctionalEntity entity;

         if (e.getParentId() != null)
            entity = topEntities.get(e.getParentId());
         else entity = project.getFunctionalEntity(e.getId());

         Validate.notNull(entity);

         topEntities.put(e.getId(), entity);
      }
   }

   /**
    * Create a file name from a string.
    * 
    * @param str - the string to process.
    * @return The file name
    */
   String strToFileName(String str)
   {
      if (str.length() > 20)
         str = str.substring(0, 20);

      str = str.toLowerCase().replace(' ', '_').replace(':', '_').replace('.', '_');
      str = str.replace(';', '_').replaceAll("___*", "_");
      str = str.replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll("ü", "ue");
      str = str.replaceAll("ß", "ss");

      return str;
   }

   /**
    * Import the products.
    */
   protected void importProducts()
   {
      analyzeFunctionalEntities();

      for (VdVirtualDevice d : vd.virtualDevices)
      {
         int entityId = d.getFunctionalEntityId();
         FunctionalEntity topEntity = topEntities.get(entityId);
         Validate.notNull(topEntity, "top level functional entity for functional entity #{} not found", entityId);

         group = groups.get(topEntity);
         if (group == null)
         {
            group = new ProductGroup(strToFileName(topEntity.getName()), topEntity.getName());
            project.addProductGroup(group);
            groups.put(topEntity, group);

            LOGGER.debug("Creating products group {} \"{}\"", group.getId(), group.getName());

            programs = new HashMap<Integer, ApplicationProgram>();
            groupPrograms.put(group, programs);
         }
         else
         {
            programs = groupPrograms.get(group);
         }

         importVirtualDevice(d);
      }
   }

   /**
    * Get the mask for an application program. The mask is imported
    * if required.
    * 
    * @param vdMaskId - the VD mask-ID of the mask to get
    * @return The mask.
    */
   protected Mask getMask(int vdMaskId)
   {
      Mask mask = masks.get(vdMaskId);
      if (mask == null)
      {
         VdMask m = vd.findMask(vdMaskId);
         mask = new Mask();
         mask.setAddressTabAddr(m.getAddressTabAddress());
         mask.setAddressTabLCS(m.getAddressTabLCS());
         mask.setApplicationProgramLCS(m.getApplicationProgramLCS());
         mask.setApplicationProgramRCS(m.getApplicationProgramRCS());
         mask.setAssocTabLCS(m.getAssocTabLCS());
         mask.setAssocTabPtrAddr(m.getAssocTabPtrAddress());
         mask.setBcuType(m.getBcuTypeId());
         mask.setCommsTabPtrAddr(m.getCommsTabPtrAddress());
         mask.setData(m.getData());
         mask.setExternalMemoryEnd(m.getExternalMemoryEnd());
         mask.setExternalMemoryStart(m.getExternalMemoryStart());
         mask.setLoadControlAddr(m.getLoadControlAddress());
         mask.setManufacturerDataAddr(m.getManufacturerDataAddress());
         mask.setManufacturerDataSize(m.getManufacturerDataSize());
         mask.setManufacturerIdAddr(m.getManufacturerIdAddress());
         mask.setMediumTypeNumber(m.getMediumTypeNumber());
         mask.setMediumTypeNumber2(m.getMediumTypeNumber2());
         mask.setPeiProgramLCS(m.getPeiProgramLCS());
         mask.setPeiProgramRCS(m.getPeiProgramRCS());
         mask.setPortADdr(m.getPortADdr());
         mask.setRouteCountAddr(m.getRouteCountAddress());
         mask.setRunControlAddress(m.getRunControlAddress());
         mask.setRunErrorAddr(m.getRunErrorAddress());
         mask.setUserEepromEnd(m.getUserEepromEnd());
         mask.setUserEepromStart(m.getUserEepromStart());
         mask.setUserRamEnd(m.getUserRamEnd());
         mask.setUserRamStart(m.getUserRamStart());
         mask.setVersion(m.getVersion());

         masks.put(vdMaskId, mask);
      }

      return mask;
   }
   
   /**
    * Import a virtual device to the current product group.
    * 
    * @param d - the device to import
    */
   protected void importVirtualDevice(VdVirtualDevice d)
   {
      int vdProgramId = d.getProgramId();
      ApplicationProgram program = programs.get(vdProgramId);
      if (program == null)
      {
         VdApplicationProgram p = vd.findProgram(vdProgramId);
         Validate.notNull(p, "application program #{} not found in virtual device", vdProgramId);

         program = group.createProgram(p.getName());
         programs.put(d.getProgramId(), program);

         Mask mask = getMask(p.getMaskId());
         programMasks.put(program, mask);

         program.setMaskVersion(mask.getVersion());
         program.setVersion(p.getVersion());
         program.setDeviceType(p.getDeviceType());
         program.setAddrTabSize(p.getAddrTabSize());
         program.setAssocTabAddr(p.getAssocTabAddr());
         program.setAssocTabSize(p.getAssocTabSize());
         program.setCommsTabAddr(p.getCommsTabAddr());
         program.setCommsTabSize(p.getCommsTabSize());
         program.setEepromData(p.getEepromData());
         program.setTypeId(p.getProgramType());
         program.setSymbolId(getSymbolId(p.getSymbolId()));

         importProgramDescription(program, vdProgramId);
         importDataBlocks(program, vdProgramId);
         importParameterTypes(program, vdProgramId);
         importParameters(program, vdProgramId);
         importComObjects(program, vdProgramId);
      }

      VirtualDevice device = group.createDevice(program);

      device.setName(d.getName());
      device.setFunctionalEntityId(d.getFunctionalEntityId());
      device.setDescription(d.getDescription());
      device.setNumber(d.getNumber());
      device.setProductTypeId(d.getProductTypeId());
      device.setSymbolId(getSymbolId(d.getSymbolId()));
      device.setProgramId(program.getId());

      VdCatalogEntry ce = vd.findCatalogEntry(d.getCatalogEntryId());

      if (group.getManufacturer() == null || group.getManufacturer() == Manufacturer.NONE)
      {
         Manufacturer m = project.getManufacturer(ce.getManufacturerId());
         group.setManufacturer(m);
      }
   }

   /**
    * Get the internal symbol ID for the VD symbol id. Imports the symbol
    * if it is not yet imported.
    *
    * @param vdSymbolId - the VD symbol ID
    * @return The symbol
    */
   Integer getSymbolId(Integer vdSymbolId)
   {
      if (vdSymbolId == null)
         return null;

      Integer id = symbolIds.get(vdSymbolId);
      if (id == null || group.getSymbol(id) == null)
      {
         VdSymbol s = vd.findSymbol(vdSymbolId);
         if (s != null)
         {
            Symbol symbol = group.createSymbol(s.getName());
            symbol.setFileName(s.getFileName());
            symbol.setData(s.getData());

            id = symbol.getId();
            symbolIds.put(vdSymbolId, id);
//            LOGGER.debug("Adding symbol {} to group {}", s.getName(), group.getName());
         }
         else
         {
            LOGGER.error("Symbol #{} not found in VD", vdSymbolId);
         }
      }

      return id;
   }

   /**
    * Import the description of an application program.
    * 
    * @param program - the program to import the description for
    * @param vdProgramId - the program ID in the VD
    */
   protected void importProgramDescription(ApplicationProgram program, int vdProgramId)
   {
      if (vd.programDescriptions == null)
         return;

      Map<Integer,VdProgramDescription> ordered= new TreeMap<Integer,VdProgramDescription>();
      for (VdProgramDescription d : vd.programDescriptions)
      {
         if (d.getProgramId() == vdProgramId)
            ordered.put((d.getLanguageId() << 10) + d.getOrder(), d);
      }

      MultiLingualText desc = new MultiLingualText();
      for (int key : ordered.keySet())
      {
         VdProgramDescription d = ordered.get(key);
         String langId = LanguageMapper.getLangId(d.getLanguageId());

         String text = desc.getText(langId);
         if (text == null || text.isEmpty())
            text = d.getText();
         else text = text + '\n' + d.getText();

         desc.setText(langId, text);
      }

      program.setDescription(desc);
   }

   /**
    * Import the parameter types of an application program.
    * 
    * @param program - the program to import the parameter types for
    * @param vdProgramId - the program ID in the VD
    */
   protected void importParameterTypes(ApplicationProgram program, int vdProgramId)
   {
      paramTypes.clear();

      for (VdParameterType t : vd.parameterTypes)
      {
         if (t.getProgramId() != vdProgramId)
            continue;
 
         ParameterType paramType = program.createParameterType(ParameterAtomicType.valueOf(t.getAtomicTypeId()));
         paramType.setName(t.getName());
         paramType.setMinValue(t.getMinValue());
         paramType.setMaxValue(t.getMaxValue());
         paramType.setMinDoubleValue(t.getMinDoubleValue());
         paramType.setMaxDoubleValue(t.getMaxDoubleValue());
         paramType.setSize(t.getSize());

         paramTypes.put(t.getId(), paramType);
      }

      for (VdParameterValue v : vd.parameterValues)
      {
         ParameterType paramType = paramTypes.get(v.getParameterTypeId());
         if (paramType == null)
            continue;

         ParameterValue paramValue = paramType.createValue();
         paramValue.setIntValue(v.getIntValue());
         paramValue.setOrder(v.getOrder());
         paramValue.setLabel(getText(v.getId(), TextColumn.PARAM_VALUE, v.getLabel()));
      }

      program.sortParameterTypes();
      for (ParameterType paramType : paramTypes.values())
      {
         paramType.sortValues();
      }
   }

   /**
    * Sort the VD parameters by order number.
    * 
    * @return The sorted parameters
    */
   protected VdParameter[] sortedParameters()
   {
      if (sortedParams == null)
      {
         sortedParams = new VdParameter[vd.parameters.size()];
         vd.parameters.toArray(sortedParams);

         Arrays.sort(sortedParams, new Comparator<VdParameter>()
         {
            @Override
            public int compare(VdParameter a, VdParameter b)
            {
//               return a.getId() - b.getId();
               return a.getOrder() - b.getOrder();
            }
         });
      }

      return sortedParams; 
   }

   /**
    * Sort the VD com-objects by order number.
    * 
    * @return The sorted com-objects
    */
   protected VdCommunicationObject[] sortedComObjects()
   {
      if (sortedComObjects == null)
      {
         sortedComObjects = new VdCommunicationObject[vd.communicationObjects.size()];
         vd.communicationObjects.toArray(sortedComObjects);

         Arrays.sort(sortedComObjects, new Comparator<VdCommunicationObject>()
         {
            @Override
            public int compare(VdCommunicationObject a, VdCommunicationObject b)
            {
//               return a.getId() - b.getId();
               return a.getOrder() - b.getOrder();
            }
         });
      }

      return sortedComObjects; 
   }

   /**
    * Import the parameters of an application program.
    * 
    * @param program - the program to import the parameters for
    * @param vdProgramId - the program ID in the VD
    */
   protected void importParameters(ApplicationProgram program, int vdProgramId)
   {
      final AbstractParameterContainer rootParam = program.getParameterRoot();
//      AbstractParameterContainer pageParam = rootParam;
      LinkedHashMap<Integer, Parameter> appParams = new LinkedHashMap<Integer, Parameter>(4096);

      for (VdParameter p : sortedParameters())
      {
         if (p.getProgramId() != vdProgramId)
            continue;

         ParameterType paramType = paramTypes.get(p.getParamTypeId());
         Validate.notNull(paramType);

         int offset = p.getBitOffset();
         if (p.getAddress() != null && paramType.getSize() > 0 && paramType.getSize() < 8)
            offset = 8 - offset - paramType.getSize();

         Parameter param = program.createParameter(paramType);
         param.setName(p.getName());
         param.setDescription(getText(p.getId(), TextColumn.PARAM_DESCRIPTION, p.getDescription()));
         param.setAddress(p.getAddress());
         param.setBitOffset(offset);
         param.setSize(p.getSize());
         param.setOrder(p.getOrder());
         param.setParentId(p.getParentId()); // ID will be corrected below
         param.setParentValue(p.getParentValue());
         param.setDefaultInt(p.getDefaultInt());
         param.setDefaultDouble(p.getDefaultDouble());
         param.setDefaultString(p.getDefaultString());
         param.setNumber(p.getNumber());
         param.setVisible(p.getLowAccess() != 0 || p.getHighAccess() != 0);

         if (p.getAddress() == null)
            param.setCategory(ParameterCategory.PAGE);
         else if (p.getSize() == null || p.getSize() == 0)
            param.setCategory(ParameterCategory.LABEL);
         else param.setCategory(ParameterCategory.VALUE);

         appParams.put(p.getId(), param);
         params.put(p.getId(), param);
      }

      for (Parameter param : appParams.values())
      {
//         ParameterCategory category = param.getCategory();
         Integer parentId = param.getParentId();
         Parameter parentParam = null;

         // Lookup the parent parameter and correct the parent ID
         if (parentId != null)
         {
            parentParam = params.get(parentId);
            Validate.notNull(parentParam, "Parent parameter #{} not found in VD", parentId);

            parentId = parentParam.getId();
            param.setParentId(parentId);
         }

//         if (category == ParameterCategory.PAGE)
//         {
//            pageParam = param;
//            rootParam.addChild(param);
//         }

         if (parentParam != null) // && getParentPageParam(parentParam) == pageParam)
            parentParam.addChild(param);
//         else if (pageParam != param)
//            pageParam.addChild(param);
         else rootParam.addChild(param);
      }

      // Ensure that all parameters were added
      for (Parameter param : appParams.values())
         Validate.notNull(param.getParent(), "parameter was not added anywhere");
   }

   /**
    * Lookup the page parameter of a parameter. This is done by searching the parameter's
    * parents until a parameter with category {@link ParameterCategory#PAGE} is found.
    * 
    * @param param - the parameter to search the parent page.
    * @return The parent page parameter, or null if not found.
    */
   protected Parameter getParentPageParam(Parameter param)
   {
      for (AbstractParameterNode parent = param.getParent(); parent != null; parent = parent.getParent())
      {
         if (parent instanceof Parameter && ((Parameter) parent).getCategory() == ParameterCategory.PAGE)
            return (Parameter) parent;
      }
      return null;
   }

//   /**
//    * Get the address of a com-object.
//    * 
//    * @param objno - the number of the com-object.
//    * @param program - the program that owns the com-object.
//    * @param mask - the mask
//    *
//    * @return The address of the com-object.
//    */
//   protected int getComObjectAddress(int objno, ApplicationProgram program, VdMask mask)
//   {
//      byte[] eeprom = program.getEepromData();
//      if (eeprom == null)
//      {
//         eeprom = mask.getData();
//      }
//
//      int commsTabPtr = program.getCommsTabAddr() - 256;
//
//      int numObjs = eeprom[commsTabPtr] & 255;
//      if (objno >= numObjs)
//      {
//         LOGGER.error("Invalid com-object #{} (have {} objects)", objno, numObjs);
//         return 0;
//      }
//      int comObjPtr = commsTabPtr + 2 + objno * 3;
//
//      int dataPtr = eeprom[comObjPtr] & 255;
//      if ((eeprom[comObjPtr + 1] & 0x20) != 0)
//         dataPtr += 0x100;
//
//      return dataPtr;
//   }

   /**
    * Import the communication objects of an application program.
    * 
    * @param program - the program to import the parameters for
    * @param vdProgramId - the program ID in the VD
    */
   protected void importComObjects(ApplicationProgram program, int vdProgramId)
   {
      Mask mask = programMasks.get(program);
      ProgramAdapter programAdapter = ProgramAdapterFactory.getProgramAdapter(program, mask);
      ArrayListModel<CommsEntry> commsTab = programAdapter.getCommsTab();

      for (VdCommunicationObject o : sortedComObjects())
      {
         if (o.getProgramId() != vdProgramId)
            continue;

         Parameter parent = null;
         Integer parentId = o.getParentParameterId();
         if (parentId != null)
         {
            parent = params.get(parentId);
            Validate.notNull(parent, "Parent parameter #%1 for com-object #%2 not found", parentId, o.getId());
            parentId = parent.getId();
         }

         CommsEntry commsEntry;
         if (o.getNumber() < commsTab.size())
            commsEntry = commsTab.get(o.getNumber());
         else
         {
            LOGGER.error("Com-object #{} of program {} does not exist in the program's comms table",
               o.getNumber(), program.getName());
            commsEntry = new CommsEntry();
         }

         CommunicationObject comObject = program.createCommunicationObject(parent);
         comObject.setName(getText(o.getId(), TextColumn.COM_OBJECT_NAME, o.getName()));
         comObject.setFunction(getText(o.getId(), TextColumn.COM_OBJECT_FUNCTION, o.getFunction()));
         comObject.setDescription(getText(o.getId(), TextColumn.COM_OBJECT_DESCRIPTION, o.getDescription()));
         comObject.setType(ObjectType.valueOf(o.getTypeId()));
         comObject.setNumber(o.getNumber());
         comObject.setOrder(o.getOrder());
         comObject.setAddress(commsEntry.valuePtr);
         comObject.setParentId(parentId);
         comObject.setParentValue(o.getParentParameterValue());
         comObject.setCommEnabled(o.isCommEnabled());
         comObject.setReadEnabled(o.isReadEnabled());
         comObject.setWriteEnabled(o.isWriteEnabled());
         comObject.setTransEnabled(o.isTransEnabled());
         comObject.setPriority(ObjectPriority.valueOf(o.getPriorityId()));
      }
   }

   /**
    * Import the S19 data blocks and S19 data block paragraphs of an application program.
    * 
    * @param program - the program to import the data blocks for
    * @param vdProgramId - the program ID in the VD
    */
   protected void importDataBlocks(ApplicationProgram program, int vdProgramId)
   {
      Map<Integer, DataBlock> blocks = new HashMap<Integer, DataBlock>();

      if (vd.s19Block == null) // nothing to do if there are no S19 blocks
         return;

      for (VdS19Block b : vd.s19Block)
      {
         if (b.getProgramId() != vdProgramId)
            continue;

         DataBlock block = program.createDataBlock();
         block.setNumber(b.getNumber());
         block.setName(b.getName());
         block.setType(b.getType());
         block.setControlCode(b.getControlCode());
         block.setSegmentType(b.getSegmentType());
         block.setSegmentId(b.getSegmentId());
         block.setSegmentAddr(b.getSegmentAddress());
         block.setSegmentLength(b.getSegmentLength());
         block.setAccessAttributes(b.getAccessAttributes());
         block.setMemoryType(b.getMemoryType());
         block.setMemoryAttributes(b.getMemoryAttributes());
         block.setData(b.getBlockData());
         block.setBlockMask(b.getBlockMask());

         blocks.put(b.getId(), block);
      }

      for (VdS19BlockParagraph p : vd.s19BlockParagraph)
      {
         DataBlock block = blocks.get(p.getBlockId());
         if (block == null)
            continue;

         DataBlockParagraph paragraph = block.createParagraph();
         paragraph.setPtColumnId(p.getPtColumnId());
         paragraph.setDataLong(p.getDataLong());
         paragraph.setDataBinary(p.getDataBinary());
      }

      program.sortDataBlocks();
   }
}

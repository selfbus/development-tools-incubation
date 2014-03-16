package org.selfbus.sbtools.devtool.project.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbtools.common.address.GroupAddress;
import org.selfbus.sbtools.devtool.project.Namespaces;
import org.selfbus.sbtools.devtool.project.model.group.Group;
import org.selfbus.sbtools.devtool.project.model.group.MainGroup;
import org.selfbus.sbtools.devtool.project.model.test.Test;

/**
 * A project.
 */
@XmlRootElement
@XmlType(name = "Project", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Project
{
   @XmlAttribute
   private String name;

   @XmlElement(name = "group")
   @XmlElementWrapper(name = "groups")
   private List<Group> groups = new LinkedList<Group>();

   @XmlElement(name = "mainGroup")
   @XmlElementWrapper(name = "mainGroups")
   private List<MainGroup> mainGroups = new LinkedList<MainGroup>();

   @XmlElement(name = "test")
   @XmlElementWrapper(name = "tests")
   private List<Test> tests = new ArrayList<Test>();

   private File file;
   private final Map<GroupAddress, Group> groupsByAddr = new HashMap<GroupAddress, Group>();

   /**
    * @return The name of the project.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the project.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return The file that contains the project, may be null.
    */
   public File getFile()
   {
      return file;
   }

   /**
    * Set the file that contains the project.
    * 
    * @param file - the file to set, may be null.
    */
   public void setFile(File file)
   {
      this.file = file;
   }

   /**
    * @return The groups.
    */
   public List<Group> getGroups()
   {
      return groups;
   }

   /**
    * @return The main groups.
    */
   public List<MainGroup> getMainGroups()
   {
      return mainGroups;
   }

   /**
    * Find a group by it's address.
    * 
    * @param address - the group address to find
    * @return The group, or null if not found.
    * 
    * @see {@link #groupsChanged()}
    */
   public Group getGroup(GroupAddress address)
   {
      return groupsByAddr.get(address);
   }

   /**
    * Update the groups index. Call this method when you changed the groups.
    */
   public void groupsChanged()
   {
      synchronized (groupsByAddr)
      {
         groupsByAddr.clear();

         for (Group group: groups)
            groupsByAddr.put(group.getAddress(), group);
      }
   }

   /**
    * @return The tests.
    */
   public List<Test> getTests()
   {
      return tests;
   }

   /**
    * Initialize the project after loading.
    *
    * @param unmarshaller - the unmarshaller that was used for loading
    * @param parent - the parent object.
    */
   void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
   {
      groupsChanged();
   }
}

package org.selfbus.sbtools.devtool.tabs.groups;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.common.gui.actions.ActionFactory;
import org.selfbus.sbtools.common.gui.actions.ActionMethod;
import org.selfbus.sbtools.common.gui.actions.MethodActionFactory;
import org.selfbus.sbtools.common.gui.tree.IconTreeCellRenderer;
import org.selfbus.sbtools.common.gui.tree.MutableIconTreeNode;
import org.selfbus.sbtools.common.gui.utils.TreeUtils;
import org.selfbus.sbtools.common.gui.window.XmlToolBarFactory;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.ProjectListener;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.selfbus.sbtools.devtool.project.model.group.Group;

/**
 * A panel for editing the groups.
 */
public class GroupsPanel extends JSplitPane implements ProjectListener
{
   private static final long serialVersionUID = 4552179443574486405L;

   ActionFactory actionFactory = new MethodActionFactory(this);
   private Project project;

   private final MutableIconTreeNode rootNode = new MutableIconTreeNode("/");
   private final JTree tree = new JTree(rootNode);
   private final JScrollPane treeView = new JScrollPane(tree);
   private Object selectedObject;

   /**
    * Create a groups editor panel
    */
   public GroupsPanel()
   {
      super(JSplitPane.HORIZONTAL_SPLIT);
      setName(I18n.getMessage("GroupsPanel.title"));

      setOneTouchExpandable(true);
      setContinuousLayout(true);
      setResizeWeight(0.4);

      JPanel listPanel = new JPanel(new BorderLayout());
      setLeftComponent(listPanel);

      listPanel.add(createToolBar(), BorderLayout.NORTH);
      listPanel.add(treeView, BorderLayout.CENTER);

      JPanel detailsPanel = new JPanel(new BorderLayout());
      detailsPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
      setRightComponent(detailsPanel);

      tree.setRootVisible(false);
      tree.setCellRenderer(new IconTreeCellRenderer());
      
      tree.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2)
            {
//               ProjectManager.getController().edit(selectedObject);
               e.consume();
            }
         }
      });

      tree.addTreeSelectionListener(new TreeSelectionListener()
      {
         @Override
         public void valueChanged(TreeSelectionEvent e)
         {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            selectedObject = node != null ? node.getUserObject() : null;
            objectSelected(selectedObject);
         }
      });

      DevTool.getInstance().getProjectService().addListener(this);
   }

   /**
    * Create the tool-bar.
    * 
    * @return The created tool-bar.
    */
   private JToolBar createToolBar()
   {
      XmlToolBarFactory toolBarFactory = new XmlToolBarFactory(actionFactory);

      String fileName = "org/selfbus/sbtools/devtool/groups-panel-toolbar.xml";
      InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
      Validate.notNull(in, "Toolbar configuration not found: " + fileName);

      final JToolBar toolBar = toolBarFactory.createToolBar(in);
      toolBar.setFloatable(false);

      return toolBar;
   }

   /**
    * Action: add a group.
    * 
    * @param event - the event that triggered the action.
    */
   @ActionMethod(name = "addGroup", icon = "icons/add")
   public void addGroupAction(ActionEvent event)
   {
   }

   /**
    * Action: remove a group.
    * 
    * @param event - the event that triggered the action.
    */
   @ActionMethod(name = "RemoveGroup", icon = "icons/delete")
   public void removeGroupAction(ActionEvent event)
   {
   }

   /**
    * Update the groups.
    */
   public void updateContents()
   {
      rootNode.removeAllChildren();

      if (project == null)
         return;

      Set<Group> groups = new TreeSet<Group>();
      groups.addAll(project.getGroups());

      for (Group group : groups)
      {
         MutableIconTreeNode node = new MutableIconTreeNode(group, true);
//         node.setIcon(mainGroupIcon);
         rootNode.add(node);
      }

      ((DefaultTreeModel) tree.getModel()).reload();
      TreeUtils.expandAll(tree);
   }

   /**
    * Called when a node in the tree is selected.
    * 
    * @param obj - the selected user object of the selected tree node.
    */
   protected void objectSelected(Object obj)
   {
   }

   /**
    * Get the user object of the tree node that is closest to the position.
    * 
    * @param pos - the position to search the object for.
    * @return The user object, or null if nothing was found.
    */
   public Object getObjectAt(Point pos)
   {
      final TreePath path = tree.getClosestPathForLocation(pos.x, pos.y);
      return ((DefaultMutableTreeNode) path.getPathComponent(path.getPathCount() - 1)).getUserObject();
   }

   /**
    * Callback: the project changed.
    */
   @Override
   public void projectChanged(Project project)
   {
      this.project = project;
      updateContents();
   }
}

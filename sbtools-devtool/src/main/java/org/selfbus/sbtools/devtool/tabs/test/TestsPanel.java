package org.selfbus.sbtools.devtool.tabs.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.selfbus.sbtools.common.gui.actions.ActionFactory;
import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.actions.MethodActionFactory;
import org.selfbus.sbtools.common.gui.components.ToolBar;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.common.gui.tree.IconTreeCellRenderer;
import org.selfbus.sbtools.common.gui.tree.MutableIconTreeNode;
import org.selfbus.sbtools.common.gui.utils.TreeUtils;
import org.selfbus.sbtools.devtool.DevTool;
import org.selfbus.sbtools.devtool.internal.I18n;
import org.selfbus.sbtools.devtool.project.ProjectListener;
import org.selfbus.sbtools.devtool.project.model.Project;
import org.selfbus.sbtools.devtool.project.model.test.Test;
import org.selfbus.sbtools.devtool.project.model.test.TestStep;

/**
 * A panel for editing the tests.
 */
public class TestsPanel extends JSplitPane implements ProjectListener
{
   private static final long serialVersionUID = 3476915526689302246L;

   ActionFactory actionFactory = new MethodActionFactory(this);
   private Project project;

   private final MutableIconTreeNode rootNode = new MutableIconTreeNode("/");
   private final JTree tree = new JTree(rootNode);
   private final JScrollPane treeView = new JScrollPane(tree);
   private final Box detailsBox;
   private Object selectedObject;

   /**
    * Create a tests editor panel
    */
   public TestsPanel()
   {
      super(JSplitPane.HORIZONTAL_SPLIT);
      setName(I18n.getMessage("TestsPanel.title"));

      setOneTouchExpandable(true);
      setContinuousLayout(true);
      setResizeWeight(0.4);

      JPanel listPanel = new JPanel(new BorderLayout());
      setLeftComponent(listPanel);

      listPanel.add(createToolBar(), BorderLayout.NORTH);
      listPanel.add(treeView, BorderLayout.CENTER);

      detailsBox = new Box(BoxLayout.Y_AXIS);
      detailsBox.setBorder(BorderFactory.createLoweredSoftBevelBorder());
      setRightComponent(detailsBox);

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
   private ToolBar createToolBar()
   {
      final ToolBar toolBar = new ToolBar();
      toolBar.setFloatable(false);

      toolBar.add(addTestAction);
      toolBar.add(removeTestAction);

      return toolBar;
   }

   /**
    * Update the groups.
    */
   public void updateContents()
   {
      rootNode.removeAllChildren();

      if (project == null)
         return;

      Set<Test> tests = new TreeSet<Test>();
      tests.addAll(project.getTests());

      for (Test test : tests)
      {
         MutableIconTreeNode node = new MutableIconTreeNode(test, true);
         //         node.setIcon(testIcon);
         rootNode.add(node);
      }

      ((DefaultTreeModel) tree.getModel()).reload();
      TreeUtils.expandAll(tree);
   }

   /**
    * Show the test in the details panel.
    * 
    * @param test - the test to show, may be null.
    */
   public void showTest(Test test)
   {
      detailsBox.removeAll();
      
      for (TestStep testStep : test.getSteps())
      {
         Component comp = testStep.createEditComponent();
         if (comp != null)
            detailsBox.add(comp);
      }

      detailsBox.add(Box.createGlue());
      detailsBox.updateUI();
   }

   /**
    * Called when a node in the tree is selected.
    * 
    * @param obj - the selected user object of the selected tree node.
    */
   protected void objectSelected(Object obj)
   {
      if (obj instanceof Test)
         showTest((Test) obj);
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

   /**
    * Action: add a test
    */
   private final BasicAction addTestAction = new BasicAction("Add", I18n.getMessage("TestsPanel.Add.toolTip"),
      ImageCache.getIcon("icons/add"))
   {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionEvent(ActionEvent e)
      {
         // TODO
      }
   };

   /**
    * Action: remove a test
    */
   private final BasicAction removeTestAction = new BasicAction("Remove", I18n.getMessage("TestsPanel.Remove.toolTip"),
      ImageCache.getIcon("icons/delete"))
   {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionEvent(ActionEvent e)
      {
         // TODO
      }
   };
}

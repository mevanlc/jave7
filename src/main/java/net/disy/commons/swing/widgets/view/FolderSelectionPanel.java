package net.disy.commons.swing.filechooser.view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.filechooser.util.FileObjectUi;
import net.disy.commons.swing.tree.ITreeNodeActionListener;
import net.disy.commons.swing.tree.SmartTreeComponent;
import net.disy.commons.swing.tree.TreeNodeSmartTree;
import net.disy.commons.swing.ui.DelegatingObjectUi;

public class FolderSelectionPanel extends AbstractActionComponent implements IFileSystemContext {
   private SmartTreeComponent<TreeNode> tree;
   private final JComponent content;
   private final FileSystemView fileSystemView;
   private final Map<File, FolderNode> nodesByFile = new HashMap<>();
   private final File[] roots;
   private final FileModel folderModel;

   public FolderSelectionPanel(FileModel folderModel) {
      this(folderModel, FileSystemView.getFileSystemView());
   }

   public FolderSelectionPanel(FileModel folderModel, FileSystemView fileSystemView) {
      this(folderModel, fileSystemView, fileSystemView.getRoots());
   }

   public FolderSelectionPanel(FileModel folderModel, File[] roots) {
      this(folderModel, FileSystemView.getFileSystemView(), roots);
   }

   public FolderSelectionPanel(final FileModel folderModel, FileSystemView fileSystemView, File[] roots) {
      this.folderModel = folderModel;
      this.fileSystemView = fileSystemView;
      this.roots = roots;
      this.content = this.createDirectoryPanel();
      if (folderModel.getValue() != null) {
         this.setSelectedFolder(folderModel.getValue());
      }

      folderModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FolderSelectionPanel.this.setSelectedFolder(folderModel.getValue());
         }
      });
      this.tree.getSelectionModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            File selectedFolder = FolderSelectionPanel.this.getSelectedFolder();
            if (selectedFolder != null) {
               folderModel.setValue(selectedFolder);
            }
         }
      });
   }

   private void setSelectedFolder(File file) {
      if (file != null && !file.equals(this.getSelectedFolder())) {
         File folder = file.getAbsoluteFile();

         List<File> files;
         for (files = new ArrayList<>(); folder != null; folder = folder.getParentFile()) {
            files.add(folder);
            if (this.isRoot(folder)) {
               break;
            }
         }

         this.reverseSelect(files, files.size() - 1);
      }
   }

   private boolean isRoot(File file) {
      for (File root : this.roots) {
         if (root.getAbsoluteFile().equals(file)) {
            return true;
         }
      }

      return false;
   }

   private void reverseSelect(List<File> files, int index) {
      FolderNode node = this.nodesByFile.get(files.get(index));
      if (node != null) {
         TreeNode[] path = node.getPath();
         this.tree.expandPath(path);
         if (index > 0) {
            this.reverseSelect(files, index - 1);
         } else {
            this.tree.getSelectionModel().setSelectionPath(path);
         }
      }
   }

   private JComponent createDirectoryPanel() {
      DelegatingObjectUi<TreeNode, File> objectUi = new DelegatingObjectUi<TreeNode, File>(new FileObjectUi(this.fileSystemView)) {
         protected File getDelegatingValue(TreeNode value) {
            Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
            return !(userObject instanceof File) ? null : (File)userObject;
         }
      };
      this.tree = new SmartTreeComponent<>(new TreeNodeSmartTree(this.createRootNode()), objectUi);
      if (this.roots.length > 1) {
         this.tree.setRootVisible(false);
      }

      this.tree.addNodeActionListener(new ITreeNodeActionListener<TreeNode>() {
         public void nodeActionPerformed(Component parentComponent, TreeNode node) {
            if (node.getChildCount() == 0) {
               FolderSelectionPanel.this.fireActionEvent();
            }
         }
      });
      JComponent component = this.tree.getContent();
      component.setPreferredSize(new Dimension(150, 250));
      return component;
   }

   private DefaultMutableTreeNode createRootNode() {
      DefaultMutableTreeNode root;
      if (this.roots.length == 1) {
         root = new FolderNode(this, this.roots[0]);
      } else {
         root = new DefaultMutableTreeNode("root");

         for (File root2 : this.roots) {
            root.add(new FolderNode(this, root2));
         }
      }

      return root;
   }

   private File getSelectedFolder() {
      FolderNode node = this.getSelectedNode();
      return node == null ? null : node.getFile();
   }

   private FolderNode getSelectedNode() {
      return (FolderNode)this.tree.getSelectionModel().getSelectedNode();
   }

   public JComponent getContent() {
      return this.content;
   }

   @Override
   public FileSystemView getFileSystemView() {
      return this.fileSystemView;
   }

   @Override
   public void setBusy(boolean busy) {
      if (this.content != null) {
         if (busy) {
            this.content.setCursor(Cursor.getPredefinedCursor(3));
         } else {
            this.content.setCursor(null);
         }
      }
   }

   @Override
   public void registerNode(File file, FolderNode node) {
      this.nodesByFile.put(file, node);
   }

   public void refresh() {
      this.nodesByFile.clear();
      this.tree.setSmartTree(new TreeNodeSmartTree(this.createRootNode()));
      this.setSelectedFolder(this.folderModel.getValue());
   }
}

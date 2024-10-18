package net.disy.commons.swing.filechooser.view;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.filechooser.util.FileComparator;

public class FolderNode extends DefaultMutableTreeNode {
   private final IFileSystemContext context;

   public FolderNode(IFileSystemContext context, File file) {
      super(file);
      Ensure.ensureArgumentNotNull(file);
      context.registerNode(file, this);
      this.context = context;
   }

   public File getFile() {
      return (File)this.getUserObject();
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   @Override
   public void setUserObject(Object userObject) {
      Ensure.ensureArgumentNotNull(userObject);
      super.setUserObject(userObject);
   }

   @Override
   public int getChildCount() {
      this.assureChildrenLoaded();
      return super.getChildCount();
   }

   @Override
   public boolean isLeaf() {
      if (this.children != null) {
         return this.children.size() == 0;
      } else {
         return this.context.getFileSystemView().isFloppyDrive(this.getFile()) ? false : this.getChildCount() == 0;
      }
   }

   private void assureChildrenLoaded() {
      if (this.children == null) {
         this.context.setBusy(true);

         try {
            this.children = new Vector<>();
            File folder = this.getFile();
            File[] childFiles = FileSystemViewUtilities.listFiles(this.context.getFileSystemView(), folder);
            Arrays.sort(childFiles, new FileComparator(this.context.getFileSystemView()));

            for (File childFile : childFiles) {
               if (childFile.isDirectory()) {
                  FolderNode node = new FolderNode(this.context, childFile);
                  this.children.add(node);
                  node.setParent(this);
               }
            }
         } finally {
            this.context.setBusy(false);
         }
      }
   }
}

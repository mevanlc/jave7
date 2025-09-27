package net.disy.commons.swing.icon;

import javax.swing.Icon;
import javax.swing.UIManager;

public class SwingIcons {
   public static Icon getOptionPaneErrorIcon() {
      return UIManager.getIcon("OptionPane.errorIcon");
   }

   public static Icon getOptionPaneWarningIcon() {
      return UIManager.getIcon("OptionPane.warningIcon");
   }

   public static Icon getOptionPaneInformationIcon() {
      return UIManager.getIcon("OptionPane.informationIcon");
   }

   public static Icon getOptionPaneQuestionIcon() {
      return UIManager.getIcon("OptionPane.questionIcon");
   }

   public static Icon getTreeLeafIcon() {
      return UIManager.getIcon("Tree.leafIcon");
   }

   public static Icon getTreeClosedIcon() {
      return UIManager.getIcon("Tree.closedIcon");
   }

   public static Icon getTreeOpenIcon() {
      return UIManager.getIcon("Tree.openIcon");
   }

   public static Icon getFileViewDirectoryIcon() {
      return UIManager.getIcon("FileView.directoryIcon");
   }

   public static Icon getFileViewFileIcon() {
      return UIManager.getIcon("FileView.fileIcon");
   }

   public static Icon getFileViewComputerIcon() {
      return UIManager.getIcon("FileView.computerIcon");
   }

   public static Icon getFileViewHardDriveIcon() {
      return UIManager.getIcon("FileView.hardDriveIcon");
   }

   public static Icon getFileViewFloppyDriveIcon() {
      return UIManager.getIcon("FileView.floppyDriveIcon");
   }

   public static Icon getFileViewNewFolderIcon() {
      Icon icon = UIManager.getIcon("FileView.newFolderIcon");
      return icon == null ? CommonIcons.FOLDER_NEW : icon;
   }

   public static Icon getFileViewUpFolderIcon() {
      Icon icon = UIManager.getIcon("FileView.upFolderIcon");
      return icon == null ? CommonIcons.FOLDER_UP : icon;
   }
}

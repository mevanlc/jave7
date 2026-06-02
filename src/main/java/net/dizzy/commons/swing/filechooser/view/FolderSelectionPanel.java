package net.dizzy.commons.swing.filechooser.view;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class FolderSelectionPanel implements IComponentContainer {
   private final JFileChooser chooser = new JFileChooser();

   public FolderSelectionPanel(FileModel model) {
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      File file = model.getValue();
      if (file != null) {
         chooser.setSelectedFile(file);
      }
      chooser.addActionListener(event -> model.setValue(chooser.getSelectedFile()));
   }

   public FolderSelectionPanel(FileModel model, FileSystemView fileSystemView) {
      this(model);
   }

   @Override public JComponent getContent() { return chooser; }
}

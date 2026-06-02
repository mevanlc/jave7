package de.jave.asciimation.action;

import de.jave.asciimation.JmovFileChooserConfiguration;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.AlwaysEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.javeplayer.JaveAnimationFile;
import java.awt.Component;
import java.io.File;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class OpenAnimationAction extends AbstractJaveAction {
   private final FileModel currentDirectoryModel;
   private final JavEApplication jave;

   public OpenAnimationAction(JaveMainPanel mainPanel, JavEApplication jave, FileModel currentDirectoryModel) {
      super(mainPanel, "Open...", JaveIcons.OPEN_ICON);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(jave);
      this.currentDirectoryModel = currentDirectoryModel;
      this.jave = jave;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AlwaysEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(parentComponent, new JmovFileChooserConfiguration(this.currentDirectoryModel));
      if (!fileSelection.isEmpty()) {
         this.jave.open(parentComponent, fileSelection.getFile());
      }
   }

   public static JaveAnimationFile open(File file, Component parentComponent) {
      JaveAnimationFile newFile = new JaveAnimationFile(file);

      try {
         newFile.load(file.toURL());
         return newFile;
      } catch (Exception var4) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE Animation Editor", "Error loading file", var4));
         return null;
      }
   }
}

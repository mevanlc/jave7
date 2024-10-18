package de.jave.asciimation.editor;

import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.showhide.ShowHideContentPanel;

public class AnimationEditorPanel {
   public static final String JAVE_ANIMATION_EDITOR_TITLE = "JavE Animation Editor";
   public static final String TITLE = "JavE Animation Editor";
   private final FileModel currentDirectoryModel;
   private final Plate plate;
   private PlateDocument document;
   private final JaveApplicationPreferences preferences;
   private final BooleanModel showAdditionalControlsModel = new BooleanModel(true);
   private final AnimationEditorAdditionalControlsPanel additionalControlsPanel;
   private final AnimationEditorModel model;
   private final AnimationExportPreferences animationExportPreferences;
   private final JComponent content;

   public AnimationEditorPanel(
      AnimationEditorModel model,
      FileModel currentDirectoryModel,
      Plate plate,
      JaveApplicationPreferences preferences,
      AnimationExportPreferences animationExportPreferences
   ) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(animationExportPreferences);
      this.animationExportPreferences = animationExportPreferences;
      this.model = model;
      this.preferences = preferences;
      this.plate = plate;
      this.currentDirectoryModel = currentDirectoryModel;
      this.additionalControlsPanel = new AnimationEditorAdditionalControlsPanel(model);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationEditorPanel.this.refresh();
         }
      });
      this.content = this.createGui();
      if (model.getAnimationFile() == null) {
         JaveAnimationFile animationFile = this.createNewAnimationFile();
         this.setAnimationFile(animationFile);
         model.setModified(false);
      }

      this.refresh();
   }

   public JComponent getContent() {
      return this.content;
   }

   private JaveAnimationFile createNewAnimationFile() {
      JaveAnimationFile animationFile = new JaveAnimationFile();
      AnimationMetaData metaData = new AnimationMetaData();
      metaData.setDate(Toolbox.getDateString());
      metaData.setSoftware(JaveTitleProvider.JAVE + " (Animation Editor)");
      metaData.setAuthorName(this.preferences.getAuthorName());
      metaData.setAuthorEmail(this.preferences.getAuthorMail());
      animationFile.setMetaData(metaData);
      return animationFile;
   }

   private final JComponent createGui() {
      JPanel toolbarPanel = AnimationEditorToolBarFactory.createAnimationEditorToolBar(
         this.model, this.showAdditionalControlsModel, this.currentDirectoryModel, this.preferences.getDisplayFontModel(), this.animationExportPreferences
      );
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new ShowHideContentPanel(this.showAdditionalControlsModel, this.additionalControlsPanel.getContent()).getContent(), "Center");
      panel.add(toolbarPanel, "North");
      return panel;
   }

   private JaveAnimationFrame getFrame(int index) {
      return this.model.getAnimationFile().getFrame(index);
   }

   public void setCurrentFrame(int index) {
      if (index == -1) {
         index = 0;
      }

      this.model.getCurrentFrameIndexModel().setCurrentFrameIndex(index);
   }

   public void refresh() {
      this.setCurrentFrame(this.model.getCurrentFrameIndexModel().getCurrentFrameIndex());
   }

   public void doRemoveBorders(Component parentComponent) {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      Dimension maxFrameSize = new Dimension(0, 0);
      int frameCount = animationFile.getFrameCount();
      JaveAnimationFrame frame = null;
      CharacterPlate content = null;

      for (int i = 0; i < frameCount; i++) {
         frame = animationFile.getFrame(i);
         content = new CharacterPlate(frame.getContent());
         if (content.getWidth() > maxFrameSize.width) {
            maxFrameSize.width = content.getWidth();
         }

         if (content.getHeight() > maxFrameSize.height) {
            maxFrameSize.height = content.getHeight();
         }
      }

      System.err.println("Max. Groesse:" + maxFrameSize);
      Insets insets = null;

      for (int i = 0; i < frameCount; i++) {
         frame = this.getFrame(i);
         CharacterPlate con = new CharacterPlate(frame.getContent());
         Insets in = con.getEmptyInsets();
         char[][] ch = frame.getSelection();
         if (ch != null) {
            CharacterPlate sel = new CharacterPlate(ch);
            int selX = frame.getSelectionX();
            int selY = frame.getSelectionY();
            int selW = sel.getWidth();
            int selH = sel.getHeight();
            if (selX < in.left) {
               in.left = selX;
            }

            if (selY < in.top) {
               in.top = selY;
            }

            if (con.getWidth() - in.right < selX + selW) {
               in.right = con.getWidth() - selX - selW;
            }

            if (con.getHeight() - in.bottom < selY + selH) {
               in.bottom = con.getHeight() - selY - selH;
            }
         }

         if (con.getWidth() < maxFrameSize.width) {
            int delta = maxFrameSize.width - con.getWidth();
            in.left += delta / 2;
            in.right += delta - delta / 2;
         }

         if (con.getHeight() < maxFrameSize.height) {
            int delta = maxFrameSize.height - con.getHeight();
            in.top += delta / 2;
            in.bottom += delta - delta / 2;
         }

         if (insets == null) {
            insets = in;
         } else {
            if (in.bottom < insets.bottom) {
               insets.bottom = in.bottom;
            }

            if (in.top < insets.top) {
               insets.top = in.top;
            }

            if (in.left < insets.left) {
               insets.left = in.left;
            }

            if (in.right < insets.right) {
               insets.right = in.right;
            }
         }
      }

      if (insets != null) {
         System.err.println("Empty Insets: " + insets);
         if (insets.left == 0 && insets.right == 0 && insets.bottom == 0 && insets.top == 0) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message("Remove borders", "There are no empty borders to remove.", MessageType.INFORMATION)
            );
         } else if (insets.left == maxFrameSize.width) {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("Remove borders", "The animation is empty.", MessageType.INFORMATION));
         }
      }
   }

   public void doDeleteFrame(Component parentComponent) {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      if (animationFile.getFrameCount() == 1) {
         MessageDialogFactory.showMessageDialog(
            parentComponent, new Message("Delete frame", "There is only one frame. It can not be deleted.", MessageType.INFORMATION)
         );
      } else {
         int currentFrameIndex = this.model.getCurrentFrameIndexModel().getCurrentFrameIndex();
         if (currentFrameIndex >= 0 && currentFrameIndex < animationFile.getFrameCount()) {
            this.model.deleteFrame(currentFrameIndex);
            if (currentFrameIndex == animationFile.getFrameCount()) {
               this.model.navigatePrevious();
            }

            if (animationFile.getFrameCount() == 0) {
               this.setCurrentFrame(0);
            }
         }
      }
   }

   public JaveAnimationFile getAnimationFile() {
      return this.model.getAnimationFile();
   }

   public void setAnimationFile(JaveAnimationFile animationFile) {
      this.model.setAnimationFile(animationFile);
   }

   private JaveAnimationFrame getCurrentFrame() {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      int currentFrameIndex = this.model.getCurrentFrameIndexModel().getCurrentFrameIndex();
      return currentFrameIndex >= 0 && currentFrameIndex < animationFile.getFrameCount() ? this.getFrame(currentFrameIndex) : null;
   }

   public CurrentFrameIndexModel getCurrentFrameIndexModel() {
      return this.model.getCurrentFrameIndexModel();
   }

   public AnimationEditorModel getModel() {
      return this.model;
   }
}

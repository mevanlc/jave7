package de.jave.jave.actions;

import de.jave.jave.CompressedDocumentState;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveLogFileParser;
import de.jave.jave.JaveMessages;
import de.jave.jave.PlateDocument;
import de.jave.jave.Selection;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.TextDocumentEditor;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import de.jave.lib.area.BooleanArea;
import java.awt.Component;
import java.awt.Point;
import java.io.File;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;

public class ConvertMakingOfToAnimationAction extends AbstractJaveAction {
   private final JavEApplication jave;
   private final JaveApplicationPreferences preferences;

   public ConvertMakingOfToAnimationAction(JaveApplicationPreferences preferences, JavEApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Convert 'Making Of' to Animation", JaveIcons.ANIMATION_MAKING_OF_ICON);
      this.preferences = preferences;
      this.jave = jave;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      TextDocumentEditor textEditor = (TextDocumentEditor)editor;
      PlateDocument doc = textEditor.getPlate().getDocument();
      if (doc.isLogging()) {
         String logFileName = doc.getLogFileName();
         File logFile = new File(logFileName);
         if (!logFile.exists()) {
            this.showMakingOfEmptyMessage(parentComponent);
         } else {
            CompressedDocumentState[] frames = JaveLogFileParser.load(logFile);
            if (frames == null) {
               MessageDialogFactory.showMessageDialog(
                  parentComponent, new Message(JaveMessages.JavE, "Error while exporting to 'Making Of': Unable to load logfile.", MessageType.ERROR)
               );
            } else if (frames.length == 0) {
               this.showMakingOfEmptyMessage(parentComponent);
            } else {
               JaveAnimationFile animationFile = this.createAnimationFile(frames);
               this.jave.openJaveAnimation(animationFile);
            }
         }
      }
   }

   private void showMakingOfEmptyMessage(Component parentComponent) {
      MessageDialogFactory.showMessageDialog(
         parentComponent,
         new Message("JavE - Export 'Making Of'", "The current document has not been edited.\nNo 'Making Of' can be exported.", MessageType.INFORMATION)
      );
   }

   private JaveAnimationFile createAnimationFile(CompressedDocumentState[] frames) {
      AnimationMetaData metaData = new AnimationMetaData();
      metaData.setAuthorName(this.preferences.getAuthorName());
      metaData.setAuthorEmail(this.preferences.getAuthorMail());
      metaData.setDate(Toolbox.getDateString());
      metaData.setTitle(DocumentEditorTitleFactory.createShortEditorTitle(this.getMainPanel().getEditor()));
      metaData.setSoftware(JaveTitleProvider.JAVE + " - Making Of");
      ColorScheme colorScheme = frames[0].getColorScheme();
      AnimationProperties properties = new AnimationProperties();
      properties.setForegroundColor(colorScheme.getColorText());
      properties.setBackgroundColor(colorScheme.getColorPlateBackground());
      JaveAnimationFile animationFile = new JaveAnimationFile();
      animationFile.setMetaData(metaData);
      animationFile.setProperties(properties);

      for (int i = 0; i < frames.length; i++) {
         JaveAnimationFrame frame = new JaveAnimationFrame();
         if (frames[i].hasSelection()) {
            CharacterPlate cp = new CharacterPlate(frames[i].getContent());
            if (frames[i].hasSelectionMask()) {
               BooleanArea mask = frames[i].getSelectionMask();
               char[][] content = frames[i].getSelectionContent();
               Point location = frames[i].getSelectionLocation();
               Selection sel = new Selection();
               sel.set(location, content, mask);
               sel.pasteIntoNormal(cp);
            } else {
               char[][] content = frames[i].getSelectionContent();
               Point location = frames[i].getSelectionLocation();
               Selection sel = new Selection();
               sel.set(location, new CharacterPlate(content));
               sel.pasteIntoNormal(cp);
            }

            frame.setContent(cp);
         } else {
            frame.setContent(frames[i].getPackedContent());
         }

         frame.setScrollX(frames[i].getScrollX());
         frame.setScrollY(frames[i].getScrollY());
         frame.setCursorX(frames[i].getCursorX());
         frame.setCursorY(frames[i].getCursorY());
         frame.setTool(frames[i].getToolName());
         frame.setAction(frames[i].getAction());
         animationFile.add(frame);
      }

      return animationFile;
   }
}

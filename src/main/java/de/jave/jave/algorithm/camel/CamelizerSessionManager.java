package de.jave.jave.algorithm.camel;

import de.jave.jave.JaveMessages;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.desktop.AppForegroundEvent;
import java.awt.desktop.AppForegroundListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

/**
 * Owns the per-tab Camelizer dialog instances. Listens to the active-editor
 * model and to app foreground/background events to keep each session's
 * visibility gates in sync.
 *
 * The visibility model has three independent gates per session
 * (userWantsVisible, tabIsActive, appIsForeground); a session is visible iff
 * all three are true. There is no fourth "was-visible-before-app-hidden" flag
 * because the three gates already encode that requirement.
 */
public final class CamelizerSessionManager {
   private static final int MIN_NON_EMPTY_CHARS = 12;

   private final JaveMainPanel mainPanel;
   private final FileModel currentDirectoryModel;
   private final Map<IDocumentEditor, CamelizerSession> sessions = new LinkedHashMap<>();
   private boolean appIsForeground = true;
   private Frame owner;

   public CamelizerSessionManager(JaveMainPanel mainPanel, FileModel currentDirectoryModel) {
      this.mainPanel = mainPanel;
      this.currentDirectoryModel = currentDirectoryModel;
      this.mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            CamelizerSessionManager.this.onActiveEditorChanged();
         }
      });
   }

   /**
    * Wire app foreground/background detection via
    * {@link AppForegroundListener}. Only fires on true inter-app
    * transitions (cmd-tab on macOS, Alt-Tab on Windows), so it is immune to
    * the intra-JVM focus flicker that {@code KeyboardFocusManager} or
    * {@code WindowListener}-on-frame would suffer when a child dialog opens.
    *
    * On platforms without {@code Desktop.Action.APP_EVENT_FOREGROUND}
    * (often Linux X11), foreground tracking degrades: the dialog stays
    * visible during app background, but tab-switch hide/show still works.
    */
   public void installAppForegroundListener(JFrame frame) {
      this.owner = frame;
      if (!Desktop.isDesktopSupported()) {
         return;
      }
      Desktop desktop = Desktop.getDesktop();
      try {
         if (!desktop.isSupported(Desktop.Action.APP_EVENT_FOREGROUND)) {
            return;
         }
         desktop.addAppEventListener(new AppForegroundListener() {
            @Override
            public void appRaisedToForeground(AppForegroundEvent e) {
               CamelizerSessionManager.this.onAppForeground();
            }

            @Override
            public void appMovedToBackground(AppForegroundEvent e) {
               CamelizerSessionManager.this.onAppBackground();
            }
         });
      } catch (UnsupportedOperationException ignored) {
         // Some platforms throw at runtime even if isSupported says yes.
      }
   }

   /**
    * Open a Camelizer for the given editor, or focus the existing session.
    * If {@code optionalImageFile} is non-null, it is loaded into the (new or
    * existing) session.
    */
   public void openOrFocus(Component parentComponent, IDocumentEditor editor, File optionalImageFile) {
      if (editor == null) {
         return;
      }
      CamelizerSession existing = this.sessions.get(editor);
      if (existing != null && !existing.isDisposed()) {
         if (optionalImageFile != null) {
            existing.loadImageFile(optionalImageFile);
         }
         existing.setTabIsActive(this.isActiveEditor(editor));
         existing.setAppIsForeground(this.appIsForeground);
         existing.show();
         return;
      }

      CharacterPlate sourcePlate = editor.getPlate().getContentOfInterest().getContent();
      if (sourcePlate.getNonEmptyCharCount() < MIN_NON_EMPTY_CHARS) {
         MessageDialogFactory.showMessageDialog(
            parentComponent,
            new Message(
               JaveMessages.JavE,
               "The current document does not contain enough text.\nThe Camelizer can not be applied.",
               MessageType.INFORMATION
            )
         );
         return;
      }

      Frame frameOwner = this.owner;
      final IDocumentEditor sessionEditor = editor;
      Runnable onDispose = new Runnable() {
         @Override
         public void run() {
            CamelizerSessionManager.this.sessions.remove(sessionEditor);
         }
      };
      CamelizerSession session = new CamelizerSession(
         editor, this.mainPanel, frameOwner, sourcePlate, this.currentDirectoryModel, optionalImageFile, onDispose
      );
      this.sessions.put(editor, session);
      session.setTabIsActive(this.isActiveEditor(editor));
      session.setAppIsForeground(this.appIsForeground);
      session.show();
   }

   private boolean isActiveEditor(IDocumentEditor editor) {
      return this.mainPanel.getActiveEditorModel().getActiveEditor() == editor;
   }

   private void onActiveEditorChanged() {
      this.sweepDeadEditors();
      IDocumentEditor active = this.mainPanel.getActiveEditorModel().getActiveEditor();
      for (Map.Entry<IDocumentEditor, CamelizerSession> entry : this.sessions.entrySet()) {
         entry.getValue().setTabIsActive(entry.getKey() == active);
      }
   }

   private void sweepDeadEditors() {
      Set<IDocumentEditor> live = new HashSet<>();
      for (int i = 0; i < this.mainPanel.getEditorCount(); i++) {
         live.add(this.mainPanel.getEditor(i));
      }
      Iterator<Map.Entry<IDocumentEditor, CamelizerSession>> it = this.sessions.entrySet().iterator();
      Map<IDocumentEditor, CamelizerSession> toDispose = new HashMap<>();
      while (it.hasNext()) {
         Map.Entry<IDocumentEditor, CamelizerSession> entry = it.next();
         if (!live.contains(entry.getKey())) {
            toDispose.put(entry.getKey(), entry.getValue());
            it.remove();
         }
      }
      for (CamelizerSession session : toDispose.values()) {
         session.dispose();
      }
   }

   private void onAppForeground() {
      if (this.appIsForeground) {
         return;
      }
      this.appIsForeground = true;
      for (CamelizerSession session : this.sessions.values()) {
         session.setAppIsForeground(true);
      }
   }

   private void onAppBackground() {
      if (!this.appIsForeground) {
         return;
      }
      this.appIsForeground = false;
      for (CamelizerSession session : this.sessions.values()) {
         session.setAppIsForeground(false);
      }
   }
}

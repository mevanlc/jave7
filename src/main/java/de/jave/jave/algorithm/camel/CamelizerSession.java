package de.jave.jave.algorithm.camel;

import de.jave.jave.Plate;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import de.jave.lib.ICharacterPlateSettable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;

import de.jave.gui.dialog.JDialogFactory;

/**
 * One non-modal Camelizer dialog bound to a single {@link IDocumentEditor}.
 *
 * Visibility is the AND of three independent gates:
 *   userWantsVisible   — set by open/focus, cleared by Apply/Close/X
 *   tabIsActive        — driven by the active-editor model
 *   appIsForeground    — driven by app foreground/background events
 *
 * Apply commits a saveCurrentState undo entry and disposes.
 * Close (or X) reverts un-applied preview to the source plate snapshot and disposes.
 */
final class CamelizerSession {
   private final IDocumentEditor editor;
   private final JaveMainPanel mainPanel;
   private final CamelizerDialogPage page;
   private final JDialog dialog;
   private final CharacterPlate sourcePlate;
   private final BooleanModel changesAppliedModel;
   private final Runnable onDispose;

   private boolean userWantsVisible;
   private boolean tabIsActive;
   private boolean appIsForeground;
   private boolean applied;
   private boolean disposed;

   CamelizerSession(
      IDocumentEditor editor,
      JaveMainPanel mainPanel,
      Frame owner,
      CharacterPlate sourcePlate,
      net.disy.commons.core.io.FileModel currentDirectoryModel,
      File optionalImageFile,
      Runnable onDispose
   ) {
      this.editor = editor;
      this.mainPanel = mainPanel;
      this.sourcePlate = sourcePlate;
      this.changesAppliedModel = new BooleanModel(false);
      this.onDispose = onDispose;

      ICharacterPlateSettable perEditorSettable = new ICharacterPlateSettable() {
         @Override
         public void setCharacterPlate(CharacterPlate plate) {
            Plate p = CamelizerSession.this.editor.getPlate();
            if (p == null) {
               return;
            }
            if (p.hasSelection()) {
               p.setSelectionContent(plate);
            } else {
               p.setContent(plate);
            }
         }
      };

      this.page = new CamelizerDialogPage(
         sourcePlate, perEditorSettable, currentDirectoryModel, this.changesAppliedModel, optionalImageFile
      );

      this.dialog = JDialogFactory.createJDialog(owner, "Camelizer", false);
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(this.page.createContent(), BorderLayout.CENTER);
      this.dialog.getContentPane().add(this.createButtonBar(), BorderLayout.SOUTH);
      this.dialog.pack();
      this.dialog.setLocationRelativeTo(owner);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            CamelizerSession.this.closeAndRevert();
         }
      });
   }

   private JComponent createButtonBar() {
      JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      JButton applyButton = new JButton("Apply");
      applyButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            CamelizerSession.this.apply();
         }
      });
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            CamelizerSession.this.closeAndRevert();
         }
      });
      bar.add(applyButton);
      bar.add(closeButton);
      return bar;
   }

   IDocumentEditor getEditor() {
      return this.editor;
   }

   void loadImageFile(File file) {
      if (file != null) {
         this.page.performOpen(this.dialog, file);
      }
   }

   void show() {
      this.userWantsVisible = true;
      this.applyVisibility();
      if (this.dialog.isVisible()) {
         this.dialog.toFront();
         this.dialog.requestFocus();
      }
   }

   void setTabIsActive(boolean active) {
      if (this.tabIsActive != active) {
         this.tabIsActive = active;
         this.applyVisibility();
      }
   }

   void setAppIsForeground(boolean foreground) {
      if (this.appIsForeground != foreground) {
         this.appIsForeground = foreground;
         this.applyVisibility();
      }
   }

   private void applyVisibility() {
      if (this.disposed) {
         return;
      }
      boolean shouldShow = this.userWantsVisible && this.tabIsActive && this.appIsForeground;
      if (shouldShow != this.dialog.isVisible()) {
         this.dialog.setVisible(shouldShow);
      }
   }

   private void apply() {
      if (this.disposed) {
         return;
      }
      if (this.changesAppliedModel.getValue()) {
         Plate p = this.editor.getPlate();
         if (p != null) {
            p.saveCurrentState("camelize");
         }
      }
      this.applied = true;
      this.userWantsVisible = false;
      this.dispose();
   }

   private void closeAndRevert() {
      if (this.disposed) {
         return;
      }
      if (!this.applied && this.changesAppliedModel.getValue()) {
         Plate p = this.editor.getPlate();
         if (p != null) {
            if (p.hasSelection()) {
               p.setSelectionContent(this.sourcePlate);
            } else {
               p.setContent(this.sourcePlate);
            }
         }
      }
      this.userWantsVisible = false;
      this.dispose();
   }

   void dispose() {
      if (this.disposed) {
         return;
      }
      this.disposed = true;
      this.dialog.setVisible(false);
      this.dialog.dispose();
      if (this.onDispose != null) {
         this.onDispose.run();
      }
   }

   boolean isDisposed() {
      return this.disposed;
   }
}

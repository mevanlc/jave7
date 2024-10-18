package net.disy.commons.swing.widgets.internal;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.provider.IProvider;
import net.disy.commons.core.util.Ensure;

public final class TextComponentKeyListener extends KeyAdapter {
   private final TextContent content;
   private final ObjectModel<TextSelection> selectionModel;
   private final IProvider<Toolkit> toolkitProvider;

   public TextComponentKeyListener(TextContent content, ObjectModel<TextSelection> selectionModel, IProvider<Toolkit> toolkitProvider) {
      Ensure.ensureArgumentNotNull(content);
      Ensure.ensureArgumentNotNull(selectionModel);
      Ensure.ensureArgumentNotNull(toolkitProvider);
      this.content = content;
      this.selectionModel = selectionModel;
      this.toolkitProvider = toolkitProvider;
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (!this.content.isEmpty()) {
         if (e.isControlDown() && !e.isShiftDown() && !e.isMetaDown()) {
            if (e.getKeyCode() == 65) {
               this.selectAll();
               e.consume();
            } else if (e.getKeyCode() == 67) {
               this.copyToClipboard();
               e.consume();
            }
         }
      }
   }

   private void copyToClipboard() {
      TextSelection selection = this.selectionModel.getValue();
      TextPosition start = selection == null ? new TextPosition(0, 0) : selection.startPosition;
      TextPosition end = selection == null ? this.content.getLastTextPosition() : selection.endPosition;
      String text = this.content.getText(start, end);
      this.writeToSystemClipboard(text);
   }

   private void writeToSystemClipboard(String string) {
      StringSelection stringSelection = new StringSelection(string);
      Clipboard clipboard = this.toolkitProvider.getObject().getSystemClipboard();
      clipboard.setContents(stringSelection, stringSelection);
   }

   private void selectAll() {
      TextPosition start = new TextPosition(0, 0);
      TextPosition end = this.content.getLastTextPosition();
      this.selectionModel.setValue(TextSelection.createSelection(start, end));
   }
}

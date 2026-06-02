package de.jave.ascii.plate.textareabased;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.ruler.HorizontalRulerRenderingStrategy;
import de.jave.ascii.plate.ruler.RulerComponent;
import de.jave.ascii.plate.ruler.VerticalRulerRenderingStrategy;
import de.jave.text.TextTools;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import net.dizzy.commons.core.model.listener.ListenerList;
import net.dizzy.commons.core.util.IClosure;
import net.dizzy.commons.core.util.ObjectUtilities;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.dizzy.commons.swing.dialog.action.AbstractCopyAction;
import net.dizzy.commons.swing.dialog.action.TextComponentSelectAllAction;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.resources.DisyCommonsSwingIconResources;

public class AsciiTextArea implements IComponentContainer {
   private final AsciiTextAreaComponent textArea;
   private final JComponent content;
   private final ListenerList<ITextContentListener> listeners = new ListenerList<>();
   private final SmartAction copyAction;
   private final SmartAction selectAllAction;
   private final SmartAction cutAction;
   private final SmartAction pasteAction;
   private final AsciiTextAreaProperties properties;

   public AsciiTextArea(Dimension size, AsciiTextAreaProperties properties) {
      this.properties = properties;
      this.textArea = new AsciiTextAreaComponent(size, properties);
      int vsbPolicy = properties.isScrollingEnabled() ? 20 : 21;
      int hsbPolicy = properties.isScrollingEnabled() ? 30 : 31;
      JScrollPane scrollPane = new JScrollPane(this.textArea, vsbPolicy, hsbPolicy);
      scrollPane.setColumnHeaderView(new RulerComponent(new HorizontalRulerRenderingStrategy(), this.textArea, properties.getRulerProperties()));
      scrollPane.setRowHeaderView(new RulerComponent(new VerticalRulerRenderingStrategy(), this.textArea, properties.getRulerProperties()));
      this.content = scrollPane;
      this.textArea.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            AsciiTextArea.this.fireTextContentChangedEvent();
         }
      });
      this.textArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            if (e.isMetaDown()) {
               AsciiTextArea.this.showPopupMenuIfEnabled(e.getPoint());
            }
         }
      });
      this.cutAction = new SmartAction(DisyCommonsSwingDialogMessages.CUT, DisyCommonsSwingIconResources.CUT) {
         @Override
         protected void execute(Component parentComponent) {
            AsciiTextArea.this.textArea.cut();
         }
      };
      this.copyAction = new AbstractCopyAction() {
         @Override
         protected void execute(Component parentComponent) {
            AsciiTextArea.this.textArea.copy();
         }
      };
      this.pasteAction = new SmartAction(DisyCommonsSwingDialogMessages.PASTE, DisyCommonsSwingIconResources.PASTE) {
         @Override
         protected void execute(Component parentComponent) {
            AsciiTextArea.this.textArea.paste();
         }
      };
      this.selectAllAction = new TextComponentSelectAllAction(this.textArea);
   }

   private void showPopupMenuIfEnabled(Point point) {
      if (this.properties.isDefaultPopupEnabled()) {
         boolean hasSelection = this.textArea.getSelectedText() != null;
         JPopupMenu menu = new JPopupMenu();
         this.cutAction.setEnabled(this.getProperties().isEditable() && hasSelection);
         menu.add(this.cutAction);
         this.copyAction.setEnabled(hasSelection);
         menu.add(this.copyAction);
         if (this.getProperties().isEditable()) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);
            boolean hasClipboardContents = transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor);
            this.pasteAction.setEnabled(hasClipboardContents);
            menu.add(this.pasteAction);
         }

         menu.addSeparator();
         menu.add(this.selectAllAction);
         menu.show(this.textArea, point.x, point.y);
      }
   }

   public void setText(String text) {
      if (!ObjectUtilities.equals(text, this.textArea.getText())) {
         this.textArea.setText(text);
      }
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   public void addMouseListener(MouseListener mouseListener) {
      this.textArea.addMouseListener(mouseListener);
   }

   public void addFocusListener(FocusListener focusListener) {
      this.textArea.addFocusListener(focusListener);
   }

   public void addTextContentListener(ITextContentListener listener) {
      this.listeners.add(listener);
   }

   public void removeTextContentListener(ITextContentListener listener) {
      this.listeners.remove(listener);
   }

   protected void fireTextContentChangedEvent() {
      this.listeners.forAllDo(new IClosure<ITextContentListener>() {
         public void execute(ITextContentListener listener) throws RuntimeException {
            listener.textContentChanged();
         }
      });
   }

   public String getText() {
      return this.textArea.getText();
   }

   public Dimension getTextSize() {
      return TextTools.getDimensionOf(this.getText());
   }

   public void selectAll() {
      this.textArea.selectAll();
      this.textArea.getCaret().setSelectionVisible(true);
   }

   public void requestFocus() {
      this.textArea.requestFocus();
   }

   public void setToolTipText(String text) {
      this.textArea.setToolTipText(text);
   }

   public AsciiTextAreaProperties getProperties() {
      return this.properties;
   }

   public void scrollToTop() {
      this.textArea.setCaretPosition(0);
   }
}

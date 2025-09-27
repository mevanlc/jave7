package net.disy.commons.swing.dialog.message.internal;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.action.AbstractCopyAction;
import net.disy.commons.swing.dialog.action.TextComponentSelectAllAction;
import net.disy.commons.swing.dialog.foldout.AbstractFoldOutPage;

public final class MessageDetailsFoldOutPage extends AbstractFoldOutPage {
   private static final Font FIXEDWIDTH_FONT = new Font("Monospaced", 0, 11);
   private final String detailText;
   private JTextArea textArea;

   public MessageDetailsFoldOutPage(String detailText) {
      Ensure.ensureArgumentNotNull(detailText);
      this.detailText = detailText;
   }

   @Override
   protected JComponent createContent() {
      this.textArea = createMessageDetailsTextArea(this.detailText);
      return new JScrollPane(this.textArea);
   }

   private static JTextArea createMessageDetailsTextArea(String detailsText) {
      final JTextArea textArea = new JTextArea(12, 70);
      textArea.setEditable(false);
      textArea.setFont(FIXEDWIDTH_FONT);
      textArea.setText(detailsText);
      textArea.setCaretPosition(0);
      textArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            if (e.isMetaDown()) {
               MessageDetailsFoldOutPage.showPopupMenu(textArea, e.getPoint());
            }
         }
      });
      return textArea;
   }

   private static void showPopupMenu(final JTextArea textArea, Point point) {
      JPopupMenu menu = new JPopupMenu();
      SmartAction copyAction = new AbstractCopyAction() {
         @Override
         protected void execute(Component parentComponent) {
            boolean hasSelection = textArea.getSelectedText() != null;
            if (!hasSelection) {
               textArea.selectAll();
            }

            textArea.copy();
         }
      };
      SmartAction selectAllAction = new TextComponentSelectAllAction(textArea);
      menu.add(copyAction);
      menu.add(selectAllAction);
      menu.show(textArea, point.x, point.y);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocus();
   }
}

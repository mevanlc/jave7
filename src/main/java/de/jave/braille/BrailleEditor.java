package de.jave.braille;

import de.jave.braille.table.BrailleTables;
import de.jave.braille.table.IBrailleTable;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class BrailleEditor extends JFrame {
   private static final Font FONT1 = new Font("Monospaced", 0, 11);
   private static final Font FONT2 = new Font("Monospaced", 0, 29);
   public static final String TITLE = "Braille Editor V0.1";
   private final BrailleDisplay brailleDisplay;
   private final JTextField textField;
   private final JTextArea textArea;
   private final JTextArea textAreaLinemarkers;
   private final JComboBox chMode;
   private String[] lines;
   private int currentLine = 0;

   public BrailleEditor() {
      super("Braille Editor V0.1");
      this.lines = new String[1];
      this.lines[0] = "";
      this.textField = new JTextField("", 40);
      this.textField.setFont(FONT2);
      this.textField
         .addKeyListener(
            new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent evt) {
                  int code = evt.getKeyCode();
                  switch (code) {
                     case 10:
                        return;
                     case 38:
                        if (BrailleEditor.this.currentLine > 0) {
                           if (BrailleEditor.this.lines.length == BrailleEditor.this.currentLine + 1
                              && BrailleEditor.this.lines[BrailleEditor.this.currentLine].isEmpty()) {
                              String[] l = new String[BrailleEditor.this.lines.length - 1];
                              System.arraycopy(BrailleEditor.this.lines, 0, l, 0, BrailleEditor.this.lines.length - 1);
                              BrailleEditor.this.lines = l;
                           }

                           --BrailleEditor.this.currentLine;
                           BrailleEditor.this.textField.setText(BrailleEditor.this.lines[BrailleEditor.this.currentLine]);
                        }

                        return;
                     case 40:
                        if (BrailleEditor.this.lines.length <= BrailleEditor.this.currentLine + 1) {
                           String[] l = new String[BrailleEditor.this.lines.length + 1];
                           System.arraycopy(BrailleEditor.this.lines, 0, l, 0, BrailleEditor.this.lines.length);
                           l[BrailleEditor.this.currentLine + 1] = "";
                           BrailleEditor.this.lines = l;
                        }

                        ++BrailleEditor.this.currentLine;
                        BrailleEditor.this.textField.setText(BrailleEditor.this.lines[BrailleEditor.this.currentLine]);
                        return;
                  }
               }
            }
         );
      this.textField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            BrailleEditor.this.updateDisplay();
            BrailleEditor.this.lines[BrailleEditor.this.currentLine] = BrailleEditor.this.textField.getText();
            BrailleEditor.this.textAreaLinemarkers.setText("");
            StringBuffer sb = new StringBuffer();
            StringBuffer sbLines = new StringBuffer();
            int l = 0;
            int selectionStart = 0;
            int selectionEnd = 0;

            for (int i = 0; i < BrailleEditor.this.lines.length; i++) {
               if (i == BrailleEditor.this.currentLine) {
                  selectionStart = l;
                  selectionEnd = l + BrailleEditor.this.lines[i].length();
                  sbLines.append("->");
               }

               sb.append(BrailleEditor.this.lines[i]);
               l += BrailleEditor.this.lines[i].length();
               if (i < BrailleEditor.this.lines.length - 1) {
                  sb.append('\n');
                  l++;
                  sbLines.append("\n");
               }
            }

            BrailleEditor.this.textArea.setText(sb.toString());
            BrailleEditor.this.textArea.select(selectionStart, selectionEnd);
            BrailleEditor.this.textAreaLinemarkers.setText(sbLines.toString());
         }
      });
      this.brailleDisplay = new BrailleDisplay("                                          ");
      this.brailleDisplay.setViewSize(4);
      this.textArea = new JTextArea("", 20, 40);
      this.textArea.setFont(FONT1);
      this.textAreaLinemarkers = new JTextArea("->", 20, 2);
      this.textAreaLinemarkers.setFont(FONT1);
      this.textAreaLinemarkers.setEditable(false);
      JPanel panel1 = new JPanel();
      panel1.setBorder(new TitledBorder("Baille Line"));
      panel1.setLayout(new GridLayout(0, 1));
      panel1.add(this.brailleDisplay);
      panel1.add(this.textField);
      IBrailleTable[] brailleTables = BrailleTables.getAvailableTables();
      this.chMode = new JComboBox<>(brailleTables);
      this.chMode.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<IBrailleTable>() {
         public String getLabel(IBrailleTable value) {
            return value.getName();
         }
      }));
      this.chMode.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent evt) {
            BrailleEditor.this.updateDisplay();
         }
      });
      JPanel p2 = new JPanel();
      p2.add(new JLabel("Braille Table:"));
      p2.add(this.chMode);
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(panel1, "North");
      this.getContentPane().add(this.textArea, "Center");
      this.getContentPane().add(this.textAreaLinemarkers, "West");
      this.getContentPane().add(p2, "South");
      this.pack();
   }

   private void updateDisplay() {
      this.brailleDisplay.setMode((IBrailleTable)this.chMode.getSelectedItem());
      this.brailleDisplay.setText(this.textField.getText());
   }
}

package de.jave.jave;

import de.jave.gui.CharField;
import de.jave.gui.CharacterModel;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.algorithm.GeneralAlgorithm;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;
import net.dizzy.commons.swing.util.ToggleComponentEnabler;

public class ReplaceCharacterDialog {
   private final JaveMainPanel mainPanel;
   private final CharacterModel cfA = new CharacterModel(' ');
   private final CharacterModel cfB = new CharacterModel(' ');
   private JTextArea tfA;
   private JTextArea tfB;
   private JCheckBox cbWildcard;
   private final CharacterModel cfWildcard = new CharacterModel('?');
   private final JDialog dialog;
   private JTabbedPane tabbedPane;

   public ReplaceCharacterDialog(JaveMainPanel mainPanel, Component parent) {
      Ensure.ensureArgumentNotNull(mainPanel);
      this.mainPanel = mainPanel;
      this.dialog = JDialogFactory.createJDialog(parent, "Replace Character", false);
      JComponent panel = this.createOptionsPanel();
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(new SmartAction("&Replace") {
         @Override
         protected void execute(Component parentComponent) {
            ReplaceCharacterDialog.this.replace();
         }
      });
      buttonPanelBuilder.add(new SmartAction("&Close") {
         @Override
         protected void execute(Component parentComponent) {
            ReplaceCharacterDialog.this.close();
         }
      });
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(panel, "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.pack();
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            ReplaceCharacterDialog.this.close();
         }
      });
   }

   private JComponent createOptionsPanel() {
      CharField cfAField = new CharField(this.cfA);
      cfAField.setFont(JaveGlobalRessources.FONT_DEFAULT);
      CharField cfBField = new CharField(this.cfB);
      cfBField.setFont(JaveGlobalRessources.FONT_DEFAULT);
      JPanel panel1 = new JPanel(new GridDialogLayout(2, false));
      panel1.add(new JLabel("Find what:"), GridDialogLayoutData.RIGHT);
      panel1.add(cfAField);
      panel1.add(new JLabel("Replace with:"), GridDialogLayoutData.RIGHT);
      panel1.add(cfBField);
      panel1.setBorder(new EmptyBorder(4, 4, 4, 4));
      this.tfA = new JTextArea(4, 20);
      this.tfA.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.tfB = new JTextArea(4, 20);
      this.tfB.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.cbWildcard = new JCheckBox("Use wildcard", false);
      JLabel lWildcard = new JLabel("Wildcard character:");
      CharField cfWildcardField = new CharField(this.cfWildcard);
      JPanel p1 = new JPanel(new GridDialogLayout(2, true));
      p1.add(new JLabel("Find what:"));
      p1.add(new JLabel("Replace with:"));
      p1.add(new JScrollPane(this.tfA), GridDialogLayoutData.FILL_BOTH);
      p1.add(new JScrollPane(this.tfB), GridDialogLayoutData.FILL_BOTH);
      JPanel p2 = new JPanel(new GridDialogLayout(3, false));
      p2.add(this.cbWildcard);
      p2.add(lWildcard);
      p2.add(cfWildcardField);
      JPanel panel2 = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      panel2.add(p1, "Center");
      panel2.add(p2, "South");
      panel2.setBorder(new EmptyBorder(4, 4, 4, 4));
      ToggleComponentEnabler.connect(this.cbWildcard, cfWildcardField, lWildcard);
      this.tabbedPane = new JTabbedPane();
      this.tabbedPane.addTab("Character", panel1);
      this.tabbedPane.addTab("Pattern", panel2);
      return this.tabbedPane;
   }

   private void close() {
      this.dialog.setVisible(false);
   }

   private void replace() {
      if (this.tabbedPane.getSelectedIndex() == 0) {
         char ch1 = this.cfA.getCharacter();
         char ch2 = this.cfB.getCharacter();
         this.replaceCharacter(ch1, ch2);
      } else {
         CharacterPlate cpA = new CharacterPlate(this.tfA.getText());
         CharacterPlate cpB = new CharacterPlate(this.tfB.getText());
         boolean wildcard = this.cbWildcard.isSelected();
         char wildcardChar = this.cfWildcard.getCharacter();
         this.replacePattern(cpA, cpB, wildcard, wildcardChar);
      }
   }

   private void replaceCharacter(char ch1, char ch2) {
      CharacterPlate cp = this.mainPanel.getContentOfInterest().getContent();
      if (cp != null) {
         int count = GeneralAlgorithm.replace(cp, ch1, ch2);
         if (count == 0) {
            this.dialog.getToolkit().beep();
            this.mainPanel.showStatus("'" + ch1 + "' not found!");
         } else {
            if (count == 1) {
               this.mainPanel.showStatus("Replaced 1 occurrence.");
            } else {
               this.mainPanel.showStatus("Replaced " + count + " occurrences.");
            }

            this.mainPanel.repaint();
            this.mainPanel.saveCurrentState("replace character");
         }
      }
   }

   private void replacePattern(CharacterPlate cp1, CharacterPlate cp2, boolean wildcard, char wildcardChar) {
      CharacterPlate cp = this.mainPanel.getContentOfInterest().getContent();
      if (cp != null) {
         int count = GeneralAlgorithm.replace(cp, cp1, cp2, wildcard, wildcardChar);
         if (count == 0) {
            this.dialog.getToolkit().beep();
            this.mainPanel.showStatus("Pattern not found!");
         } else {
            if (count == 1) {
               this.mainPanel.showStatus("Replaced 1 occurrence.");
            } else {
               this.mainPanel.showStatus("Replaced " + count + " occurrences.");
            }

            this.mainPanel.repaint();
            this.mainPanel.saveCurrentState("replace pattern");
         }
      }
   }

   public JDialog getDialog() {
      return this.dialog;
   }

   public void show() {
      this.dialog.setVisible(true);
   }
}

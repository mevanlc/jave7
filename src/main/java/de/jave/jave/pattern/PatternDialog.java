package de.jave.jave.pattern;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.jave.tool.fill.PatternPreviewComponent;
import de.jave.text.TextTools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.message.MessageDialogUtilities;
import net.disy.commons.swing.dialog.message.YesNoCancel;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class PatternDialog {
   private static final String TITLE = "Pattern Library";
   private final JDialog dialog;
   private final List list;
   private final PatternList patternList;
   private final AsciiTextArea taPattern;
   private final TextField tfAuthor;
   private Pattern currentPattern;
   private final PatternPreviewComponent previewComponent;

   public PatternDialog(Component parent, PatternList patternList) {
      Ensure.ensureArgumentNotNull(patternList);
      this.patternList = patternList;
      this.dialog = JDialogFactory.createJDialog(parent, "Pattern Library", true);
      this.list = new List();
      int size = patternList.getPatternCount();

      for (int i = 0; i < size; i++) {
         this.list.add(patternList.getPattern(i).getName());
      }

      this.list.setMultipleMode(false);
      this.list.select(0);
      this.list.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            PatternDialog.this.updatePreview();
         }
      });
      this.currentPattern = patternList.getPattern(0);
      FontModel fontModel = new FontModel(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      AsciiTextAreaProperties patternTextAreaProperties = new AsciiTextAreaProperties(fontModel);
      this.taPattern = new AsciiTextArea(new Dimension(10, 6), patternTextAreaProperties);
      patternTextAreaProperties.setEditable(false);
      this.tfAuthor = new TextField(15);
      this.tfAuthor.setEditable(false);
      final SmartAction deleteAction = new SmartAction("Delete") {
         @Override
         protected void execute(Component parentComponent) {
            PatternDialog.this.performDeleteSelectedPattern(parentComponent);
         }
      };
      this.list.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            PatternDialog.this.updateDeleteActionEnabled(deleteAction);
         }
      });
      this.updateDeleteActionEnabled(deleteAction);
      SmartAction okAction = new SmartAction("&OK") {
         @Override
         protected void execute(Component parentComponent) {
            PatternDialog.this.performOk();
         }
      };
      SmartAction cancelAction = new SmartAction("&Cancel") {
         @Override
         protected void execute(Component parentComponent) {
            PatternDialog.this.performCancel();
         }
      };
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(okAction);
      buttonPanelBuilder.add(cancelAction);
      JPanel detailsPanel = new JPanel(new GridDialogLayout(2, false));
      detailsPanel.add(new JLabel("Author:"));
      detailsPanel.add(this.tfAuthor);
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalSpan(2);
      detailsPanel.add(new JButton(deleteAction), data);
      this.previewComponent = new PatternPreviewComponent(fontModel);
      JPanel patternPanel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), 1));
      patternPanel.add(new JLabel("Pattern:"), "North");
      patternPanel.add(this.list, "West");
      patternPanel.add(this.taPattern.getContent(), "Center");
      patternPanel.add(detailsPanel, "East");
      JPanel previewPanel = new JPanel(new BorderLayout());
      previewPanel.add(new JLabel("Preview:"), "North");
      previewPanel.add(this.previewComponent.getContent(), "Center");
      this.dialog.getContentPane().setLayout(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      this.dialog.getContentPane().add(patternPanel, "North");
      this.dialog.getContentPane().add(previewPanel, "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            PatternDialog.this.performCancel();
         }
      });
      this.updatePreview();
   }

   private void performCancel() {
      this.currentPattern = null;
      this.performOk();
   }

   private void updateDeleteActionEnabled(SmartAction deleteAction) {
      deleteAction.setEnabled(this.list.getSelectedIndex() != -1);
   }

   public void addPattern(Pattern newPattern) {
      this.patternList.add(newPattern);
      this.patternList.save();
      this.updateList();
      this.updatePreview();
   }

   private void updateList() {
      this.list.removeAll();
      int size = this.patternList.getPatternCount();

      for (int i = 0; i < size; i++) {
         this.list.add(this.patternList.getPattern(i).getName());
      }

      this.list.select(size - 1);
   }

   private void updatePreview() {
      int index = this.list.getSelectedIndex();
      this.currentPattern = this.patternList.getPattern(index);
      this.previewComponent.setPattern(this.currentPattern);
      char[][] ch = AsciiPacker.decode(this.currentPattern.getCode());
      this.taPattern.setText(TextTools.toString(ch));
      this.tfAuthor.setText(this.currentPattern.getAuthor());
   }

   private void performOk() {
      this.dialog.setVisible(false);
   }

   private void performDeleteSelectedPattern(Component parentComponent) {
      int index = this.list.getSelectedIndex();
      this.currentPattern = this.patternList.getPattern(index);
      String question = "Do you really want to delete\nthe Pattern \"" + this.currentPattern.getName() + "\" ?";
      YesNoCancel answer = MessageDialogUtilities.showYesNoCancelDialog(parentComponent, new Message("Delete pattern", question, MessageType.WARNING));
      if (answer == YesNoCancel.YES) {
         this.patternList.delete(index);
         this.patternList.save();
         this.updateList();
         this.list.select(index - 1);
         this.updatePreview();
      }
   }

   public Dialog getDialog() {
      return this.dialog;
   }

   public Pattern getSelectedPattern() {
      return this.currentPattern;
   }

   public boolean isCanceled() {
      return this.currentPattern == null;
   }

   public void show() {
      this.dialog.setVisible(true);
   }
}

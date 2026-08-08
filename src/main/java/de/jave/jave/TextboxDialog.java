package de.jave.jave;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.layout.IHorizontalAlignmentVisitor;
import de.jave.figlet.swing.layout.HorizontalAlignmentPanel;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.algorithm.AlignLeft;
import de.jave.jave.algorithm.AlignRight;
import de.jave.jave.algorithm.CenterByTheLine;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.rectangle.RectangleStylePanel;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;

public class TextboxDialog {
   private static final String TITLE = "Textbox Editor";
   private final AsciiTextArea taInput;
   private final AsciiTextArea taPreview;
   private final RectangleStylePanel rectangleStylePanel;
   private final JDialog dialog;
   private Point location;
   private final HorizontalAlignmentPanel horizontalAlignmentPanel;

   public TextboxDialog(JavEApplication parent) {
      this(parent, "Your text here", new Point(0, 0));
   }

   public TextboxDialog(final JavEApplication parent, String content, Point location) {
      this.location = location;
      this.dialog = JDialogFactory.createJDialog(parent.getFrame(), "Textbox Editor", true);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            TextboxDialog.this.performCancel();
         }
      });
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.horizontalAlignmentPanel = new HorizontalAlignmentPanel(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            TextboxDialog.this.preview();
         }
      });
      this.horizontalAlignmentPanel.setSelectedAlignment(HorizontalAlignment.LEFT);
      JPanel pAlign = new JPanel(new GridDialogLayout(4, false, 0, 2));
      pAlign.add(new JLabel("Alignment:", 4));
      pAlign.add(this.horizontalAlignmentPanel.getComponent());
      this.rectangleStylePanel = new RectangleStylePanel(parent.getMainPanel().getMouseCharacterModel());
      this.rectangleStylePanel.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            TextboxDialog.this.preview();
         }
      });
      JPanel pEast = new JPanel(new GridDialogLayout(1, false));
      pEast.add(pAlign);
      pEast.add(this.rectangleStylePanel.getContent());
      FontModel displayFontModel = parent.getApplicationPreferences().getDisplayFontModel();
      AsciiTextAreaProperties inputProperties = new AsciiTextAreaProperties(displayFontModel);
      this.taInput = new AsciiTextArea(new Dimension(40, 8), inputProperties);
      this.taInput.setText(content);
      this.taInput.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            TextboxDialog.this.preview();
         }
      });
      AsciiTextAreaProperties outputProperties = new AsciiTextAreaProperties(displayFontModel);
      outputProperties.setEditable(false);
      this.taPreview = new AsciiTextArea(new Dimension(40, 8), outputProperties);
      JPanel inputPanel = new JPanel(new BorderLayout());
      inputPanel.add(new JLabel("Text:"), "North");
      inputPanel.add(this.taInput.getContent(), "Center");
      JPanel outputPanel = new JPanel(new BorderLayout());
      outputPanel.add(new JLabel("Preview:"), "North");
      outputPanel.add(this.taPreview.getContent(), "Center");
      JPanel mainPanel = new JPanel(new GridDialogLayout(2, false));
      mainPanel.add(outputPanel, GridDialogLayoutData.FILL_BOTH);
      mainPanel.add(pEast);
      GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      data.setHorizontalSpan(2);
      mainPanel.add(inputPanel, data);
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(new SmartAction("&OK") {
         @Override
         protected void execute(Component parentComponent) {
            TextboxDialog.this.performOk(parent);
         }
      });
      buttonPanelBuilder.add(new SmartAction("&Cancel") {
         @Override
         protected void execute(Component parentComponent) {
            TextboxDialog.this.performCancel();
         }
      });
      this.dialog.getContentPane().add(mainPanel, "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.pack();
      this.preview();
   }

   private void performCancel() {
      this.setVisible(false);
   }

   public void setContent(String content) {
      this.taInput.setText(content);
      this.preview();
   }

   public void setLocation(Point location) {
      this.location = location;
   }

   public void setTextboxStyle(RectangleStyle textboxStyle) {
      this.rectangleStylePanel.setStyle(textboxStyle);
      this.preview();
   }

   public void show() {
      this.dialog.setVisible(true);
      this.taInput.selectAll();
      this.taInput.requestFocus();
   }

   public void preview() {
      this.taPreview.setText(this.getResult().toString());
   }

   protected CharacterPlate getResult() {
      final CharacterPlate cp = new CharacterPlate(this.taInput.getText());
      this.horizontalAlignmentPanel.getSelectedAlignment().accept(new IHorizontalAlignmentVisitor() {
         @Override
         public void visitCenterAlignment(HorizontalAlignment horizontalAlignment) {
            CenterByTheLine.getInstance().apply(cp);
         }

         @Override
         public void visitRightAlignment(HorizontalAlignment horizontalAlignment) {
            AlignRight.getInstance().apply(cp);
         }

         @Override
         public void visitLeftAlignment(HorizontalAlignment horizontalAlignment) {
            AlignLeft.getInstance().apply(cp);
         }
      });
      cp.addColumnsLeft(1);
      cp.addColumnsRight(1);
      cp.addLinesBottom(1);
      cp.addLinesTop(1);
      char[] chars = this.rectangleStylePanel.getCurrentChars();
      RectangleAlgorithm.drawRectangle(cp, new Rectangle(0, 0, cp.getWidth(), cp.getHeight()), chars);
      return cp;
   }

   public void setVisible(boolean visible) {
      this.dialog.setVisible(visible);
   }

   public static RectangleStyle getTextboxStyle(int[][] content) {
      return RectangleAlgorithm.getRectangleStyle(content);
   }

   public JDialog getDialog() {
      return this.dialog;
   }

   private void performOk(JavEApplication parent) {
      parent.pasteAsNewSelection(this.getResult(), this.location);
      this.setVisible(false);
   }
}

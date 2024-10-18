package de.jave.figlet.swing.action;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigFontOptions;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.application.FontCategoriesListView;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.util.FigUtilities;
import de.jave.lib.gui.GuiUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;

public abstract class FontInfoAction extends AbstractFontListAction {
   public FontInfoAction(IFigDriver figlet) {
      super(figlet, "Font info");
      this.setIcon(FigletIcons.INFO_ICON);
      this.setToolTipText("Show infos for selected font");
   }

   @Override
   protected void execute(FigFont font, Component parentComponent) {
      showFontInfoDialog(parentComponent, this.getFiglet(), font);
   }

   public static void showFontInfoDialog(Component parentComponent, IFigDriver figDriver, FigFont font) {
      final JDialog dialog = GuiUtilities.createDialog(parentComponent, "Font info");
      dialog.setModal(true);
      SmartAction closeAction = new SmartAction("&Close") {
         @Override
         protected void execute(Component parent) {
            dialog.dispose();
         }
      };
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      JButton closeButton = new JButton(closeAction);
      dialog.getRootPane().setDefaultButton(closeButton);
      buttonPanelBuilder.add(closeButton);
      dialog.getContentPane().setLayout(new BorderLayout());
      dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      dialog.getContentPane().add(createFontInfoPanel(figDriver, font), "Center");
      dialog.setDefaultCloseOperation(2);
      dialog.pack();
      GuiUtilities.centerToParent(dialog);
      dialog.setVisible(true);
   }

   private static JComponent createFontInfoPanel(IFigDriver figDriver, FigFont font) {
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(createFontOptionsPanel(font), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new JLabel("Comments by the font author:", 2));
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties();
      properties.setEditable(false);
      AsciiTextArea commentsArea = new AsciiTextArea(new Dimension(72, 6), properties);
      commentsArea.setText(font.getComments());
      panel.add(commentsArea.getContent(), GridDialogLayoutData.FILL_BOTH);
      panel.add(new JLabel("Font categories containing this font:", 2));
      FontCategoriesListView categoriesListView = new FontCategoriesListView();
      IFigFontCategory[] containingCategories = FigUtilities.getCategoriesContainingFont(figDriver.getFileLibrary().getFontCategorization(), font.getName());
      categoriesListView.setCategories(containingCategories);
      panel.add(categoriesListView.getContent());
      panel.setBorder(GuiUtilities.DEFAULT_EMPTY_BORDER);
      return panel;
   }

   private static JComponent createFontOptionsPanel(FigFont font) {
      FigFontOptions options = font.getOptions();
      options.getActualMaxLineWidth();
      JPanel panelLeft = new JPanel(new GridDialogLayout(2, false));
      panelLeft.add(new JLabel("Name:"), GridDialogLayoutData.RIGHT);
      panelLeft.add(createTextField(font.getName()), GridDialogLayoutData.FILL_HORIZONTAL);
      panelLeft.add(new JLabel("Height:"), GridDialogLayoutData.RIGHT);
      panelLeft.add(createTextField(String.valueOf(options.getHeight())), GridDialogLayoutData.FILL_HORIZONTAL);
      panelLeft.add(new JLabel("Max length:"), GridDialogLayoutData.RIGHT);
      panelLeft.add(createTextField(String.valueOf(options.getMaxLength())), GridDialogLayoutData.FILL_HORIZONTAL);
      JPanel panelRight = new JPanel(new GridDialogLayout(2, false));
      panelRight.add(new JLabel("Actual max width:"), GridDialogLayoutData.RIGHT);
      panelRight.add(createTextField(String.valueOf(options.getActualMaxLineWidth())), GridDialogLayoutData.FILL_HORIZONTAL);
      panelRight.add(new JLabel("Baseline:"), GridDialogLayoutData.RIGHT);
      panelRight.add(createTextField(String.valueOf(options.getBaseline())), GridDialogLayoutData.FILL_HORIZONTAL);
      panelRight.add(new JLabel("Codetag count:"), GridDialogLayoutData.RIGHT);
      panelRight.add(createTextField(String.valueOf(options.getCodetagCount())), GridDialogLayoutData.FILL_HORIZONTAL);
      JPanel panel = new JPanel(new GridDialogLayout(2, true));
      panel.add(panelLeft);
      panel.add(panelRight);
      return panel;
   }

   private static JTextField createTextField(String text) {
      JTextField field = new JTextField(text, 13);
      field.setEditable(false);
      return field;
   }
}

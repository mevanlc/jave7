package de.jave.jave.tool.fill;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.jave.AsciiGradientComboBoxFactory;
import de.jave.jave.JaveMessages;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.algorithm.fill.FillMode;
import de.jave.jave.algorithm.fill.GradientStyle;
import de.jave.jave.algorithm.fill.GradientStyleUi;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pattern.Pattern;
import de.jave.jave.pattern.PatternDialog;
import de.jave.jave.pattern.PatternList;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.gui.GuiUtilities;
import de.jave.text.TextTools;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class FillInlineOptionsPanel implements IInlineToolOptions {
   private static final String CARD_SOLID = "SOLID";
   private static final String CARD_PATTERN = "PATTERN";
   private static final String CARD_GRADIENT = "GRADIENT";

   private final FillOptions options;
   private final JaveApplicationPreferences applicationPreferences;
   private final PatternList patternList;
   private final JComponent content;

   private final JComboBox chMode;
   private final JComboBox chMatchMode;
   private final JPanel cardHost;
   private final CardLayout cardLayout;

   private PatternPreviewComponent patternPreview;
   private PatternDialog patternDialog;

   public FillInlineOptionsPanel(
      final FillOptions options,
      MouseCharacterModel mouseCharacterModel,
      JaveApplicationPreferences applicationPreferences,
      PatternList patternList,
      AsciiGradientConfiguration gradientConfiguration
   ) {
      Ensure.ensureArgumentNotNull(options);
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      Ensure.ensureArgumentNotNull(applicationPreferences);
      Ensure.ensureArgumentNotNull(patternList);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.options = options;
      this.applicationPreferences = applicationPreferences;
      this.patternList = patternList;

      this.chMode = new JComboBox<>(FillMode.values());
      this.chMode.setRenderer(new ObjectUiListCellRenderer(new FillModeUi()));
      this.chMode.setSelectedItem(options.getFillMode());
      this.chMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FillMode mode = (FillMode) FillInlineOptionsPanel.this.chMode.getSelectedItem();
            options.setFillMode(mode);
            FillInlineOptionsPanel.this.showCard(mode);
         }
      });

      this.chMatchMode = new JComboBox<>(FillMatchMode.values());
      this.chMatchMode.setRenderer(new ObjectUiListCellRenderer(new FillMatchModeUi()));
      this.chMatchMode.setSelectedItem(options.getMatchMode());
      this.chMatchMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setMatchMode((FillMatchMode) FillInlineOptionsPanel.this.chMatchMode.getSelectedItem());
         }
      });

      this.cardLayout = new CardLayout();
      this.cardHost = new JPanel(this.cardLayout);
      this.cardHost.add(this.buildSolidCard(mouseCharacterModel), CARD_SOLID);
      this.cardHost.add(this.buildPatternCard(), CARD_PATTERN);
      this.cardHost.add(this.buildGradientCard(gradientConfiguration), CARD_GRADIENT);

      JPanel modeRow = new JPanel(new GridDialogLayout(2, false));
      modeRow.add(new JLabel("Mode:"));
      modeRow.add(this.chMode, GridDialogLayoutData.FILL_HORIZONTAL);

      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(modeRow, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(this.cardHost, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new JLabel("Match mode:"));
      panel.add(this.chMatchMode, GridDialogLayoutData.FILL_HORIZONTAL);
      this.content = panel;

      this.showCard(options.getFillMode());
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   private void showCard(FillMode mode) {
      switch (mode) {
         case SOLID:
            this.cardLayout.show(this.cardHost, CARD_SOLID);
            break;
         case PATTERN:
            this.cardLayout.show(this.cardHost, CARD_PATTERN);
            break;
         case GRADIENT:
            this.cardLayout.show(this.cardHost, CARD_GRADIENT);
            break;
      }
   }

   private JComponent buildSolidCard(MouseCharacterModel mouseCharacterModel) {
      return new SolidFillOptionsPanel(mouseCharacterModel).getContent();
   }

   private JComponent buildPatternCard() {
      this.patternPreview = new PatternPreviewComponent(this.applicationPreferences.getDisplayFontModel());
      this.patternPreview.setPattern(this.options.getPattern());

      SmartAction openAction = new SmartAction(JaveIcons.OPEN_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            FillInlineOptionsPanel.this.performChoosePattern(parentComponent);
         }
      };
      openAction.setToolTipText("Open Pattern…");
      SmartAction saveAction = new SmartAction(JaveIcons.SAVE_EDIT_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            FillInlineOptionsPanel.this.performSavePattern(parentComponent);
         }
      };
      saveAction.setToolTipText("Save Current Pattern…");
      JButton bOpen = new JButton(openAction);
      bOpen.setText("Open");
      bOpen.setMargin(new java.awt.Insets(2, 4, 2, 4));
      JButton bSave = new JButton(saveAction);
      bSave.setText("Save");
      bSave.setMargin(new java.awt.Insets(2, 4, 2, 4));
      JPanel buttonRow = new JPanel(new GridDialogLayout(2, false));
      buttonRow.add(bOpen);
      buttonRow.add(bSave);

      JButton bEdit = new JButton(new SmartAction("Edit…") {
         @Override
         protected void execute(Component parentComponent) {
            FillInlineOptionsPanel.this.performEditPattern(parentComponent);
         }
      });
      bEdit.setMargin(new java.awt.Insets(2, 4, 2, 4));

      JPanel card = new JPanel(new GridDialogLayout(1, false));
      card.add(buttonRow);
      card.add(this.patternPreview.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      card.add(bEdit, GridDialogLayoutData.FILL_HORIZONTAL);
      return card;
   }

   private JComponent buildGradientCard(AsciiGradientConfiguration gradientConfiguration) {
      final JComboBox gradientCombo = AsciiGradientComboBoxFactory.createComponent(gradientConfiguration);
      Dimension gp = gradientCombo.getPreferredSize();
      gradientCombo.setPreferredSize(new Dimension(125, gp.height));
      this.options.setGradient(extractGradient(gradientCombo));
      gradientCombo.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            FillInlineOptionsPanel.this.options.setGradient(extractGradient(gradientCombo));
         }
      });

      final JComboBox styleCombo = new JComboBox<>(GradientStyle.values());
      styleCombo.setRenderer(new ObjectUiListCellRenderer(new GradientStyleUi()));
      styleCombo.setSelectedItem(this.options.getGradientStyle());
      Dimension sp = styleCombo.getPreferredSize();
      styleCombo.setPreferredSize(new Dimension(95, sp.height));
      styleCombo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FillInlineOptionsPanel.this.options.setGradientStyle((GradientStyle) styleCombo.getSelectedItem());
         }
      });

      final JCheckBox cbDither = new JCheckBox("Dither", this.options.isDither());
      cbDither.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FillInlineOptionsPanel.this.options.setDither(cbDither.isSelected());
         }
      });

      JPanel styleRow = new JPanel(new GridDialogLayout(2, false));
      styleRow.add(new JLabel("Style:"));
      styleRow.add(styleCombo, GridDialogLayoutData.FILL_HORIZONTAL);

      JPanel card = new JPanel(new GridDialogLayout(1, false));
      card.add(new JLabel("Gradient:"));
      card.add(gradientCombo, GridDialogLayoutData.FILL_HORIZONTAL);
      card.add(styleRow, GridDialogLayoutData.FILL_HORIZONTAL);
      card.add(cbDither);
      return card;
   }

   private static char[] extractGradient(JComboBox combo) {
      String s = (String) combo.getSelectedItem();
      char[] ch = s == null ? new char[0] : s.toCharArray();
      if (ch.length == 0) {
         ch = new char[]{' '};
      }
      return ch;
   }

   private void performChoosePattern(Component parentComponent) {
      PatternDialog dialog = this.getPatternDialog(parentComponent);
      dialog.show();
      if (!dialog.isCanceled()) {
         Pattern selected = dialog.getSelectedPattern();
         this.applyPattern(selected);
         this.applicationPreferences.setFillPatternName(selected.getName());
      }
   }

   private void performSavePattern(final Component parentComponent) {
      String authorName = this.applicationPreferences.getAuthorName();
      final JTextField tfAuthor = new JTextField(authorName, 15);
      final JTextField tfName = new JTextField("", 20);
      final JPanel form = new JPanel(new GridDialogLayout(2, false));
      form.add(new JLabel("Pattern Name:"), GridDialogLayoutData.RIGHT);
      form.add(tfName, GridDialogLayoutData.FILL_HORIZONTAL);
      form.add(new JLabel("Author:"), GridDialogLayoutData.RIGHT);
      form.add(tfAuthor, GridDialogLayoutData.FILL_HORIZONTAL);
      IDialogPage page = new AbstractDialogPage("") {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            return form;
         }

         @Override
         public String getTitle() {
            return "Add as new pattern";
         }
      };
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(page) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      });
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         String name = tfName.getText();
         String author = tfAuthor.getText();
         if (name.trim().length() == 0) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message(JaveMessages.JavE, "The specified name is not a correct name for a pattern.", MessageType.ERROR)
            );
         } else {
            Pattern newPattern = new Pattern(name, this.options.getPattern().getCode(), author);
            PatternDialog dialog = this.getPatternDialog(parentComponent);
            dialog.addPattern(newPattern);
         }
      }
   }

   private void performEditPattern(Component parentComponent) {
      final AsciiTextArea editArea = new AsciiTextArea(
         new Dimension(40, 12), new AsciiTextAreaProperties(this.applicationPreferences.getDisplayFontModel()).setDefaultPopupEnabled(false)
      );
      editArea.setText(TextTools.toString(this.options.getPattern().getContent()));
      editArea.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            // committed on dialog close
         }
      });
      final JPanel form = new JPanel(new GridDialogLayout(1, false));
      form.add(editArea.getContent(), GridDialogLayoutData.FILL_BOTH);
      IDialogPage page = new AbstractDialogPage("") {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            return form;
         }

         @Override
         public String getTitle() {
            return "Edit Pattern";
         }
      };
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(page) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      });
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         Pattern edited = new Pattern(AsciiPacker.encode(TextTools.toCharField(editArea.getText())));
         this.applyPattern(edited);
      }
   }

   private void applyPattern(Pattern pattern) {
      this.options.setPattern(pattern);
      if (this.patternPreview != null) {
         this.patternPreview.setPattern(pattern);
      }
   }

   private PatternDialog getPatternDialog(Component parentComponent) {
      if (this.patternDialog == null) {
         this.patternDialog = new PatternDialog(parentComponent, this.patternList);
         this.patternDialog.getDialog().pack();
         GuiUtilities.centerOnScreen(this.patternDialog.getDialog());
      }
      return this.patternDialog;
   }
}

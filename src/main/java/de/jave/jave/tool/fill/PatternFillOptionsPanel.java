package de.jave.jave.tool.fill;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.jave.JaveMessages;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pattern.Pattern;
import de.jave.jave.pattern.PatternDialog;
import de.jave.jave.pattern.PatternList;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.gui.GuiUtilities;
import de.jave.text.TextTools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.cardlayout.CardPanelKey;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutDirection;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.toolbar.ToolBarBuilder;
import net.disy.commons.swing.toolbar.ToolBarConfiguration;

public class PatternFillOptionsPanel implements IComponentContainer {
   private static final CardPanelKey PREVIEW_KEY = new CardPanelKey("preview");
   private static final CardPanelKey EDIT_KEY = new CardPanelKey("edit");
   private final JaveApplicationPreferences applicationPreferences;
   private final PatternList patternList;
   private final FillOptions options;
   private final JComponent content;
   private final AsciiTextArea editTextArea;
   private final PatternPreviewComponent previewTextArea;
   private PatternDialog patternDialog;

   public PatternFillOptionsPanel(final JaveApplicationPreferences applicationPreferences, PatternList patternList, final FillOptions options) {
      Ensure.ensureArgumentNotNull(applicationPreferences);
      Ensure.ensureArgumentNotNull(patternList);
      Ensure.ensureArgumentNotNull(options);
      this.applicationPreferences = applicationPreferences;
      this.patternList = patternList;
      this.options = options;
      this.previewTextArea = new PatternPreviewComponent(applicationPreferences.getDisplayFontModel());
      this.editTextArea = new AsciiTextArea(
         new Dimension(10, 6), new AsciiTextAreaProperties(applicationPreferences.getDisplayFontModel()).setDefaultPopupEnabled(false)
      );
      this.editTextArea.setText(TextTools.toString(options.getPattern().getContent()));
      this.editTextArea
         .addTextContentListener(
            new ITextContentListener() {
               @Override
               public void textContentChanged() {
                  PatternFillOptionsPanel.this.setPattern(
                     new Pattern(AsciiPacker.encode(TextTools.toCharField(PatternFillOptionsPanel.this.editTextArea.getText())))
                  );
               }
            }
         );
      SmartAction openPatternAction = new SmartAction("Open...", JaveIcons.OPEN_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            PatternFillOptionsPanel.this.performChoosePattern(parentComponent);
         }
      };
      SmartAction savePatternAction = new SmartAction("Save...", JaveIcons.SAVE_EDIT_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            String authorName = applicationPreferences.getAuthorName();
            JTextField tfAuthor = new JTextField(authorName, 15);
            JTextField tfName = new JTextField("", 20);
            final JPanel p = new JPanel(new GridDialogLayout(2, false));
            p.add(new JLabel("Pattern Name:"), GridDialogLayoutData.RIGHT);
            p.add(tfName, GridDialogLayoutData.FILL_HORIZONTAL);
            p.add(new JLabel("Author:"), GridDialogLayoutData.RIGHT);
            p.add(tfAuthor, GridDialogLayoutData.FILL_HORIZONTAL);
            IDialogPage page = new AbstractDialogPage("") {
               @Override
               public IBasicMessage createCurrentMessage() {
                  return this.getDefaultMessage();
               }

               @Override
               public JComponent createContent() {
                  return p;
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
                  Pattern newPattern = new Pattern(name, options.getPattern().getCode(), author);
                  PatternDialog dialog = PatternFillOptionsPanel.this.getPatternDialog(parentComponent);
                  dialog.addPattern(newPattern);
                  PatternFillOptionsPanel.this.showPatternDialog(parentComponent);
               }
            }
         }
      };
      ToolBarConfiguration configuration = new ToolBarConfiguration()
         .setOrientation(LayoutDirection.VERTICAL)
         .setFloatable(false)
         .setRolloverEffectEnabled(false);
      ToolBarBuilder builder = new ToolBarBuilder(configuration);
      builder.add(openPatternAction);
      builder.add(savePatternAction);
      final CardPanel cardPanel = new CardPanel();
      cardPanel.add(this.previewTextArea.getContent(), PREVIEW_KEY);
      cardPanel.add(this.editTextArea.getContent(), EDIT_KEY);
      cardPanel.setSelectedSubPanel(PREVIEW_KEY);
      this.previewTextArea.setToolTipText("Double click to switch between preview and edit");
      this.previewTextArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (!e.isMetaDown()) {
               if (e.getClickCount() == 2) {
                  cardPanel.setSelectedSubPanel(PatternFillOptionsPanel.EDIT_KEY);
                  PatternFillOptionsPanel.this.editTextArea.requestFocus();
               }
            }
         }
      });
      this.editTextArea.setToolTipText("Double click to switch between preview and edit");
      this.editTextArea.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (!e.isMetaDown()) {
               if (e.getClickCount() == 2) {
                  cardPanel.setSelectedSubPanel(PatternFillOptionsPanel.PREVIEW_KEY);
               }
            }
         }
      });
      this.editTextArea.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent e) {
            cardPanel.setSelectedSubPanel(PatternFillOptionsPanel.PREVIEW_KEY);
         }
      });
      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      panel.add(cardPanel.getContent(), "Center");
      panel.add(builder.createToolBar(), "East");
      panel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      this.content = panel;
      this.setPattern(options.getPattern());
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   private void performChoosePattern(Component parentComponent) {
      this.showPatternDialog(parentComponent);
   }

   private void showPatternDialog(Component parentComponent) {
      PatternDialog dialog = this.getPatternDialog(parentComponent);
      dialog.show();
      if (!dialog.isCanceled()) {
         Pattern selectedPattern = dialog.getSelectedPattern();
         this.setPattern(selectedPattern);
         this.applicationPreferences.setFillPatternName(selectedPattern.getName());
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

   private void setPattern(Pattern pattern) {
      this.previewTextArea.setPattern(pattern);
      char[][] decodedPattern = AsciiPacker.decode(pattern.getCode());
      this.editTextArea.setText(TextTools.toString(decodedPattern));
      this.options.setPattern(pattern);
   }
}

package de.jave.image2ascii.algorithm.dialog.banned;

import de.jave.gui.layout.Gap;
import de.jave.jave.JaveGlobalRessources;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class AsciiCharactersSelectDialogPanel {
   private final JComponent content;
   private final JTextField bannedCharactersTextField;
   private final JTextField availableCharactersTextField;
   private final AsciiCharacterSetModel model;

   public AsciiCharactersSelectDialogPanel(String selectedCharacters, Font font) {
      Ensure.ensureArgumentNotNull(selectedCharacters);
      Ensure.ensureArgumentNotNull(font);
      int rowCount = 14;
      int columnCount = (int)Math.ceil(6.785714285714286);
      this.model = new AsciiCharacterSetModel();
      this.model.setSelectedCharacters(selectedCharacters);
      JPanel charactersPanel = new JPanel(
         new GridDialogLayout(columnCount, false, LayoutUtilities.getComponentGroupsSpacing(), LayoutUtilities.getDpiAdjusted(2))
      );

      for (int y = 0; y < 14; y++) {
         for (int x = 0; x < columnCount; x++) {
            char character = (char)(32 + y + x * 14);
            if (character >= 127) {
               charactersPanel.add(new Gap());
            } else {
               AsciiCharactersSelectComponent checkBox = new AsciiCharactersSelectComponent(this.model, character, font);
               JComponent component = checkBox.getComponent();
               charactersPanel.add(component, GridDialogLayoutData.FILL_BOTH);
            }
         }
      }

      JPanel bottomPanel = new JPanel(new GridDialogLayout(2, false));
      bottomPanel.add(new JLabel("Banned Characters:"), GridDialogLayoutData.RIGHT);
      this.bannedCharactersTextField = new JTextField(this.model.getSelectedCharacters());
      this.bannedCharactersTextField.setEditable(false);
      this.bannedCharactersTextField.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      bottomPanel.add(this.bannedCharactersTextField, GridDialogLayoutData.FILL_HORIZONTAL);
      bottomPanel.add(new JLabel("Available Characters:"), GridDialogLayoutData.RIGHT);
      this.availableCharactersTextField = new JTextField(this.model.getNotSelectedCharacters());
      this.availableCharactersTextField.setEditable(false);
      this.availableCharactersTextField.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      bottomPanel.add(this.availableCharactersTextField, GridDialogLayoutData.FILL_HORIZONTAL);
      ButtonPanelBuilder builder = new ButtonPanelBuilder();
      builder.add(new SmartAction("&Select All") {
         @Override
         protected void execute(Component parentComponent) {
            AsciiCharactersSelectDialogPanel.this.model.selectAll();
         }
      });
      builder.add(new SmartAction("&Unselect All") {
         @Override
         protected void execute(Component parentComponent) {
            AsciiCharactersSelectDialogPanel.this.model.clear();
         }
      });
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalSpan(2);
      bottomPanel.add(builder.createPanel(), data);
      JPanel mainPanel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentGroupsSpacing()));
      mainPanel.add(charactersPanel, "Center");
      mainPanel.add(bottomPanel, "South");
      this.content = mainPanel;
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiCharactersSelectDialogPanel.this.updateView();
         }
      });
      this.updateView();
   }

   private void updateView() {
      if (!AsciiCharacterSetModel.isEquivalent(this.model.getSelectedCharacters(), this.bannedCharactersTextField.getText())) {
         this.bannedCharactersTextField.setText(this.model.getSelectedCharacters());
      }

      if (!AsciiCharacterSetModel.isEquivalent(this.model.getNotSelectedCharacters(), this.availableCharactersTextField.getText())) {
         this.availableCharactersTextField.setText(this.model.getNotSelectedCharacters());
      }
   }

   public JComponent getContent() {
      return this.content;
   }

   public String getSelectedCharacters() {
      return this.model.getSelectedCharacters();
   }
}

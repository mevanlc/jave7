package net.disy.commons.swing.text.alignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.text.TextAlignment;
import net.disy.commons.swing.model.TextAlignmentModel;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;
import net.disy.commons.swing.text.TextActionResources;

public class TextAlignmentPanel {
   private static final Map<TextAlignment, Icon> ICONS = new HashMap<TextAlignment, Icon>() {
      {
         this.put(TextAlignment.LEFT, TextActionResources.ALIGN_LEFT);
         this.put(TextAlignment.CENTER, TextActionResources.ALIGN_CENTER);
         this.put(TextAlignment.RIGHT, TextActionResources.ALIGN_RIGHT);
      }
   };
   private static final Map<TextAlignment, String> LABELS = new HashMap<TextAlignment, String>() {
      {
         this.put(TextAlignment.LEFT, DisyCommonsSwingMessages.getString("TextAlignmentPanel.Left"));
         this.put(TextAlignment.CENTER, DisyCommonsSwingMessages.getString("TextAlignmentPanel.Center"));
         this.put(TextAlignment.RIGHT, DisyCommonsSwingMessages.getString("TextAlignmentPanel.Right"));
      }
   };
   private final ButtonGroup buttonGroup = new ButtonGroup();
   private JComponent[] content;
   private final TextAlignmentModel alignmentModel;

   private JToggleButton createToggleButton(final TextAlignment textAlignment) {
      Icon icon = ICONS.get(textAlignment);
      String toolTip = LABELS.get(textAlignment);
      final JToggleButton toggleButton = new JToggleButton(toolTip, icon);
      toggleButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            TextAlignmentPanel.this.alignmentModel.setTextAlignment(textAlignment);
         }
      });
      this.alignmentModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (TextAlignmentPanel.this.alignmentModel.getTextAlignment() == textAlignment) {
               toggleButton.setSelected(true);
            }
         }
      });
      this.buttonGroup.add(toggleButton);
      toggleButton.setSelected(this.alignmentModel.getTextAlignment() == textAlignment);
      return toggleButton;
   }

   public TextAlignmentPanel(TextAlignmentModel alignmentModel) {
      this.alignmentModel = alignmentModel;
   }

   public JComponent[] getComponents() {
      if (this.content == null) {
         this.content = this.createContent();
      }

      return this.content;
   }

   private JComponent[] createContent() {
      return new JComponent[]{
         this.createToggleButton(TextAlignment.LEFT), this.createToggleButton(TextAlignment.CENTER), this.createToggleButton(TextAlignment.RIGHT)
      };
   }
}

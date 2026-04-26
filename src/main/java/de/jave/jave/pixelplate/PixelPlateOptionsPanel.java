package de.jave.jave.pixelplate;

import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.JaveMessages;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.button.ButtonGroupLinker;
import net.disy.commons.swing.button.RolloverButtonFactory;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.component.VerticalLine;
import net.disy.commons.swing.fontchooser.util.FontUtilities;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.cardlayout.CardPanelKey;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class PixelPlateOptionsPanel extends JPanel {
   private static final CardPanelKey FELTPENSTYLE_KEY = new CardPanelKey();
   private static final CardPanelKey LINESTYLE_KEY = new CardPanelKey();
   private final ObjectModel<PencilSize> sizeModel;
   private final ObjectModel<LineStyle> lineStyleModel;
   private final ObjectModel<Character> feltPenStyleModel;

   public PixelPlateOptionsPanel(PixelPlateModel model, BooleanModel mixCharactersModel) {
      this.sizeModel = model.getSizeModel();
      this.lineStyleModel = model.getLineStyleModel();
      this.feltPenStyleModel = model.getFeltPenStyleModel();
      JToggleButton bs0 = this.createToggleButton(JaveIcons.PENCIL1, JaveIcons.PENCIL1_DISABLED);
      JToggleButton bs1 = this.createToggleButton(JaveIcons.PENCIL4, JaveIcons.PENCIL4_DISABLED);
      JToggleButton bs2 = this.createToggleButton(JaveIcons.PENCIL5, JaveIcons.PENCIL5_DISABLED);
      JToggleButton bs3 = this.createToggleButton(JaveIcons.PENCIL6, JaveIcons.PENCIL6_DISABLED);
      JToggleButton bs4 = this.createToggleButton(JaveIcons.PENCIL7, JaveIcons.PENCIL7_DISABLED);
      ButtonGroupLinker<PencilSize> sizeButtonGroupLinker = new ButtonGroupLinker<>(this.sizeModel);
      sizeButtonGroupLinker.addButton(bs0, PencilSize.THIN);
      sizeButtonGroupLinker.addButton(bs1, PencilSize.THICK1);
      sizeButtonGroupLinker.addButton(bs2, PencilSize.THICK2);
      sizeButtonGroupLinker.addButton(bs3, PencilSize.THICK3);
      sizeButtonGroupLinker.addButton(bs4, PencilSize.THICK4);
      JPanel sizePanel = new JPanel(new FlowLayout(1, 0, 2));
      sizePanel.add(bs0);
      sizePanel.add(new VerticalLine());
      sizePanel.add(bs1);
      sizePanel.add(bs2);
      sizePanel.add(bs3);
      sizePanel.add(bs4);
      ButtonGroupLinker<Character> styleButtonGroupLinker = new ButtonGroupLinker<>(this.feltPenStyleModel);
      JPanel feltpenStylePanel = new JPanel(new FlowLayout(1, 0, 2));

      for (int i = 0; i < PixelPlate.FELTPEN_CHARS.length; i++) {
         char character = PixelPlate.FELTPEN_CHARS[i];
         JToggleButton toggleButton = createStyleToggleButton(character);
         styleButtonGroupLinker.addButton(toggleButton, character);
         feltpenStylePanel.add(toggleButton);
      }

      JToggleButton bl0 = new JRadioButton(JaveMessages.Tool_Generic_LineStyle_Lines);
      JToggleButton bl1 = new JRadioButton(JaveMessages.Tool_Generic_LineStyle_Dots);
      ButtonGroupLinker<LineStyle> lineStyleButtonGroupLinker = new ButtonGroupLinker<>(this.lineStyleModel);
      lineStyleButtonGroupLinker.addButton(bl0, LineStyle.LINE);
      lineStyleButtonGroupLinker.addButton(bl1, LineStyle.DOT);
      JPanel lineStylePanel = new JPanel(new FlowLayout(1, 0, 2));
      lineStylePanel.add(bl0);
      lineStylePanel.add(bl1);
      final CardPanel cardPanel = new CardPanel();
      cardPanel.add(lineStylePanel, LINESTYLE_KEY);
      cardPanel.add(feltpenStylePanel, FELTPENSTYLE_KEY);
      this.setLayout(new GridDialogLayout(1, false));
      this.add(sizePanel, GridDialogLayoutData.FILL_HORIZONTAL);
      this.add(cardPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.add(new MergeCharactersPanel(mixCharactersModel).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.sizeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            PixelPlateOptionsPanel.this.updateCardPanel(cardPanel);
         }
      });
      this.updateCardPanel(cardPanel);
   }

   private void updateCardPanel(CardPanel cardPanel) {
      if (this.sizeModel.getValue() == PencilSize.THIN) {
         cardPanel.setSelectedSubPanel(LINESTYLE_KEY);
      } else {
         cardPanel.setSelectedSubPanel(FELTPENSTYLE_KEY);
      }
   }

   private static JToggleButton createStyleToggleButton(char character) {
      Icon icon = createCharacterIcon(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH, character);
      JToggleButton button = RolloverButtonFactory.createToggleButton(new SmartAction(icon) {
         @Override
         protected void execute(Component parentComponent) {
         }
      });
      button.setPreferredSize(new Dimension(16, 19));
      button.setMargin(new Insets(0, 0, 0, 0));
      return button;
   }

   private static Icon createCharacterIcon(final Font font, final char character) {
      return new Icon() {
         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(SwingColors.getTextAreaForegroundColor());
            g.setFont(font);
            FontMetrics fontMetrics = g.getFontMetrics();
            String text = String.valueOf(character);
            int stringWidth = fontMetrics.stringWidth(text);
            int dx = (this.getIconWidth() - stringWidth) / 2;
            g.drawString(text, x + dx, y + fontMetrics.getAscent());
         }

         @Override
         public int getIconWidth() {
            Rectangle2D bounds = font.getMaxCharBounds(FontUtilities.DEFAULT_FONT_RENDER_CONTEXT);
            return (int)Math.ceil(bounds.getWidth());
         }

         @Override
         public int getIconHeight() {
            Rectangle2D bounds = font.getMaxCharBounds(FontUtilities.DEFAULT_FONT_RENDER_CONTEXT);
            return (int)Math.ceil(bounds.getHeight());
         }
      };
   }

   private JToggleButton createToggleButton(Icon icon, Icon disabledIcon) {
      SmartAction action = new SmartAction() {
         @Override
         protected void execute(Component parentComponent) {
         }
      };
      action.setIcon(icon);
      JToggleButton button = RolloverButtonFactory.createToggleButton(action);
      button.setDisabledIcon(disabledIcon);
      return button;
   }

}

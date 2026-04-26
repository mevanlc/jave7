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
import javax.swing.JComponent;
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

/**
 * View component for {@link PixelPlateModel}: pencil size selector,
 * card-switched line-style/feltpen-char selector, and mix-characters
 * checkbox. One instance per generic tool — six instances at runtime,
 * all bound to the same {@link PixelPlateModel}, so the user's
 * selection survives tool switches.
 *
 * <p>Replaces the JPanel-extending {@code PixelPlateOptionsPanel}
 * (which is removed when the generic tools migrate to inline options
 * in PHASE1 unit δ).
 */
public final class PixelPlateOptionsView {
   private static final CardPanelKey FELTPENSTYLE_KEY = new CardPanelKey();
   private static final CardPanelKey LINESTYLE_KEY = new CardPanelKey();
   private final ObjectModel<PencilSize> sizeModel;
   private final JComponent content;

   public PixelPlateOptionsView(PixelPlateModel model, BooleanModel mixCharactersModel) {
      this.sizeModel = model.getSizeModel();
      ObjectModel<LineStyle> lineStyleModel = model.getLineStyleModel();
      ObjectModel<Character> feltPenStyleModel = model.getFeltPenStyleModel();

      JToggleButton bs0 = createPencilButton(JaveIcons.PENCIL1, JaveIcons.PENCIL1_DISABLED);
      JToggleButton bs1 = createPencilButton(JaveIcons.PENCIL4, JaveIcons.PENCIL4_DISABLED);
      JToggleButton bs2 = createPencilButton(JaveIcons.PENCIL5, JaveIcons.PENCIL5_DISABLED);
      JToggleButton bs3 = createPencilButton(JaveIcons.PENCIL6, JaveIcons.PENCIL6_DISABLED);
      JToggleButton bs4 = createPencilButton(JaveIcons.PENCIL7, JaveIcons.PENCIL7_DISABLED);
      ButtonGroupLinker<PencilSize> sizeLinker = new ButtonGroupLinker<>(this.sizeModel);
      sizeLinker.addButton(bs0, PencilSize.THIN);
      sizeLinker.addButton(bs1, PencilSize.THICK1);
      sizeLinker.addButton(bs2, PencilSize.THICK2);
      sizeLinker.addButton(bs3, PencilSize.THICK3);
      sizeLinker.addButton(bs4, PencilSize.THICK4);
      JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
      sizePanel.add(bs0);
      sizePanel.add(new VerticalLine());
      sizePanel.add(bs1);
      sizePanel.add(bs2);
      sizePanel.add(bs3);
      sizePanel.add(bs4);

      JPanel feltpenStylePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
      ButtonGroupLinker<Character> styleLinker = new ButtonGroupLinker<>(feltPenStyleModel);
      for (char character : PixelPlate.FELTPEN_CHARS) {
         JToggleButton button = createCharacterButton(character);
         styleLinker.addButton(button, character);
         feltpenStylePanel.add(button);
      }

      JToggleButton bl0 = new JRadioButton(JaveMessages.Tool_Generic_LineStyle_Lines);
      JToggleButton bl1 = new JRadioButton(JaveMessages.Tool_Generic_LineStyle_Dots);
      ButtonGroupLinker<LineStyle> lineStyleLinker = new ButtonGroupLinker<>(lineStyleModel);
      lineStyleLinker.addButton(bl0, LineStyle.LINE);
      lineStyleLinker.addButton(bl1, LineStyle.DOT);
      JPanel lineStylePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
      lineStylePanel.add(bl0);
      lineStylePanel.add(bl1);

      final CardPanel cardPanel = new CardPanel();
      cardPanel.add(lineStylePanel, LINESTYLE_KEY);
      cardPanel.add(feltpenStylePanel, FELTPENSTYLE_KEY);

      JPanel root = new JPanel(new GridDialogLayout(1, false));
      root.add(sizePanel, GridDialogLayoutData.FILL_HORIZONTAL);
      root.add(cardPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      root.add(new MergeCharactersPanel(mixCharactersModel).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);

      this.sizeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            updateCardPanel(cardPanel);
         }
      });
      updateCardPanel(cardPanel);
      this.content = root;
   }

   public JComponent getContent() {
      return this.content;
   }

   private void updateCardPanel(CardPanel cardPanel) {
      if (this.sizeModel.getValue() == PencilSize.THIN) {
         cardPanel.setSelectedSubPanel(LINESTYLE_KEY);
      } else {
         cardPanel.setSelectedSubPanel(FELTPENSTYLE_KEY);
      }
   }

   private static JToggleButton createPencilButton(Icon icon, Icon disabledIcon) {
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

   private static JToggleButton createCharacterButton(char character) {
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
            FontMetrics fm = g.getFontMetrics();
            String text = String.valueOf(character);
            int dx = (this.getIconWidth() - fm.stringWidth(text)) / 2;
            g.drawString(text, x + dx, y + fm.getAscent());
         }

         @Override
         public int getIconWidth() {
            Rectangle2D bounds = font.getMaxCharBounds(FontUtilities.DEFAULT_FONT_RENDER_CONTEXT);
            return (int) Math.ceil(bounds.getWidth());
         }

         @Override
         public int getIconHeight() {
            Rectangle2D bounds = font.getMaxCharBounds(FontUtilities.DEFAULT_FONT_RENDER_CONTEXT);
            return (int) Math.ceil(bounds.getHeight());
         }
      };
   }
}

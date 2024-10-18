package de.jave.jave.algorithm.steganogram;

import de.jave.gui.layout.Gap;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmGradientOptionsPanel;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.lib.CharacterPlate;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class GradientSteganogramPage extends AbstractDialogPage {
   private final CharacterPlate plate;
   private final JTextField tfGradient;
   private final JTextField tfDecoded;

   public GradientSteganogramPage(CharacterPlate plate, AsciiGradientConfiguration gradientConfiguration) {
      super("Please enter a key to decode the current document with. Note that the current document must contain an ASCII art image with steganogram.");
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.plate = plate;
      this.tfGradient = new JTextField(gradientConfiguration.getDefaultGradient(), 27);
      this.tfDecoded = new JTextField("", 27);
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getTitle() {
      return "Steganogram Decoder";
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      this.tfGradient.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.tfDecoded.setEditable(false);
      panel.add(new JLabel("Key (gradient):"), GridDialogLayoutData.RIGHT);
      panel.add(this.tfGradient);
      panel.add(Image2AsciiAlgorithmGradientOptionsPanel.createSteganographyHelpButton());
      GridDialogLayoutData lineData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      lineData.setHorizontalSpan(3);
      panel.add(new Gap(0, LayoutUtilities.getComponentGroupsSpacing()), lineData);
      panel.add(new JLabel("Decoded message:"), GridDialogLayoutData.RIGHT);
      panel.add(this.tfDecoded);
      this.tfGradient.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            GradientSteganogramPage.this.doDecode();
         }
      });
      this.doDecode();
      return panel;
   }

   private void doDecode() {
      this.tfDecoded.setText(GradientSteganogramDecoder.decode(this.plate, this.tfGradient.getText()));
   }
}

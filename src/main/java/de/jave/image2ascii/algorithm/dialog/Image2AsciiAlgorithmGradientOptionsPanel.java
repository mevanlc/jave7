package de.jave.image2ascii.algorithm.dialog;

import de.jave.gui.dialog.HtmlMessage;
import de.jave.gui.dialog.HtmlMessageDialogFactory;
import de.jave.image2ascii.algorithm.AlgorithmGradientOptionsModel;
import de.jave.jave.AsciiGradientComboBoxFactory;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.component.IDisposableComponentContainer;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.toolbar.ToolBarUtilities;
import net.disy.commons.swing.util.ToggleComponentEnabler;

public class Image2AsciiAlgorithmGradientOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmGradientOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgorithmGradientOptionsPanel(final AlgorithmGradientOptionsModel optionsModel, AsciiGradientConfiguration gradientConfiguration) {
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.optionsModel = optionsModel;
      final JComboBox tfGradient = AsciiGradientComboBoxFactory.createComponent(gradientConfiguration);
      tfGradient.setSelectedItem(optionsModel.getGradient());
      final JCheckBox cbStegano = new JCheckBox("Steganography:", optionsModel.isSteganogram());
      cbStegano.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setSteganogram(cbStegano.isSelected());
         }
      });
      final JTextField tfStegano = new JTextField(optionsModel.getSteganogramText(), 15);
      DocumentListener documentListener = new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            optionsModel.setSteganogramText(tfStegano.getText());
         }
      };
      tfStegano.getDocument().addDocumentListener(documentListener);
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      panel.add(new JLabel("Gradient:"), GridDialogLayoutData.RIGHT);
      GridDialogLayoutData twoColumnsData = new GridDialogLayoutData();
      twoColumnsData.setHorizontalSpan(2);
      panel.add(tfGradient, twoColumnsData);
      panel.add(cbStegano, GridDialogLayoutData.RIGHT);
      panel.add(tfStegano, GridDialogLayoutData.FILL_HORIZONTAL);
      GridDialogLayoutData buttonData = new GridDialogLayoutData();
      buttonData.setHorizontalAlignment(GridAlignment.BEGINNING);
      panel.add(createSteganographyHelpButton(), buttonData);
      tfGradient.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setGradient((String)tfGradient.getSelectedItem());
         }
      });
      ToggleComponentEnabler.connect(cbStegano, tfStegano);
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            tfGradient.setSelectedItem(optionsModel.getGradient());
            cbStegano.setSelected(optionsModel.isSteganogram());
            if (!tfStegano.getText().equals(optionsModel.getSteganogramText())) {
               tfStegano.setText(optionsModel.getSteganogramText());
            }
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void dispose() {
      this.optionsModel.removeChangeListener(this.optionsModelChangeListener);
   }

   public static JComponent createSteganographyHelpButton() {
      SmartAction action = new SmartAction(JaveIcons.HELP_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            HtmlMessageDialogFactory.showMessageDialog(
               parentComponent,
               new HtmlMessage(
                  "Steganography",
                  "<html><h1>Steganography in JavE</h1>With JavE's steganography feature you can embed secret messages in Ascii art images.<h2>1. Embedding the Message</h2>In the image2ascii converter (menu 'Tools', 'Image2Ascii Converter') you must chose the 'Gradient' converting algorithm.There you can enter a short text to be embedded in the converted image.The encoding key is the chosen gradient.<h2>2. Decoding a Steganogram</h2>Open the Steganogram document in JavE and open the Steganogram Decoder (menu 'Special', 'Decode Steganogram').When the correct key is entered, the steganogram is decoded automatically and you can see the embedded message.<h2>Hints</h2>For the steganogram to provide a certain level of security, make sure not to use the default gradients provided by the software. A good idea is to makesome modifications, e.g. by swapping or adding characters (e.g. <code> .:oO8@M</code>). Note that duplicate characters in the gradient will not work.</html>",
                  MessageType.INFORMATION
               )
            );
         }
      };
      action.setToolTipText("Show help for this feature");
      return ToolBarUtilities.createToolBarButton(action);
   }
}

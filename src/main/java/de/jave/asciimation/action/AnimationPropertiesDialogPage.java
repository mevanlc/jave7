package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.dialog.color.ColorChooserButton;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public final class AnimationPropertiesDialogPage extends AbstractDialogPage {
   private final AnimationEditorModel model;
   private JTextField tfSoftware;
   private JTextField tfEMail;
   private JTextField tfAuthor;
   private JTextField tfDate;
   private JTextField tfTitle;
   private ColorModel foregroundColorModel;
   private ColorModel backgroundColorModel;
   private SpinnerNumberModel durationModel;

   public AnimationPropertiesDialogPage(AnimationEditorModel model) {
      super("Adjust the properties of the animation.");
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   public String getTitle() {
      return "Animation Properties";
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public JComponent createContent() {
      JPanel metaDataPanel = new JPanel(new GridDialogLayout(2, false));
      metaDataPanel.add(new JLabel("Title:"), GridDialogLayoutData.RIGHT);
      this.tfTitle = new JTextField(22);
      metaDataPanel.add(this.tfTitle);
      metaDataPanel.add(new JLabel("Date:"), GridDialogLayoutData.RIGHT);
      this.tfDate = new JTextField(22);
      metaDataPanel.add(this.tfDate);
      metaDataPanel.add(new JLabel("Author:"), GridDialogLayoutData.RIGHT);
      this.tfAuthor = new JTextField(22);
      metaDataPanel.add(this.tfAuthor);
      metaDataPanel.add(new JLabel("EMail:"), GridDialogLayoutData.RIGHT);
      this.tfEMail = new JTextField(22);
      metaDataPanel.add(this.tfEMail);
      metaDataPanel.add(new JLabel("Software:"), GridDialogLayoutData.RIGHT);
      this.tfSoftware = new JTextField(22);
      metaDataPanel.add(this.tfSoftware);
      metaDataPanel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      AnimationProperties properties = this.model.getAnimationFile().getProperties();
      this.foregroundColorModel = new ColorModel(properties.getForegroundColor());
      this.backgroundColorModel = new ColorModel(properties.getBackgroundColor());
      this.durationModel = new SpinnerNumberModel(properties.getFrameDuration(), 0, 60000, 1);
      JPanel propertiesPanel = new JPanel(new GridDialogLayout(2, false));
      propertiesPanel.add(new JLabel("Foreground:"), GridDialogLayoutData.RIGHT);
      propertiesPanel.add(new ColorChooserButton(this.foregroundColorModel).getContent());
      propertiesPanel.add(new JLabel("Background:"), GridDialogLayoutData.RIGHT);
      propertiesPanel.add(new ColorChooserButton(this.backgroundColorModel).getContent());
      propertiesPanel.add(new JLabel("Frame duration (ms):"), GridDialogLayoutData.RIGHT);
      propertiesPanel.add(new JSpinner(this.durationModel));
      propertiesPanel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      JTabbedPane pane = new JTabbedPane();
      pane.addTab("Properties", propertiesPanel);
      pane.addTab("Metadata", metaDataPanel);
      AnimationMetaData metaData = this.model.getAnimationFile().getMetaData();
      this.tfTitle.setText(metaData.getTitle());
      this.tfDate.setText(metaData.getDate());
      this.tfAuthor.setText(metaData.getAuthorName());
      this.tfEMail.setText(metaData.getAuthorEmail());
      this.tfSoftware.setText(metaData.getSoftware());
      return pane;
   }

   public void applySettings() {
      AnimationMetaData metaData = new AnimationMetaData();
      metaData.setAuthorEmail(this.tfEMail.getText());
      metaData.setAuthorName(this.tfAuthor.getText());
      metaData.setTitle(this.tfTitle.getText());
      metaData.setDate(this.tfDate.getText());
      metaData.setSoftware(this.tfSoftware.getText());
      this.model.setMetaData(metaData);
      AnimationProperties properties = new AnimationProperties();
      properties.setForegroundColor(this.foregroundColorModel.getColor());
      properties.setBackgroundColor(this.backgroundColorModel.getColor());
      properties.setFrameDuration(this.durationModel.getNumber().intValue());
      this.model.setProperties(properties);
   }
}

package de.jave.jave.figlet.export;

import de.jave.gui.layout.CenterLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletExportWizardPage2 extends AbstractFigletExportWizardPage {
   private final SpinnerNumberModel ifWidth = new SpinnerNumberModel(8, 1, 30, 1);
   private final SpinnerNumberModel ifHeight = new SpinnerNumberModel(5, 1, 30, 1);
   private final SpinnerNumberModel ifDescent = new SpinnerNumberModel(1, 0, 20, 1);
   private final SpinnerNumberModel ifHSpacing = new SpinnerNumberModel(0, 0, 20, 1);
   private final SpinnerNumberModel ifVSpacing = new SpinnerNumberModel(0, 0, 20, 1);
   private JSpinner widthSpinner;

   public FigletExportWizardPage2(FigletExportModel model) {
      super(
         model,
         "Character Size",
         "Adjust the dimensions of the FIGlet characters you have created. You can verify the settings by looking at the colored markers in the main editor window."
      );
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FigletExportWizardPage2.this.updateView();
         }
      });
      this.updateView();
      this.ifWidth.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FigletExportWizardPage2.this.getModel().setCharacterWidth(FigletExportWizardPage2.this.ifWidth.getNumber().intValue());
         }
      });
      this.ifHeight.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FigletExportWizardPage2.this.getModel().setCharacterHeight(FigletExportWizardPage2.this.ifHeight.getNumber().intValue());
         }
      });
      this.ifDescent.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FigletExportWizardPage2.this.getModel().setCharacterDescent(FigletExportWizardPage2.this.ifDescent.getNumber().intValue());
         }
      });
      this.ifHSpacing.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FigletExportWizardPage2.this.getModel().setHorizontalSpacing(FigletExportWizardPage2.this.ifHSpacing.getNumber().intValue());
         }
      });
      this.ifVSpacing.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FigletExportWizardPage2.this.getModel().setVerticalSpacing(FigletExportWizardPage2.this.ifVSpacing.getNumber().intValue());
         }
      });
   }

   private void updateView() {
      this.ifWidth.setValue(this.getModel().getCharacterWidth());
      this.ifHeight.setValue(this.getModel().getCharacterHeight());
      this.ifDescent.setValue(this.getModel().getCharacterDescent());
      this.ifHSpacing.setValue(this.getModel().getHorizontalSpacing());
      this.ifVSpacing.setValue(this.getModel().getVerticalSpacing());
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      JPanel p1 = new JPanel(new GridDialogLayout(2, false));
      p1.add(new JLabel("Width:"), GridDialogLayoutData.RIGHT);
      this.widthSpinner = new JSpinner(this.ifWidth);
      p1.add(this.widthSpinner);
      p1.add(new JLabel("Height:"), GridDialogLayoutData.RIGHT);
      p1.add(new JSpinner(this.ifHeight));
      p1.add(new JLabel("Descent:"), GridDialogLayoutData.RIGHT);
      p1.add(new JSpinner(this.ifDescent));
      p1.add(new JLabel("Horizontal spacing:"), GridDialogLayoutData.RIGHT);
      p1.add(new JSpinner(this.ifHSpacing));
      p1.add(new JLabel("Vertical spacing:"), GridDialogLayoutData.RIGHT);
      p1.add(new JSpinner(this.ifVSpacing));
      JPanel panel = new JPanel();
      panel.setLayout(new CenterLayout());
      panel.add(p1);
      return panel;
   }

   @Override
   public boolean canFinish() {
      return this.getModel().canFinish();
   }

   @Override
   public void requestFocus() {
      this.widthSpinner.requestFocus();
   }
}

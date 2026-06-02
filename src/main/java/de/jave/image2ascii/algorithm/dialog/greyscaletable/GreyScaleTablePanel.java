package de.jave.image2ascii.algorithm.dialog.greyscaletable;

import de.jave.ascii.font.ChooseDisplayFontAction;
import de.jave.image2ascii.AsciiGreyScaleTableItem;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.grid.GridAlignment;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;
import net.dizzy.commons.swing.util.ToggleComponentEnabler;

public class GreyScaleTablePanel implements IDialogComponent {
   private final JComboBox chTable;
   private final JRadioButton radioButtonDynamical;
   private final JRadioButton radioButtonPredefined;
   private final FontModel fontModel;
   private final GreyScaleTableSelectionModel model;
   private final IChangeListener modelChangeListener;

   public GreyScaleTablePanel(final GreyScaleTableSelectionModel model, FontModel fontModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(fontModel);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.model = model;
      this.fontModel = fontModel;
      this.chTable = createFontTableChoiceComponent(greyscaleTableConfiguration);
      this.chTable.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            model.setGreyScaleTable(((AsciiGreyScaleTableItem)GreyScaleTablePanel.this.chTable.getSelectedItem()).getGreyscaleTable());
         }
      });
      this.radioButtonDynamical = new JRadioButton("Automatic from current font");
      this.radioButtonDynamical.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setAutomaticFromFont(true);
         }
      });
      this.radioButtonPredefined = new JRadioButton("Predefined:");
      this.radioButtonPredefined.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setAutomaticFromFont(false);
         }
      });
      ButtonGroup group = new ButtonGroup();
      group.add(this.radioButtonDynamical);
      group.add(this.radioButtonPredefined);
      this.modelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            GreyScaleTablePanel.this.updateView();
         }
      };
      model.addChangeListener(this.modelChangeListener);
      this.updateView();
   }

   private void updateView() {
      this.radioButtonDynamical.setSelected(this.model.isAutomaticFromFont());
      this.radioButtonPredefined.setSelected(!this.model.isAutomaticFromFont());

      for (int i = 0; i < this.chTable.getModel().getSize(); i++) {
         AsciiGreyScaleTableItem item = (AsciiGreyScaleTableItem)this.chTable.getModel().getElementAt(i);
         if (item.getGreyscaleTable() == this.model.getGreyscaleTable()) {
            this.chTable.setSelectedIndex(i);
            break;
         }
      }
   }

   public GreyScaleTableSelectionModel getModel() {
      return this.model;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      JButton displayFontButton = new JButton(new ChooseDisplayFontAction(this.fontModel));
      displayFontButton.setText("Adjust display font...");
      JPanel panel1 = new JPanel(new GridDialogLayout(2, false));
      panel1.add(this.radioButtonPredefined);
      panel1.add(this.chTable);
      JPanel panel2 = new JPanel(new GridDialogLayout(2, false));
      panel2.add(this.radioButtonDynamical);
      panel2.add(displayFontButton);
      JPanel rightPanel = new JPanel(new GridDialogLayout(1, false));
      rightPanel.add(panel1);
      rightPanel.add(panel2);
      ToggleComponentEnabler.connect(this.radioButtonPredefined, this.chTable);
      ToggleComponentEnabler.connect(this.radioButtonDynamical, displayFontButton);
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setHorizontalSpan(columnCount - 1);
      GridDialogLayoutData labelLayoutData = new GridDialogLayoutData(GridDialogLayoutData.RIGHT);
      labelLayoutData.setVerticalAlignment(GridAlignment.BEGINNING);
      panel.add(new JLabel("Character greyscale table:"), labelLayoutData);
      panel.add(rightPanel, layoutData);
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   private static JComboBox createFontTableChoiceComponent(AsciiGreyscaleTableConfiguration configuration) {
      JComboBox comboBox = new JComboBox<>(configuration.getTableItems());
      comboBox.setSelectedItem(configuration.getDefaultTableItem());
      comboBox.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<AsciiGreyScaleTableItem>() {
         public String getLabel(AsciiGreyScaleTableItem value) {
            return value.getName();
         }
      }));
      return comboBox;
   }

   public void dispose() {
      this.model.removeChangeListener(this.modelChangeListener);
   }
}

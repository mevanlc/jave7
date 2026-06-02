package de.jave.jave.actions.export;

import de.jave.lib.CharacterPlate;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.input.select.ISomeOutOfManyDialogPanelConfiguration;
import net.dizzy.commons.swing.dialog.input.select.RadioButtonPanel;
import net.dizzy.commons.swing.dialog.input.select.SelectSomeOutOfManyDialogPanel;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.list.ListSelectionMode;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.IObjectUi;
import net.dizzy.commons.swing.widgets.HorizontalLine;

public final class TextExportDialogPage extends AbstractDialogPage {
   private SelectSomeOutOfManyDialogPanel<ITextExportFormat> formatPanel;
   private final CharacterPlate plate;
   private final TextExportDialogModel model;

   public TextExportDialogPage(CharacterPlate plate, TextExportDialogModel model) {
      super("Please select the format for exporting the current text.");
      Ensure.ensureArgumentNotNull(plate);
      Ensure.ensureArgumentNotNull(model);
      this.plate = plate;
      this.model = model;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      IBasicMessage message = this.formatPanel.createOptionalCurrentMessage();
      return message == null ? this.getDefaultMessage() : message;
   }

   @Override
   public JComponent createContent() {
      final FixedOptionsObjectSelectionModel<ITextExportFormat> formatSelectionModel = this.model.getFormatSelectionModel();
      this.formatPanel = new SelectSomeOutOfManyDialogPanel<>(formatSelectionModel, new ISomeOutOfManyDialogPanelConfiguration<ITextExportFormat>() {
         @Override
         public IObjectUi<ITextExportFormat> getObjectUi() {
            return new AbstractObjectUi<ITextExportFormat>() {
               public String getLabel(ITextExportFormat value) {
                  return value.getName();
               }

               public Icon getIcon(ITextExportFormat value) {
                  return value.getIcon();
               }
            };
         }

         public ITextExportFormat[] getItems() {
            return formatSelectionModel.getAllValues();
         }

         @Override
         public String getNoItemSelectedErrorMessageText() {
            return "There is no output format selected. Please select the format for exporting the current text.";
         }

         @Override
         public ListSelectionMode getListSelectionMode() {
            return ListSelectionMode.SINGLE_SELECTION;
         }

         @Override
         public String getLabel() {
            return "&Format:";
         }
      });
      this.formatPanel.addChangeListener(this.getCheckInputValidListener());
      final ObjectModel<JComponent> previewComponentModel = new ObjectModel<>();
      formatSelectionModel.addChangeListener(
         new IChangeListener() {
            @Override
            public void stateChanged() {
               TextExportDialogPage.this.updatePreviewComponent(
                  previewComponentModel, formatSelectionModel.getFirstSelectedValue(), TextExportDialogPage.this.model.getOptionsModel()
               );
            }
         }
      );
      this.model
         .getOptionsModel()
         .addChangeListener(
            new IChangeListener() {
               @Override
               public void stateChanged() {
                  TextExportDialogPage.this.updatePreviewComponent(
                     previewComponentModel, formatSelectionModel.getFirstSelectedValue(), TextExportDialogPage.this.model.getOptionsModel()
                  );
               }
            }
         );
      this.updatePreviewComponent(previewComponentModel, formatSelectionModel.getFirstSelectedValue(), this.model.getOptionsModel());
      GridDialogPanelBuilder panelBuilder = new GridDialogPanelBuilder();
      panelBuilder.add(this.formatPanel);
      JComponent formatComponent = panelBuilder.createPanel();
      JComponent previewComponent = new ExportPreviewPanel(previewComponentModel).getContent();
      JComponent optionsComponent = new TextExportOptionsPanel(this.model.getOptionsModel(), this.model.getFormatSelectionModel()).getContent();
      RadioButtonPanel<ExportDestination> radioButtonPanel = new RadioButtonPanel<>(
         ExportDestination.values(), this.model.getDestinationModel(), new AbstractObjectUi<ExportDestination>() {
            public String getLabel(ExportDestination value) {
               return value.getName();
            }
         }
      );
      JComponent destinationComponent = radioButtonPanel.getContent();
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      GridDialogLayoutData formatData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      formatData.setGrabExcessVerticalSpace(true);
      formatData.setVerticalSpan(3);
      panel.add(formatComponent, formatData);
      panel.add(destinationComponent);
      panel.add(new HorizontalLine(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(optionsComponent);
      panel.add(previewComponent, GridDialogLayoutDataFactory.createHorizontalSpanData(2, GridDialogLayoutData.FILL_BOTH));
      return panel;
   }

   private void updatePreviewComponent(ObjectModel<JComponent> previewComponentModel, ITextExportFormat selectedFormat, ITextExportOptions options) {
      if (selectedFormat == null) {
         previewComponentModel.setValue(null);
      } else {
         previewComponentModel.setValue(selectedFormat.createPreviewComponent(this.plate, options));
      }
   }

   @Override
   public String getTitle() {
      return "Export";
   }
}

package de.jave.jave.actions.export;

import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.core.model.ObjectModel;

public class TextExportDialogModel {
   private final FixedOptionsObjectSelectionModel<ITextExportFormat> formatSelectionModel;
   private final ObjectModel<ExportDestination> destinationModel = new ObjectModel<>(ExportDestination.CLIPBOARD);
   private final TextExportOptionsModel optionsModel = new TextExportOptionsModel();

   public TextExportDialogModel() {
      ITextExportFormat[] formats = TextExportFormatFactory.getExportFormats();
      this.formatSelectionModel = new FixedOptionsObjectSelectionModel<>(formats);
      this.formatSelectionModel.setSelectedValue(formats[0]);
   }

   public FixedOptionsObjectSelectionModel<ITextExportFormat> getFormatSelectionModel() {
      return this.formatSelectionModel;
   }

   public ObjectModel<ExportDestination> getDestinationModel() {
      return this.destinationModel;
   }

   public TextExportOptionsModel getOptionsModel() {
      return this.optionsModel;
   }
}

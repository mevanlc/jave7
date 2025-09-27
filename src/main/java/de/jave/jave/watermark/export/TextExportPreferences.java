package de.jave.jave.actions.export;

import de.jave.preferences.SmartPreferences;

public class TextExportPreferences extends SmartPreferences {
   private static final String KEY_TRIM = "trim";
   private static final String KEY_CONNECTED_LINES_VIEW = "connectedLinesView";
   private static final String KEY_FORMAT = "format";
   private static final String KEY_DESTINATION = "destination";
   private static final boolean DEFAULT_TRIM = true;
   private static final boolean DEFAULT_CONNECTED_LINES_VIEW = false;
   private static final ExportDestination DEFAULT_DESTINATION = ExportDestination.CLIPBOARD;

   public TextExportPreferences(SmartPreferences parentPreferences) {
      super(parentPreferences.getSubPreferences("textExport"));
   }

   private void setFormat(ITextExportFormat format) {
      String formatId = format.getId();
      this.put("format", formatId);
   }

   private void setConnectedLinesView(boolean connectedLinesView) {
      this.put("connectedLinesView", connectedLinesView);
   }

   private void setTrim(boolean trim) {
      this.put("trim", trim);
   }

   private void setDestination(ExportDestination destination) {
      String destinationId = destination.name();
      this.put("destination", destinationId);
   }

   private ITextExportFormat getFormat() {
      ITextExportFormat defaultFormat = TextExportFormatFactory.getExportFormats()[0];
      String id = this.get("format", defaultFormat.getId());
      ITextExportFormat format = TextExportFormatFactory.getExportFormatById(id);
      return format != null ? format : defaultFormat;
   }

   private ExportDestination getDestination() {
      String id = this.get("destination", DEFAULT_DESTINATION.name());
      return ExportDestination.valueOf(id);
   }

   private boolean isTrim() {
      return this.getBoolean("trim", true);
   }

   private boolean isConnectedLinesView() {
      return this.getBoolean("connectedLinesView", false);
   }

   public TextExportDialogModel createModel() {
      TextExportDialogModel model = new TextExportDialogModel();
      model.getOptionsModel().setTrim(this.isTrim());
      model.getOptionsModel().setConnectedLinesView(this.isConnectedLinesView());
      model.getFormatSelectionModel().setSelectedValue(this.getFormat());
      model.getDestinationModel().setValue(this.getDestination());
      return model;
   }

   public void saveSettings(TextExportDialogModel model) {
      this.setDestination(model.getDestinationModel().getValue());
      this.setTrim(model.getOptionsModel().isTrim());
      this.setConnectedLinesView(model.getOptionsModel().isConnectedLinesView());
      this.setFormat(model.getFormatSelectionModel().getFirstSelectedValue());
   }
}

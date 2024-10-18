package de.jave.jave.clipart;

import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.gui.listpanel.IClickHandler;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class ClipartDialogPage extends AbstractDialogPage {
   private static final String TITLE = "Clipart Library";
   private final ObjectModel<ClipartGroup> groupSelectionModel;
   private final AsciiTextAreaProperties properties;
   private final ClipartManager clipartManager;
   private Clipart selectedClipart;

   public ClipartDialogPage(AsciiTextAreaProperties properties, ClipartManager clipartManager, ObjectModel<ClipartGroup> groupSelectionModel) {
      super("Please select a clipart from the library.");
      Ensure.ensureArgumentNotNull(properties);
      Ensure.ensureArgumentNotNull(clipartManager);
      Ensure.ensureArgumentNotNull(groupSelectionModel);
      this.clipartManager = clipartManager;
      this.groupSelectionModel = groupSelectionModel;
      this.properties = properties;
      properties.setEditable(false);
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public JComponent createContent() {
      ClipartGroupListPanel groupListPanel = new ClipartGroupListPanel(this.clipartManager, this.groupSelectionModel);
      IClickHandler<Clipart> clickHandler = new IClickHandler<Clipart>() {
         public void handleClick(Clipart value) {
            ClipartDialogPage.this.selectedClipart = value;
            ClipartDialogPage.this.fireRequestFinish();
         }
      };
      ClipartListSelectionPanel listSelectionPanel = new ClipartListSelectionPanel(this.properties, this.groupSelectionModel, clickHandler);
      this.groupSelectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ClipartDialogPage.this.checkInputValid();
         }
      });
      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      panel.add(groupListPanel.getContent(), "West");
      panel.add(listSelectionPanel.getContent(), "Center");
      return panel;
   }

   @Override
   public String getTitle() {
      return "Clipart Library";
   }

   public ClipartDialogResult getSelectionResult() {
      return new ClipartDialogResult(this.groupSelectionModel.getValue(), this.selectedClipart);
   }
}

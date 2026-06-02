package de.jave.asciimation.export.swf;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.gui.io.AcceptAllFileFilter;
import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import java.awt.Component;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.io.NonEditableFileStringTextField;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class SwfAnimationOutputOptionsPage extends DefaultAnimationOutputOptionsPage {
   public SwfAnimationOutputOptionsPage(AnimationExportWizardModel model) {
      super(model, new AnimationOutputOptionsConfiguration(new ExtensionFileFilter[]{ExtensionFileFilters.SWF}, true, false));
   }

   @Override
   protected void addAdditionalOptionsComponents(GridDialogPanelBuilder dialogPanel) {
      dialogPanel.add(
         new IDialogComponent() {
            @Override
            public void fillInto(JPanel panel, int columnCount) {
               panel.add(new JLabel("MTASC compiler:"));
               File compilerBinaryFile = SwfAnimationOutputOptionsPage.this.getModel().getExportOptions().getAdditionalOptions().getMtascCompilerBinaryFile();
               String compilerName = compilerBinaryFile == null ? "" : compilerBinaryFile.getAbsolutePath();
               final ObjectModel<String> compilerNameModel = new ObjectModel<>(compilerName);
               NonEditableFileStringTextField textField = new NonEditableFileStringTextField(compilerNameModel);
               GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
               data.setHorizontalSpan(columnCount - 2);
               panel.add(textField.getContent(), data);
               panel.add(
                  new JButton(
                     new SmartAction("Browse...") {
                        @Override
                        protected void execute(Component parentComponent) {
                           FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(parentComponent, new IFileChooserConfiguration() {
                              @Override
                              public FileModel getCurrentDirectoryModel() {
                                 return SwfAnimationOutputOptionsPage.this.getModel().getCurrentDirectoryModel();
                              }

                              @Override
                              public String getSaveDialogTitle() {
                                 return null;
                              }

                              @Override
                              public String getOpenDialogTitle() {
                                 return "MTASC compiler binary";
                              }

                              @Override
                              public SmartFileFilter[] getFileFilters() {
                                 return new SmartFileFilter[]{new AcceptAllFileFilter()};
                              }

                              @Override
                              public String getFileNameSuggestion() {
                                 return null;
                              }

                              @Override
                              public boolean isMultipleOpenFileSelectionAllowed() {
                                 return false;
                              }
                           });
                           if (!fileSelection.isEmpty()) {
                              compilerNameModel.setValue(fileSelection.getFile().getAbsolutePath());
                              SwfAnimationOutputOptionsPage.this.getModel()
                                 .getExportOptions()
                                 .getAdditionalOptions()
                                 .setMtascCompilerBinaryFile(fileSelection.getFile());
                              SwfAnimationOutputOptionsPage.this.checkInputValid();
                           }
                        }
                     }
                  )
               );
            }

            @Override
            public int getColumnCount() {
               return 3;
            }
         }
      );
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      File mtascCompilerBinaryFile = this.getModel().getExportOptions().getAdditionalOptions().getMtascCompilerBinaryFile();
      if (mtascCompilerBinaryFile == null) {
         return new BasicMessage(
            "There is no MTASC compiler binary file configured. Please install the free MTASC compiler (available from http://www.mtasc.org/) and specify the path to the compiler binary.",
            MessageType.ERROR
         );
      } else {
         return mtascCompilerBinaryFile.exists() && mtascCompilerBinaryFile.canRead()
            ? super.createCurrentMessage()
            : new BasicMessage(
               "The specified MTASC compiler binary file does not exist or cannot be accessed. Please install the free MTASC compiler (available from http://www.mtasc.org/) and specify the path to the compiler binary.",
               MessageType.ERROR
            );
      }
   }
}

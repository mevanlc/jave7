package de.jave.asciimation.export;

import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileExtension;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.io.FileUtilities;
import net.disy.commons.core.io.IWorkingDirectoryProvider;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.io.NonEditableFileStringTextField;
import net.disy.commons.swing.dialog.wizard.AbstractWizardPage;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public class DefaultAnimationOutputOptionsPage extends AbstractWizardPage {
   private final AnimationExportWizardModel model;
   private final IAnimationOutputOptionsConfiguration configuration;

   public DefaultAnimationOutputOptionsPage(AnimationExportWizardModel model, IAnimationOutputOptionsConfiguration configuration) {
      super(model.getExportOptions().getFormat().getName(), "Please select a destination file.");
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(configuration);
      this.model = model;
      this.configuration = configuration;
   }

   protected final AnimationExportWizardModel getModel() {
      return this.model;
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.model.getExportOptions().getFile() == null
         ? new BasicMessage("There is no file selected. Please select a file for output.", MessageType.ERROR)
         : this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      GridDialogPanelBuilder dialogPanel = new GridDialogPanelBuilder(false);
      File suggestedFile = this.createOutputFileSuggestion();
      this.model.getExportOptions().setOutputFile(suggestedFile);
      dialogPanel.add(new IDialogComponent() {
         @Override
         public void fillInto(JPanel panel, int columnCount) {
            panel.add(new JLabel("File:"), GridDialogLayoutData.RIGHT);
            File outputFile = DefaultAnimationOutputOptionsPage.this.model.getExportOptions().getFile();
            final ObjectModel<String> fileNameModel = new ObjectModel<>(outputFile == null ? null : outputFile.getAbsolutePath());
            NonEditableFileStringTextField textField = new NonEditableFileStringTextField(fileNameModel);
            GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
            data.setHorizontalSpan(columnCount - 2);
            panel.add(textField.getContent(), data);
            panel.add(new JButton(new SmartAction("Browse...") {
               @Override
               protected void execute(Component parentComponent) {
                  File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new IFileChooserConfiguration() {
                     @Override
                     public FileModel getCurrentDirectoryModel() {
                        return DefaultAnimationOutputOptionsPage.this.model.getCurrentDirectoryModel();
                     }

                     @Override
                     public String getSaveDialogTitle() {
                        return "Export Output File";
                     }

                     @Override
                     public String getOpenDialogTitle() {
                        return null;
                     }

                     @Override
                     public SmartFileFilter[] getFileFilters() {
                        return DefaultAnimationOutputOptionsPage.this.configuration.getOutputFileFilters();
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
                  if (file != null) {
                     fileNameModel.setValue(file.getAbsolutePath());
                     DefaultAnimationOutputOptionsPage.this.model.getExportOptions().setOutputFile(file);
                  } else {
                     fileNameModel.setValue(null);
                  }

                  DefaultAnimationOutputOptionsPage.this.checkInputValid();
               }
            }));
         }

         @Override
         public int getColumnCount() {
            return 3;
         }
      });
      this.addAdditionalOptionsComponents(dialogPanel);
      if (this.configuration.isLoopAvailable()) {
         dialogPanel.add(
            new IDialogComponent() {
               @Override
               public void fillInto(JPanel panel, int columnCount) {
                  GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
                  data.setHorizontalSpan(columnCount);
                  final JCheckBox checkBox = new JCheckBox(
                     "Loop", DefaultAnimationOutputOptionsPage.this.model.getExportOptions().getAdditionalOptions().isLoop()
                  );
                  checkBox.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                        DefaultAnimationOutputOptionsPage.this.model.getExportOptions().getAdditionalOptions().setLoop(checkBox.isSelected());
                        DefaultAnimationOutputOptionsPage.this.checkInputValid();
                     }
                  });
                  panel.add(checkBox, data);
               }

               @Override
               public int getColumnCount() {
                  return 1;
               }
            }
         );
      }

      if (this.configuration.isControlsAvailable()) {
         dialogPanel.add(
            new IDialogComponent() {
               @Override
               public void fillInto(JPanel panel, int columnCount) {
                  GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
                  data.setHorizontalSpan(columnCount);
                  final JCheckBox checkBox = new JCheckBox(
                     "Show player controls", DefaultAnimationOutputOptionsPage.this.model.getExportOptions().getAdditionalOptions().isControls()
                  );
                  checkBox.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                        DefaultAnimationOutputOptionsPage.this.model.getExportOptions().getAdditionalOptions().setControls(checkBox.isSelected());
                        DefaultAnimationOutputOptionsPage.this.checkInputValid();
                     }
                  });
                  panel.add(checkBox, data);
               }

               @Override
               public int getColumnCount() {
                  return 1;
               }
            }
         );
      }

      return dialogPanel.createPanel();
   }

   private File createOutputFileSuggestion() {
      FileExtension extension = this.configuration.getDefaultOutputFileExtension();
      IWorkingDirectoryProvider workingDirectoryProvider = new IWorkingDirectoryProvider() {
         @Override
         public File getWorkingDirectory() {
            return DefaultAnimationOutputOptionsPage.this.model.getCurrentDirectoryModel().getValue();
         }
      };
      return FileUtilities.createFileNameSuggestion(workingDirectoryProvider, "animation", "." + extension.getString());
   }

   protected void addAdditionalOptionsComponents(GridDialogPanelBuilder dialogPanel) {
   }

   @Override
   public boolean canFinish() {
      return !this.createCurrentMessage().isErrorMessage();
   }

   @Override
   public void performHelp() {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isHelpAvailable() {
      return false;
   }

   @Override
   public void requestFocus() {
   }
}

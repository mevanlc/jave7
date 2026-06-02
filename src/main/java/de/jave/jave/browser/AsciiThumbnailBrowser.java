package de.jave.jave.browser;

import de.jave.core.io.FileSelectionModel;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.gui.io.FileListPanel;
import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.progress.ProgressUtilities;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.progress.ProgressMonitorDialog;
import net.dizzy.commons.swing.filechooser.view.FolderSelectionPanel;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;

public class AsciiThumbnailBrowser {
   private static final String TITLE = "JavE Thumbnail Browser";
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 400;
   private final JavEApplication jave;
   private final JDialog dialog;
   private final FileModel folderModel;
   private final FileListPanel fileListPanel;
   private final JTextField statusTextField;
   private final ThumbnailsModel thumbnailsModel;
   private final FileSystemView fileSystemView;
   private final FileSelectionModel selectionModel = new FileSelectionModel();

   public AsciiThumbnailBrowser(Component parent, final JavEApplication jave, FileModel currentFolderModel) {
      this.dialog = JDialogFactory.createJDialog(parent, "JavE Thumbnail Browser", false);
      this.fileSystemView = FileSystemView.getFileSystemView();
      this.statusTextField = new JTextField();
      this.statusTextField.setEditable(false);
      this.jave = jave;
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            AsciiThumbnailBrowser.this.doClose();
         }
      });
      this.folderModel = currentFolderModel;
      this.folderModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiThumbnailBrowser.this.setDirectory(AsciiThumbnailBrowser.this.folderModel.getValue());
         }
      });
      FolderSelectionPanel folderSelectionPanel = new FolderSelectionPanel(this.folderModel, this.fileSystemView);
      AsciiThumbnailPanel thumbnailPanel = new AsciiThumbnailPanel(this.selectionModel) {
         @Override
         protected void handleDoubleClick(JaveFilePreviewItem dia) {
            AsciiThumbnailBrowser.this.handleOpenFile(AsciiThumbnailBrowser.this.dialog, dia.getFile());
         }
      };
      this.thumbnailsModel = thumbnailPanel.getModel();
      this.fileListPanel = new FileListPanel(this.fileSystemView, this.selectionModel);
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(new SmartAction("Refresh") {
         @Override
         protected void execute(Component parentComponent) {
            AsciiThumbnailBrowser.this.setDirectory(AsciiThumbnailBrowser.this.folderModel.getValue());
         }
      });
      final SmartAction openAction = new SmartAction("&Open") {
         @Override
         protected void execute(Component parentComponent) {
            File[] files = AsciiThumbnailBrowser.this.selectionModel.getSelectedFiles();
            if (jave != null && files.length != 0) {
               for (int i = 0; i < files.length; i++) {
                  AsciiThumbnailBrowser.this.handleOpenFile(parentComponent, files[i]);
               }
            }
         }
      };
      this.selectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiThumbnailBrowser.this.updateOpenActionEnabled(openAction);
         }
      });
      this.updateOpenActionEnabled(openAction);
      buttonPanelBuilder.add(openAction);
      buttonPanelBuilder.add(new SmartAction("&Cancel") {
         @Override
         protected void execute(Component parentComponent) {
            AsciiThumbnailBrowser.this.doClose();
         }
      });
      JSplitPane rightPanel = new JSplitPane(0, thumbnailPanel.getContent(), this.fileListPanel.getContent());
      rightPanel.setDividerLocation(280);
      JSplitPane splitPane = new JSplitPane(1, folderSelectionPanel.getContent(), rightPanel);
      JPanel mainPanel = new JPanel(new BorderLayout(2, 2));
      mainPanel.add(splitPane, "Center");
      mainPanel.add(this.statusTextField, "South");
      this.dialog.getContentPane().setLayout(new BorderLayout(2, 2));
      this.dialog.getContentPane().add(mainPanel, "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.pack();
      this.dialog.setSize(600, 400);
      thumbnailPanel.getModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiThumbnailBrowser.this.updateMessage();
         }
      });
      this.setDirectory(this.folderModel.getValue());
   }

   private void updateOpenActionEnabled(SmartAction openAction) {
      openAction.setEnabled(!this.selectionModel.isEmpty());
   }

   private void handleOpenFile(Component parentComponent, File file) {
      if (this.jave != null) {
         this.doClose();
         this.jave.open(parentComponent, file);
      }
   }

   private void setDirectory(File dir) {
      if (dir != null) {
         this.dialog.setTitle("JavE Thumbnail Browser - " + dir.getAbsolutePath());
         final File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
               return file.isFile() && file.canRead();
            }
         });
         if (files != null) {
            ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(this.dialog, JaveMessages.JavE);

            try {
               progressMonitorDialog.run(
                  new IInterruptableRunnableWithProgress() {
                     @Override
                     public void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException {
                        monitor.beginTask("Reading directory...", -1);
                        AsciiThumbnailBrowser.this.fileListPanel.setFiles(files);
                        AsciiThumbnailBrowser.this.statusTextField.setText("loading...");
                        AsciiThumbnailBrowser.this.thumbnailsModel.clear();

                        for (int i = 0; i < files.length; i++) {
                           ProgressUtilities.checkInterrupted(cancelable);
                           File file = files[i];
                           JaveFileType type = JaveFileType.guessType(file);
                           if (type != null) {
                              Icon icon = AsciiThumbnailBrowser.this.fileSystemView.getSystemIcon(file);
                              String label = AsciiThumbnailBrowser.this.fileSystemView.getSystemDisplayName(file);
                              JaveFilePreviewItem dia = new JaveFilePreviewItem(icon, file, label, type);
                              AsciiThumbnailBrowser.this.thumbnailsModel.add(dia);
                           }

                           Thread.yield();
                        }

                        JaveFilePreviewItem[] thumbnails = AsciiThumbnailBrowser.this.thumbnailsModel.getThumbnails();
                        monitor.beginTask("Creating thumbnails...", thumbnails.length);

                        for (int i = 0; i < thumbnails.length; i++) {
                           ProgressUtilities.checkInterrupted(cancelable);
                           monitor.subTask("Thumbnail " + (i + 1) + " of " + thumbnails.length);
                           JaveFilePreviewItem data = thumbnails[i];

                           try {
                              if (data.getFileType() == JaveFileType.TEXT) {
                                 int[] dim = new int[2];
                                 Dimension imageSize = JaveFilePreviewItemRenderer.getPreferredImageSize();
                                 Image image = AsciiThumbnailBrowser.this.dialog
                                    .createImage(AsciiToThumbnailConverter.convertFile(data.getFile(), imageSize.width, imageSize.height, dim, false));
                                 AsciiThumbnailBrowser.this.thumbnailsModel.setPreview(data, image, new Dimension(dim[0], dim[1]));
                              } else if (data.getFileType() == JaveFileType.ANIMATION) {
                                 int[] dim = new int[2];
                                 Dimension imageSize = JaveFilePreviewItemRenderer.getPreferredImageSize();
                                 Image image = AsciiThumbnailBrowser.this.dialog
                                    .createImage(AsciiToThumbnailConverter.convertAnimationFile(data.getFile(), imageSize.width, imageSize.height, dim));
                                 AsciiThumbnailBrowser.this.thumbnailsModel.setPreview(data, image, new Dimension(dim[0], dim[1]));
                              } else if (data.getFileType() == JaveFileType.RASTER_IMAGE) {
                                 BufferedImage image = ImageIO.read(data.getFile());
                                 int width = image.getWidth();
                                 int height = image.getHeight();
                                 Dimension originalImageSize = new Dimension(width, height);
                                 Dimension preferredSize = JaveFilePreviewItemRenderer.getPreferredImageSize();
                                 Dimension resultSize = AsciiThumbnailBrowser.this.getResultSize(originalImageSize, preferredSize);
                                 Image previewImage;
                                 if (resultSize.equals(originalImageSize)) {
                                    previewImage = image;
                                 } else {
                                    previewImage = image.getScaledInstance(resultSize.width, resultSize.height, 2);
                                 }

                                 AsciiThumbnailBrowser.this.thumbnailsModel.setPreview(data, previewImage, new Dimension(width, height));
                              } else {
                                 if (data.getFileType() != JaveFileType.VT) {
                                    throw new UnsupportedOperationException();
                                 }

                                 AsciiThumbnailBrowser.this.thumbnailsModel.setPreview(data, null, null);
                              }
                           } catch (Throwable var13) {
                              System.err.println("Unable to load preview for image " + data.getFile().getAbsolutePath() + var13);
                              var13.printStackTrace();
                           }

                           monitor.worked(1);
                        }
                     }
                  }
               );
            } catch (InterruptedException var5) {
            } catch (InvocationTargetException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   private Dimension getResultSize(Dimension actualSize, Dimension maxSize) {
      double scaleX = (double)maxSize.width / (double)actualSize.width;
      double scaleY = (double)maxSize.height / (double)actualSize.height;
      if (scaleX >= 1.0 && scaleY >= 1.0) {
         return actualSize;
      } else {
         double scale = Math.min(scaleX, scaleY);
         return new Dimension((int)((double)actualSize.width * scale), (int)((double)actualSize.height * scale));
      }
   }

   public void doClose() {
      this.dialog.setVisible(false);
   }

   public void show() {
      this.dialog.setVisible(true);
   }

   public void setVisible(boolean visible) {
      this.dialog.setVisible(visible);
   }

   private void updateMessage() {
      int imageDiaCount = this.thumbnailsModel.getSize();
      int fileCount = this.fileListPanel.getFileCount();
      String statusMessage;
      if (fileCount == 0) {
         statusMessage = "Folder is empty.";
      } else {
         statusMessage = fileCount + " file(s), " + imageDiaCount + " detected as supported.";
      }

      this.statusTextField.setText(statusMessage);
   }

   public JDialog getDialog() {
      return this.dialog;
   }
}

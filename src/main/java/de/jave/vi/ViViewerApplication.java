package de.jave.vi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class ViViewerApplication {
   public static final int DEFAULT_PAUSE = 10;

   private ViViewerApplication() {
   }

   public static void playFile(Component parentComponent, final File file, final ViPlayMode mode, final int pause) {
      final JTextField tf = new JTextField();
      tf.setEditable(false);
      final JTextArea ta = new JTextArea("", 20, 80);
      ta.setFont(new Font("Monospaced", 0, 11));
      ta.setEditable(false);
      IDialogPage page = new AbstractDialogPage("Experimental VT animation player") {
         @Override
         public String getTitle() {
            return "JavE VT Player";
         }

         @Override
         public JComponent createContent() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JScrollPane(ta), "Center");
            panel.add(tf, "South");
            return panel;
         }

         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }
      };
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<>(page));
      IViOutput output = new IViOutput() {
         @Override
         public void setText(final String text) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ta.setText(text);
               }
            });
         }

         @Override
         public void setStatus(final String status) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  tf.setText(status);
               }
            });
         }

         @Override
         public void setColorsInverted(final boolean inverted) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (inverted) {
                     ta.setForeground(Color.black);
                     ta.setBackground(Color.white);
                  } else {
                     ta.setForeground(Color.white);
                     ta.setBackground(Color.black);
                  }
               }
            });
         }
      };
      final ViAnimator animator = new ViAnimator(output);
      new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               BufferedReader br = new BufferedReader(new FileReader(file));
               String line = null;

               while ((line = br.readLine()) != null) {
                  animator.execute(line, mode, pause);
                  animator.newLine();
                  if (mode == ViPlayMode.LINE) {
                     animator.updateOutput();
                     if (pause > 0) {
                        try {
                           Thread.sleep(pause);
                        } catch (InterruptedException var4) {
                        }
                     }
                  }
               }

               br.close();
            } catch (Exception var5) {
               System.err.println(var5);
               var5.printStackTrace();
            }
         }
      }, "vt animator").start();
      userDialog.show();
   }
}

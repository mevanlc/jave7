package de.jave.jave;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Jave {
   public static void main(String[] args) {
      if (!isJre16OrGreater()) {
         printAndShowJdkOutdatedError();
      } else {
         launchJavE(args);
      }
   }

   private static void launchJavE(String[] args) {
      JavELauncher.launchJavE(args);
   }

   private static void printAndShowJdkOutdatedError() {
      String version = System.getProperty("java.version");
      String requiredJreName = "JRE 1.6.0";
      String message = JaveLaunchMessages.getString("jreOutdatedDialog_MessageText", new Object[]{"JRE 1.6.0", version});
      System.err.println(message);
      String title = JaveLaunchMessages.getString("jreOutdatedDialog_Title");
      showAwtErrorMessageDialog(message, title);
   }

   private static void showAwtErrorMessageDialog(String message, String title) {
      Frame frame = new Frame(title);
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            disposeAndExitWithError(frame);
         }
      });
      frame.setLayout(new BorderLayout());
      TextArea textArea = new TextArea(message, 0, 0, 3);
      textArea.setEditable(false);
      frame.add(textArea, "Center");
      Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
      Button exitButton = new Button(JaveLaunchMessages.getString("jreOutdatedDialog_ExitButtonText"));
      exitButton.addActionListener((event) -> disposeAndExitWithError(frame));
      buttonPanel.add(exitButton);
      frame.add(buttonPanel, "South");
      frame.pack();
      centerOnScreen(frame);
      frame.setVisible(true);
      exitButton.requestFocus();
   }

   private static void disposeAndExitWithError(Frame frame) {
      frame.dispose();
      System.exit(1);
   }

   private static void centerOnScreen(Window window) {
      try {
         Dimension screenSize = window.getToolkit().getScreenSize();
         Dimension windowSize = window.getSize();
         int x = (screenSize.width - windowSize.width) / 2;
         int y = (screenSize.height - windowSize.height) / 2;
         window.setLocation(x, y);
      } catch (Exception var5) {
      }
   }

   private static boolean isJre16OrGreater() {
      String classNameOfClassThatCameWithJdk16 = "java.awt.Desktop";

      try {
         Class.forName("java.awt.Desktop");
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }
}

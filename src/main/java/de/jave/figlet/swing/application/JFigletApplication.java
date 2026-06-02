package de.jave.figlet.swing.application;

import de.jave.ascii.font.ChooseDisplayFontAction;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.swing.preferences.JFigletApplicationPreferences;
import de.jave.figlet.swing.preferences.JFigletPreferences;
import de.jave.maxosx.MacOsXInitializer;
import de.jave.preferences.JavePreferences;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.menu.HelpImplementedMenuBar;

public class JFigletApplication {
   private final JFrame frame;
   private final JFigletEditor editor;
   private final JFigletApplicationPreferences preferences;
   private ChooseDisplayFontAction chooseDisplayFontAction;
   private final JavePreferences javePreferences = new JavePreferences();

   public JFigletApplication(IFigDriver figDriver, File tmpFolder) {
      this.preferences = new JFigletApplicationPreferences(this.javePreferences);
      this.editor = new JFigletEditor(figDriver, this.javePreferences.getDisplayFontModel(), new JFigletPreferences(this.javePreferences), tmpFolder);
      this.frame = new JFrame("JFIGlet 0.3");
      JOptionPane.setRootFrame(this.frame);
      this.frame.setIconImages(this.editor.getIcon());
      this.frame.getContentPane().setLayout(new BorderLayout());
      this.frame.getContentPane().add(this.editor.getContent());
      this.frame.setDefaultCloseOperation(0);
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            JFigletApplication.this.performExit();
         }
      });
      this.frame.setJMenuBar(this.createJMenuBar());
      this.frame.pack();
      this.frame.setBounds(this.preferences.getApplicationFrameBounds());
   }

   private void savePreferences() {
      this.editor.savePreferences();
      this.preferences.setApplicationFrameBounds(this.frame.getBounds());
      this.preferences.flush();
   }

   private JMenuBar createJMenuBar() {
      JMenu fileMenu = new JMenu("File");
      MacOsXInitializer.setMnemonic(fileMenu, 'F');
      fileMenu.add(new SmartAction("E&xit") {
         @Override
         protected void execute(Component parentComponent) {
            JFigletApplication.this.performExit();
         }
      });
      JMenu preferencesMenu = new JMenu("Preferences");
      MacOsXInitializer.setMnemonic(preferencesMenu, 'P');
      this.chooseDisplayFontAction = new ChooseDisplayFontAction(this.javePreferences.getDisplayFontModel());
      preferencesMenu.add(this.chooseDisplayFontAction);
      JMenu helpMenu = new JMenu("Help");
      MacOsXInitializer.setMnemonic(helpMenu, 'H');
      helpMenu.add(new SmartAction("&About") {
         @Override
         protected void execute(Component parentComponent) {
            JFigletApplication.this.showInfoDialog(parentComponent);
         }
      });
      JMenuBar bar = new HelpImplementedMenuBar();
      bar.add(fileMenu);
      bar.add(preferencesMenu);
      bar.setHelpMenu(helpMenu);
      return bar;
   }

   private void performExit() {
      try {
         this.savePreferences();
      } finally {
         this.frame.dispose();
         System.exit(0);
      }
   }

   public void start() {
      this.frame.setVisible(true);
   }

   public void showInfoDialog(Component parentComponent) {
      JOptionPane.showMessageDialog(
         parentComponent,
         "<html><center>JFIGlet Version 0.3<p><a href=\"http://www.jave.de/figlet/figlet.html\">http://www.jave.de/figlet/figlet.html</a></center></html>",
         "JFIGlet",
         1
      );
   }

   public JFrame getFrame() {
      return this.frame;
   }
}

package de.jave.braille;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("removal")
public class BrailleEditorApplet extends Applet implements ActionListener {
   private Frame frame;
   private boolean initialized = false;

   @Override
   public void actionPerformed(ActionEvent evt) {
      this.frame = new BrailleEditor();
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
         }
      });
      this.frame.show();
   }

   @Override
   public String getAppletInfo() {
      return "Braille Editor V0.1";
   }

   @Override
   public void init() {
      if (!this.initialized) {
         Button b = new Button("Hit once to start program in seperate frame");
         b.addActionListener(this);
         this.setLayout(new BorderLayout());
         this.add(b, "Center");
         this.initialized = true;
      }
   }

   @Override
   public void stop() {
      if (this.frame != null) {
         this.frame.dispose();
      }
   }
}

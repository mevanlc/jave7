package de.jave.javeplayer;

import java.applet.AudioClip;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.UIManager;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

@SuppressWarnings("removal")
public class JavePlayerApplet extends JApplet {
   private static final String VALUE_EVENT = "event";
   private static final String VALUE_LOOP = "loop";
   private static final String VALUE_ONCE = "once";
   private static final String PARAMETER_SOUNDSTART = "SOUNDSTART";
   private static final String PARAMETER_SOUND = "SOUND";
   private static final String PARAMETER_CONTROLS = "CONTROLS";
   private static final String PARAMETER_LOOP = "LOOP";
   private static final String PARAMETER_AUTOSTART = "AUTOSTART";
   private static final String PARAMETER_FILE = "FILE";
   private boolean initialized = false;
   private JavePlayer player;

   @Override
   public String getAppletInfo() {
      return JavePlayer.TITLE;
   }

   @Override
   public String[][] getParameterInfo() {
      return new String[][]{
         {"FILE", "String", "Name of the JavE animation file to be loaded"},
         {"AUTOSTART", "int", "Player will immediately start playing the animation if value is 1"},
         {"LOOP", "int", "Player will loop infinite times if value is 1"},
         {"CONTROLS", "int", "0: No controls 1: Just buttons (default) 2: All controls visible"},
         {"SOUND", "String", "Name of a sound file (*.mid, *.au or *.wav) to be played at play()"},
         {"SOUNDSTART", "String", "When to play the sound file: 'once', 'loop' or 'event'"}
      };
   }

   @Override
   public void init() {
      if (!this.initialized) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var8) {
         }

         JavePlayerConfiguration controls = JavePlayerConfiguration.DEFAULT_CONTROLS;
         boolean loop = false;
         boolean autostart = false;
         String soundFile = null;
         int soundStart = 0;

         try {
            String s = this.getParameter("CONTROLS");
            if (s != null) {
               if (s.equals("0")) {
                  controls = JavePlayerConfiguration.NO_CONTROLS;
               } else if (s.equals("1")) {
                  controls = JavePlayerConfiguration.DEFAULT_CONTROLS;
               } else if (s.equals("2")) {
                  controls = JavePlayerConfiguration.ALL_CONTROLS;
               } else {
                  System.err.println("Error reading parameter 'CONTROLS': Must be 0, 1 or 2");
               }
            }

            s = this.getParameter("LOOP");
            if (s != null && s.equals("1")) {
               loop = true;
            }

            s = this.getParameter("SOUND");
            if (s != null && s.length() > 0) {
               soundFile = s;
            }

            s = this.getParameter("SOUNDSTART");
            if (s != null) {
               s = s.toLowerCase();
               if (s.equals("once")) {
                  soundStart = 0;
               } else if (s.equals("loop")) {
                  soundStart = 1;
               } else if (s.equals("event")) {
                  soundStart = 2;
               } else {
                  System.err.println("Error reading parameter 'SOUNDSTART': Must be one of 'once', 'loop', 'event'");
               }
            }

            s = this.getParameter("AUTOSTART");
            if (s != null && s.equals("1")) {
               autostart = true;
            }
         } catch (Exception var7) {
            System.err.println("Error reading parameters: " + var7);
         }

         this.player = new JavePlayer(controls, loop, autostart);
         if (soundFile != null) {
            AudioClip audio = this.getAudioClip(this.getDocumentBase(), soundFile);
            this.player.setAudioClip(audio);
            this.player.setAudioStart(soundStart);
         }

         this.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
         this.getContentPane().add(this.player);
         this.initialized = true;
      }
   }

   @Override
   public void start() {
      String file = this.getParameter("FILE");
      if (file == null) {
         this.showErrorMessage("Required Applet parameter 'FILE' missing.", null);
      } else {
         URL url;
         try {
            url = new URL(this.getCodeBase(), file);
         } catch (MalformedURLException var4) {
            this.showErrorMessage(
               "Illegal URL '"
                  + this.getCodeBase().toExternalForm()
                  + "' / '"
                  + file
                  + "' for loading animation file, specified as Applet parameter '"
                  + "FILE"
                  + "'.",
               var4
            );
            return;
         }

         this.player.load(url);
      }
   }

   private void showErrorMessage(String message, Throwable cause) {
      MessageDialogFactory.showMessageDialog(this, new Message(message, cause));
   }

   @Override
   public void stop() {
      this.player.stop();
   }
}

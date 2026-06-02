package de.jave.javeplayer;

import de.jave.jave.version.JaveVersion;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

@SuppressWarnings("removal")
public class JavePlayer extends JPanel implements Runnable {
   private final JavePlate plate;
   private JaveAnimationFile animationFile;
   private AudioClip audioClip;
   private int audioStart;
   private int currentFrameIndex = -1;
   public static final String TITLE = "JavE Asciimation Player " + JaveVersion.getFullVersionNumber();
   public static final int SOUND_ONCE = 0;
   public static final int SOUND_LOOP = 1;
   public static final int SOUND_EVENT = 2;
   public static final boolean DEFAULT_LOOP = false;
   public static final boolean DEFAULT_AUTOSTART = false;
   public static final Color DEFAULT_BACKGROUND = Color.white;
   public static final Color DEFAULT_FOREGROUND = Color.black;
   public static final int DEFAULT_SOUND_START = 0;
   private JTextField tfFrame;
   private JLabel lFps;
   private boolean reverse = false;
   private static final String[] STR_SPEEDS = new String[]{
      "Slowest (0.1x)", "Slower (0.2x)", "Slow (0.5x)", "Normal Speed (1x)", "Fast (1.3x)", "Faster (2x)", "Fastest (4x)", "As fast as possible"
   };
   private static final double[] DURATION_MULTIPLICATOR = new double[]{10.0, 5.0, 2.0, 1.0, 0.75, 0.5, 0.25, 0.0};
   private static final int DEFAULT_SPEED = 3;
   private int sleepTime;
   private double durationMultiplicator = 1.0;
   private Thread thread;
   private boolean pause;
   private boolean shallStop;
   private final BooleanModel loop = new BooleanModel();
   private final boolean autostart;
   private final DefaultBoundedRangeModel progressModel = new DefaultBoundedRangeModel();
   private final IPlayerControlBar controlBar;

   public JavePlayer() {
      this(JavePlayerConfiguration.DEFAULT_CONTROLS, false, false);
   }

   public JavePlayer(JavePlayerConfiguration configuration, boolean loop, boolean autostart) {
      this.loop.setValue(loop);
      this.autostart = autostart;
      this.setLayout(new BorderLayout(2, 2));
      this.plate = new JavePlate();
      this.plate.setBackground(DEFAULT_BACKGROUND);
      this.plate.setForeground(DEFAULT_FOREGROUND);
      this.plate
         .addMouseListener(
            new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent evt) {
                  if (!evt.isMetaDown()) {
                     if (JavePlayer.this.thread == null) {
                        JavePlayer.this.doPlay();
                     } else {
                        JavePlayer.this.doPause();
                     }
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  if (e.isMetaDown()) {
                     JPopupMenu menu = new JPopupMenu();
                     menu.add(
                        new SmartAction("About JavE Asciimation Player") {
                           @Override
                           protected void execute(Component parentComponent) {
                              MessageDialogFactory.showMessageDialog(
                                 parentComponent, new Message(JavePlayer.TITLE + " " + JaveVersion.getBuildDate(), MessageType.INFORMATION)
                              );
                           }
                        }
                     );
                     menu.show(JavePlayer.this.plate, e.getX(), e.getY());
                  }
               }
            }
         );
      this.add(this.plate, "Center");
      if (configuration == JavePlayerConfiguration.ALL_CONTROLS) {
         PlayerControlBar bar = new PlayerControlBar(this.progressModel, this, this.loop);
         this.controlBar = bar;
         JPanel p = new JPanel();
         p.setLayout(new GridLayout(0, 1, 2, 2));
         p.add(this.createStatusBar());
         p.add(bar.getContent());
         this.add(p, "South");
      } else if (configuration == JavePlayerConfiguration.DEFAULT_CONTROLS) {
         PlayerControlBar bar = new PlayerControlBar(this.progressModel, this, this.loop);
         this.controlBar = bar;
         this.add(bar.getContent(), "South");
      } else {
         this.controlBar = new NullControlBar();
      }

      this.progressModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            JavePlayer.this.setFrame(JavePlayer.this.progressModel.getValue());
         }
      });
      this.updateProgress();
      this.addAncestorListener(new AncestorListener() {
         @Override
         public void ancestorMoved(AncestorEvent event) {
         }

         @Override
         public void ancestorRemoved(AncestorEvent event) {
            JavePlayer.this.doStop();
         }

         @Override
         public void ancestorAdded(AncestorEvent event) {
         }
      });
   }

   public void setAudioClip(AudioClip audioClip) {
      this.audioClip = audioClip;
   }

   public void setAudioStart(int audioStart) {
      this.audioStart = audioStart;
   }

   public void setPlateBackground(Color bgColor) {
      this.plate.setBackground(bgColor);
   }

   public void setPlateForeground(Color fgColor) {
      this.plate.setForeground(fgColor);
   }

   public void load(URL url) {
      try {
         this.animationFile = new JaveAnimationFile();
         this.animationFile.load(url);
         this.updateProgress();
      } catch (Exception var3) {
         this.plate.setStatusMessage(var3.getMessage());
         this.plate.setContent(null);
         return;
      }

      this.plate.setStatusMessage(null);
      if (this.animationFile.getFrameCount() > 0) {
         this.setFrame(0);
      } else {
         this.plate.setContent(null);
      }

      if (this.autostart) {
         this.doPlay();
      }
   }

   public void load(File file) throws IOException {
      this.load(file.toURL());
      this.animationFile.setFile(file);
   }

   @Override
   public void run() {
      long timeStart = System.currentTimeMillis();
      this.pause = false;
      int fpsCounter = 0;

      for (long fpsTimerStart = System.currentTimeMillis();
         !this.shallStop
            && this.animationFile != null
            && (
               this.loop.getValue()
                  || !this.reverse && this.currentFrameIndex < this.animationFile.getFrameCount() - 1
                  || this.reverse && this.currentFrameIndex > 0
            );
         this.plate.getToolkit().sync()
      ) {
         long wait = (long)((double)this.sleepTime * this.durationMultiplicator) - (System.currentTimeMillis() - timeStart);
         if (wait > 0L) {
            try {
               Thread.sleep(wait);
            } catch (InterruptedException var12) {
            }
         }

         if (++fpsCounter % 10 == 0) {
            long delta = System.currentTimeMillis() - fpsTimerStart;
            fpsTimerStart = System.currentTimeMillis();
            double fps = 10000.0 / (double)delta;
            if (this.lFps != null) {
               this.lFps.setText((double)((int)(fps * 10.0)) / 10.0 + " fps");
            }
         }

         timeStart = System.currentTimeMillis();

         while (!this.shallStop && !this.plate.isRepainted()) {
            Thread.yield();
         }

         if (!this.pause && !this.shallStop) {
            this.doNext();
         }
      }

      this.thread = null;
   }

   public void setAnimationFile(JaveAnimationFile animationFile) {
      this.doStop();
      this.animationFile = animationFile;
      this.currentFrameIndex = -1;
      this.plate.setStatusMessage(null);
      this.setFrame(0);
   }

   private void setFrame(int index) {
      if (index != this.currentFrameIndex) {
         this.currentFrameIndex = index;
         this.updateProgress();
         if (this.currentFrameIndex >= 0 && this.currentFrameIndex < this.animationFile.getFrameCount()) {
            JaveAnimationFrame currentFrame = this.animationFile.getFrame(this.currentFrameIndex);
            this.sleepTime = this.animationFile.getProperties().getFrameDuration();
            this.plate.setContent(currentFrame);
            this.plate.setBackground(this.animationFile.getProperties().getBackgroundColor());
            this.plate.setForeground(this.animationFile.getProperties().getForegroundColor());
            this.plate.repaint();
            if (this.tfFrame != null) {
               this.tfFrame.setText(this.currentFrameIndex + 1 + "/" + this.animationFile.getFrameCount());
            }
         } else {
            if (this.animationFile.getFrameCount() == 0) {
               this.plate.setStatusMessage("* Empty Animation *");
            }

            this.plate.setContent(null);
            this.plate.repaint();
            if (this.tfFrame != null) {
               this.tfFrame.setText("");
            }
         }
      }
   }

   private void updateProgress() {
      if (this.animationFile != null) {
         this.progressModel.setMaximum(this.animationFile.getFrameCount() - 1);
      }

      this.progressModel.setMinimum(0);
      this.progressModel.setValue(this.currentFrameIndex);
      if (this.animationFile != null) {
         this.controlBar.setProgressToolTip("Frame " + (this.currentFrameIndex + 1) + " of " + this.animationFile.getFrameCount());
      }

      boolean navigationControlsEnabled = this.animationFile != null && this.animationFile.getFrameCount() > 1;
      this.controlBar.setEnabled(navigationControlsEnabled);
   }

   public void doPlay() {
      if (this.reverse) {
         this.currentFrameIndex = this.animationFile.getFrameCount() - 1;
      } else {
         this.currentFrameIndex = 0;
      }

      this.setFrame(this.currentFrameIndex);
      if (this.thread != null) {
         this.shallStop = true;

         try {
            this.thread.interrupt();

            while (this.thread != null && this.thread.isAlive()) {
               Thread.yield();
            }
         } catch (Exception var2) {
         }
      }

      this.thread = new Thread(this, "Animator");
      this.thread.setPriority(1);
      this.shallStop = false;
      if (this.audioClip != null) {
         if (this.audioStart == 0) {
            this.audioClip.play();
         } else if (this.audioStart == 1) {
            this.audioClip.loop();
         } else if (this.audioStart == 2) {
            JaveAnimationFrame currentFrame = this.animationFile.getFrame(this.currentFrameIndex);
            if (currentFrame.isSoundTrigger()) {
               this.audioClip.play();
            }
         }
      }

      this.thread.start();
   }

   public void doPause() {
      this.pause = !this.pause;
      if (this.thread != null) {
         try {
            this.thread.interrupt();
         } catch (SecurityException var2) {
         }
      }
   }

   public void doStop() {
      if (this.thread != null) {
         this.shallStop = true;

         try {
            this.thread.interrupt();

            while (this.thread != null && this.thread.isAlive()) {
               Thread.yield();
            }
         } catch (Exception var2) {
         }

         if (this.audioClip != null) {
            this.audioClip.stop();
         }
      }
   }

   public void doPrev() {
      if (this.reverse) {
         if (this.currentFrameIndex < this.animationFile.getFrameCount() - 1) {
            this.setFrame(this.currentFrameIndex + 1);
         } else if (this.loop.getValue()) {
            this.setFrame(0);
         }
      } else if (this.currentFrameIndex > 0) {
         this.setFrame(this.currentFrameIndex - 1);
      } else if (this.loop.getValue()) {
         this.setFrame(this.animationFile.getFrameCount() - 1);
      }
   }

   public void doNext() {
      if (this.reverse) {
         if (this.currentFrameIndex > 0) {
            this.setFrame(this.currentFrameIndex - 1);
         } else if (this.loop.getValue()) {
            this.setFrame(this.animationFile.getFrameCount() - 1);
         }
      } else {
         if (this.currentFrameIndex < this.animationFile.getFrameCount() - 1) {
            this.setFrame(this.currentFrameIndex + 1);
         } else if (this.loop.getValue()) {
            this.setFrame(0);
         }

         if (this.audioClip != null && this.audioStart == 2) {
            JaveAnimationFrame currentFrame = this.animationFile.getFrame(this.currentFrameIndex);
            if (currentFrame.isSoundTrigger()) {
               this.audioClip.play();
            }
         }
      }
   }

   public void doZoomIn() {
      this.plate.zoomIn();
   }

   public void doZoomOut() {
      this.plate.zoomOut();
   }

   private Component createStatusBar() {
      JPanel p = new JPanel();
      this.tfFrame = new JTextField(10);
      this.tfFrame.setEditable(false);
      p.add(new JLabel("Frame:"));
      p.add(this.tfFrame);
      final JCheckBox cbReverse = new JCheckBox("Reverse", false);
      cbReverse.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JavePlayer.this.reverse = cbReverse.isSelected();
         }
      });
      final JComboBox chSpeed = new JComboBox<>(STR_SPEEDS);
      chSpeed.setSelectedIndex(3);
      chSpeed.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JavePlayer.this.durationMultiplicator = JavePlayer.DURATION_MULTIPLICATOR[chSpeed.getSelectedIndex()];
         }
      });
      this.durationMultiplicator = DURATION_MULTIPLICATOR[chSpeed.getSelectedIndex()];
      p.add(cbReverse);
      p.add(chSpeed);
      this.lFps = new JLabel("0.00 fps");
      p.add(this.lFps);
      return p;
   }

   public void stop() {
      if (this.audioClip != null) {
         this.audioClip.stop();
      }
   }
}

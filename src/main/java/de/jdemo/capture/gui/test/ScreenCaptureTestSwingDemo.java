package de.jdemo.capture.gui.test;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jdemo.extensions.SwingDemoCase;

/**
 * @author Markus Gebhard
 */
public class ScreenCaptureTestSwingDemo extends SwingDemoCase {

  public void demoComplex() {
    JPanel panel = new JPanel(new GridLayout(5, 0, 2, 2));
    setPreferredLookAndFeelMotif();
    for (int i = 0; i < 25; ++i) {
      JButton button = new JButton(String.valueOf(i * i * i * i * i));
      button.setFont(new Font(Font.DIALOG, Font.PLAIN, i / 2 + 5));
      panel.add(button);
    }
    show(panel);
  }


  public void demoMedium() {
    JPanel panel = new JPanel(new GridLayout(5, 0, 2, 2));
    setPreferredLookAndFeelSystem();
    for (int i = 0; i < 25; ++i) {
      JButton button = new JButton(String.valueOf(i * i * i * i * i));
      panel.add(button);
    }
    show(panel);
  }

  public void demoSimple() {
    show(new JLabel("test")); //$NON-NLS-1$
  }
}
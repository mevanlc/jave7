package de.jave.figlet.applet;

import de.jave.figlet.engine.FigDriver;
import de.jave.figlet.file.ClasspathFigFileResource;
import de.jave.figlet.util.FigException;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

@SuppressWarnings("removal")
public class FigletApplet extends Applet implements ActionListener, ItemListener {
   private boolean started = false;
   private Frame frame;
   private static final String TITLE = "FIGLet Applet 0.3";
   private static final String INFO = "FIGLet Applet 0.32009 by Gebhard Markus, markus@jave.de";
   private static final String COPYRIGHT = "copyright Markus Gebhard 1998-2009";
   private FigDriver figletizer;
   private String currentFont;
   private int scale = 8;
   private TextArea taOutput;
   private TextArea taInput;
   private Button bScalePlus;
   private Button bScaleMinus;
   private Button bDemo;
   private Button bConvert;
   private Choice choice_scale;
   private Choice choice_font;

   @Override
   public void init() {
      if (!this.started) {
         this.started = true;
         this.createGui();
      }
   }

   @Override
   public void start() {
      this.currentFont = "standard";

      try {
         this.figletizer = new FigDriver(new ClasspathFigFileResource());
      } catch (FigException var2) {
         this.showStatus("Error: " + var2.getMessage());
         return;
      }

      this.showStatus("loading font " + this.currentFont + "...");
      this.validate();
      this.figletize();
      this.repaint();
      this.taInput.requestFocus();
      this.showStatus("ready");
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      if (evt.getSource() == this.bConvert) {
         this.figletize();
      } else if (evt.getSource() == this.bDemo) {
         StringBuffer string = new StringBuffer();
         string.append("! _$%&/()=?`123");
         string.append("\n");
         string.append("4567890abcdefghi");
         string.append("\n");
         string.append("jklmnopqrstuvwxy");
         string.append("\n");
         string.append("zäöüABCDEFGHIJKLMNO");
         string.append("\n");
         string.append("PQRSTUVWXYZÄÖÜß*+#-:");
         this.taInput.setText(string.toString());
         this.figletize();
      } else if (evt.getSource() == this.bScaleMinus) {
         int selected_scale = Integer.parseInt(this.choice_scale.getSelectedItem()) - 1;
         if (selected_scale >= 4) {
            this.scale = selected_scale;
            this.taOutput.setFont(new Font("Monospaced", 0, this.scale));
            this.choice_scale.select(String.valueOf(this.scale));
            this.validate();
         }
      } else if (evt.getSource() == this.bScalePlus) {
         int selected_scale = Integer.parseInt(this.choice_scale.getSelectedItem()) + 1;
         if (selected_scale <= 20) {
            this.scale = selected_scale;
            this.taOutput.setFont(new Font("Monospaced", 0, this.scale));
            this.choice_scale.select(String.valueOf(this.scale));
            this.validate();
         }
      }
   }

   @Override
   public void itemStateChanged(ItemEvent e) {
      if (e.getSource() == this.choice_font) {
         String selected_font = this.choice_font.getSelectedItem();
         if (!selected_font.equals(this.currentFont)) {
            this.showStatus("loading font " + selected_font + "...");
            this.currentFont = selected_font;
            this.figletize();
            this.showStatus("finished");
         }
      } else if (e.getSource() == this.choice_scale) {
         int selected_scale = Integer.parseInt(this.choice_scale.getSelectedItem());
         if (selected_scale != this.scale) {
            this.scale = selected_scale;
            this.taOutput.setFont(new Font("Monospaced", 0, this.scale));
            this.validate();
         }
      }
   }

   public void figletize() {
      if (this.figletizer != null) {
         this.showStatus("converting...");
         this.taOutput.setText("");
         String source = this.taInput.getText();

         for (int i = source.indexOf(10); i != -1; i = source.indexOf(10)) {
            source = source.substring(0, i) + "<br>" + source.substring(i + 1);
         }

         try {
            source = "<figlet ascii 80><" + this.currentFont + ".flf>" + source + "</fig>";
            String string = this.figletizer.figml(source);
            this.taOutput.append(string);
            this.showStatus("ready");
         } catch (FigException var5) {
            this.showStatus("Error: " + var5);
         }
      }
   }

   public void createGui() {
      this.add(new Label("Figlet Applet running"));
      Panel panelLeft = new Panel();
      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints cl = new GridBagConstraints();
      panelLeft.setLayout(gbl);
      this.taInput = new TextArea(5, 10);
      this.taInput.setEditable(true);
      this.taInput.setText("FIGLet Applet 0.3");
      cl.gridwidth = -1;
      cl.gridheight = -1;
      cl.fill = 1;
      cl.ipadx = 0;
      cl.weightx = 1.0;
      gbl.setConstraints(this.taInput, cl);
      panelLeft.add(this.taInput);
      this.bConvert = new Button("convert");
      this.bConvert.addActionListener(this);
      cl.gridwidth = 0;
      cl.gridheight = -1;
      cl.fill = 3;
      cl.ipadx = 0;
      cl.weightx = 0.0;
      cl.weighty = 1.0;
      cl.insets = new Insets(10, 5, 10, 15);
      gbl.setConstraints(this.bConvert, cl);
      panelLeft.add(this.bConvert);
      this.taOutput = new TextArea(24, 90);
      this.taOutput.setFont(new Font("Monospaced", 0, this.scale));
      this.taOutput.setEditable(false);
      cl.gridwidth = 0;
      cl.gridheight = 0;
      cl.weightx = 1.0;
      cl.weighty = 1.0;
      cl.fill = 1;
      cl.insets = new Insets(5, 0, 0, 10);
      gbl.setConstraints(this.taOutput, cl);
      panelLeft.add(this.taOutput);
      Panel panelRight = new Panel();
      GridBagLayout gbr = new GridBagLayout();
      GridBagConstraints cr = new GridBagConstraints();
      panelRight.setLayout(gbr);
      Label label_fig = new Label("FIGLet Applet 0.3", 0);
      label_fig.setFont(new Font("Dialog", 1, 24));
      cr.gridwidth = 0;
      cr.fill = 1;
      cr.anchor = 12;
      gbr.setConstraints(label_fig, cr);
      panelRight.add(label_fig);
      Label label_written = new Label("copyright Markus Gebhard 1998-2009", 0);
      label_written.setFont(new Font("Dialog", 1, 10));
      cr.gridwidth = 0;
      cr.fill = 1;
      cr.insets = new Insets(0, 0, 50, 0);
      gbr.setConstraints(label_written, cr);
      panelRight.add(label_written);
      Label label_font = new Label("font:");
      cr.fill = 0;
      cr.gridwidth = 1;
      cr.insets = new Insets(0, 0, 0, 0);
      gbr.setConstraints(label_font, cr);
      panelRight.add(label_font);
      this.choice_font = new Choice();
      this.choice_font.addItemListener(this);
      String fontNames = this.getParameter("fonts");
      if (fontNames == null) {
         this.choice_font.addItem("standard");
      } else {
         StringTokenizer st = new StringTokenizer(fontNames, "\n\t, ;:\r");

         while (st.hasMoreTokens()) {
            String name = st.nextToken();
            this.choice_font.addItem(name.trim());
         }
      }

      this.choice_font.select("standard");
      cr.gridwidth = 0;
      cr.fill = 2;
      gbr.setConstraints(this.choice_font, cr);
      panelRight.add(this.choice_font);
      Label label_smush = new Label("layout:");
      cr.gridwidth = 1;
      cr.insets = new Insets(0, 0, 0, 0);
      cr.fill = 0;
      gbr.setConstraints(label_smush, cr);
      panelRight.add(label_smush);
      Label labelScale = new Label("size:");
      cr.gridwidth = 1;
      cr.insets = new Insets(0, 0, 0, 0);
      cr.fill = 0;
      gbr.setConstraints(labelScale, cr);
      panelRight.add(labelScale);
      this.bScaleMinus = new Button("-");
      this.bScaleMinus.addActionListener(this);
      cr.fill = 0;
      cr.gridwidth = 2;
      cr.insets = new Insets(5, 5, 5, 5);
      cr.anchor = 13;
      gbr.setConstraints(this.bScaleMinus, cr);
      panelRight.add(this.bScaleMinus);
      this.choice_scale = new Choice();

      for (int i = 4; i < 21; i++) {
         this.choice_scale.addItem(String.valueOf(i));
      }

      this.taOutput.setFont(new Font("Monospaced", 0, this.scale));
      this.choice_scale.select(String.valueOf(this.scale));
      this.choice_scale.addItemListener(this);
      cr.insets = new Insets(0, 5, 0, 5);
      cr.fill = 2;
      cr.gridwidth = -1;
      cr.anchor = 10;
      gbr.setConstraints(this.choice_scale, cr);
      panelRight.add(this.choice_scale);
      this.bScalePlus = new Button("+");
      this.bScalePlus.addActionListener(this);
      cr.fill = 0;
      cr.gridwidth = 0;
      cr.ipadx = 0;
      cr.insets = new Insets(5, 5, 5, 5);
      cr.anchor = 17;
      gbr.setConstraints(this.bScalePlus, cr);
      panelRight.add(this.bScalePlus);
      this.bDemo = new Button("demo");
      this.bDemo.addActionListener(this);
      cr.gridwidth = 0;
      cr.insets = new Insets(5, 5, 5, 5);
      gbr.setConstraints(this.bDemo, cr);
      panelRight.add(this.bDemo);
      this.frame = new Frame("FIGLet Applet 0.3");
      GridBagLayout gbf = new GridBagLayout();
      GridBagConstraints cf = new GridBagConstraints();
      this.frame.setLayout(gbf);
      cf.fill = 1;
      cf.anchor = 10;
      cf.gridwidth = -1;
      cf.gridheight = 0;
      cf.insets = new Insets(0, 0, 0, 0);
      cf.weightx = 1.0;
      cf.weighty = 1.0;
      gbf.setConstraints(panelLeft, cf);
      this.frame.add(panelLeft);
      cf.fill = 1;
      cf.gridwidth = 0;
      cf.gridheight = 0;
      cf.insets = new Insets(0, 0, 0, 0);
      cf.weightx = 0.0;
      cf.weighty = 1.0;
      gbf.setConstraints(panelRight, cf);
      this.frame.add(panelRight);
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            FigletApplet.this.frame.dispose();
         }
      });
      this.frame.pack();
      this.frame.show();
   }

   @Override
   public String getAppletInfo() {
      return "FIGLet Applet 0.32009 by Gebhard Markus, markus@jave.de";
   }

   @Override
   public String[][] getParameterInfo() {
      return new String[][]{{"fonts", "String", "a list of all available font-files seperated by >,<"}};
   }
}

package de.jave.jave;

import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.actions.JaveKeyBindings;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;

public class UnicodePickerDialog {

   private static final int FONT_SIZE_MIN = 8;
   private static final int FONT_SIZE_MAX = 40;
   private static final int FONT_SIZE_DEFAULT = 12;
   private static final int FONT_SIZE_STEP = 2;
   private static final String PREF_KEY_LIST_FONT_SIZE = "unicodePickerListFontSize";
   private static final String PREF_KEY_WINDOW_WIDTH = "unicodePickerWindowWidth";
   private static final String PREF_KEY_WINDOW_HEIGHT = "unicodePickerWindowHeight";
   private static final int WINDOW_MIN_WIDTH = 460;
   private static final int WINDOW_MIN_HEIGHT = 360;

   private static volatile List<UnicodeEntry> allEntries;

   private final JaveMainPanel mainPanel;
   private final JDialog dialog;
   private final JTextField searchField;
   private final DefaultListModel<UnicodeEntry> listModel;
   private final JList<UnicodeEntry> entryList;
   private final JLabel previewLabel;
   private final JLabel infoLabel;
   private int listFontSize;

   public UnicodePickerDialog(Component parent, JaveMainPanel mainPanel) {
      int saved = Preferences.userRoot().node("JavE").getInt(PREF_KEY_LIST_FONT_SIZE, FONT_SIZE_DEFAULT);
      this.listFontSize = Math.max(FONT_SIZE_MIN, Math.min(FONT_SIZE_MAX, saved));
      this.mainPanel = mainPanel;
      this.dialog = JDialogFactory.createJDialog(parent, "Unicode Picker", false);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            closeDialog();
         }
      });

      this.searchField = new JTextField();
      this.listModel = new DefaultListModel<>();
      this.entryList = new JList<>(this.listModel);
      this.entryList.setCellRenderer(new UnicodeEntryRenderer());
      this.entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.entryList.setFont(new Font("Monospaced", Font.PLAIN, this.listFontSize));

      this.previewLabel = new JLabel(" ", SwingConstants.CENTER);
      this.previewLabel.setFont(new Font("Monospaced", Font.PLAIN, 36));
      this.previewLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
      this.infoLabel = new JLabel(" ");
      this.infoLabel.setFont(JaveGlobalRessources.FONT_DEFAULT);

      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(buildContentPanel(), BorderLayout.CENTER);
      this.dialog.getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      bindKeys();
      wireListeners();
      this.dialog.pack();
      this.dialog.setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT));
      Dimension savedSize = readSavedWindowSize();
      if (savedSize != null) {
         this.dialog.setSize(savedSize);
      }
      this.dialog.setLocationRelativeTo(parent);
   }

   private JPanel buildContentPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(new EmptyBorder(8, 8, 4, 8));
      GridBagConstraints gbc = new GridBagConstraints();

      // Row 0: search label + text field
      gbc.gridx = 0; gbc.gridy = 0;
      gbc.weightx = 0; gbc.weighty = 0;
      gbc.fill = GridBagConstraints.NONE;
      gbc.anchor = GridBagConstraints.WEST;
      gbc.insets = new Insets(0, 0, 4, 6);
      panel.add(new JLabel("Search:"), gbc);

      gbc.gridx = 1;
      gbc.weightx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(0, 0, 4, 0);
      panel.add(this.searchField, gbc);

      // Row 1: list (stretches to fill all extra vertical space)
      gbc.gridx = 0; gbc.gridy = 1;
      gbc.gridwidth = 2;
      gbc.weightx = 1; gbc.weighty = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.insets = new Insets(0, 0, 0, 0);
      JScrollPane scrollPane = new JScrollPane(this.entryList);
      scrollPane.setPreferredSize(new Dimension(440, 280));
      panel.add(scrollPane, gbc);

      // Row 2: zoom buttons, flush to the bottom-right of the list
      gbc.gridy = 2;
      gbc.gridwidth = 2;
      gbc.weightx = 1; gbc.weighty = 0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(2, 0, 4, 0);
      panel.add(buildZoomStrip(), gbc);

      // Row 3: character preview + description
      gbc.gridy = 3;
      gbc.gridwidth = 2;
      gbc.weightx = 1; gbc.weighty = 0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(0, 0, 0, 0);
      JPanel previewPanel = new JPanel(new BorderLayout(6, 0));
      previewPanel.add(this.previewLabel, BorderLayout.WEST);
      previewPanel.add(this.infoLabel, BorderLayout.CENTER);
      panel.add(previewPanel, gbc);

      return panel;
   }

   private Box buildZoomStrip() {
      JButton zoomOutBtn = new JButton(JaveIcons.ZOOM_MINUS_ICON);
      JButton zoomInBtn = new JButton(JaveIcons.ZOOM_PLUS_ICON);
      Insets compact = new Insets(1, 5, 1, 5);
      zoomOutBtn.setMargin(compact);
      zoomInBtn.setMargin(compact);
      zoomOutBtn.setToolTipText("Zoom out list (Cmd+−)");
      zoomInBtn.setToolTipText("Zoom in list (Cmd++)");
      zoomOutBtn.addActionListener(e -> zoomOut());
      zoomInBtn.addActionListener(e -> zoomIn());

      Box strip = Box.createHorizontalBox();
      strip.add(Box.createHorizontalGlue());
      strip.add(zoomOutBtn);
      strip.add(Box.createHorizontalStrut(2));
      strip.add(zoomInBtn);
      return strip;
   }

   private JPanel buildButtonPanel() {
      JButton insertBtn = new JButton(new AbstractAction("Insert") {
         @Override
         public void actionPerformed(ActionEvent e) {
            insertSelected();
         }
      });
      JButton insertCloseBtn = new JButton(new AbstractAction("Insert & Close") {
         @Override
         public void actionPerformed(ActionEvent e) {
            insertSelected();
            closeDialog();
         }
      });
      JButton closeBtn = new JButton(new AbstractAction("Close") {
         @Override
         public void actionPerformed(ActionEvent e) {
            closeDialog();
         }
      });
      return new ButtonPanelBuilder().add(insertBtn, insertCloseBtn, closeBtn).createPanel();
   }

   private void bindKeys() {
      JComponent root = this.dialog.getRootPane();
      javax.swing.InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      javax.swing.ActionMap am = root.getActionMap();

      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "unicode.insert");
      am.put("unicode.insert", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            insertSelected();
         }
      });

      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK), "unicode.insertClose");
      am.put("unicode.insertClose", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            insertSelected();
            closeDialog();
         }
      });

      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "unicode.close");
      am.put("unicode.close", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            closeDialog();
         }
      });

      im.put(JaveKeyBindings.ZOOM_IN, "unicode.zoomIn");
      am.put("unicode.zoomIn", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            zoomIn();
         }
      });

      im.put(JaveKeyBindings.ZOOM_OUT, "unicode.zoomOut");
      am.put("unicode.zoomOut", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            zoomOut();
         }
      });
   }

   private void wireListeners() {
      this.searchField.getDocument().addDocumentListener(new DocumentListener() {
         @Override public void insertUpdate(DocumentEvent e) { updateFilter(); }
         @Override public void removeUpdate(DocumentEvent e) { updateFilter(); }
         @Override public void changedUpdate(DocumentEvent e) { updateFilter(); }
      });

      this.entryList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) updatePreview();
      });
   }

   private void zoomIn() {
      if (this.listFontSize < FONT_SIZE_MAX) {
         this.listFontSize = Math.min(this.listFontSize + FONT_SIZE_STEP, FONT_SIZE_MAX);
         updateListFont();
         saveZoomPreference();
      }
   }

   private void zoomOut() {
      if (this.listFontSize > FONT_SIZE_MIN) {
         this.listFontSize = Math.max(this.listFontSize - FONT_SIZE_STEP, FONT_SIZE_MIN);
         updateListFont();
         saveZoomPreference();
      }
   }

   private void saveZoomPreference() {
      Preferences prefs = Preferences.userRoot().node("JavE");
      prefs.putInt(PREF_KEY_LIST_FONT_SIZE, this.listFontSize);
      try {
         prefs.flush();
      } catch (BackingStoreException e) {
         // ignore — non-critical
      }
   }

   private Dimension readSavedWindowSize() {
      Preferences prefs = Preferences.userRoot().node("JavE");
      int w = prefs.getInt(PREF_KEY_WINDOW_WIDTH, -1);
      int h = prefs.getInt(PREF_KEY_WINDOW_HEIGHT, -1);
      if (w < WINDOW_MIN_WIDTH || h < WINDOW_MIN_HEIGHT) return null;
      return new Dimension(w, h);
   }

   private void saveWindowSize() {
      Dimension size = this.dialog.getSize();
      Preferences prefs = Preferences.userRoot().node("JavE");
      prefs.putInt(PREF_KEY_WINDOW_WIDTH, size.width);
      prefs.putInt(PREF_KEY_WINDOW_HEIGHT, size.height);
      try {
         prefs.flush();
      } catch (BackingStoreException e) {
         // ignore — non-critical
      }
   }

   private void closeDialog() {
      saveWindowSize();
      this.dialog.setVisible(false);
   }

   private void updateListFont() {
      this.entryList.setFont(new Font("Monospaced", Font.PLAIN, this.listFontSize));
      this.entryList.setFixedCellHeight(-1);
      this.entryList.revalidate();
      this.entryList.repaint();
   }

   private void updateFilter() {
      List<UnicodeEntry> entries = allEntries;
      if (entries == null) return;
      String query = this.searchField.getText().trim().toLowerCase(Locale.ENGLISH);
      this.listModel.clear();
      int count = 0;
      for (UnicodeEntry entry : entries) {
         if (matches(entry, query)) {
            this.listModel.addElement(entry);
            if (++count >= 500) break;
         }
      }
      if (this.listModel.getSize() > 0) {
         this.entryList.setSelectedIndex(0);
         this.entryList.ensureIndexIsVisible(0);
      }
      updatePreview();
   }

   private static boolean matches(UnicodeEntry entry, String query) {
      if (query.isEmpty()) return true;
      if (entry.name.toLowerCase(Locale.ENGLISH).contains(query)) return true;
      String hex = String.format("%04x", entry.codePoint);
      return hex.startsWith(query) || ("u+" + hex).startsWith(query);
   }

   private void updatePreview() {
      UnicodeEntry entry = this.entryList.getSelectedValue();
      if (entry == null) {
         this.previewLabel.setText(" ");
         this.infoLabel.setText(" ");
      } else {
         this.previewLabel.setText(String.valueOf(entry.getChar()));
         this.infoLabel.setText(String.format("U+%04X  %s  [%s]", entry.codePoint, entry.name, entry.category));
      }
   }

   private void insertSelected() {
      UnicodeEntry entry = this.entryList.getSelectedValue();
      if (entry == null) return;
      Plate plate = this.mainPanel.getPlate();
      if (plate == null) return;
      Point loc = plate.getPasteLocation();
      if (loc == null) return;
      plate.setCharForce(loc, entry.getChar());
      plate.saveCurrentState("insert unicode character");
      plate.repaint();
   }

   public void show() {
      if (allEntries == null) {
         loadEntriesAsync();
      }
      this.dialog.setVisible(true);
      this.searchField.requestFocusInWindow();
   }

   private void loadEntriesAsync() {
      new SwingWorker<List<UnicodeEntry>, Void>() {
         @Override
         protected List<UnicodeEntry> doInBackground() throws Exception {
            return loadUnicodeEntries();
         }

         @Override
         protected void done() {
            try {
               allEntries = get();
               updateFilter();
            } catch (Exception ex) {
               // ignore load failures — picker just stays empty
            }
         }
      }.execute();
   }

   private static List<UnicodeEntry> loadUnicodeEntries() {
      List<UnicodeEntry> entries = new ArrayList<>(32768);
      File zipFile = new File(JaveGlobalRessources.codeBase, "ext/UnicodeData.zip");
      try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
         ZipEntry ze;
         while ((ze = zis.getNextEntry()) != null) {
            if (ze.getName().endsWith("UnicodeData.txt")) {
               BufferedReader reader = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
               String line;
               while ((line = reader.readLine()) != null) {
                  UnicodeEntry entry = parseLine(line);
                  if (entry != null) entries.add(entry);
               }
               break;
            }
         }
      } catch (IOException e) {
         // return whatever was collected before the error
      }
      return entries;
   }

   private static UnicodeEntry parseLine(String line) {
      String[] fields = line.split(";", 3);
      if (fields.length < 3) return null;
      int cp;
      try {
         cp = Integer.parseInt(fields[0].trim(), 16);
      } catch (NumberFormatException e) {
         return null;
      }
      if (cp > 0xFFFF || cp < 0x0020) return null;
      String name = fields[1].trim();
      if (name.startsWith("<")) return null;
      String category = fields[2].trim();
      if ("Cc".equals(category) || "Cs".equals(category)) return null;
      return new UnicodeEntry(cp, name, category);
   }

   // -------------------------------------------------------------------------

   static final class UnicodeEntry {
      final int codePoint;
      final String name;
      final String category;

      UnicodeEntry(int codePoint, String name, String category) {
         this.codePoint = codePoint;
         this.name = name;
         this.category = category;
      }

      char getChar() {
         return (char) codePoint;
      }
   }

   private static final class UnicodeEntryRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (value instanceof UnicodeEntry) {
            UnicodeEntry e = (UnicodeEntry) value;
            setText(String.format("U+%04X  %s  %s", e.codePoint, String.valueOf(e.getChar()), e.name));
         }
         return this;
      }
   }
}

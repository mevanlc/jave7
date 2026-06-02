package de.jave.jave.clipart;

import de.jave.jave.JaveGlobalRessources;
import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class ClipartManager {
   private static final File BASE_FOLDER = new File(JaveGlobalRessources.codeBase, "clipart");
   private final List<ClipartGroup> groups = new ArrayList<>();

   private ClipartManager() {
   }

   private void loadGroups() throws Exception {
      File f = BASE_FOLDER;
      if (!f.exists()) {
         throw new Exception("The folder " + f.getAbsolutePath() + " does not exist.");
      } else {
         File[] fl = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
               return name.toLowerCase().endsWith(".jcf");
            }
         });

         for (int i = 0; i < fl.length; i++) {
            ClipartGroup mg = ClipartGroup.load(fl[i]);
            this.groups.add(mg);
         }

         final Collator collator = Collator.getInstance();
         Collections.sort(this.groups, new Comparator<ClipartGroup>() {
            public int compare(ClipartGroup o1, ClipartGroup o2) {
               return collator.compare(o1.getName(), o2.getName());
            }
         });
      }
   }

   public int getGroupCount() {
      return this.groups.size();
   }

   public ClipartGroup getGroup(int index) {
      return this.groups.get(index);
   }

   public boolean hasGroupNameIgnoringCase(String name) {
      for (int i = 0; i < this.groups.size(); i++) {
         if (name.equalsIgnoreCase(this.groups.get(i).getName())) {
            return true;
         }
      }

      return false;
   }

   public ClipartGroup getGroup(String name) {
      if (name == null) {
         return null;
      } else {
         for (int i = 0; i < this.groups.size(); i++) {
            if (name.equals(this.groups.get(i).getName())) {
               return this.groups.get(i);
            }
         }

         return null;
      }
   }

   public void performSave(Component parentComponent, ClipartGroup group) {
      group.performSave(parentComponent, BASE_FOLDER);
   }

   public static ClipartManager performLoad(Component parentComponent) {
      ClipartManager manager = new ClipartManager();

      try {
         manager.loadGroups();
         return manager;
      } catch (Exception var3) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("Error loading Clipart files.", var3));
         return null;
      }
   }
}

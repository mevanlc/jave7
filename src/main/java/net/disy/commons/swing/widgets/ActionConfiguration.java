package net.disy.commons.swing.action;

import javax.swing.Icon;
import net.disy.commons.core.util.ObjectUtilities;

public class ActionConfiguration implements IActionConfiguration {
   private final String name;
   private final Icon icon;
   private final String toolTipText;

   public ActionConfiguration() {
      this(null, null, null);
   }

   public ActionConfiguration(String name) {
      this(name, null, null);
   }

   public ActionConfiguration(Icon icon) {
      this(null, icon);
   }

   public ActionConfiguration(String name, Icon icon) {
      this(name, icon, null);
   }

   public ActionConfiguration(String name, String toolTipText) {
      this(name, null, toolTipText);
   }

   public ActionConfiguration(String name, Icon icon, String toolTipText) {
      this.name = name;
      this.icon = icon;
      this.toolTipText = toolTipText;
   }

   @Override
   public Icon getIcon() {
      return this.icon;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getToolTipText() {
      return this.toolTipText;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof ActionConfiguration)) {
         return false;
      } else {
         ActionConfiguration other = (ActionConfiguration)obj;
         return ObjectUtilities.equals(other.name, this.name)
            && ObjectUtilities.equals(other.icon, this.icon)
            && ObjectUtilities.equals(this.toolTipText, other.toolTipText);
      }
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }
}

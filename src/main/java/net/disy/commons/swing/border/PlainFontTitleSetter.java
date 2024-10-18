package net.disy.commons.swing.border;

import javax.swing.border.TitledBorder;
import net.disy.commons.core.util.IClosure;

public final class PlainFontTitleSetter implements IClosure<TitledBorder> {
   public void execute(TitledBorder each) {
      each.setTitleFont(each.getTitleFont().deriveFont(0));
   }
}

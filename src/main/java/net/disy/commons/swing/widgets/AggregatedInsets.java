package net.disy.commons.swing.layout;

import java.awt.Insets;

public class AggregatedInsets extends Insets {
   public AggregatedInsets(Insets first, Insets second) {
      super(first.top + second.top, first.left + second.left, first.bottom + second.bottom, first.right + second.right);
   }
}

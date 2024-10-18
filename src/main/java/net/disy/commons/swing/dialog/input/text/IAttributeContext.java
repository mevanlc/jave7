package net.disy.commons.swing.dialog.input.text;

import java.util.Collection;

public interface IAttributeContext {
   Collection<String> getAttributeNames();

   Object getCurrentValueOf(String var1);
}

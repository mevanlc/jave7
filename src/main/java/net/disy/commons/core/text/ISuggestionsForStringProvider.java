package net.disy.commons.core.text;

import java.io.IOException;
import net.disy.commons.core.progress.ICancelable;

public interface ISuggestionsForStringProvider<T> {
   SuggestionResult<T> querySuggestions(String var1, ICancelable var2) throws IOException, InterruptedException;
}

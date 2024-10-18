package de.jave.figlet.engine.layouter;

import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigFragment;
import de.jave.figlet.engine.primitives.FigLayout;

public interface IFigLayouter {
   FigFragment createFragment(FigFont var1, FigLayout var2, int var3);

   FigFragment appendVertical(FigFragment var1, FigFragment var2);

   FigFragment appendHorizontal(FigFragment var1, FigFragment var2);
}

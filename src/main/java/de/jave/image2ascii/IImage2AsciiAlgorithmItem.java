package de.jave.image2ascii;

import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public interface IImage2AsciiAlgorithmItem {
   IDisposableComponentContainer createAdjustmentComponent();

   IImage2AsciiAlgorithm getAlgorithm();
}

package de.jave.image2ascii;

import de.jave.image2ascii.algorithm.Image2AsciiAlgorithm1;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithm3d;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithm4;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmBraille;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmEdgeDetect;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmEdgeTracing;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmFeltpen;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmGradient;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmJavE;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmPixelPlate;
import de.jave.image2ascii.algorithm.kicad.Image2AsciiAlgorithmKicad;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class Image2AsciiAlgorithmFactory {
   private Image2AsciiAlgorithmFactory() {
   }

   private static final AbstractImage2AsciiAlgorithm[] createAlgorithms(
      FontModel displayFontModel, AsciiGradientConfiguration gradientConfiguration, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration, Filter filter
   ) {
      SharedImage2AsciiOptions sharedOptions = new SharedImage2AsciiOptions(displayFontModel, greyscaleTableConfiguration);
      List<AbstractImage2AsciiAlgorithm> algorithmList = new ArrayList<>();
      algorithmList.add(new Image2AsciiAlgorithmJavE(sharedOptions, greyscaleTableConfiguration));
      algorithmList.add(new Image2AsciiAlgorithm4(sharedOptions, greyscaleTableConfiguration));
      algorithmList.add(new Image2AsciiAlgorithm1(sharedOptions, greyscaleTableConfiguration));
      algorithmList.add(new Image2AsciiAlgorithmGradient(gradientConfiguration));
      algorithmList.add(new Image2AsciiAlgorithm3d());
      algorithmList.add(new Image2AsciiAlgorithmEdgeTracing(filter));
      algorithmList.add(new Image2AsciiAlgorithmEdgeDetect(filter));
      algorithmList.add(new Image2AsciiAlgorithmPixelPlate(filter));
      algorithmList.add(new Image2AsciiAlgorithmFeltpen(filter));
      algorithmList.add(new Image2AsciiAlgorithmBraille());
      algorithmList.add(new Image2AsciiAlgorithmKicad());
      return algorithmList.toArray(new AbstractImage2AsciiAlgorithm[0]);
   }

   public static final AbstractImage2AsciiAlgorithm getAlgorithm(
      String name,
      FontModel displayFontModel,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      AbstractImage2AsciiAlgorithm[] algorithms = createAlgorithms(displayFontModel, gradientConfiguration, greyscaleTableConfiguration, filter);
      name = name.toLowerCase();

      for (int i = 0; i < algorithms.length; i++) {
         if (algorithms[i].getName().toLowerCase().equals(name)) {
            return algorithms[i];
         }
      }

      for (int ix = 0; ix < algorithms.length; ix++) {
         if (algorithms[ix].getName().toLowerCase().startsWith(name)) {
            return algorithms[ix];
         }
      }

      return null;
   }

   public static IImage2AsciiAlgorithmItem[] createAlgorithmItems(
      FontModel displayFontModel,
      AsciiGradientConfiguration gradientConfigurations,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      AbstractImage2AsciiAlgorithm[] algorithms = createAlgorithms(displayFontModel, gradientConfigurations, greyscaleTableConfiguration, filter);
      IImage2AsciiAlgorithmItem[] items = new IImage2AsciiAlgorithmItem[algorithms.length];

      for (int i = 0; i < items.length; i++) {
         final AbstractImage2AsciiAlgorithm algorithm = algorithms[i];
         items[i] = new IImage2AsciiAlgorithmItem() {
            @Override
            public IImage2AsciiAlgorithm getAlgorithm() {
               return algorithm;
            }

            @Override
            public IDisposableComponentContainer createAdjustmentComponent() {
               return algorithm.createAdjustmentComponent();
            }
         };
      }

      return items;
   }
}

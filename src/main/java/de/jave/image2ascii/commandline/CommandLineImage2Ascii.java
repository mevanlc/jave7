package de.jave.image2ascii.commandline;

import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.AsciiGreyscaleTableConfigurationInitializable;
import de.jave.image2ascii.ConversionException;
import de.jave.image2ascii.Converter;
import de.jave.image2ascii.Image2AsciiAlgorithmFactory;
import de.jave.jave.JaveConfigurationFileLoader;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.algorithm.gradient.AsciiGradientInitializable;
import de.jave.jave.configuration.IConfigurationFileErrorHandler;
import de.jave.jave.configuration.RuntimeExceptionThrowingErrorHandler;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterInitializable;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class CommandLineImage2Ascii {
   private final AsciiGradientConfiguration gradientConfiguration;
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;
   private final Filter filter;

   public static CommandLineImage2Ascii initialize() {
      IConfigurationFileErrorHandler errorHandler = new RuntimeExceptionThrowingErrorHandler();
      JaveConfigurationFileLoader loader = new JaveConfigurationFileLoader(errorHandler);
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration = loader.initConfigFile(new AsciiGreyscaleTableConfigurationInitializable());
      AsciiGradientConfiguration gradientConfiguration = loader.initConfigFile(new AsciiGradientInitializable());
      Filter filter = loader.initConfigFile(new FilterInitializable());
      return new CommandLineImage2Ascii(gradientConfiguration, greyscaleTableConfiguration, filter);
   }

   public CommandLineImage2Ascii(AsciiGradientConfiguration gradientConfiguration, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration, Filter filter) {
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.gradientConfiguration = gradientConfiguration;
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
      this.filter = filter;
   }

   public void performJob(List<String> options) throws ConversionException {
      if (options.size() == 0) {
         System.err.println("Missing parameter INPUTFILE");
      } else {
         String fileName = options.get(0);
         File imageFile = new File(fileName);
         List<String> settingsList = options.size() == 1 ? new ArrayList<>() : options.subList(1, options.size() - 1);
         CommandLineImage2AsciiSettings settings = this.parseCommandLineSettings(settingsList);
         CharacterPlate cp = this.createConvertedImage(imageFile, settings);
         System.out.println(cp.asString());
      }
   }

   public CharacterPlate createConvertedImage(File imageFile, CommandLineImage2AsciiSettings settings) throws ConversionException {
      String algorithmName = settings.getAlgorithmName().replace('_', ' ');
      FontModel displayFontModel = new FontModel();
      AbstractImage2AsciiAlgorithm algorithm = Image2AsciiAlgorithmFactory.getAlgorithm(
         algorithmName, displayFontModel, this.gradientConfiguration, this.greyscaleTableConfiguration, this.filter
      );
      if (algorithm == null) {
         throw new ConversionException("Illegal parameter; No such algorithm '" + algorithmName + "'");
      } else {
         String specialCharacters = settings.getSpecialCharacters();
         if (specialCharacters != null) {
            algorithm.setSpecialChars(specialCharacters);
         }

         algorithm.setGreyscaleTable(settings.getGreyscaleTable());
         return Converter.convert(
            imageFile,
            settings.getRotate(),
            settings.getShapeFactor(),
            settings.getResultWidth(),
            algorithm,
            settings.isNormalize(),
            settings.isInvert(),
            settings.getGamma(),
            settings.getHighlight(),
            settings.getShadow(),
            settings.getSharpen(),
            settings.getDithering()
         );
      }
   }

   private CommandLineImage2AsciiSettings parseCommandLineSettings(List<String> subList) {
      CommandLineImage2AsciiSettings settings = new CommandLineImage2AsciiSettings(this.greyscaleTableConfiguration);

      for (int i = 0; i < subList.size(); i++) {
         String option = subList.get(i);
         int i1 = option.indexOf(61);
         if (i1 == -1) {
            System.err.println("Unknown option or syntax error: '" + option + "' - ignored.");
         } else {
            String name = option.substring(0, i1).toLowerCase().trim();
            String value = option.substring(i1 + 1).toLowerCase().trim();
            if (name.equals("width")) {
               try {
                  int resultWidth = Integer.parseInt(value);
                  settings.setResultWidth(resultWidth);
               } catch (Exception var19) {
                  System.err.println("Wrong number format for width: '" + value + "' - ignored.");
               }
            } else if (name.equals("algorithm")) {
               settings.setAlgorithmName(value);
            } else if (name.equals("shape")) {
               try {
                  double shapeFactor = Double.parseDouble(value);
                  settings.setShapeFactor(shapeFactor);
               } catch (Exception var18) {
                  System.err.println("Wrong number format for shape factor: '" + value + "' - ignored.");
               }
            } else if (name.equals("charfile")) {
               FileReader fileReader = null;

               try {
                  fileReader = new FileReader(value);
                  BufferedReader br = new BufferedReader(fileReader);
                  String charfile = br.readLine();
                  settings.setSpecialCharacters(charfile);
               } catch (Exception var16) {
                  System.err.println("Error reading charfile '" + value + "':" + var16);
               } finally {
                  IOUtilities.close(fileReader);
               }
            } else if (name.equals("table")) {
               String tableName = TextTools.firstLetterUp(value.replace('_', ' '));
               AsciiGreyscaleTable t = this.greyscaleTableConfiguration.getTable(tableName);
               if (t != null) {
                  settings.setGreyscaleTable(t);
               } else {
                  System.err.println("Greyscale table not found '" + value + "'");
               }
            } else {
               System.err.println("Unknown option '" + option + "' - ignored.");
            }
         }
      }

      return settings;
   }
}

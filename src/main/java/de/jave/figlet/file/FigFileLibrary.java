package de.jave.figlet.file;

import de.jave.figlet.io.FigCategoriesFileParser;
import de.jave.figlet.util.FigException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.dizzy.commons.core.util.Ensure;

public class FigFileLibrary implements IFigFileLibrary {
   public static final String FONT_DEFAULT = "standard";
   private final IFigFileResource fileResource;
   private String[] fontNames;
   private String defaultFontName;
   private String[] controlNames;
   private IFigFontCategorization categorization;

   private FigFileLibrary(IFigFileResource fileResource) {
      Ensure.ensureArgumentNotNull(fileResource);
      this.fileResource = fileResource;
   }

   private static FigFileName[] removeDuplicatesAndSort(FigFileName[] filenames) {
      Set<String> fontSet = new HashSet<>();
      List<FigFileName> fileNameList = new ArrayList<>();

      for (int i = 0; i < filenames.length; i++) {
         if (!fontSet.contains(filenames[i].getName())) {
            fontSet.add(filenames[i].getName());
            fileNameList.add(filenames[i]);
         }
      }

      FigFileName[] resultFileNames = fileNameList.toArray(new FigFileName[0]);
      Arrays.sort(resultFileNames, new Comparator<FigFileName>() {
         public int compare(FigFileName o1, FigFileName o2) {
            String name1 = o1.getName().toLowerCase();
            String name2 = o2.getName().toLowerCase();
            return name1.compareTo(name2);
         }
      });
      return resultFileNames;
   }

   @Override
   public void initialize() throws FigException {
      this.fontNames = new String[0];
      this.controlNames = new String[0];
      FigFileName[] figletFileNames = this.fileResource.loadFigletFileNames();
      if (figletFileNames == null) {
         throw new FigException(
            "Unable to load index file containing a list of all font/control files (" + this.fileResource.getRourceBaseDescription() + ")", 1
         );
      } else {
         figletFileNames = removeDuplicatesAndSort(figletFileNames);
         int fontCount = 0;
         int controlCount = 0;

         for (int i = 0; i < figletFileNames.length; i++) {
            if (figletFileNames[i].isFont()) {
               fontCount++;
            } else if (figletFileNames[i].isControlFile()) {
               controlCount++;
            }
         }

         if (fontCount == 0) {
            throw new FigException("Figlet font list empty (" + this.fileResource.getRourceBaseDescription() + ")", 3);
         } else {
            this.fontNames = new String[fontCount];
            this.controlNames = new String[controlCount];
            int i1 = 0;
            int i2 = 0;

            for (int ix = 0; ix < figletFileNames.length; ix++) {
               if (figletFileNames[ix].isFont()) {
                  this.fontNames[i1] = figletFileNames[ix].getPrintName();
                  if (i1 == 0 || this.fontNames[i1].equals("standard")) {
                     this.defaultFontName = this.fontNames[i1];
                  }

                  i1++;
               } else if (figletFileNames[ix].isControlFile()) {
                  this.controlNames[i2++] = figletFileNames[ix].getPrintName();
               }
            }

            Arrays.sort(this.fontNames);
            Arrays.sort(this.controlNames);

            try {
               this.categorization = new FigCategoriesFileParser().loadCategoriesFile(this.fontNames, this.fileResource, "categoriestree.txt");
            } catch (Exception var9) {
               try {
                  this.categorization = new FigCategoriesFileParser().loadCategoriesFile(this.fontNames, this.fileResource, "categories.txt");
               } catch (Exception var8) {
                  this.categorization = FigFontCategorization.createFallBackCategorization(this.fontNames);
                  throw new FigException("No figlet font categories file found or wrong format (" + this.fileResource.getRourceBaseDescription() + ")", var9, 4);
               }
            }
         }
      }
   }

   @Override
   public String[] getAllFontNames() {
      return this.fontNames;
   }

   @Override
   public String getDefaultFontName() {
      return this.defaultFontName;
   }

   @Override
   public IFigFontCategory getFontCategory(String categoryName) {
      return this.categorization.getChildCategory(categoryName);
   }

   public static FigFileLibrary loadFileLibrary(IFigFileResource fileResource) throws FigException {
      FigFileLibrary fileLibrary = new FigFileLibrary(fileResource);
      fileLibrary.initialize();
      return fileLibrary;
   }

   @Override
   public IFigFileResource getFileResource() {
      return this.fileResource;
   }

   @Override
   public IFigFontCategorization getFontCategorization() {
      return this.categorization;
   }
}

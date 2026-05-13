package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.disy.commons.core.util.Ensure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class JaveDocArchive {
   public static final String FILE_EXTENSION = "javedoc";
   public static final String MANIFEST_ENTRY_NAME = "layers.xml";

   private JaveDocArchive() {
   }

   public static void write(LayeredDocument layeredDocument, File file) throws IOException {
      Ensure.ensureArgumentNotNull(layeredDocument);
      Ensure.ensureArgumentNotNull(file);
      Map<String, String> layerIdToEntryName = createLayerEntryNames(layeredDocument);

      try (ZipOutputStream zip = new ZipOutputStream(new java.io.FileOutputStream(file))) {
         writeManifest(layeredDocument, layerIdToEntryName, zip);
         writeLayer(zip, layerIdToEntryName.get(layeredDocument.getDocumentLayer().getId()), layeredDocument.getDocumentLayer().getContent());
         for (SecondaryLayer layer : layeredDocument.getSecondaryLayers()) {
            writeLayer(zip, layerIdToEntryName.get(layer.getId()), layer.getContent());
         }
      } catch (ParserConfigurationException | TransformerException exception) {
         throw new IOException("Could not write " + MANIFEST_ENTRY_NAME, exception);
      }
   }

   public static LayeredDocument read(File file) throws IOException {
      Ensure.ensureArgumentNotNull(file);
      try (ZipFile zipFile = new ZipFile(file)) {
         ZipEntry manifestEntry = zipFile.getEntry(MANIFEST_ENTRY_NAME);
         if (manifestEntry == null) {
            throw new IOException("Missing " + MANIFEST_ENTRY_NAME + " in " + file);
         }

         Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(zipFile.getInputStream(manifestEntry));
         Element root = document.getDocumentElement();
         NodeList layerNodes = root.getElementsByTagName("layer");
         LayeredDocument layeredDocument = null;
         String activeLayerId = root.getAttribute("activeLayerId");

         for (int i = 0; i < layerNodes.getLength(); i++) {
            Element layerElement = (Element)layerNodes.item(i);
            String kind = layerElement.getAttribute("kind");
            String id = layerElement.getAttribute("id");
            String name = layerElement.getAttribute("name");
            String entryName = layerElement.getAttribute("file");
            CharacterPlate content = readLayer(zipFile, entryName);
            if ("document".equals(kind)) {
               layeredDocument = new LayeredDocument(new DocumentLayer(id, name, content));
            } else if ("secondary".equals(kind)) {
               if (layeredDocument == null) {
                  throw new IOException("Document layer must precede secondary layers in " + MANIFEST_ENTRY_NAME);
               }
               SecondaryLayer layer = new SecondaryLayer(id, name, new Point(parseInt(layerElement, "x"), parseInt(layerElement, "y")), content);
               layer.setVisible(Boolean.parseBoolean(layerElement.getAttribute("visible")));
               layer.setOpaque(Boolean.parseBoolean(layerElement.getAttribute("opaque")));
               layeredDocument.addSecondaryLayer(layer);
            } else {
               throw new IOException("Unknown layer kind: " + kind);
            }
         }

         if (layeredDocument == null) {
            throw new IOException("Missing document layer in " + MANIFEST_ENTRY_NAME);
         }
         if (activeLayerId != null && activeLayerId.length() > 0) {
            layeredDocument.setActiveLayerId(activeLayerId);
         }
         return layeredDocument;
      } catch (ParserConfigurationException | SAXException exception) {
         throw new IOException("Could not read " + MANIFEST_ENTRY_NAME, exception);
      }
   }

   private static void writeManifest(LayeredDocument layeredDocument, Map<String, String> layerIdToEntryName, ZipOutputStream zip)
      throws ParserConfigurationException, TransformerException, IOException {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element root = document.createElement("javedoc");
      root.setAttribute("version", "1");
      root.setAttribute("activeLayerId", layeredDocument.getActiveLayerId());
      root.setAttribute("width", String.valueOf(layeredDocument.getSize().width));
      root.setAttribute("height", String.valueOf(layeredDocument.getSize().height));
      document.appendChild(root);

      Element layersElement = document.createElement("layers");
      root.appendChild(layersElement);
      DocumentLayer documentLayer = layeredDocument.getDocumentLayer();
      Element documentLayerElement = document.createElement("layer");
      documentLayerElement.setAttribute("kind", "document");
      documentLayerElement.setAttribute("id", documentLayer.getId());
      documentLayerElement.setAttribute("name", documentLayer.getName());
      documentLayerElement.setAttribute("visible", "true");
      documentLayerElement.setAttribute("opaque", "true");
      documentLayerElement.setAttribute("file", layerIdToEntryName.get(documentLayer.getId()));
      layersElement.appendChild(documentLayerElement);

      for (SecondaryLayer layer : layeredDocument.getSecondaryLayers()) {
         Element layerElement = document.createElement("layer");
         Point position = layer.getPosition();
         layerElement.setAttribute("kind", "secondary");
         layerElement.setAttribute("id", layer.getId());
         layerElement.setAttribute("name", layer.getName());
         layerElement.setAttribute("visible", String.valueOf(layer.isVisible()));
         layerElement.setAttribute("opaque", String.valueOf(layer.isOpaque()));
         layerElement.setAttribute("x", String.valueOf(position.x));
         layerElement.setAttribute("y", String.valueOf(position.y));
         layerElement.setAttribute("file", layerIdToEntryName.get(layer.getId()));
         layersElement.appendChild(layerElement);
      }

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
      transformer.transform(new DOMSource(document), new StreamResult(buffer));
      ZipEntry entry = new ZipEntry(MANIFEST_ENTRY_NAME);
      zip.putNextEntry(entry);
      zip.write(buffer.toByteArray());
      zip.closeEntry();
   }

   private static Map<String, String> createLayerEntryNames(LayeredDocument document) {
      Map<String, String> result = new HashMap<>();
      Set<String> usedNames = new HashSet<>();
      result.put(document.getDocumentLayer().getId(), createUniqueEntryName(document.getDocumentLayer().getName(), usedNames));
      for (SecondaryLayer layer : document.getSecondaryLayers()) {
         result.put(layer.getId(), createUniqueEntryName(layer.getName(), usedNames));
      }
      return result;
   }

   private static String createUniqueEntryName(String layerName, Set<String> usedNames) {
      String baseName = sanitizeLayerName(layerName);
      String entryName = baseName + ".txt";
      int suffix = 2;
      while (usedNames.contains(entryName)) {
         entryName = baseName + "-" + suffix + ".txt";
         suffix++;
      }
      usedNames.add(entryName);
      return entryName;
   }

   private static String sanitizeLayerName(String layerName) {
      String sanitized = layerName.replaceAll("[^A-Za-z0-9._-]", "_");
      sanitized = sanitized.replaceAll("_+", "_");
      if (sanitized.length() == 0 || ".".equals(sanitized) || "..".equals(sanitized)) {
         return "layer";
      }
      return sanitized;
   }

   private static void writeLayer(ZipOutputStream zip, String entryName, CharacterPlate content) throws IOException {
      zip.putNextEntry(new ZipEntry(entryName));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zip, StandardCharsets.UTF_8));
      String[] lines = content.toStringArray();
      for (int i = 0; i < lines.length; i++) {
         if (i > 0) {
            writer.newLine();
         }
         writer.write(lines[i]);
      }
      writer.flush();
      zip.closeEntry();
   }

   private static CharacterPlate readLayer(ZipFile zipFile, String entryName) throws IOException {
      ZipEntry entry = zipFile.getEntry(entryName);
      if (entry == null) {
         throw new IOException("Missing layer text file: " + entryName);
      }
      java.util.List<String> lines = new java.util.ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
         String line;
         while ((line = reader.readLine()) != null) {
            lines.add(line);
         }
      }
      return new CharacterPlate(lines.toArray(new String[0]));
   }

   private static int parseInt(Element element, String attributeName) throws IOException {
      try {
         return Integer.parseInt(element.getAttribute(attributeName));
      } catch (NumberFormatException exception) {
         throw new IOException("Invalid " + attributeName + " in " + MANIFEST_ENTRY_NAME, exception);
      }
   }
}

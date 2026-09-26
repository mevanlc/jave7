package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.ascii.plate.CharacterSizeModel;
import de.jave.ascii.plate.ruler.AsciiRulerProperties;
import de.jave.ascii.plate.ruler.HorizontalRulerRenderingStrategy;
import de.jave.ascii.plate.ruler.RulerComponent;
import de.jave.ascii.plate.ruler.VerticalRulerRenderingStrategy;
import de.jave.gui.xor.IXorPainter;
import de.jave.jave.plate.ToolManager;
import de.jave.jave.layers.SecondaryLayer;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.jave.rendering.ConnectedLinesViewRenderer;
import de.jave.jave.rendering.GlyphRenderer;
import de.jave.jave.tool.text.RowRange;
import de.jave.jave.watermark.IWatermarkPainter;
import de.jave.lib.CharacterPlate;
import de.jave.lib.cell.Cell;
import de.jave.lib.area.BooleanArea;
import de.jave.lib.gui.IStatusDisplay;
import de.jave.text.TextTools;
import de.jave.undo.LogFile;
import de.jave.undo.UndoManager;
import de.jave.undo.UndoState;
import de.jave.util.RelativeTimeClock;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.text.AttributedCharacterIterator;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class Plate extends JComponent implements MouseListener, MouseMotionListener, KeyListener, InputMethodListener {
   private final JScrollPane scrollPanel;
   private IXorPainter xorPainter = null;
   private final CharacterSizeModel characterSizeModel = new CharacterSizeModel();
   private IStatusDisplay status;
   private List<IWatermarkPainter> watermarkPainters;
   private Selection selection;
   private final JavEApplication jave;
   private final PlateDocument document;
   private boolean keyMark1 = false;
   private boolean keyMark2 = false;
   private boolean keyMark3 = false;
   private boolean mouseRightButton = false;
   private char pendingHighSurrogate;
   private final AsciiRulerProperties rulerProperties;
   private final ToolManager toolManager;
   private final PlatePreferences platePreferences;
   private final ObjectModel<ColorScheme> colorSchemeModel;
   private final CharacterSets characterSets;
   private final ZoomableFontModel zoomFontModel;
   private boolean autoZoomUpdateQueued;
   private boolean disposed;
   private final IChangeListener autoZoomChangeListener = this::scheduleAutoZoom;
   private final IChangeListener repaintChangeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         Plate.this.repaint();
      }
   };
   private final IChangeListener updateRulerPropertiesListener;
   private final IChangeListener updateOnCellScalingChangedListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         Plate.this.updateToNewFont();
      }
   };
   private final RulerComponent verticalRulerComponent;
   private final RulerComponent horizontalRulerComponent;

   public Plate(
      PlateDocument document,
      JavEApplication parent,
      PlatePreferences platePreferences,
      ToolManager toolManager,
      FontModel displayFontModel,
      ObjectModel<ColorScheme> colorSchemeModel,
      CharacterSets characterSets
   ) {
      Ensure.ensureArgumentNotNull(document);
      Ensure.ensureArgumentNotNull(parent);
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(toolManager);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(colorSchemeModel);
      Ensure.ensureArgumentNotNull(characterSets);
      this.platePreferences = platePreferences;
      this.jave = parent;
      this.colorSchemeModel = colorSchemeModel;
      this.toolManager = toolManager;
      this.characterSets = characterSets;
      this.zoomFontModel = new ZoomableFontModel(displayFontModel, platePreferences.getDefaultZoomDelta(), platePreferences.getAutoZoomModel());
      this.rulerProperties = new AsciiRulerProperties(this.characterSizeModel);
      this.rulerProperties.setShowMouseLocation(true);
      this.updateRulerPropertiesListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            Plate.this.updateRulerProperties();
         }
      };
      platePreferences.getRulerModel().addChangeListener(this.updateRulerPropertiesListener);
      this.updateRulerProperties();
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.addKeyListener(this);
      this.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent evt) {
            Tool tool = Plate.this.getCurrentTool();
            if (tool != null) {
               tool.updateModifiers(0);
            }
         }
      });
      this.enableInputMethods(true);
      this.addInputMethodListener(this);
      this.scrollPanel = new JScrollPane(this);
      this.horizontalRulerComponent = new RulerComponent(new HorizontalRulerRenderingStrategy(), this, this.rulerProperties);
      this.scrollPanel.setColumnHeaderView(this.horizontalRulerComponent);
      this.verticalRulerComponent = new RulerComponent(new VerticalRulerRenderingStrategy(), this, this.rulerProperties);
      this.scrollPanel.setRowHeaderView(this.verticalRulerComponent);
      this.scrollPanel.setAutoscrolls(false);
      this.scrollPanel.getViewport().addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent evt) {
            Plate.this.scheduleAutoZoom();
         }
      });
      this.addHierarchyListener(evt -> {
         if ((evt.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && this.isShowing()) {
            this.scheduleAutoZoom();
         }
      });
      platePreferences.getAutoZoomModel().addChangeListener(this.autoZoomChangeListener);
      this.updateScrollIncrements();
      platePreferences.getGridVisibilityModel().addChangeListener(this.repaintChangeListener);
      platePreferences.getMarkIllegalModel().addChangeListener(this.repaintChangeListener);
      platePreferences.getConnectedLinesViewModel().addChangeListener(this.repaintChangeListener);
      platePreferences.getCellScalingModeModel().addChangeListener(this.updateOnCellScalingChangedListener);
      platePreferences.getCellScalingWidthModel().addChangeListener(this.updateOnCellScalingChangedListener);
      platePreferences.getCellScalingHeightModel().addChangeListener(this.updateOnCellScalingChangedListener);
      this.document = document;
      this.selection = document.getSelection();
      this.selection.setPlate(this);
      this.handleDocumentSizeChanged();
      this.setScrollPoint(document.getScrollOrigin());
      this.initializeUndoManager(document);
      this.zoomFontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Plate.this.updateToNewFont();
         }
      });
      this.updateToNewFont();
   }

   private void updateRulerProperties() {
      this.rulerProperties.setHorizontalRulerVisible(this.platePreferences.getRulerModel().getValue());
      this.rulerProperties.setVerticalRulerVisible(this.platePreferences.getRulerModel().getValue());
      this.scheduleAutoZoom();
   }

   public void dispose() {
      this.disposed = true;
      this.platePreferences.getAutoZoomModel().removeChangeListener(this.autoZoomChangeListener);
      this.platePreferences.getGridVisibilityModel().removeChangeListener(this.repaintChangeListener);
      this.platePreferences.getMarkIllegalModel().removeChangeListener(this.repaintChangeListener);
      this.platePreferences.getConnectedLinesViewModel().removeChangeListener(this.repaintChangeListener);
      this.platePreferences.getCellScalingModeModel().removeChangeListener(this.updateOnCellScalingChangedListener);
      this.platePreferences.getCellScalingWidthModel().removeChangeListener(this.updateOnCellScalingChangedListener);
      this.platePreferences.getCellScalingHeightModel().removeChangeListener(this.updateOnCellScalingChangedListener);
      this.platePreferences.getRulerModel().removeChangeListener(this.updateRulerPropertiesListener);
      this.horizontalRulerComponent.dispose();
      this.verticalRulerComponent.dispose();
      this.zoomFontModel.dispose();
   }

   private void updateToNewFont() {
      Font font = this.zoomFontModel.getFont();
      this.characterSizeModel.setCharSize(CharacterMetrics.createCharacterMetrics(font));
      this.updateScrollIncrements();
      this.revalidate();
      this.repaint();
      this.scheduleAutoZoom();
   }

   private void scheduleAutoZoom() {
      if (this.disposed || this.autoZoomUpdateQueued || !this.platePreferences.getAutoZoomModel().getValue()) {
         return;
      }
      this.autoZoomUpdateQueued = true;
      SwingUtilities.invokeLater(() -> {
         this.autoZoomUpdateQueued = false;
         if (this.disposed || !this.isShowing() || !this.platePreferences.getAutoZoomModel().getValue()) {
            return;
         }
         Dimension available = this.getAutoZoomAvailableSize();
         if (available.width <= 0 || available.height <= 0) {
            return;
         }
         int delta = AutoZoom.findDelta(this.zoomFontModel.getOriginalFont(), this.getDocumentSize(), available);
         if (delta != this.zoomFontModel.getSizeDelta()) {
            this.zoomFontModel.setAutoZoomDelta(delta);
            this.scrollPanel.getViewport().setViewPosition(new Point());
         }
      });
   }

   private Dimension getAutoZoomAvailableSize() {
      // Measure the viewport with scrollbars removed: using its current extent
      // would leave unnecessary slack after zooming out makes the bars disappear.
      Insets insets = this.scrollPanel.getInsets();
      int width = this.scrollPanel.getWidth() - insets.left - insets.right;
      int height = this.scrollPanel.getHeight() - insets.top - insets.bottom;
      if (this.scrollPanel.getRowHeader() != null && this.scrollPanel.getRowHeader().isVisible()) {
         width -= this.scrollPanel.getRowHeader().getPreferredSize().width;
      }
      if (this.scrollPanel.getColumnHeader() != null && this.scrollPanel.getColumnHeader().isVisible()) {
         height -= this.scrollPanel.getColumnHeader().getPreferredSize().height;
      }
      if (this.scrollPanel.getViewportBorder() != null) {
         Insets border = this.scrollPanel.getViewportBorder().getBorderInsets(this.scrollPanel);
         width -= border.left + border.right;
         height -= border.top + border.bottom;
      }
      return new Dimension(width, height);
   }

   public PlatePreferences getPlatePreferences() {
      return this.platePreferences;
   }

   private void initializeUndoManager(PlateDocument document) {
      if (document.getUndoManager() == null) {
         document.setUndoManager(new UndoManager(this.getDocumentState(null)));
         document.getUndoManager().setMaxSize(524288);

         try {
            LogFile logFile = new LogFile();
            document.getUndoManager().setLogFile(logFile);
         } catch (Exception var3) {
            System.err.println("Unable to create a new logfile: " + var3);
         }

         RelativeTimeClock clock = new RelativeTimeClock();
         document.setRelativeTimeClock(clock);
      }
   }

   private Tool getCurrentTool() {
      return this.toolManager.getCurrentTool();
   }

   public ToolManager getToolManager() {
      return this.toolManager;
   }

   private void updateScrollIncrements() {
      this.scrollPanel.getVerticalScrollBar().setUnitIncrement(this.getCharHeight());
      this.scrollPanel.getHorizontalScrollBar().setUnitIncrement(this.getCharWidth());
      this.scrollPanel.getVerticalScrollBar().setBlockIncrement(this.scrollPanel.getVisibleRect().height);
   }

   @Override
   public boolean isFocusable() {
      return true;
   }

   public synchronized void setXORPainter(IXorPainter newPainter) {
      IXorPainter previousPainter = this.xorPainter;
      this.xorPainter = newPainter;
      // Use Swing's buffered presentation path. Direct getGraphics() drawing can
      // sit in the Metal queue until its periodic flush, batching cursor moves.
      if (previousPainter != null) {
         this.repaint(previousPainter.getBounds());
      }
      if (newPainter != null) {
         this.repaint(newPainter.getBounds());
      }
   }

   public void setMix(boolean what) {
      if (this.document != null) {
         this.getContent().setMix(what);
      }
   }

   public void addWatermarkPainter(IWatermarkPainter p) {
      if (this.watermarkPainters == null) {
         this.watermarkPainters = new ArrayList<>(10);
      }

      this.watermarkPainters.add(p);
   }

   public void removeWatermarkPainter(IWatermarkPainter p) {
      this.watermarkPainters.remove(p);
   }

   public PlateDocument getDocument() {
      return this.document;
   }

   public boolean hasDocument() {
      return this.document != null;
   }

   public CompressedDocumentState getDocumentState(String actionName) {
      int[][] cContent = this.document.getCompositeContent().glyphPlane();
      Point cLocation = this.getScrollPoint();
      int[][] cSelectionContent = null;
      Point cSelectionLocation = null;
      BooleanArea cSelectionMask = null;
      if (this.hasSelection()) {
         cSelectionContent = this.selection.getContent().glyphPlane();
         cSelectionLocation = this.selection.getLocation();
         cSelectionMask = this.selection.getMask();
      }

      Point cursorLocation = this.document.getCursorLocation();
      String toolName = null;
      if (this.getCurrentTool() != null) {
         toolName = this.getCurrentTool().getName();
      }

      return new CompressedDocumentState(
         cContent, cLocation, cSelectionContent, cSelectionLocation, cSelectionMask, cursorLocation, toolName, actionName, this.colorSchemeModel.getValue()
      );
   }

   private Point getScrollPoint() {
      return new Point(this.scrollPanel.getHorizontalScrollBar().getValue(), this.scrollPanel.getVerticalScrollBar().getValue());
   }

   public void setContent(CharacterPlate cp) {
      if (this.document == null) {
         throw new RuntimeException("ERROR: No document in plate.setContent()");
      } else {
         this.document.setContent(cp);
         this.handleDocumentSizeChanged();
      }
   }

   public void handleDocumentSizeChanged() {
      this.rulerProperties.setDocumentSize(this.getDocumentSize());
      this.revalidate();
      this.repaint();
      this.jave.updateSizeLabelToDocumentSize();
      this.scheduleAutoZoom();
   }

   private void setScrollPoint(Point point) {
      this.scrollPanel.getHorizontalScrollBar().setValue(point.x);
      this.scrollPanel.getVerticalScrollBar().setValue(point.y);
   }

   public boolean isInside(Point location) {
      return this.isInside(location.x, location.y);
   }

   public boolean isInside(int x, int y) {
      return x >= 0 && y >= 0 && x < this.document.getSize().width && y < this.document.getSize().height;
   }

   public boolean isEmpty() {
      return this.document.isEmpty();
   }

   public void crop() {
      if (!this.hasSelection()) {
         // Layers: this is still document crop. Add a separate Layer > Crop path
         // for active-layer selection cropping.
         Insets in = this.document.getContent().getEmptyInsets();
         if (in.left != 0 || in.right != 0 || in.top != 0 || in.bottom != 0) {
            CharacterPlate content = this.document.getContent();
            int newWidth = content.getWidth() - in.left - in.right;
            int newHeight = content.getHeight() - in.top - in.bottom;
            CharacterPlate cp = content.getCopy(in.left, in.top, newWidth, newHeight);
            content.setSize(newWidth, newHeight);
            content.clear();
            boolean wasMix = content.isMix();
            content.setMix(false);
            cp.pasteInto(content, 0, 0);
            content.setMix(wasMix);
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
            this.handleDocumentSizeChanged();
         }
      } else {
         this.cropToSelection();
         this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      }
   }

   private void cropToSelection() {
      if (this.selection.hasSelection()) {
         // Layers: selection crop currently resizes the whole document layer.
         // Layer crop should instead clear outside selection on the active layer.
         int newWidth = this.selection.getWidth();
         int newHeight = this.selection.getHeight();
         CharacterPlate content = this.document.getContent();
         content.setSize(newWidth, newHeight);
         boolean wasMix = content.isMix();
         content.setMix(false);
         content.clear();
         CharacterPlate newContent = this.selection.getContent();
         newContent.pasteInto(content, 0, 0);
         this.selection.delete();
         content.setMix(wasMix);
         this.handleDocumentSizeChanged();
      }
   }

   public boolean cropActiveLayerToSelection() {
      if (!this.selection.hasSelection()) {
         return false;
      }
      CharacterPlate content = this.getContent();
      boolean wasMix = content.isMix();
      content.setMix(false);
      content.clear();
      this.selection.pasteIntoNormal(content);
      content.setMix(wasMix);
      this.handleDocumentSizeChanged();
      return true;
   }

   public void unselect() {
      this.selection.delete();
      this.jave.updateSelectionMenu();
      this.repaint();
   }

   public Point getPasteLocation() {
      return this.hasSelection() ? this.selection.getLocation() : this.document.getCursorLocation();
   }

   public void setSelection(Rectangle region) {
      this.setSelection(region, this.cutSelection(region));
   }

   public void setSelectionContent(CharacterPlate cp) {
      this.setSelectionContent(new JaveSelection(cp));
   }

   public void setSelectionContent(JaveSelection newSelection) {
      Dimension newSize = newSelection.getSize();
      int h = newSize.height;
      int w = newSize.width;
      if (h != 0 && w != 0) {
         Rectangle region = this.selection.getRegion();
         region.height = h;
         region.width = w;
         this.setSelection(region, newSelection);
      } else {
         this.unselect();
      }
   }

   @Deprecated
   public void setSelectionContent(int[][] ch) {
      this.setSelectionContent(new CharacterPlate(ch));
   }

   public void setSelection(Rectangle region, CharacterPlate content) {
      this.selection.set(region, new JaveSelection(content));
      this.jave.updateSelectionMenu();
      this.repaint();
   }

   public void setSelection(Rectangle region, JaveSelection content) {
      if (content.getMask() != null && content.getMask().isEmpty()) {
         this.unselect();
         return;
      }
      this.selection.set(region, content);
      this.jave.updateSelectionMenu();
      this.repaint();
   }

   public Point moveSelection(int dx, int dy, boolean collisionDetection) {
      Point p = this.selection.move(dx, dy, collisionDetection);
      this.repaint();
      return p;
   }

   public Point moveSelection(int dx, int dy) {
      Point p = this.selection.move(dx, dy);
      this.repaint();
      return p;
   }

   public boolean hasSelection() {
      return this.selection == null ? false : this.selection.hasSelection();
   }

   public CharacterPlate getSelectionContent() {
      return this.selection.getContent();
   }

   public Rectangle getSelectionRegion() {
      return this.selection.getRegion();
   }

   public boolean selectionContains(Point location) {
      return this.selection.contains(location);
   }

   public Selection getSelection() {
      return this.selection;
   }

   public ZoomableFontModel getZoomableFontModel() {
      return this.zoomFontModel;
   }

   @Override
   public void setCursor(Cursor cursor) {
      if (!cursor.equals(this.getCursor())) {
         super.setCursor(cursor);
      }
   }

   public void setStatusDisplay(IStatusDisplay status) {
      this.status = status;
   }

   public void showStatus(String s) {
      if (this.status != null) {
         this.status.showStatus(s);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return this.document == null
         ? new Dimension(80 * this.getCharWidth() + 1, 24 * this.getCharHeight() + 1)
         : new Dimension(this.getDocumentWidth() * this.getCharWidth() + AutoZoom.CANVAS_BORDER,
            this.getDocumentHeight() * this.getCharHeight() + AutoZoom.CANVAS_BORDER);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   public int[][] copy(Rectangle rectangle) {
      return this.getContent().getCopy(rectangle).glyphPlane();
   }

   public CharacterPlate cut(Rectangle rectangle) {
      return this.cutSelection(rectangle).getContent();
   }

   public JaveSelection cutSelection(Rectangle rectangle) {
      CharacterPlate content = this.getContent();
      BooleanArea mask = this.getActiveLayerCoverageMask(rectangle);
      int width = content.getWidth();
      int height = content.getHeight();
      CharacterPlate sel = new CharacterPlate(rectangle.width, rectangle.height);

      for (int x = 0; x < rectangle.width; x++) {
         int xx = x + rectangle.x;

         for (int y = 0; y < rectangle.height; y++) {
            int yy = y + rectangle.y;
            if ((mask == null || mask.isSet(x, y)) && xx >= 0 && xx < width && yy >= 0 && yy < height) {
               sel.setForce(x, y, content.glyphAt(xx, yy));
               content.set(xx, yy, ' ');
            }
         }
      }

      this.repaint();
      return new JaveSelection(sel, mask);
   }

   public BooleanArea getActiveLayerCoverageMask(Rectangle rectangle) {
      return this.document.getLayeredDocument().getActiveLayerCoverageMask(rectangle);
   }

   public void setPlateSize(Dimension d) {
      this.setPlateSize(d.width, d.height);
   }

   public void setPlateSize(int newWidth, int newHeight) {
      if (this.document.getSize().width != newWidth || this.document.getSize().height != newHeight) {
         if (this.hasSelection()) {
            this.selection.paste();
            this.selection.delete();
         }

         this.document.resizeDocument(newWidth, newHeight);
         this.handleDocumentSizeChanged();
      }
   }

   @Deprecated
   public int getDocumentHeight() {
      return this.document == null ? -1 : this.document.getSize().height;
   }

   @Deprecated
   public int getDocumentWidth() {
      return this.document == null ? -1 : this.document.getSize().width;
   }

   public Dimension getDocumentSize() {
      return this.document == null ? null : this.document.getSize();
   }

   public void insertLine(int line) {
      CharacterPlate content = this.getContent();
      content.insertLine(line);
      this.handleDocumentSizeChanged();
   }

   public void insertLine(int line, String text) {
      CharacterPlate content = this.getContent();
      content.insertLine(line, text);
      this.handleDocumentSizeChanged();
   }

   public void addColumnsRight(int count) {
      CharacterPlate content = this.getContent();
      content.addColumnsRight(count);
      this.handleDocumentSizeChanged();
   }

   public void removeLine(int line) {
      CharacterPlate content = this.getContent();
      content.removeLine(line);
      this.handleDocumentSizeChanged();
   }

   public void setText(String text) {
      // Layers: full-document text replacement remains DL-oriented for now.
      // Revisit if paste/import flows should target the active layer.
      CharacterPlate parsed = new CharacterPlate(text);
      this.setPlateSize(parsed.getSize());
      CharacterPlate content = this.document.getContent();
      parsed.pasteIntoForce(content, 0, 0);

      this.repaint();
   }

   public CharacterPlate getContent() {
      return this.document.getEditableContent();
   }

   private CharacterPlate getRenderedContent() {
      return this.document.getCompositeContent();
   }

   public JaveSelection getContentOfInterest() {
      if (this.document == null) {
         return null;
      } else {
         return this.hasSelection() ? this.selection.getJaveSelection() : new JaveSelection(this.getRenderedContent());
      }
   }

   public void setContentOfInterest(JaveSelection sel) {
      if (this.hasSelection()) {
         this.setSelectionContent(sel);
      } else {
         this.document.setContent(sel.getContent());
         this.handleDocumentSizeChanged();
      }
   }

   public final void beep() {
      this.getToolkit().beep();
   }

   public void clear() {
      this.unselect();
      this.getContent().clear();
      this.handleDocumentSizeChanged();
   }

   public boolean isMouseRightButton() {
      return this.mouseRightButton;
   }

   public void saveCurrentState() {
      this.saveCurrentState(null);
   }

   /** Refresh cursor metadata without adding an undo step or discarding redo. */
   public void rememberCursorForUndo() {
      if (this.document != null && this.document.getUndoManager() != null) {
         CompressedDocumentState state = (CompressedDocumentState)this.document.getUndoManager().getCurrentState();
         Point cursor = this.document.getCursorLocation();
         state.setCursorX(cursor.x);
         state.setCursorY(cursor.y);
      }
   }

   public void saveCurrentState(String actionName) {
      if (this.document != null) {
         CompressedDocumentState state = this.getDocumentState(actionName);
         long duration = this.document.getRelativeTimeClock().getMillisSinceLastCall();
         duration /= 2L;
         if (duration > 1000L) {
            duration = 1000L;
         }

         state.setDuration((int)duration);
         this.document.getUndoManager().saveCurrentState(state);
         if (!this.document.isModified()) {
            this.document.setModified(true);
            this.jave.updateFrameTitle();
         }

         this.jave.updateUndoRedo();
         this.document.documentChanged();
      }
   }

   public Point getScreenPointFor(Point location) {
      return this.getScreenPointFor(location.x, location.y);
   }

   public Point getScreenPointFor(int x, int y) {
      return this.getScreenPoint(new Point(x, y));
   }

   public Point getScreenPointFor(Point2d p) {
      return this.getScreenPointFor(p.getX(), p.getY());
   }

   public Point getScreenPointFor(double x, double y) {
      Point origin = this.getPlateOrigin();
      return new Point((int)(x * (double)this.getCharWidth() + (double)origin.x), (int)(y * (double)this.getCharHeight() + (double)origin.y));
   }

   public Point getLocationForScreenPoint(Point point) {
      Point location = this.getLocationForScreenPointAnywhere(point);
      if (location.x >= 0 && location.y >= 0 && location.x < this.getDocumentWidth() && location.y < this.getDocumentHeight()) {
         return location;
      } else {
         return !this.getCurrentTool().containsScreenPoint(point) && !this.getCurrentTool().containsLocation(location) ? null : location;
      }
   }

   private Point getLocationForScreenPointAnywhere(Point point) {
      Point plateOrigin = this.getPlateOrigin();
      int x = point.x - plateOrigin.x;
      int y = point.y - plateOrigin.y;
      int xLocation = x / this.getCharWidth();
      int yLocation = y / this.getCharHeight();
      return new Point(xLocation, yLocation);
   }

   public void ensureVisible(Point location) {
      this.scrollRectToVisible(this.getRectangleForLocation(new Point(location.x, location.y)));
   }

   private Rectangle getRectangleForLocation(Point location) {
      Point screenPoint = this.getScreenPoint(location);
      return new Rectangle(screenPoint, new Dimension(this.getCharWidth(), this.getCharHeight()));
   }

   private Point getScreenPoint(Point location) {
      Point origin = this.getPlateOrigin();
      int x = location.x * this.getCharWidth() + origin.x;
      int y = location.y * this.getCharHeight() + origin.y;
      return new Point(x, y);
   }

   public Point2d getRealLocationForScreenPoint(Point point) {
      Point origin = this.getPlateOrigin();
      double x = (double)(point.x - origin.x) / (double)this.getCharWidth();
      double y = (double)(point.y - origin.y) / (double)this.getCharHeight();
      return new Point2d(x, y);
   }

   private Point getPlateOrigin() {
      PlateDocument r = this.getDocument();
      int documentPixelWidth = r.getSize().width * this.getCharWidth();
      PlateDocument r1 = this.getDocument();
      int documentPixelHeight = r1.getSize().height * this.getCharHeight();
      int requiredWidth = documentPixelWidth + 4;
      int requiredHeight = documentPixelHeight + 4;
      int availableWidth = this.scrollPanel.getViewport().getExtentSize().width;
      int availableHeight = this.scrollPanel.getViewport().getExtentSize().height;
      int x = 1;
      int y = 1;
      if (availableHeight > requiredHeight) {
         y += (availableHeight - requiredHeight) / 2;
      }

      if (availableWidth > requiredWidth) {
         x += (availableWidth - requiredWidth) / 2;
      }

      Point origin = new Point(x, y);
      this.rulerProperties.setPlateOrigin(origin);
      return origin;
   }

   public void repaintCursor() {
      this.repaint();
   }

   @Override
   public void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      if (this.document == null) {
         g.setColor(this.colorSchemeModel.getValue().getColorPlateEmpty());
         g.fillRect(0, 0, d.width, d.height);
      } else {
         Point plateOrigin = this.getPlateOrigin();
         ColorScheme colorScheme = this.document.getColorScheme();
         g.setColor(colorScheme.getColorPlateEmpty());
         g.fillRect(0, 0, d.width, d.height);
         int documentWidth = this.getDocumentWidth();
         int documentHeight = this.getDocumentHeight();
         g.setColor(colorScheme.getColorPlateBackground());
         g.fillRect(plateOrigin.x, plateOrigin.y, documentWidth * this.getCharWidth(), documentHeight * this.getCharHeight());
         if (this.watermarkPainters != null) {
            for (int i = 0; i < this.watermarkPainters.size(); i++) {
               IWatermarkPainter painter = this.watermarkPainters.get(i);
               painter.paint(g, plateOrigin, colorScheme, this.getCharWidth(), this.getCharHeight());
            }
         }

         this.paintGrid(g, plateOrigin, colorScheme);
         this.paintDocumentBorder(g, plateOrigin, colorScheme);
         this.paintDocumentContents(g, plateOrigin);
         this.paintActiveSecondaryLayerBounds(g, plateOrigin, colorScheme);
         this.paintIllegalCharacterMarks(g, plateOrigin);
         this.selection.paint(g, colorScheme);
         this.selection.paintBorder((Graphics2D)g, colorScheme);
         g.setFont(this.zoomFontModel.getFont());
         this.getCurrentTool().paintCursorFeature((Graphics2D)g, plateOrigin, colorScheme);
         synchronized (this) {
            if (this.xorPainter != null) {
               this.xorPainter.paintXor(g);
            }
         }
      }
   }

   private void paintActiveSecondaryLayerBounds(Graphics g, Point plateOrigin, ColorScheme colorScheme) {
      SecondaryLayer activeSecondaryLayer = this.document.getActiveSecondaryLayer();
      if (activeSecondaryLayer == null) {
         return;
      }
      Rectangle bounds = activeSecondaryLayer.getBounds();
      if (bounds.width <= 0 || bounds.height <= 0) {
         return;
      }
      int x = plateOrigin.x + bounds.x * this.getCharWidth();
      int y = plateOrigin.y + bounds.y * this.getCharHeight();
      int width = bounds.width * this.getCharWidth();
      int height = bounds.height * this.getCharHeight();
      g.setColor(colorScheme.getColorToolPreview());
      g.drawRect(x, y, width - 1, height - 1);
   }

   private void paintDocumentContents(Graphics g, Point plateOrigin) {
      g.setColor(this.getDocument().getColorScheme().getColorText());
      g.setFont(this.zoomFontModel.getFont());
      RowRange rowRange = this.getVisibleRowRange();
      boolean connectedLinesView = this.platePreferences.getConnectedLinesViewModel().getValue();
      if (connectedLinesView) {
         ConnectedLinesViewRenderer.paintConnectedLinesView(g, this.getRenderedContent(), rowRange, plateOrigin, this.characterSizeModel.getCharacterSize());
      } else {
         CharacterPlate content = this.getRenderedContent();

         for (int y = rowRange.getRowStartIndex(); y <= rowRange.getRowEndIndex(); y++) {
            GlyphRenderer.drawRow(
               g,
               content.glyphPlane()[y],
               plateOrigin.x,
               plateOrigin.y + y * this.getCharHeight() + this.characterSizeModel.getCharacterSize().getAscent(),
               this.getCharWidth()
            );
         }
      }
   }

   private RowRange getVisibleRowRange() {
      Point plateOrigin = this.getPlateOrigin();
      Rectangle clipBounds = this.scrollPanel.getViewport().getViewRect();
      int y0 = clipBounds.y - plateOrigin.y;
      int yStart = y0 / this.getCharHeight();
      if (yStart < 0) {
         yStart = 0;
      }

      int y1 = y0 + clipBounds.height;
      int yEnd = y1 / this.getCharHeight();
      if (yEnd > this.getDocumentHeight() - 1) {
         yEnd = this.getDocumentHeight() - 1;
      }

      return new RowRange(yStart, yEnd);
   }

   private void paintIllegalCharacterMarks(Graphics g, Point plateOrigin) {
      boolean markIllegal = this.platePreferences.getMarkIllegalModel().getValue();
      if (markIllegal) {
         g.setColor(Color.red);
         CharacterPlate content = this.getRenderedContent();
         RowRange rowRange = this.getVisibleRowRange();
         int documentWidth = this.getDocumentWidth();

         for (int y = rowRange.getRowStartIndex(); y <= rowRange.getRowEndIndex(); y++) {
            for (int x = 0; x < documentWidth; x++) {
               int ch = content.glyphAt(x, y);
               if (!this.characterSets.isLegal(ch)) {
                  g.drawOval(
                     plateOrigin.x + x * this.getCharWidth() - 2,
                     plateOrigin.y + y * this.getCharHeight() - 2,
                     this.getCharWidth() + 4,
                     this.getCharHeight() + 4
                  );
               }
            }
         }
      }
   }

   private void paintDocumentBorder(Graphics g, Point plateOrigin, ColorScheme colorScheme) {
      int documentWidth = this.getDocumentWidth();
      int documentHeight = this.getDocumentHeight();
      g.setColor(colorScheme.getColorPlateShadow());
      g.drawRect(plateOrigin.x, plateOrigin.y, documentWidth * this.getCharWidth(), documentHeight * this.getCharHeight());
      g.fillRect(plateOrigin.x + documentWidth * this.getCharWidth(), plateOrigin.y + 3, 3, documentHeight * this.getCharHeight());
      g.fillRect(plateOrigin.x + 2, plateOrigin.y + documentHeight * this.getCharHeight(), documentWidth * this.getCharWidth() - 2, 3);
   }

   private void paintGrid(Graphics g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.hasGrid()) {
         int documentWidth = this.getDocumentWidth();
         int documentHeight = this.getDocumentHeight();
         g.setColor(colorScheme.getColorPlateLines());

         for (int xIndex = 0; xIndex <= documentWidth; xIndex++) {
            g.drawLine(
               plateOrigin.x + xIndex * this.getCharWidth(),
               plateOrigin.y,
               plateOrigin.x + xIndex * this.getCharWidth(),
               plateOrigin.y + documentHeight * this.getCharHeight()
            );
         }

         for (int yIndex = 0; yIndex <= documentHeight; yIndex++) {
            g.drawLine(
               plateOrigin.x,
               plateOrigin.y + yIndex * this.getCharHeight(),
               plateOrigin.x + documentWidth * this.getCharWidth(),
               plateOrigin.y + yIndex * this.getCharHeight()
            );
         }
      }
   }

   public void paintPreview(Graphics g, CharacterPlate pl, ColorScheme colorScheme, int x0, int y0, Point location) {
      this.paintPreview(g, pl, colorScheme, x0, y0, location.x, location.y);
   }

   public void paintPreview(Graphics g, CharacterPlate pl, ColorScheme colorScheme, int x0, int y0, int locationX, int locationY) {
      g.setColor(colorScheme.getColorToolPreview());
      g.setFont(this.zoomFontModel.getFont());
      Point p0 = this.getScreenPointFor(locationX, locationY);
      int height = pl.getHeight();

      for (int y = 0; y < height; y++) {
         int[] line = pl.glyphPlane()[y];
         boolean empty = true;

         for (int i = 0; empty && i < line.length; i++) {
            if (line[i] != ' ') {
               empty = false;
            }
         }

         CharacterPlate content = this.getContent();

         for (int ix = 0; ix < line.length; ix++) {
            if (content.contains(locationX + ix, locationY + y)) {
               line[ix] = content.getPasteResult(line[ix], locationX + ix, locationY + y);
            }
         }

         if (!empty) {
            GlyphRenderer.drawRow(
               g,
               line,
               p0.x - x0 * this.getCharWidth(),
               p0.y + (y - y0) * this.getCharHeight() + this.characterSizeModel.getCharacterSize().getAscent(),
               this.getCharWidth()
            );
         }
      }
   }

   public void showCoordinates(Point location) {
      if (location == null) {
         this.showStatus("");
      } else {
         this.showStatus("(" + location.x + "," + location.y + ")");
      }
   }

   @Override
   public void mouseClicked(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         this.getCurrentTool().mouseClicked(evt.getPoint(), this.getLocationForScreenPoint(evt.getPoint()), evt);
      }
   }

   @Override
   public void mousePressed(MouseEvent evt) {
      this.updateModifiers(evt);
      this.requestFocus();
      this.mouseRightButton = evt.isMetaDown();
      if (this.document != null) {
         this.getCurrentTool().mousePressed(evt.getPoint(), this.getLocationForScreenPoint(evt.getPoint()), evt);
      }
   }

   @Override
   public void mouseReleased(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         this.getCurrentTool().mouseReleased(evt.getPoint(), this.getLocationForScreenPointAnywhere(evt.getPoint()), evt);
         this.mouseRightButton = false;
      }
   }

   @Override
   public void mouseEntered(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document == null) {
         this.setCursor(Cursor.getDefaultCursor());
      } else {
         this.setCursor(this.getCurrentTool().getCursor());
         this.getCurrentTool().mouseEntered(evt.getPoint(), this.getLocationForScreenPoint(evt.getPoint()), evt);
      }
   }

   @Override
   public void mouseExited(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         this.showStatus("");
         this.getCurrentTool().mouseExited(evt.getPoint(), this.getLocationForScreenPoint(evt.getPoint()), evt);
      }
   }

   @Override
   public void mouseMoved(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         Point location = this.getLocationForScreenPoint(evt.getPoint());
         this.showCoordinates(location);
         boolean markIllegal = this.platePreferences.getMarkIllegalModel().getValue();
         if (location != null && markIllegal && this.isInside(location)) {
            int ch = this.getChar(location.x, location.y);
            if (!this.characterSets.isLegal(ch)) {
               this.showStatus("Illegal Character: " + new Cell(ch).text());
            }
         }

         this.getCurrentTool().mouseMoved(evt.getPoint(), location, evt);
         if (location == null) {
            this.setCursor(Cursor.getDefaultCursor());
         } else {
            this.setCursor(this.getCurrentTool().getCursor());
         }
      }
   }

   @Override
   public void mouseDragged(MouseEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         this.scrollRectToVisible(new Rectangle(evt.getX(), evt.getY(), 1, 1));
         Point location = this.getLocationForScreenPointAnywhere(evt.getPoint());
         this.getCurrentTool().mouseDragged(evt.getPoint(), location, evt);
         if (location == null) {
            this.setCursor(Cursor.getDefaultCursor());
         } else {
            this.setCursor(this.getCurrentTool().getCursor());
         }
      }
   }

   @Override
   public void keyTyped(KeyEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         char ch = evt.getKeyChar();
         if (this.keyMark1) {
            if (ch == ' ') {
               this.getCurrentTool().keyTyped('´', evt);
               this.keyMark1 = false;
               return;
            }

            if (ch == 'a') {
               this.getCurrentTool().keyTyped('á', evt);
               this.keyMark1 = false;
               return;
            }

            if (ch == 'o') {
               this.getCurrentTool().keyTyped('ó', evt);
               this.keyMark1 = false;
               return;
            }

            if (ch == 'i') {
               this.getCurrentTool().keyTyped('í', evt);
               this.keyMark1 = false;
               return;
            }

            if (ch == 'u') {
               this.getCurrentTool().keyTyped('ú', evt);
               this.keyMark1 = false;
               return;
            }

            this.keyMark1 = false;
         }

         if (this.keyMark2) {
            if (ch == ' ') {
               this.getCurrentTool().keyTyped('`', evt);
               this.keyMark2 = false;
               return;
            }

            if (ch == 'a') {
               this.getCurrentTool().keyTyped('à', evt);
               this.keyMark2 = false;
               return;
            }

            if (ch == 'o') {
               this.getCurrentTool().keyTyped('ò', evt);
               this.keyMark2 = false;
               return;
            }

            if (ch == 'i') {
               this.getCurrentTool().keyTyped('ì', evt);
               this.keyMark2 = false;
               return;
            }

            if (ch == 'u') {
               this.getCurrentTool().keyTyped('ù', evt);
               this.keyMark2 = false;
               return;
            }

            this.keyMark2 = false;
         }

         if (this.keyMark3) {
            if (ch == ' ') {
               this.getCurrentTool().keyTyped('^', evt);
               this.keyMark3 = false;
               return;
            }

            if (ch == 'a') {
               this.getCurrentTool().keyTyped('â', evt);
               this.keyMark3 = false;
               return;
            }

            if (ch == 'o') {
               this.getCurrentTool().keyTyped('ô', evt);
               this.keyMark3 = false;
               return;
            }

            if (ch == 'i') {
               this.getCurrentTool().keyTyped('î', evt);
               this.keyMark3 = false;
               return;
            }

            if (ch == 'u') {
               this.getCurrentTool().keyTyped('û', evt);
               this.keyMark3 = false;
               return;
            }

            this.keyMark3 = false;
         }

         if (Character.isHighSurrogate(ch)) {
            this.pendingHighSurrogate = ch;
            return;
         }
         if (Character.isLowSurrogate(ch) && this.pendingHighSurrogate != 0) {
            this.getCurrentTool().textTyped(new String(new char[]{this.pendingHighSurrogate, ch}), evt);
            this.pendingHighSurrogate = 0;
            return;
         }
         this.pendingHighSurrogate = 0;
         this.getCurrentTool().textTyped(String.valueOf(ch), evt);
      }
   }

   @Override
   public void inputMethodTextChanged(InputMethodEvent evt) {
      AttributedCharacterIterator text = evt.getText();
      int committedCount = evt.getCommittedCharacterCount();
      if (this.document != null && text != null && committedCount > 0) {
         StringBuilder committed = new StringBuilder(committedCount);
         char ch = text.first();
         for (int i = 0; i < committedCount && ch != AttributedCharacterIterator.DONE; i++) {
            committed.append(ch);
            ch = text.next();
         }
         this.getCurrentTool().textTyped(committed.toString(), null);
      }
      evt.consume();
   }

   @Override
   public void caretPositionChanged(InputMethodEvent evt) {
   }

   @Override
   public void keyPressed(KeyEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         int code = evt.getKeyCode();
         switch (code) {
            case 33:
               this.scrollBlockUp(evt.isControlDown());
               evt.consume();
               return;
            case 34:
               this.scrollBlockDown(evt.isControlDown());
               evt.consume();
               return;
            case 35:
               if (evt.isControlDown()) {
                  this.scrollEnd();
               }
               break;
            case 36:
               if (evt.isControlDown()) {
                  this.scrollHome();
               }
               break;
            case 123:
            case 155:
               this.toggleInsert();
         }

         char ch = evt.getKeyChar();
         if (ch == '+' && evt.isControlDown()) {
            this.zoomFontModel.zoomIn();
            evt.consume();
         } else if (ch == '-' && evt.isControlDown()) {
            this.zoomFontModel.zoomOut();
            evt.consume();
         } else if (code == KeyEvent.VK_0 && evt.isControlDown() && !evt.isShiftDown() && !evt.isAltDown()) {
            this.zoomFontModel.resetZoom();
            evt.consume();
         } else {
            if (this.keyMark1 && code != 93 && (ch < 'a' || ch > 'z') && ch != ' ') {
               this.keyMark1 = false;
            }

            if (this.keyMark2 && code != 93 && (ch < 'a' || ch > 'z') && ch != ' ') {
               this.keyMark2 = false;
            }

            if (this.keyMark3 && code != 92 && (ch < 'a' || ch > 'z') && ch != ' ') {
               this.keyMark3 = false;
            }

            if (code == 93 && !evt.isShiftDown()) {
               if (this.keyMark1) {
                  this.getCurrentTool().keyTyped('´', evt);
                  this.keyMark1 = false;
                  return;
               }

               this.keyMark1 = true;
            }

            if (code == 93 && evt.isShiftDown()) {
               if (this.keyMark2) {
                  this.getCurrentTool().keyTyped('`', evt);
                  this.keyMark2 = false;
                  return;
               }

               this.keyMark2 = true;
            }

            if (code == 92 && !evt.isShiftDown()) {
               if (this.keyMark3) {
                  this.getCurrentTool().keyTyped('^', evt);
                  this.keyMark3 = false;
                  return;
               }

               this.keyMark3 = true;
            }

            this.getCurrentTool().keyPressed(code, evt);
         }
      }
   }

   private void scrollBlockUp(boolean controlDown) {
      JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
      int rowCount = this.getVisibleRowRange().getRowCount() - 3;
      int dy = rowCount * this.getCharHeight();
      verticalScrollBar.setValue(verticalScrollBar.getValue() - dy);
      if (!controlDown) {
         this.getCurrentTool().cursorUp(rowCount);
      }
   }

   private void scrollBlockDown(boolean controlDown) {
      JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
      int rowCount = this.getVisibleRowRange().getRowCount() - 3;
      int dy = rowCount * this.getCharHeight();
      verticalScrollBar.setValue(verticalScrollBar.getValue() + dy);
      if (!controlDown) {
         this.getCurrentTool().cursorDown(rowCount);
      }
   }

   private void scrollEnd() {
      JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
      verticalScrollBar.setValue(verticalScrollBar.getMaximum());
   }

   private void scrollHome() {
      JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
      verticalScrollBar.setValue(verticalScrollBar.getMinimum());
   }

   @Override
   public void keyReleased(KeyEvent evt) {
      this.updateModifiers(evt);
      if (this.document != null) {
         this.getCurrentTool().keyReleased(evt);
      }
   }

   private void updateModifiers(InputEvent evt) {
      Tool tool = this.getCurrentTool();
      if (tool != null) {
         tool.updateModifiers(evt.getModifiersEx());
      }
   }

   private void toggleInsert() {
      this.jave.toggleInsert();
   }

   public void drawString(String text, int x0, int y0) {
      int[] glyphs = TextTools.toGlyphs(text);
      for (int i = 0; i < glyphs.length; i++) {
         this.setChar(x0 + i, y0, glyphs[i]);
      }
   }

   public void pan(int dx, int dy) {
      if (dx != 0 || dy != 0) {
         JScrollBar horizontalScrollBar = this.scrollPanel.getHorizontalScrollBar();
         JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
         horizontalScrollBar.setValue(horizontalScrollBar.getValue() - dx * this.getCharWidth());
         verticalScrollBar.setValue(verticalScrollBar.getValue() - dy * this.getCharHeight());
         this.repaint(50L);
      }
   }

   public String getUndoActionName() {
      return this.document != null && this.document.getUndoManager() != null ? this.document.getUndoManager().getUndoActionName() : "";
   }

   public String getRedoActionName() {
      return this.document != null && this.document.getUndoManager() != null ? this.document.getUndoManager().getRedoActionName() : "";
   }

   public boolean canUndo() {
      return this.document != null && this.document.getUndoManager() != null ? this.document.getUndoManager().canUndo() : false;
   }

   public boolean canRedo() {
      return this.document != null && this.document.getUndoManager() != null ? this.document.getUndoManager().canRedo() : false;
   }

   public synchronized void redo() {
      if (this.document != null && this.canRedo()) {
         CompressedDocumentState d = (CompressedDocumentState)this.document.getUndoManager().redo();
         this.setDocumentState(d);
         this.setToolState(d);
         this.setCursorState(d);
         this.document.documentChanged();
      } else {
         System.err.println("ERROR! redo can not be performed!");
      }
   }

   public synchronized void undo() {
      if (this.document != null && this.canUndo()) {
         CompressedDocumentState d = (CompressedDocumentState)this.document.getUndoManager().undo();
         this.setDocumentState(d);
         CompressedDocumentState next = (CompressedDocumentState)this.document.getUndoManager().getNextState();
         if (next != null) {
            this.setToolState(next);
         }

         this.setCursorState(d);
         this.document.documentChanged();
      } else {
         System.err.println("ERROR! undo can not be performed!");
      }
   }

   private void setDocumentState(CompressedDocumentState d) {
      this.document.setDocumentState(d);
      this.selection = this.document.getSelection();
      this.selection.setPlate(this);
      this.jave.performSetColorScheme(d.getColorScheme());
      this.handleDocumentSizeChanged();
      this.setScrollPoint(this.document.getScrollOrigin());
   }

   private void setToolState(CompressedDocumentState d) {
      String toolName = d.getToolName();
      if (toolName != null) {
         int index = this.toolManager.getToolIndex(toolName);
         if (index != -1) {
            this.jave.setTool(index);
         }
      }
   }

   private void setCursorState(CompressedDocumentState d) {
      Point cursorLocation = this.document.getCursorLocation();
      cursorLocation.x = d.getCursorLocation().x;
      cursorLocation.y = d.getCursorLocation().y;
   }

   public int getChar(int x, int y) {
      return this.getContent().glyphAt(x, y);
   }

   public void setChar(Point location, int ch) {
      if (location != null) {
         this.setChar(location.x, location.y, ch);
      }
   }

   public void setChar(int x, int y, int ch) {
      if (x >= 0 && y >= 0 && x < this.getDocumentWidth() && y < this.getDocumentHeight()) {
         CharacterPlate content = this.getContent();
         if (ch != content.glyphAt(x, y)) {
            content.set(x, y, ch);
         }
      }
   }

   public void setCharForce(Point location, int ch) {
      if (location != null) {
         this.setCharForce(location.x, location.y, ch);
      }
   }

   public void setCharForce(int x, int y, int ch) {
      if (x >= 0 && y >= 0 && x < this.getDocumentWidth() && y < this.getDocumentHeight()) {
         CharacterPlate content = this.getContent();
         if (ch != content.glyphAt(x, y)) {
            content.setForce(x, y, ch);
         }
      }
   }

   public boolean hasGrid() {
      return this.platePreferences.getGridVisibilityModel().getValue();
   }

   public CharacterMetrics getCharacterMetrics() {
      return this.characterSizeModel.getCharacterSize();
   }

   public JComponent getComponent() {
      return this.scrollPanel;
   }

   @Deprecated
   public int getCharHeight() {
      return this.getCharacterMetrics().getHeight();
   }

   @Deprecated
   public int getCharWidth() {
      return this.getCharacterMetrics().getWidth();
   }

   @Deprecated
   public int getCharAscent() {
      return this.getCharacterMetrics().getAscent();
   }

   public ObjectModel<ColorScheme> getColorSchemeModel() {
      return this.colorSchemeModel;
   }
}

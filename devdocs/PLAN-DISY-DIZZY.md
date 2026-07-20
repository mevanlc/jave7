# PLAN — Replacing `net.disy` with `net.dizzy`

Survey + reimplementation plan for the open-sourcing of JavE as JavE7.

## Status — 2026-06-02

Implemented and committed. Java imports now target `net.dizzy.commons.*`, the
minimal replacement API exists under `src/main/java/net/dizzy/commons`, and the
project builds successfully without the removed Disy sources.

Milestone commits:
- `657aa39` — restore the `net.dizzy` commons compile surface and migrate Java imports.
- `349e3e6` — add smoke coverage for model notification, async drop/cancel, and the layout adapter.

Verification completed:
- `./gradlew compileJava`
- `./gradlew compileJava compileTestJava test --rerun-tasks`
- `./gradlew build`

## 0. Context & method

JavE depends pervasively on a third-party house library, `net.disy.commons.*`
("Disy commons"), whose author/rights-holder we cannot reach for permission.
The library has been removed from the build (commit `3f49bf9 "remove disy"`,
888 `.java` files). The project **does not compile** — confirmed:
`./gradlew compileJava` fails with hundreds of `package net.disy.* does not
exist` errors across **510 of 1078** source files.

We are reimplementing a minimal replacement under **`net.dizzy.commons.*`**.

**Cleanroom discipline (semi-cleanroom).** We do **not** read Disy's original
source (it survives in git history but is off-limits). The specification is
**the union of API members JavE actually references** — every constructor,
method, constant, and generic signature exercised by a call site, and nothing
more. We work *forward from JavE's usage*, not backward from Disy's design.
Disy is likely abandonware, so this is risk-reduction, not paranoia: don't
copy code, do infer intent from how results are used.

> Note: the project's `.properties` resource files were already migrated
> (`ext/net/disy/...` → `ext/net/dizzy/...`). Only the `.java` is missing.
> The `dialog`, `fontchooser`, `resources` message bundles survive there and
> are reusable for labels/strings.

**Evidence base.** Tests are nearly absent (8 test files; only 1 touches
disy), so call sites — not tests — are the spec. Every claim below is grounded
in `file:line` references gathered by per-subsystem survey agents.

### Out of scope (decided)
- **Grid layout → MigLayout adapter** (see §5; thin `net.dizzy` adapters over
  `net.miginfocom` MigLayout — call sites untouched, not a per-panel rewrite).
- **File chooser → `JFileChooser`/native.** `net.disy.commons.swing.filechooser.*`
  is dropped. (Only 2 references: `FolderSelectionPanel`, `FileObjectUi` —
  replace inline.) **`fontchooser` is *in* scope** — do not conflate the two.

---

## 1. Scope at a glance

~140 distinct Disy types across two top-level packages. Usage by 2nd-level
package (reference counts):

| Package | Refs | Disposition |
|---|---:|---|
| `core.util` | 311 | reimplement (tiny) — `Ensure` alone ≈ 560 calls |
| `swing.dialog` | 239 | **mixed**: messages → JOptionPane; framework → reimplement |
| `swing.layout` | 231 | MigLayout adapter (call sites untouched; see §5) |
| `core.model` | 219 | reimplement — observer framework, load-bearing |
| `core.message` | 122 | reimplement (small) |
| `swing.action` | 79 | reimplement — `SmartAction` is central |
| `core.io` | 78 | reimplement (small) |
| `core.progress` | 70 | reimplement (small) |
| `swing.fontchooser` | 58 | reimplement |
| `swing.ui` | 52 | reimplement |
| `swing.mousecursor` | 40 | reimplement (+ cursor art) |
| `swing.component` | 34 | reimplement/stdlib (trivial) |
| `swing.events` | 15 | reimplement (trivial) |
| `swing.resources` | 14 | reimplement (+ icon art) |
| `core.exception` | 14 | reimplement (trivial) |
| `swing.widgets` | 12 | reimplement (trivial) |
| `swing.util` | 9 | reimplement (trivial) |
| `swing.color` | 8 | reimplement → `UIManager` |
| `core.asynchronous` | 7 | reimplement (the one tricky concurrency class) |
| `swing.button` | 6 | reimplement |
| `swing.icon` | 5 | reimplement (+ icon art) |
| `swing.toolbar` | 4 | reimplement (trivial) |
| `swing.label` | 4 | reimplement (mnemonic parsing) |
| `swing.image` | 4 | reimplement |
| `swing.{message,list,textfield,showhide,menu,border,font}` | ~13 | reimplement (trivial) |
| `core.string`, `animatedinterpolation` | 2 | reimplement (trivial) |
| `swing.filechooser` | 2 | **drop** → JFileChooser |

**Overall: reimplement nearly everything.** The surface JavE actually
exercises is narrow and shallow even where reference counts are high (`Ensure`
= 8 static guards; `IChangeListener` = 1 method). Two areas carry real design
weight: the **dialog framework** (§4) and the **layout migration** (§5). Two
areas need **art assets** sourced (cursors §3.10, edit/folder icons §3.9).

---

## 2. Build order (dependency DAG)

Restore compilation **bottom-up**. Each phase depends only on those above it.
This makes the migration incremental and testable rather than all-or-nothing.

```
Phase 0  core leaves (no disy deps)
         util · message · model · exception · string
Phase 1  core services      (→ model.listener, message)
         io · progress · asynchronous · animatedinterpolation
Phase 2  swing leaves       (→ core)
         util · color · events · component · widgets · label ·
         icon · image · resources · mousecursor · list · font ·
         textfield · border · menu
Phase 3  layout adapter     (→ swing leaves + MigLayout dep)
         MigLayout-backed grid + cardlayout + builders
Phase 4  action / ui / widgets  (→ core + layout)
         action · ui · message · button · toolbar · showhide · fontchooser
Phase 5  dialog             (→ everything)
         message dialogs (JOptionPane) · UserDialog framework ·
         wizard · input · progress dialogs · SmartTabbedPane
```

After each phase, `./gradlew compileJava` should resolve strictly more
symbols. A **stub-compile completeness check** (§7) catches any call site the
survey missed.

---

## 3. Per-subsystem specifications

Format: **decision** · effort · key surface · load-bearing facts / ambiguities.
Effort scale: trivial (<30 min) · small (≈1–2 h) · medium (½–1 day) · large (multi-day).

### 3.1 `core.util` — reimplement · small total
8 types, all small. Highest *breadth*, lowest *depth*.

- **`Ensure`** — ~560 calls / ~290 files. Static `void` guards (no call site
  consumes a return). Exact set + signatures (message-first ordering is
  load-bearing):
  `ensureArgumentNotNull(Object)` (534×), `ensureArgumentTrue(String,boolean)`,
  `ensureArrayIndex(int index,int min,int max)` (validates `min≤index≤max`),
  `ensureTrue(String,boolean)`, **both** `ensureNotNull(Object)` and
  `ensureNotNull(String,Object)`, `ensureArgumentInstanceOf(Object,Class<?>)`,
  `ensureArgumentArrayContentsNotNull(Object[])`. Throw any unchecked
  exception (`IllegalArgumentException`/`NPE` — no call site catches them).
- **`ObjectUtilities.equals(Object,Object)`** → delegate to `java.util.Objects.equals`.
- **`StringUtilities.isNullOrTrimmedEmpty(String)`** → `s==null||s.trim().isEmpty()`.
- **`IClosure<T>`** — `void execute(T) throws RuntimeException` (keep the
  `throws` clause — 3 anon impls declare it). Sole consumer: `ListenerList.forAllDo`.
- **`IBlock`** — `void execute()` (no-arg; a named type, *not* `Runnable`).
- **`ITransformer<S,T>`** — `T transform(S)` (generic order `<Source,Target>`).
- **`CollectionUtilities.transform(Collection<S>, ITransformer<S,T>) : List<T>`** — order-preserving map.
- **`ArrayUtilities.toPrimitive(Character[]) : char[]`** — only this overload used.

### 3.2 `core.model` — reimplement · medium · **load-bearing observer framework**
The keystone of the app. ~42 JavE classes `extends AbstractChangeableModel`.

Inheritance graph to provide:
```
interface IChangeableModel { addChangeListener(IChangeListener); removeChangeListener(IChangeListener); }
class AbstractChangeableModel implements IChangeableModel { protected void fireChangeEvent(); }
class ObjectModel<T> extends AbstractChangeableModel { T getValue(); void setValue(T); ctor(), ctor(T); }
class BooleanModel extends ObjectModel<Boolean> { ctor()->false, ctor(boolean); }
class FixedOptionsObjectSelectionModel<T> extends AbstractChangeableModel {
    ctor(T[]); void setSelectedValue(T); T[] getAllValues(); T getFirstSelectedValue(); }
```
- **`IChangeListener.stateChanged()`** — **NO arguments** (≈190 anon impls).
  Verbatim and non-negotiable. Do **not** map to `javax.swing.ChangeListener`
  / `PropertyChangeListener` — the no-arg signature is load-bearing.
- **`fireChangeEvent()`** (protected, no-arg) — 115 subclass call sites.
- **`ListenerList<T>`** (in `model.listener`) — generic listener list used for
  JavE's *own* listener types: `ListenerList()`, `add(T)`, `remove(T)`,
  `forAllDo(IClosure<T>)` (snapshot-safe iteration).
- **Ambiguity:** `BooleanModel()` default — must be **`false`**, not `null`
  (several no-arg constructions feed straight into `if (model.getValue())`).
- **Ambiguity (benign):** whether `setValue` fires always or only on change —
  prefer fire-on-change (matches sibling guarded models).

### 3.3 `core.message` — reimplement · small
```
enum MessageType { ERROR, INFORMATION, WARNING, QUESTION, NORMAL }   // exact 5, compared with ==
interface IBasicMessage { MessageType getType(); String getText(); String getTitle(); }
interface IMessage extends IBasicMessage { String getDetailedText(); }
class BasicMessage implements IBasicMessage { ctor(String text, MessageType type); }
class Message implements IMessage { /* 5 ctors below */ }
```
`Message` constructors (resolved from 64 call sites):
`(text, MessageType)`, `(text, Throwable)`, `(title, text, MessageType)`,
`(title, text, Throwable)`, `(title, text, MessageType, Throwable)`.
- **Ambiguity:** default `MessageType` for the cause-bearing/no-type forms →
  use `ERROR` (all are error/exception paths).
- **Ambiguity:** `getDetailedText()` composition → return text + cause message
  when present. Surfaced to users (e.g. wrapped in a `RuntimeException`), so
  must be non-null and meaningful.

### 3.4 `core.exception` — reimplement · trivial
- `UnreachableCodeReachedException extends RuntimeException` — `()` and `(Throwable)` ctors.
- `IExceptionHandler` — `void handle(Throwable)`.
- `PrintStackTraceExceptionHandler implements IExceptionHandler` — `printStackTrace()`.
- `CentralExceptionHandling.setHandler(IExceptionHandler)` (static; consider
  also wiring `Thread.setDefaultUncaughtExceptionHandler`).
- `MessageException` — `ctor(Message)`, `Message getMessageObject()`. **Depends
  on `core.message`** (build that first).

### 3.5 `core.string` — reimplement · trivial
`StringConcatenationBuilder(String separator)`, `append(String)`, `getString()`.
≡ `java.util.StringJoiner`. One call site.

### 3.6 `core.io` — reimplement · small
- **`IOUtilities`** — `close(Closeable)` (quiet, null-tolerant, from `finally`),
  `copyStream(Reader,Writer)`, `copyStream(InputStream,OutputStream)`,
  `copy(File,File) throws IOException`, `readString(Reader) throws IOException`.
  Does not close its stream args. (Reimplement over refactoring 50+ `finally`
  blocks to try-with-resources.)
- **`FileModel`** — observable `File` holder: `FileModel()`, `File getValue()`,
  `void setValue(File)` (null OK), `addChangeListener(IChangeListener)`.
  Effectively `ObjectModel<File>` (depends on `core.model`).
- `IWorkingDirectoryProvider` — `File getWorkingDirectory()`.
- `FileUtilities.createFileNameSuggestion(IWorkingDirectoryProvider, String base, String ext) : File`
  — append integer suffix until non-existent (dedup scheme is the only
  uncertainty; cosmetic).
- `FileDisplayNameUtilities.createShortenedFileName(String path, int maxLen) : String`
  — middle-elide, keep filename tail (cosmetic; JavE has an equivalent in
  `de.jave.lib.io.IoUtilities.getDisplayFilename` to mirror).

### 3.7 `core.progress` — reimplement · small (interfaces) · **JFace-style**
- **`IProgressMonitor`**: `beginTask(String,int)` (`-1` = indeterminate),
  `worked(int)`, `subTask(String)`, `done()`, `isCanceled()`,
  `setCanceled(boolean)`, `beginTaskWithUnknownTotalWork(String)`.
  Do **not** map to `javax.swing.ProgressMonitor` (different protocol; JavE
  classes implement this interface).
- `ICancelable` — `boolean isCanceled()` (inferred name, mirrors monitor).
- `ProgressUtilities.checkInterrupted(ICancelable) throws InterruptedException`
  — checks cancelable flag **and** `Thread.interrupted()`; called in tight loops.
- `NullProgressMonitor` (no-op impl), `NonCancelable.getInstance()` (always-false singleton).
- `ICanceledListener` — `void canceled()`.
- `IInterruptableRunnableWithProgress` — `run(IProgressMonitor, ICancelable) throws InterruptedException, InvocationTargetException`.
- `INonInterruptableRunnableWithProgress` — `run(IProgressMonitor) throws InvocationTargetException`.
  *(The driver of these two is the swing `ProgressMonitorDialog` — §4.)*

### 3.8 `core.asynchronous` — reimplement · medium · **the one tricky class**
- `IJobProcessor<T>` — `void process(ICancelable, T) throws InterruptedException`.
- **`AsynchronousDroppingJobProcessor<T>`** — `ctor(IJobProcessor<T>, IExceptionHandler)`,
  `void startJob(T)`. Single-worker async processor that **coalesces to the
  latest** job: when a new job arrives, the in-flight `ICancelable` is canceled
  (so `checkInterrupted` aborts superseded work) and only the newest pending
  job runs; exceptions route to the handler. Back with a 1-thread executor +
  1-slot replacing queue. **Preserve drop-and-cancel** or superseded
  conversions pile up. ~60–100 LOC; the only place to be careful.

### 3.9 `animatedinterpolation` — reimplement · trivial
`AbstractInterpolator<T>`: abstract `T interpolate(T,T,double t)` + protected
`int interpolate(int,int,double t)` linear helper. 1 subclass.

### 3.10 swing leaves — reimplement · mostly trivial
(`util`, `color`, `events`, `component`, `widgets`, `label`, `icon`, `image`,
`resources`, `mousecursor`, `list`, `font`, `textfield`, `border`, `menu`.)

**Pure stdlib-backed (trivial):**
- `color.SwingColors` — **7 static `Color` getters** (methods, *not* fields):
  `getControlDkShadowColor/getControlLtHighlightColor/getControlShadowColor/
  getControlHighlightColor/getTextAreaBackgroundColor/getTextAreaForegroundColor/
  getTextAreaInactiveForegroundColor` → back with `UIManager`/`SystemColor`.
- `util.GuiUtilities.getWindowFor(...)` → `SwingUtilities.getWindowAncestor`;
  `util.EventDispatchThreadUtilities.ensureIsEventDispatchThread()`;
  `util.ToggleComponentEnabler.connect(AbstractButton, Component...)`;
  `util.RelativePosition` (enum; only `RIGHT` used — consumed by `UserDialog`).
- `component.IComponentContainer { JComponent getContent(); }`,
  `IDisposableComponentContainer extends IComponentContainer { void dispose(); }` (~20 impls),
  `component.Gap(int,int)` (1 site — or just repoint to existing `de.jave.gui.layout.Gap`),
  `component.VerticalLine()` (→ `JSeparator(VERTICAL)`).
- `widgets.HorizontalLine` — `()`, `(int)`, `setMargin(Insets)` (the `int` ctor
  arg semantics are **ambiguous** — likely length/width hint; `JSeparator`
  lacks `setMargin` so reimplement a small JComponent).
- `widgets.AutoWrappingLabel(String[, int width])`, `getContent()`, `setEnabled` —
  HTML-body JLabel.
- `border.TitledPanel(String, JComponent)` → `JPanel` + `TitledBorder`.
- `list.ListSelectionMode` — enum mapping to `ListSelectionModel` ints;
  `SINGLE_SELECTION`, `MULTIPLE_INTERVAL_SELECTION` (+`SINGLE_INTERVAL_SELECTION`
  for completeness); `int getListSelectionMode()`.
- `font.FontFactory.getAwtStyle(style)` — see FontModel ambiguity (§3.11); may
  collapse to identity.
- `textfield.DoubleField(int cols)`: `setValue(double)`/`getValue():double`/
  `addDocumentListener(AbstractDocumentChangeListener)`/`getContent()`.
  `textfield.DoubleModelTextField(int cols, ObjectModel<Double>)`, `getContent()`.

**Mnemonic / behavioral:**
- `events.AbstractDocumentChangeListener` — `DocumentListener` adapter, single
  `protected void documentChanged()` (17 anon impls).
- `events.mouse.OverallMouseListeningPanel(JComponent content)` — JComponent
  that forwards child mouse events to its own listeners (subtle; 1 consumer).
- `label.MnemonicLabelParser.parse(String) : MnemonicLabel`; `MnemonicLabel`
  holds `String getPlainText()` + nullable `Character getMnemonicCharacter()`.
  Parse `&X` markup; handle `&&` → literal `&` (inferred). `label.SmartLabel
  extends JLabel`: `(String)`, `(String, Component labelFor)`.
- `menu.HelpImplementedMenuBar extends JMenuBar` — override `setHelpMenu(JMenu)`
  to actually work (stock `JMenuBar.setHelpMenu` throws under most L&Fs).
- `image.ImageProvider(String basePath)`: `Image getImage(String)`,
  `ImageIcon getImageIcon(String)` — classpath loader, ~6 subclasses/uses
  (pervasive; get signatures exact).
- `image.ClipboardImage(BufferedImage)` — `Transferable`+`ClipboardOwner`,
  offers `DataFlavor.imageFlavor`.

**Icon-art-dependent (reimplement class is trivial; *sourcing images* is the work):**
- `icon.CommonIcons` — constants `FOLDER`, `FOLDER_NEW`.
- `resources.DizzyCommonsSwingIconResources` — constants `CUT`, `COPY`, `PASTE`,
  `UNDO_MODERN`, `REDO_MODERN`.
- `resources.IIconResources` — **inert marker interface** (zero methods called;
  cheapest win — empty interface or drop the `implements`).
- `icon.util.IconUtilities.createBufferedImage(Icon) : BufferedImage`;
  `icon.IBaseIconProvider { Icon getBaseIcon(); }`;
  `icon.BaseIconImageIcon(URL, ImageIcon) extends ImageIcon implements IBaseIconProvider`.
  → **Action:** source replacement gifs (JavE already ships folder/edit icons
  under `icons/`/`ext/`; reuse rather than redraw).

**Cursor-art-dependent:**
- `mousecursor.CursorId` — enum: `CROSSHAIR_SELECTION`, `CROSSHAIR_SELECTION_PLUS`,
  `CROSSHAIR_SELECTION_MINUS`, `TEXT` (exact 4).
- `mousecursor.CursorProvider.getInstance()`, `getCursor(CursorId) : Cursor`
  (40 calls). `TEXT` → `Cursor.getPredefinedCursor(TEXT_CURSOR)`; the three
  crosshair-selection variants need **custom cursor bitmaps**
  (`Toolkit.createCustomCursor`). → **Action:** check `icons/`/`ext/` for
  recoverable art, else draw simple 32×32 crosshair + ±badge.

### 3.11 `swing.fontchooser` — reimplement · medium
- **`model.FontModel`** (~50 uses) — observable `Font` holder extending
  `AbstractChangeableModel`. Ctors: `()`, `(Font)`, `(FontDescription)`.
  Methods: `getFont()`, `setFont(Font)`, `getFontSize()`, `getFontFamilyName()`,
  `getFontStyle()`, `getFontDescription()`, `add/removeChangeListener`.
  - **Ambiguity (decide before coding):** `getFontStyle()` return type. It
    round-trips only through `FontFactory.getAwtStyle(...)`. **Recommend
    collapsing to AWT `int`** and making `getAwtStyle` identity. Then
    `getFontDescription()`/`FontModel(FontDescription)` can be a thin
    `(family,style,size)` holder — keep the distinct method/ctor names to
    preserve the one call site (`ChooseDisplayFontAction`).
- `util.FontUtilities.isFixedWidth(Font) : boolean` + static
  `FontRenderContext DEFAULT_FONT_RENDER_CONTEXT`.
- `view.FontChooserDialog.show(String title) : IDialogResult`, `getFont() : Font`
  (**depends on `dialog.core.IDialogResult`** — §4).
- `view.fixedwidth.FixedWidthFontChooserDialogFactory.getInstance()`,
  `createFontChooserDialog(Component, FontModel) : FontChooserDialog` (monospaced-only).
- `view.FontChooserButton(FontModel[, factory])`, `getContent()`.
- `color.widgets.ColorModel(Color)`, `getColor()` (mutated by `ColorChooserButton`, §4).
- `resources.DizzyCommonsSwingFontChooserIcons.FONT_ICON` (any non-null Icon).

### 3.12 `swing.action` — reimplement · small · **central abstraction**
```
class AbstractDizzyAction extends AbstractAction { public static final String BASE_ICON; }  // getValue key
abstract class SmartAction extends AbstractDizzyAction {
    ctor(String label) | (Icon) | (String label, Icon);
    protected abstract void execute(java.awt.Component parentComponent);   // ← the template method
    setName / setToolTipText / setAcceleratorKey(KeyStroke) / setIcon / setEnabled / isEnabled / getValue
}
class SmartToggleAction extends SmartAction {            // two-way bound to a BooleanModel
    ctor(BooleanModel,String) | (BooleanModel,String,Icon) | (BooleanModel,Icon); }
```
- **The abstract method is `protected void execute(Component)`** — confirmed
  across 37 overrides. *Not* `run`/`runWithEvent`/`actionPerformed`. The base's
  `actionPerformed` resolves a parent `Component` from the event and calls
  `execute`. Map `setName`→`NAME`, `setToolTipText`→`SHORT_DESCRIPTION`,
  `setAcceleratorKey`→`ACCELERATOR_KEY`, icon→`SMALL_ICON`,
  `BASE_ICON`→a custom value key returning the un-decorated icon.
- `ActionWidgetFactory` (static): `createToggleButton(SmartToggleAction):JToggleButton`,
  `createToggleMenuItem(SmartToggleAction):JCheckBoxMenuItem`,
  `createCheckBox(SmartToggleAction):JCheckBox`.

### 3.13 `swing.ui` — reimplement · trivial
```
interface IObjectUi<T> { String getLabel(T); Icon getIcon(T); String getToolTipText(T); }
class AbstractObjectUi<T> implements IObjectUi<T> {     // defaults: toString / null / null
    String getLabel(T value) { return String.valueOf(value); } ... }              // ~30 subclasses
class ObjectUiListCellRenderer extends DefaultListCellRenderer { ctor(IObjectUi); }  // 21 uses
```
Renderer maps `getLabel`→text, `getIcon`→icon, `getToolTipText`→tooltip. Used
as both `JComboBox` and `JList` renderer.

### 3.14 `swing.message` — reimplement · small (depends on `core.message` + icon art)
- `MessageTypeUi.getInstance()`, `getLabel(MessageType):String`, static `Icon infoIcon`.
- `LargeIconMessageTypeUi()`, `getIcon(MessageType):Icon` (large dialog icons).
  → labels reconstructable from migrated `ext/net/dizzy/.../resources` bundles;
  icons need sourcing.

### 3.15 `swing.button` / `swing.toolbar` / `swing.showhide` — reimplement · low–medium
- `button.RolloverButtonFactory.createToggleButton(Action):JToggleButton` (borderless rollover).
- `button.DropDownButton()`: `setToolTipText`, `getComponent():JComponent`,
  `setMenu(JPopupMenu)` (nullable → no menu). Shows popup on click.
- `button.SmartButtonGroup()`: `add(AbstractButton)`, `getButton(int)`,
  `getSelectedIndex()`, `setSelectedIndex(int)` — `ButtonGroup` + index list.
- **`button.ButtonGroupLinker<T>(ObjectModel<T>)`**, `addButton(AbstractButton, T value)`
  — two-way bind a toggle set to an `ObjectModel<T>` (medium; the genuinely
  stateful one).
- `toolbar.ToolBarUtilities.createToolBarButton(Action):AbstractButton`;
  `toolbar.ToolBarBuilder()`: `add(Action)`, `add(JComponent)`, `createToolBar():JToolBar`.
- `showhide.ShowHideButton(BooleanModel, String hideText, String showText)`, `getContent()`;
  `showhide.ShowHideContentPanel(BooleanModel, JComponent content)`, `getContent()`
  — same `BooleanModel` instance drives both; click flips model, model toggles
  child `setVisible`.

---

## 4. The dialog subsystem (§3 of the big decisions)

239 references. Splits into **three tiers** with different dispositions.
**Two hard prerequisites gate all of it:** `core.message` (every dialog
signature speaks `Message`/`MessageType`/`IBasicMessage`) and, for progress
dialogs, `core.progress`.

### Tier 1 — Message dialogs → **REPLACE with `JOptionPane`** · small (~63 sites)
A thin `Message`→JOptionPane shim. Highest single win.
- `MessageDialogFactory.showMessageDialog(Component, Message)` (void, 53 sites);
  `createMessageDialog(...)` → non-modal needs a tiny `JDialog`, not JOptionPane (1 site).
- `MessageDialogUtilities.showYesNoCancelDialog(Component,Message):YesNoCancel`,
  `showOkCancelDialog(...):boolean`, `showYesNoDialog(...):boolean` →
  `JOptionPane.showConfirmDialog` + map return codes.
- `YesNoCancel` — enum `YES,NO,CANCEL` (compared with `==`; `OK` unused).
- `MessageUserDialogConfiguration(Message, buttonConfig)` (1 site),
  `DizzyCommonsSwingDialogMessages` (button-label constants `OK/CANCEL/APPLY/CUT/PASTE`).
- *Build a `Message`→(title, text, JOptionPane messageType, optional
  stack-trace expander) adapter once; everything else is mechanical.*

### Tier 2 — `UserDialog` page framework → **REIMPLEMENT on `JDialog`** · medium–large (bulk of effort)
No stdlib equivalent: JOptionPane cannot host the **live-validation /
disable-OK loop** driven by `createCurrentMessage()` returning an ERROR
message. JavE subclasses `AbstractDialogPage` ~21× and overrides config hooks
via anonymous subclasses, so this is real API surface.

The pattern (trace it once, then replicate):
```
page    = new AbstractDialogPage(defaultMessageText) {        // ctor(String)
            JComponent createContent();                       // abstract
            String getTitle();                                // abstract
            IBasicMessage createCurrentMessage();             // validation hook → getDefaultMessage() or BasicMessage(err)
            IDialogHelpHandler getHelpHandler();              // optional
          };
config  = new DefaultDialogConfiguration<P>(page[, buttonConfig]) {   // generic <P extends IDialogPage>
            IDialogHeaderPanelConfiguration getHeaderPanelConfiguration();  // usually createInvisible()
            boolean performOk(Component);                     // return true to allow close
            boolean performCancel(Component);
            JComponent[] createAdditionalButtons();
          };
dialog  = new UserDialog(parent, config[, RelativePosition]);
result  = dialog.show();                                      // modal IDialogResult
if (result.isCanceled()) ...                                  // the ONLY result method used
```
Members to provide:
- `UserDialog(Component, IDialogConfiguration)`, `(…, RelativePosition)`,
  `(Component, P page)`; `IDialogResult show()`; `void setVisible(boolean)`;
  `void showNonModal(IDialogCloseHandler)`.
- `IDialogConfiguration` / `DefaultDialogConfiguration<P>` with the overridable
  hooks above (default Ok/Cancel close with the right result; default header
  invisible; no extra buttons).
- `AbstractDialogPage` / `IDialogPage`: ctor(String), abstract
  `createContent`/`getTitle`/`createCurrentMessage`, inherited
  `getDefaultMessage()` + `void checkInputValid()` (re-queries
  `createCurrentMessage`; disables OK/Next when `getType()==ERROR`).
- `DialogButtonConfigurationFactory.createCloseOnly()/createOkOnly()/createYesCancel()`
  → a button-set descriptor (which buttons + their result mapping).
- `IDialogResult` (only `boolean isCanceled()`), `DialogResult`.
- `IDialogHeaderPanelConfiguration` + `DialogHeaderPanelConfiguration.createInvisible()` / `createVisibleWithIcon(Icon)`.
- `IDialogHelpHandler.execute(Component)`, `IVetoDialogCloseHandler.handleDialogAboutToClose(IDialogResult,Component):boolean`,
  `IDialogCloseHandler.handleDialogClose(IDialogResult)`,
  `DialogDefaults.getInstance().setFrameIconImages(...)`.
- **Spec ambiguity:** the opaque *button-config token* type returned by
  `DialogButtonConfigurationFactory` is not visible from call sites — design it
  freely (it's only passed into config ctors).

### Tier 3 — Wizard + standalone widgets → **REIMPLEMENT** · medium–high
- `WizardDialog(Component, IWizardConfiguration)`, `IDialogResult show()`.
- `AbstractWizardConfiguration`/`IWizardConfiguration`: `addPages()`,
  `getStartingPage()`, `getNextPage(IWizardPage)`, `getPreviousPage(IWizardPage)`,
  `isHelpAvailable()`, `getHeaderPanelConfiguration()`, `getVetoCloseHandler()`.
- `AbstractWizardPage`/`IWizardPage`: ctor(title, defaultMessageText);
  overrides `createContent`/`createCurrentMessage`/`canFlipToNextPage`/
  `canFinish`/`requestFocus`/`getHelpHandler`; inherited `setWizard`/`getWizard`
  (→ `.getContainer().requestNext()`), `checkInputValid`, `getDefaultMessage`,
  `getMessage`, `getNextPage`, `getTitle`.
  **Spec ambiguity:** the wizard-*container* type from `getWizard().getContainer()`
  (exposes `requestNext()`) isn't visible from call sites — design freely.
- `progress.ProgressMonitorDialog(Component, String title)`,
  `run(INonInterruptableRunnableWithProgress) throws InvocationTargetException`
  — modal dialog running the task off-EDT, updating a bar from `IProgressMonitor`.
  `progress.ProgressMonitorBar()` — embeddable `JComponent` that *is* an
  `IProgressMonitor`; `getContent()`.
- `tabbed.SmartTabbedPane(ISmartTabbedPaneCloseHandler)` — central document
  host: `addTab`, `removeTab(int)`, `setTitleAt`, `get/setSelectedTabIndex`,
  `getContent`, `addTabSelectionChangeListener(IChangeListener)`; handler
  `handleTabClosing(SmartTabbedPane,int)`. `JTabbedPane` + per-tab close buttons.
- `input.text.SmartTextInputDialog.showTextInputDialog(Component, ITextInputDialogConfiguration, String):ITextInputDialogResult`
  (validated input); `input.select.*` (RadioButtonPanel, SelectSomeOutOfMany*,
  SmartSomeOutOfManySelectionDialog, configs) — ride on the page framework +
  `IObjectUi`/selection-model.
- `color.ColorChooserButton(ColorModel)`, `getContent()` — swatch + `JColorChooser`.
- `io.NonEditableFileStringTextField(fileNameModel)`, `getContent()` — read-only field.
- `action.TextComponentSelectAllAction(JTextComponent)`, `action.AbstractCopyAction`
  (abstract `SmartAction`) — or drop for stdlib `DefaultEditorKit` actions.

### Dialog: recommended call
**Reimplement the framework, replace the message boxes.** Do Tier 1 first
(unblocks ~63 sites cheaply once `core.message` exists), then Tier 2 (the spine
— ~21 pages depend on it), then Tier 3. The grid layout (§5) is *independent*
of the framework but builds most page *content*, so schedule it in parallel.

---

## 5. The layout migration (§2 of the big decisions)

**It's SWT GridLayout, reskinned.** Disy's grid is not a bespoke design — its
vocabulary (`numColumns` + `makeColumnsEqualWidth`; per-cell `horizontalSpan`/
`verticalSpan`/`grabExcessHorizontalSpace`/`horizontalAlignment ∈ {BEGINNING,
FILL, END}`/`horizontalIndent`) is **SWT `GridLayout`/`GridData`**, near
field-for-field. `aidocs/NOTES-disy-swing-layouts.md` already observed it reads
"closer to SWT's GridLayout than to AWT." This is the key to the whole section:
the behavioral reference spec is the *public, documented* SWT model, not
anything we must reverse-engineer from Disy.

**Decision (resolved): adapt to MigLayout.** The original "use `GridBagLayout`"
framing was about not owning a layout manager — but vanilla `GridBagLayout`
can't do column-aligned wrap-every-N, so any call-site-preserving path *is* a
reimplementation of Disy's manager. Rather than hand-write that (or churn 82
panels onto raw GBC), we keep `GridDialogLayout`/`GridDialogLayoutData` as thin
`net.dizzy` **adapters that delegate to MigLayout** (`net.miginfocom`,
BSD-licensed). MigLayout natively does count-based wrap + the full per-cell
constraint vocabulary, so a mature library owns the hard sizing math and the
~231 call sites (incl. 161 bare-constant `add`s) compile **untouched**. JavE
already adopts MigLayout for new development, so this is a dependency we want
regardless — and a maintained BSD lib is a strictly better dependency than the
abandonware it replaces.

> **Fallback (no new dependency):** if MigLayout is ever undesirable, the same
> adapters can instead back a hand-written `LayoutManager2` implementing the SWT
> `GridLayout` sizing algorithm (~200–400 LOC). Same call sites, same adapter
> surface — only the engine swaps. Documented here so the choice stays
> reversible; not the plan of record.

The idiom JavE uses:
```
JPanel p = new JPanel(new GridDialogLayout(N, equalColumns));   // N columns, wrap every N adds
p.add(label, GridDialogLayoutData.RIGHT);                       // 83×: align right
p.add(field, GridDialogLayoutData.FILL_HORIZONTAL);             // 84×: grab + fill (weightx=1)
p.add(comp,  new GridDialogLayoutData().setHorizontalSpan(N-1));// span → "span n"
```
Two behaviors the adapter **must** preserve (both native in MigLayout):
1. **Auto-wrapping** — call sites never set coordinates; components wrap to a
   new row every `N` grid columns (spans count toward the N). → `"wrap N"`.
2. The **bare-int `add(comp, FILL_HORIZONTAL)` overload** (implicit
   `new GridDialogLayoutData(constant)`), used 161× — the adapter's
   `addLayoutComponent` translates the int/`GridDialogLayoutData` to a MigLayout
   `CC`/constraint string.

### `GridDialogLayout(Data)` → MigLayout
Layout-level: `new GridDialogLayout(N, false)` → `"wrap " + N`; 4-arg hgap/vgap
→ add `"gap <h>px <v>px"` (+ `insets` as needed). `equalColumns=true` (~6
sites) → column constraints with a size-group (the one spot needing care — see
below). Per-cell `GridDialogLayoutData` / the bare-int constants:

| disy (verbatim) | uses | MigLayout component constraint |
|---|---:|---|
| `FILL_HORIZONTAL` | 84 | `growx, pushx` *(the `push` = the disy `weightx=1` grab; bare `growx` won't claim slack)* |
| `RIGHT` | 83 | `align right` |
| `FILL_BOTH` | 29 | `grow, push` |
| `FILL_VERTICAL` | 2 | `growy, pushy` |
| `setHorizontalSpan(n)` | 29 | `span n` |
| `setVerticalSpan(n)` | 1 | `spany n` |
| `setGrabExcessVerticalSpace(true)` | 1 | `pushy` |
| `setHorizontalIndent(px)` | 5 | `gapleft <px>` |
| `setHorizontalAlignment(GridAlignment.BEGINNING)` | — | `align left` |
| `setHorizontalAlignment(GridAlignment.FILL)` | 1 | `growx` |
| `setVerticalAlignment(GridAlignment.BEGINNING)` | — | `aligny top` |
| `setVerticalAlignment(GridAlignment.END)` | 1 | `aligny bottom` |
| `new GridDialogLayoutData()` (default) | many | `align left` (no grow/fill) |
| invisible `Gap`/filler cell | — | `hidemode 3` (hidden ⇒ no cell) — matches Disy/SWT |

`GridAlignment` exercised exhaustively: `BEGINNING`, `FILL`, `END`.

**Two mapping caveats to get right:**
- **`FILL_HORIZONTAL` is grab + fill** (`weightx=1`), so it maps to
  `growx, pushx`, not bare `growx` — else fields stop stretching. Same logic for
  `FILL_BOTH`/`FILL_VERTICAL` (`push`/`pushy`).
- **`equalColumns=true`** maps to MigLayout column size-groups; it's the one
  construct that needs visual verification (and was already flagged quirky in
  NOTES — the `equalColumns × span` feedback loop). Only ~6 sites; low stakes.

### Components & builders
- `GridDialogLayout(int cols, boolean equalColumns[, int hgap, int vgap])` —
  **82 files construct it** (96× 2-arg, 10× 4-arg; `equalColumns=true` only ~6×).
  The adapter: a `LayoutManager2` wrapping a configured `MigLayout`; ctor builds
  the `"wrap N[, gap …]"` layout constraint.
- `GridDialogLayoutData()` / `(int constant)` + fluent setters (return `this`) —
  an accumulator that renders to a MigLayout `CC`/constraint string when the
  adapter's `addLayoutComponent` sees it.
- `GridDialogPanelBuilder()` / `(boolean)`: `add(component|IDialogComponent)`,
  `createPanel()` — auto-manages columns; subclass hook
  `addAdditionalOptionsComponents(builder)` (4 export pages).
- `GridDialogLayoutDataFactory.createHorizontalSpanData(int[, int constant])` — 2-line static.
- `IDialogComponent { void fillInto(JPanel, int columnCount); int getColumnCount(); }`
  — 15 impls; the builder dispatches on it. **Keep as a thin interface.**
- `EndOfLineMarkerComponent()` — row-break marker → a `"wrap"` add (or `wrap` on
  the prior component).
- `util.ButtonPanelBuilder()` / `(LayoutDirection)`: `add(Action|JButton|varargs)`,
  `createPanel()` — OK/Cancel-style equal-width button strip (`GridLayout(1,n)`/`Box`).
- `util.LayoutUtilities` (static, DPI/spacing): `getDpiAdjusted(int)`,
  `getComponentSpacing()`, `getComponentGroupsSpacing()`, `getDefaultEmptyBorder()`.
- `util.LayoutDirection` — enum `HORIZONTAL`, `VERTICAL`.
- `cardlayout.CardPanel` + `CardPanelKey` → **stdlib `CardLayout`**:
  `add(Component, CardPanelKey)`, `setSelectedSubPanel(CardPanelKey)`,
  `getContent()`; `CardPanelKey` = wrapper around a unique `String`
  (`cardLayout.show(panel, key.toString())`). ~3 sites.

### Scope & effort
- **Add the dependency:** `net.miginfocom:miglayout-swing` (BSD) in
  `build.gradle`. One line; unblocks the whole adapter.
- The real work is the **6 `net.dizzy` adapter classes** (`GridDialogLayout`,
  `GridDialogLayoutData`, `GridDialogLayoutDataFactory`, `GridDialogPanelBuilder`,
  `IDialogComponent`, `EndOfLineMarkerComponent`) + the trivial siblings
  (`GridAlignment`, `LayoutDirection`, `LayoutUtilities`, `ButtonPanelBuilder`,
  `cardlayout.*` → stdlib `CardLayout`). The **82 panels migrate untouched** —
  they keep calling the same `net.dizzy` API.
- **Effort: medium**, but front-loaded into getting the adapter right *once*
  (the constant→`CC` translation + `wrap N`), after which the 82 panels need no
  edits. Manual verification — not editing — concentrates on the long tail:
  `equalColumns=true` (~6), explicit hgap/vgap (~10), the single
  `setVerticalSpan`/`setGrabExcessVerticalSpace` (`TextExportDialogPage`), and
  the lone `GridAlignment.FILL`/`END` cases. Eyeball these against the old build.
- See `aidocs/NOTES-disy-swing-layouts.md` for the Disy grid quirks already
  learned (the `equalColumns=true` × span feedback loop, FILL hiding natural
  widths). MigLayout owns the sizing now, so these become *verification
  checkpoints* on the adapter rather than math you re-derive.

---

## 6. Load-bearing facts & ambiguity register

**Must be verbatim (wrong-but-compiles risk; break many sites):**
- `Ensure.*` method names + **message-first** arg order; dual `ensureNotNull` overloads.
- `IChangeListener.stateChanged()` — **no args** (~190 sites).
- `AbstractChangeableModel.fireChangeEvent()` — protected, no-arg (115 sites).
- `SmartAction.execute(Component)` — protected abstract template (37 sites).
- `MessageType` = exactly `{ERROR, INFORMATION, WARNING, QUESTION, NORMAL}`.
- `IDialogResult.isCanceled()` is the *only* result method JavE calls.
- `GridDialogLayoutData` constants + auto-wrapping + bare-int `add` overload.
- `IClosure.execute(T) throws RuntimeException`; `ITransformer<S,T>` order.
- `CursorId` = exactly the 4 listed; `GridAlignment` = `{BEGINNING, FILL, END}`.

**Design ambiguities (pick a default, note it in code):**
1. `BooleanModel()` default → **`false`** (else NPE in `if(getValue())`).
2. `Message` default `MessageType` for cause-only ctors → **`ERROR`**.
3. `Message.getDetailedText()` composition → text + cause message.
4. `FontModel.getFontStyle()` type → **collapse to AWT `int`**; `getAwtStyle` identity.
5. `FontModel.getFontDescription()` `D` → thin `(family,style,size)` holder.
6. `DialogButtonConfigurationFactory` token type → free design (opaque at call sites).
7. `AbstractWizardPage.getWizard().getContainer()` type → free design (exposes `requestNext()`).
8. `HorizontalLine(int)` arg semantics → length/width hint (cosmetic).
9. `IOUtilities.close` — single `Closeable` param suffices (no site needs overloads).
10. `FileUtilities`/`FileDisplayNameUtilities` exact string algorithms → cosmetic.

**Asset sourcing (class trivial, art is the work):**
- Cursors: 3 crosshair-selection bitmaps (`CursorProvider`).
- Edit icons: `CUT/COPY/PASTE/UNDO_MODERN/REDO_MODERN` (`DizzyCommonsSwingIconResources`).
- Folder icons: `FOLDER/FOLDER_NEW` (`CommonIcons`).
- Message icons: small `infoIcon` + large per-type icons (`MessageTypeUi`/`LargeIconMessageTypeUi`).
- Font icon: `FONT_ICON` (any non-null).
- → Prefer reusing JavE's existing assets under `icons/`, `ext/`, and the
  migrated `ext/net/dizzy/.../resources` bundles over redrawing.

---

## 7. Execution & verification

1. **Implement Phase 0→5** (§2 order). Keep each `net.dizzy` class to the
   surveyed surface — minimal, not a faithful Disy clone.
2. **Per-phase:** `./gradlew compileJava` — each phase resolves strictly more
   symbols; track the shrinking `cannot find symbol` count.
3. **Stub-compile completeness check:** before fleshing out a package, drop in
   empty stubs matching the surveyed surface and compile — any remaining
   `cannot find symbol` for `net.dizzy.*` reveals a call site the survey
   missed. Fold it into the spec.
4. **Smoke-test the behavioral cores** (no Disy test coverage to inherit):
   observer fire/notify (`AbstractChangeableModel`), the drop-and-cancel
   processor (`AsynchronousDroppingJobProcessor`), the dialog validation loop
   (`AbstractDialogPage.checkInputValid` → OK enablement), and a representative
   migrated `GridDialogLayout` (MigLayout-backed) panel rendered against a
   screenshot of the old build.
5. **Drop filechooser inline** (2 sites) and delete the dead `net.disy` import
   trail as packages come online.

### Rough effort
| Block | Effort |
|---|---|
| `core.*` (Phases 0–1) | ~1 day (asynchronous is the only care point) |
| swing leaves + action/ui/buttons/fontchooser (Phases 2,4) | ~2–3 days (+ art sourcing) |
| layout adapter (Phase 3) | ~2–4 days (MigLayout adapter + long-tail verification) |
| dialog framework (Phase 5) | ~1.5–2.5 weeks (Tier 1 ~1 day; Tiers 2–3 the bulk) |

**Critical path:** `core.message` + `core.model` → unblock everything →
dialog framework dominates the schedule. Layout runs in parallel.

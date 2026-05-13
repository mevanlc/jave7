# Layers - MVP1 Plan

> **Status:** planning doc only. Class/interface names below are conceptual
> placeholders until implementation starts. The plan is grounded in the current
> text-document stack (`PlateDocument`, `Plate`, `TextDocumentEditor`,
> `SavePerformer`, `JavEApplication.open`) but does not require a specific
> final package layout.

## Summary

MVP1 introduces layers for JavE text documents. A layered document is still
edited through the existing `Plate` interaction model, but the document owns a
stack of layer contents instead of exactly one `CharacterPlate`.

Each layer is "much like a separate document": it has its own character content,
visibility, opacity, and display name. The visible editor view is a composited
plate produced from the visible layers, while drawing tools mutate only the
active layer.

User-visible result:

- A toggleable Layers panel appears on the right side of a text document.
- The panel contains a vertical stack of layer cards with preview thumbnails.
- Bottom actions support create, delete, duplicate, and basic ordering actions;
  drag-and-drop reordering is the primary reordering UX.
- Double-clicking a layer card name turns it into an inline text field.
- Layers can be hidden and revealed.
- Secondary layers can be toggled Opaque or non-Opaque.
- The Layers panel can be hidden without changing the layer stack.
- A new Layers menu provides `Flatten` and `Flatten Visible`.
- The status bar exposes lightweight MVP controls:
  `Layer count: N [-][+] | Current layer: n`. `+` creates a secondary layer,
  `-` is present but can remain unimplemented initially, and clicking the current
  layer cycles the active layer.
- Layered documents are saved as `.javedoc` zip files containing `layers.xml`
  plus one text file per layer.

## Assumptions

- A legacy plain text document opens as a one-layer document.
- Layer 1 is the document layer. It is immovable, always visible, always Opaque,
  and always the full document size.
- Secondary layers are positioned within the document and have dynamic bounding
  boxes determined by their non-space characters.
- A new secondary layer is inserted above the active layer, starts visible,
  starts Opaque, and starts with size 0 because it contains no non-space
  characters.
- Layered save format is determined by layer count in MVP1: any document with
  more than one layer saves as `.javedoc`, even if every secondary layer is empty.
- The layer stack UI shows topmost layers at the top of the list, with the
  document layer fixed at the bottom.
- Layer stack ordering and selection behavior should follow Photoshop-style UX
  unless this plan calls out a JavE-specific exception.
- The Layers panel visibility is a user preference. Default: hidden.
- `Flatten` and `Flatten Visible` are explicit layer operations, not side effects
  of hiding the panel. `Flatten` treats hidden layers as visible before
  flattening; `Flatten Visible` ignores hidden layers.
- Normal `Save` preserves classic JavE behavior for one-layer documents: one text
  document remains one solo `.txt` file unless the user explicitly saves in a
  layered format.
- When a document has more than one layer, normal `Save` refuses to silently
  flatten. It prompts the user that preserving layers requires a dedicated
  `.javedoc` file and that saving as one text file requires flattening first.
- Layered documents are not compatible with animation/game documents in MVP1.
- Layer grouping is explicitly deferred to MVP2.

## Goals

- Preserve existing behavior for single-layer text documents.
- Route all existing drawing tools to the active layer with minimal per-tool
  changes.
- Render a correct visible composite across visible, hidden, opaque, and
  non-Opaque layers.
- Persist enough information to reopen a layered document without losing layer
  names, order, active layer, visibility, opacity, secondary-layer positions,
  document size, and color scheme.
- Keep existing document tabs, close prompts, recent files, crash recovery, and
  save-before-close behavior coherent for layered documents.
- Keep the MVP scoped to text documents. Animation frames and game documents do
  not gain layer support in MVP1.

## Non-goals

- Layer groups.
- Blend modes beyond Opaque versus non-Opaque.
- Per-layer color schemes.
- User-controlled per-layer canvas sizes. Secondary-layer bounds are algorithmic
  in MVP1.
- Transform controls, masks, clipping, alpha, or partial transparency.
- Exploded layer-folder load/save. MVP1 writes `.javedoc` zip archives only;
  MVP2 can add load/save support for an unpacked folder.
- Reworking every drawing algorithm around a new document abstraction in one
  large step. The implementation should use adapter points where practical.
- Making "making of" animation replay layer-aware, unless it falls out cheaply
  from the undo/logging work. Preserve current single-layer replay behavior.

## Existing Integration Points

### Document model

Current text documents are represented by `PlateDocument`
(`src/main/java/de/jave/jave/PlateDocument.java`). The document owns:

- one `CharacterPlate content`
- one `Selection`
- one cursor location
- one `ColorScheme`
- one `UndoManager`
- one optional `File`

MVP1 should extend or wrap this shape rather than bypassing it. Most of JavE
already reaches document content through `Plate`, so the cleanest implementation
path is to preserve the common `PlateDocument` entry points while teaching the
document about layer state.

### Editor/view model

`TextDocumentEditor` builds one `Plate` and returns `plate.getComponent()`.
That component is currently the scroll pane owned by `Plate`.

The Layers panel belongs at this editor-content boundary:

- Keep `Plate` focused on rendering/editing the plate.
- Wrap `plate.getComponent()` plus the Layers panel in a `JSplitPane` in
  `TextDocumentEditor` or a small `LayeredTextEditorPanel`.
- Let `TextDocumentEditor.getContent()` return the split pane for text
  documents.

This avoids changing the application tabbed pane (`JaveMainPanel`) into a
layers-aware container.

### Save/open

Current text save/open is centered in:

- `PlateDocument.load(File, ColorScheme)`
- `PlateDocument.save(...)`
- `SavePerformer.performSaveDocument*`
- `JavEApplication.open(Component, File)`
- `JaveFileType.guessType(File)`

Layer-preserving save/load should integrate through those paths so recent files,
save-before-close, and revert keep working.

### Undo

`Plate` initializes one `UndoManager` with `CompressedDocumentState`.
`CompressedDocumentState` currently stores one packed content payload, selection
state, scroll origin, cursor location, tool name, action name, and color scheme.

MVP1 should introduce a layered undo state or extend the existing state format
so normal layer content edits, layer structural operations, and explicit flatten
operations can be restored coherently. If flatten undo turns out to be
disproportionately expensive, defer only that part to MVP2 and document the
tradeoff in the implementation notes.

## Layer Semantics

### Data model

Conceptual model:

```java
final class LayeredDocument {
   DocumentLayer documentLayer;
   List<SecondaryLayer> secondaryLayers;
   String activeLayerId;
   Dimension size;
}

interface Layer {
   String id;
   String name;
}

final class DocumentLayer implements Layer {
   String id;
   String name;
   boolean visible; // always true
   boolean opaque;  // always true
   CharacterPlate content;
}

final class SecondaryLayer implements Layer {
   String id;
   String name;
   boolean visible;
   boolean opaque;
   Point position;
   CharacterPlate content;
   Rectangle bounds;
}
```

MVP1 deliberately avoids a sentinel cell value or persisted filled-cell mask.
The only transparent glyph is the space character, and only when the cell is
outside a secondary layer's bounds or inside a non-Opaque secondary layer's
bounds. This keeps layer text files simple and human-viewable.

Layer 1 is the document layer:

- It is always the full document size.
- It is always visible.
- It is always Opaque.
- It cannot be reordered.

Secondary layers:

- Have a position within the document.
- Use `position` as the document coordinate of the current bounds/content
  top-left.
- Have dynamic bounds derived from their non-space characters.
- Are transparent outside their bounds, even when Opaque.
- A secondary layer with no non-space characters has size 0 and contributes
  nothing to the composite.
- Spaces inside an Opaque secondary layer's bounds can hide lower content.

For a secondary layer, bounds are recomputed from the leftmost, rightmost,
topmost, and bottommost non-space characters. Spaces between those non-space
extents are inside the bounds; leading/trailing spaces outside those extents are
outside the bounds and do not contribute.

Cell states:

- **Non-space:** contributes that character when the layer is visible.
- **Space inside Opaque layer bounds:** contributes a blank and hides lower cells.
- **Space outside layer bounds:** contributes nothing and lets lower cells show.
- **Space inside non-Opaque secondary-layer bounds:** contributes nothing and lets
  lower cells show.

### Compositing

Composite from bottom to top:

1. Start with an empty `CharacterPlate` filled with spaces.
2. Composite the document layer; it is always visible, full-size, and Opaque.
3. For each visible secondary layer, bottom to top:
   - Ignore cells outside the layer's bounds.
   - If the cell is a non-space character, write that character at
     `layer.position + cell`.
   - If the cell is a space and the layer is Opaque, write a space at
     `layer.position + cell`.
   - If the cell is a space and the layer is non-Opaque, skip it.

Hidden layers do not participate.

### Editing

Tools edit only the active layer.

The compatibility bridge should make this true without touching every tool at
once:

- `Plate.getContent()` or its effective replacement must expose an editable
  active-layer content view to tool code.
- `Plate` rendering, illegal-character marking, copy-visible/export preview, and
  flattened save must use the visible composite.
- Existing methods like `setChar`, `setCharForce`, paste, delete, resize,
  and selection operations need deliberate mapping to active-layer edits.
- Tools may draw on the selected secondary layer outside its current bounds.
  Drawing a non-space character there expands the layer bounds on the next bounds
  recomputation. Drawing only spaces outside the current bounds does not expand
  the bounds.

Recommended mapping:

- Vanilla copy should follow Photoshop's lead: copy from the active layer.
- Add or expose a separate copy-visible/copy-flattened command for the visible
  composite.
- Selection behavior should follow Photoshop's lead. Where JavE has no obvious
  equivalent, prefer active-layer semantics for editing and visible-composite
  semantics for preview/export.
- Eraser-like clearing can continue writing spaces in MVP1. On a non-Opaque
  secondary layer that reveals lower cells. On an Opaque secondary layer it
  creates blank coverage only when the erased cell remains inside the layer's
  non-space-derived bounds.

### Resizing and Pan

Canvas size remains document-wide in MVP1. Resizing changes the document layer's
size and clips secondary-layer content/bounds to the new document extents. It
does not reposition layers in MVP1.

Pan is a viewport/scroll operation over the whole document. It should not mutate
the document layer, secondary-layer positions, or any other document structure.

## UI

### Shell

Text document content becomes:

```text
JSplitPane(HORIZONTAL_SPLIT)
  left:  existing Plate scroll component
  right: LayersPanel
```

Panel details:

- The right component is collapsible/toggleable through a View menu item and,
  if convenient, a top-toolbar toggle.
- Store the last divider location in preferences if this is low effort.
- The panel is hidden by default unless the user preference says otherwise.
- Hiding or showing the panel never changes the document's layer stack.
- Creating a second layer may show the panel for discoverability, but only if
  that does not fight the user's explicit visibility preference.

### Layers menu

Add a Layers menu with at least:

- Show Layers Panel
- New Layer
- Delete Layer
- Duplicate Layer
- Crop
- Flatten
- Flatten Visible

`Crop` is an active-layer command, distinct from document crop in the Edit menu.
It is best understood as a discoverable shortcut for keeping the current
selection on the active layer and clearing everything outside that selection. On
secondary layers, the normal non-space bounds recomputation then auto-shrinks the
layer. On the document layer, the layer remains document-sized.

`Flatten` and `Flatten Visible` replace the layer stack with one visible Opaque
document layer and mark the document modified. Both should be undoable in MVP1
if the layered undo state makes that practical.

`Flatten` acts as if every layer were visible before flattening. `Flatten
Visible` uses only currently visible layers, so hidden layers are ignored and
effectively deleted by the operation.

### Layer cards

Each layer card should show:

- preview thumbnail
- name
- active/selected state
- visibility toggle
- Opaque toggle

The document layer card should show the same identity/status information as
other layers, but its move controls are disabled and its visibility and Opaque
toggles are locked on.

Interactions:

- Single-click selects/activates the layer.
- Double-click the name starts inline rename.
- Enter commits rename.
- Escape cancels rename.
- Drag a secondary layer card to reorder the stack.
- Context menu can mirror bottom actions if low effort, but is not required.

### Bottom actions

MVP1 bottom buttons:

- New layer
- Delete layer
- Duplicate layer
- Move up
- Move down

Drag-and-drop is still required for rearrange. Move up/down buttons are the
keyboard-accessible and implementation-simple fallback.

Rules:

- Delete is disabled when only one layer remains.
- Delete is disabled for the document layer.
- If a bug or import path ever leaves the document with zero layers, create one
  fresh blank visible opaque document layer as a defensive fallback.
- Duplicate inserts the copy above the source and makes it active.
- Duplicating the document layer creates a secondary layer above it.
- New inserts above active and makes the new layer active.
- Move up/down are disabled at stack boundaries and for the document layer.

## Persistence

### Flattened save

Flattened save writes the current visible composite through the existing text
save path. This remains compatible with all current `.txt` consumers.

Consequences:

- Layer metadata is lost.
- Hidden layers are omitted.
- The result is ordinary visible text with no layer semantics.

### Layer-preserving save

Preserving layers saves to a pkzip archive with a `.javedoc` extension:

```text
example.javedoc
  layers.xml
  Background.txt
  Lettering.txt
  ...
```

`layers.xml` stores:

- format version
- document width and height
- active layer id
- color scheme
- layer order
- per-layer id
- per-layer kind (`document` or `secondary`)
- per-layer display name
- per-secondary-layer visible flag
- per-secondary-layer opaque flag
- per-secondary-layer position
- per-layer text filename

The separate text files store each layer's character grid. Their filenames are
derived from the layer names, filesystem-sanitized and uniquified. The XML file
tracks each layer's true display name and the text filename, so external readers
do not need to infer metadata from filenames.

The document layer text file is document-sized. Secondary layer text files should
store the layer's current bounding-box content, with the position persisted in
`layers.xml`. Empty secondary layers have no non-space bounds; represent them in
the simplest round-trippable way, such as an empty text file plus XML metadata.

All layer metadata belongs in `layers.xml`, not hidden or encoded in the layer
text files. That keeps the text files human-viewable now and leaves room for
MVP2 exploded-folder workflows and external editing.

### Open

Opening a `.javedoc` file should create one text editor document with the
restored layer stack.

Implementation notes:

- Extend file choosing and `JaveFileType.guessType` to recognize `.javedoc`.
- Read the archive, parse `layers.xml`, then load each referenced layer text
  file.
- Keep `DocumentManager.isAlreadyOpen(File)` working by storing the `.javedoc`
  file as the document backing target.
- Add the `.javedoc` file to recent files.
- Defer opening or saving exploded layer folders to MVP2.

### Save command behavior

Recommended command behavior:

- `Save` on a one-layer `.txt` document writes `.txt` exactly like classic JavE.
- `Save` on a `.javedoc` document preserves layers.
- `Save` on an unsaved or `.txt`-backed multi-layer document shows a dialog:

```text
Your document contains multiple layers and must be saved in a dedicated
.javedoc file to preserve the layers. If you would like to save the document as
a single text file, first use the Layers menu to flatten the document to a
single layer.

[OK] [Cancel]
[x] Keep showing this reminder.
```

- `OK` continues to a `.javedoc` save chooser.
- `Cancel` aborts the save.
- The reminder checkbox controls whether this explanatory dialog appears again,
  but skipping the reminder must not silently flatten. It should go directly to
  the `.javedoc` save chooser for multi-layer documents.
- `Save As...` should include `.txt` for one-layer documents and `.javedoc` for
  layered documents. If the user explicitly picks `.txt` while the document has
  multiple layers, show the same flatten-first message.

## Work Units

### Unit 1 - Layer content model

**Change.**

- Add model classes for layer stack and layer metadata.
- Add compositing logic with unit tests covering visibility, opacity, spaces, and
  stack order.
- Add dynamic secondary-layer bounds calculation from non-space characters.
- Add conversion helpers:
  - plain `CharacterPlate` to one-layer document
  - layer stack to flattened `CharacterPlate`
  - duplicate layer
  - resize/pan document layer and secondary-layer positions

**Acceptance.**

- A new empty Opaque secondary layer has size 0 and does not hide lower content.
- A space inside an Opaque secondary layer's bounds hides lower content.
- A space inside a non-Opaque secondary layer's bounds lets lower content show.
- Spaces outside a secondary layer's dynamic bounds do not hide lower content.
- Drawing a non-space character outside a secondary layer's current bounds
  expands that layer's bounds.
- Hidden layers do not affect the composite.

### Unit 2 - Bridge `PlateDocument` to layers

**Change.**

- Teach `PlateDocument` to own a layer stack while preserving current
  single-layer APIs where practical.
- Enforce document-layer invariants: first layer, immovable, document-sized, and
  always visible and Opaque.
- Track secondary-layer positions and dynamic non-space-derived bounds.
- Add active-layer accessors for editing.
- Add composite accessors for rendering and flattened save.
- Track document modified state when layer metadata or active-layer content
  changes.

**Acceptance.**

- Existing single-layer create/open/save still behaves as before.
- Existing tool code can still compile against the document/plate bridge.
- Creating a secondary layer changes only the active layer when drawing.

### Unit 3 - Rendering and tool editing path

**Change.**

- Update `Plate` so paint/render paths use the composite plate.
- Update editing paths so tool writes mutate active-layer content.
- Ensure selection, paste, copy, crop, and resize operate against active
  layer where that matches user intent, and against composite only for
  read-only/export/preview behavior.
- Keep pan as a viewport/scroll operation over the whole document, not a
  per-layer edit.
- Add copy-visible/copy-flattened behavior separately from vanilla active-layer
  copy.

**Acceptance.**

- Drawing on a secondary layer does not mutate the document layer.
- Switching active layers changes where subsequent edits land.
- Copy/export preview reflects the visible composite.
- Erasing on a non-Opaque secondary layer reveals lower visible content.
- Vanilla copy targets the active layer; copy-visible targets the composite.

### Unit 4 - Undo state

**Change.**

- Add `CompressedLayeredDocumentState` or extend `CompressedDocumentState` to
  capture the layer stack and active layer id.
- Restore layer stack, active layer, selection, cursor, scroll origin, tool
  state, and color scheme on undo/redo.
- Save undo snapshots for layer content edits and structural actions
  (new/delete/duplicate/reorder/visibility/opaque/rename/flatten).

**Acceptance.**

- Undo/redo restores content edits on the active layer.
- Undo/redo restores layer visibility, opacity, order, and names.
- Undo/redo restores the pre-flatten layer stack unless flatten undo is
  explicitly deferred to MVP2 due to implementation cost.

### Unit 5 - Layers panel UI

**Change.**

- Add `LayersPanel`, layer list/card renderer, preview thumbnail generator, and
  bottom action bar.
- Add card selection/activation.
- Add inline rename.
- Add visibility and Opaque toggles.
- Add drag-and-drop reorder plus move up/down fallback actions.
- Reuse `AsciiToThumbnailConverter` for previews where possible.

**Acceptance.**

- Layer cards reflect current order, active layer, names, visibility, and
  opacity.
- Thumbnails update after layer edits.
- Double-click rename works with Enter/Escape.
- Drag reorder updates the composite immediately.
- Delete is impossible when only one layer remains.
- The document layer cannot be dragged, moved, hidden, deleted, or made
  non-Opaque.

### Unit 6 - Split pane and toggle action

**Change.**

- Wrap text editor content in a horizontal `JSplitPane`.
- Add a View menu action to show/hide the Layers panel.
- Add a user preference for default Layers panel visibility.
- Add the Layers menu and explicit flatten actions.
- Keep focus returning to the `Plate` after panel actions.

**Acceptance.**

- The panel appears on the right of text documents.
- Hiding the panel never prompts and never changes layers.
- The default preference keeps the panel hidden unless changed.
- Flatten actions reduce the document to exactly one visible Opaque document
  layer.

### Unit 7 - Layer-preserving persistence

**Change.**

- Add XML read/write for `layers.xml`.
- Write layer text files.
- Write and read `.javedoc` zip archives.
- Generate sanitized unique layer text filenames from layer names.
- Support opening `.javedoc` files.
- Support saving `.javedoc`-backed documents.
- Add multi-layer Save/Save As flow with the flatten-first message for `.txt`.

**Acceptance.**

- A layered document saved preserving layers reopens with the same visual
  composite, layer order, names, active layer, visibility, opacity, and
  secondary-layer positions.
- A layered document saved flattened opens as a one-layer plain text document.
- Recent files can reopen a `.javedoc` file.
- Save-before-close correctly saves or cancels for `.javedoc` docs.

### Unit 8 - Polish and regression pass

**Change.**

- Add user-facing strings to `messages.properties` and relevant localized files
  where the repo expects them.
- Check keyboard traversal and focus behavior in the panel.
- Verify icons or fallback button labels are consistent with existing JavE UI.
- Run build/test checks.
- Manually exercise core drawing tools against multiple layers.

**Acceptance.**

- Existing single-layer workflows have no visible regression.
- Layer panel controls do not steal focus permanently from the editor.
- Save, Save As, Open, Revert, Close, and Recent Files work for both plain text
  and `.javedoc` documents.
- Manual smoke tests cover draw, type, erase, selection copy/paste, resize, pan,
  undo/redo, flatten, and reopen.

## Suggested Implementation Order

1. Build and test the pure layer model/compositor.
2. Bridge `PlateDocument` and `Plate` with one active layer while preserving
   single-layer behavior.
3. Add active-layer editing and composite read paths.
4. Add layered undo state.
5. Add the Layers panel against the model.
6. Add `JSplitPane` integration, panel toggle preference, and Layers menu.
7. Add layer-preserving save/load.
8. Finish UI polish, messages, tests, and manual regression.

## Test Plan

Automated tests:

- compositor with visible/hidden layers
- compositor with opaque/non-Opaque filled spaces
- document layer is always full-size, immovable, visible, and Opaque
- empty Opaque secondary layer has zero bounds and does not affect composite
- secondary-layer bounds expand when drawing non-space characters outside the
  current bounds
- layer duplicate preserves content and metadata
- layer reorder changes composite
- XML round trip preserves metadata and text-file references
- `.javedoc` zip round trip preserves layers
- flattened save output matches composite

Manual tests:

- Open old `.txt`, save it, diff output.
- Create a secondary layer and verify it does not hide lower content while empty.
- Draw outside a secondary layer's current bounds and verify the bounds expand.
- Draw on top, hide/reveal top.
- Draw spaces on an opaque top layer and confirm lower cells disappear.
- Toggle top layer non-Opaque and confirm lower cells show through spaces.
- Confirm the document layer cannot be moved, hidden, deleted, or made
  non-Opaque.
- Rename, duplicate, delete, and reorder layer cards.
- Toggle panel off with two layers and verify layers remain intact.
- Use Layers > Flatten with hidden layers and confirm hidden content is included.
- Use Layers > Flatten Visible with hidden layers and confirm hidden content is
  ignored.
- Undo flatten if MVP1 flatten undo lands.
- Save preserving layers, close, reopen `.javedoc`.
- Save flattened, close, reopen as one-layer text.
- Undo/redo content edits and layer structural edits.

## MVP2 Deferred Items

- Layer groups.
- Group visibility/opacity.
- Exploded `.javedoc` folder load/save.
- More advanced layer context menu.
- Import/export selected layer.
- Merge down without hiding the panel.
- Per-layer lock state.
- Per-layer color or style metadata.
- Persisted filled-cell masks or sentinel cell values if the space-only
  transparency model proves too limiting.
- Layer-aware "making of" animation replay.

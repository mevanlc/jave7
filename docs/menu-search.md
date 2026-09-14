# Primary menus and search

`JaveMenuBar` declares the ordered primary menu tree through `MenuHub`. Every executable leaf binds a `MenuCommand` (stable ID, Swing `Action`, persistence policy) to its menu item. Submenus and separators describe layout. Search traverses this same tree, including dynamic sections; it has no separate list of commands to maintain.

When adding a command:

- Use `hub.item(id, action)` or `hub.toggle(id, action)`, and create submenus with `hub.menu(id, label)`.
- Reuse existing shared Actions. Put enabled, selected, name, and accelerator changes on the Action. Disabled ancestor menus also prevent search execution.
- Choose an ID independent of the display label. Dynamic targets need target identity, not a mutable list index. Unsaved window IDs are session-only; file targets use normalized absolute URIs.
- Rebuild dynamic sections on model changes. The hub observes menu structure and Action changes. Its state refresher checks availability before results are shown or invoked.

Search uses Cmd/Ctrl+Shift+/ or the field at the right end of the menu bar. Query words match item labels in any order, ignoring case. Tab/Down and Shift-Tab/Up cycle through the field and results. Enter focuses the first result from the field and executes a focused result. Hover reveals the actual menu location; single click executes. Typing from a result continues the query, and Escape clears and dismisses it.

The result window is 320 logical pixels wide, right aligned with the field, with at most 12 rows and no scrolling. Overflow reserves a final informational row. Empty queries show distinct commands invoked through search, most recent first. This history persists without a retention cap; ordinary menu, toolbar, and shortcut use does not add history.

The controller restores the previous focus owner and invokes the current bound menu item, preserving clipboard delegation, checkbox behavior, and radio groups. History is recorded before invocation so Exit can persist itself. Missing dynamic targets are omitted from results and resolved again before execution.

Run `./gradlew test` for automated coverage. Run `./gradlew menuSearchUiProbe` in a desktop session for the native keyboard/mouse probe. It opens its own window and uses in-memory preferences plus recovery files under `build/menu-search-probe`; screenshots are saved in its `output` directory. It requires desktop automation permission for Java Robot. This probe is deliberately separate from the normal test suite.

package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.text.SuggestionResult;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IBlock;
import net.disy.commons.swing.dialog.input.text.suggest.ISuggestionTextFieldConfiguration;
import net.disy.commons.swing.dispose.IDisposable;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.cardlayout.CardPanelKey;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.list.IListMouseHandler;
import net.disy.commons.swing.list.JListMouseListener;
import net.disy.commons.swing.list.TypeSafeListModel;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;
import net.disy.commons.swing.util.EventDispatchThreadUtilities;
import net.disy.commons.swing.util.GuiUtilities;

public class SuggestionWindow<T> implements IDisposable {
   private static final CardPanelKey LIST_COMPONENT_KEY = new CardPanelKey();
   private static final CardPanelKey BUSY_COMPONENT_KEY = new CardPanelKey();
   private static final CardPanelKey NO_RESULTS_COMPONENT_KEY = new CardPanelKey();
   private final FocusAdapter focusLossListener = new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
         if (!e.isTemporary()) {
            SuggestionWindow.this.handleFokusLossInWindowOrTextField();
         }
      }
   };
   private final ObjectModel<IMessage> optionalStateMessageModel = new ObjectModel<>();
   private final JList list = new JList();
   private final CardPanel cardPanel = new CardPanel();
   private final JComponent hookComponent;
   private final ISuggestionTextFieldConfiguration<T> configuration;
   private JWindow window;
   private TypeSafeListModel<T> listModel;
   private boolean busy = false;
   private final SuggestionWindowDisposeListener disposeListener;

   public SuggestionWindow(final JComponent hookComponent, ISuggestionTextFieldConfiguration<T> configuration, final IBlock doubleClickCallback) {
      Ensure.ensureArgumentNotNull(hookComponent);
      Ensure.ensureArgumentNotNull(configuration);
      Ensure.ensureArgumentNotNull(doubleClickCallback);
      this.disposeListener = new SuggestionWindowDisposeListener(this);
      this.hookComponent = hookComponent;
      this.configuration = configuration;
      this.list.setCellRenderer(new ObjectUiListCellRenderer(configuration.getObjectUi()));
      IListMouseHandler listMouseHandler = new IListMouseHandler() {
         @Override
         public void handleDoubleClick(Object[] selectedValues) {
            doubleClickCallback.execute();
         }

         @Override
         public void handleContextMenuClick(Object[] selectedValues) {
         }
      };
      JListMouseListener.attachTo(this.list, listMouseHandler);
      this.list.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            hookComponent.requestFocus();
         }
      });
      hookComponent.addFocusListener(this.focusLossListener);
   }

   public void showWithQueryStarted() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.window == null) {
         JScrollPane listScrollPane = new SuggestionWindowScrollPane(this.list);
         this.cardPanel.add(this.createOptionalMessageDecoratedPanel(listScrollPane), LIST_COMPONENT_KEY);
         this.cardPanel.add(new SuggestionWindowBusyComponent(this.configuration.getBusyLabelText()).getContent(), BUSY_COMPONENT_KEY);
         String noResultLabelText = this.configuration.getNoResultLabelText();
         if (noResultLabelText != null) {
            this.cardPanel
               .add(this.createOptionalMessageDecoratedPanel(new SuggestionWindowNoResultsComponent(noResultLabelText).getContent()), NO_RESULTS_COMPONENT_KEY);
         }

         Window parentWindow = GuiUtilities.getWindowFor(this.hookComponent);
         this.window = new JWindow(parentWindow);
         this.window.getContentPane().setLayout(new BorderLayout());
         this.window.getContentPane().add(this.cardPanel.getContent(), "Center");
         this.window.addFocusListener(this.focusLossListener);
      }

      this.cardPanel.setSelectedSubPanel(BUSY_COMPONENT_KEY);
      this.window.pack();
      WindowToComponentOnScreenPositioner.adjustPosition(this.hookComponent, this.window);
      this.window.setVisible(true);
      this.disposeListener.attachTo(this.hookComponent);
      this.busy = true;
   }

   private JComponent createOptionalMessageDecoratedPanel(JComponent component) {
      JPanel panel = new JPanel(new GridDialogLayout(1, true, 0, 0));
      panel.add(component, GridDialogLayoutData.FILL_BOTH);
      panel.add(new OptionalMessageButton(this.optionalStateMessageModel).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      return panel;
   }

   protected void handleFokusLossInWindowOrTextField() {
      if (this.window != null && this.window.isVisible()) {
         if (!this.hookComponent.hasFocus() && !this.window.hasFocus()) {
            this.dispose();
         }
      }
   }

   public void showLoadedSuggestions(SuggestionResult<T> suggestionResult) {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      this.listModel = new TypeSafeListModel<>(suggestionResult.getItems());
      this.list.setModel(this.listModel);
      this.busy = false;
      if (suggestionResult.getItems().isEmpty()) {
         if (this.configuration.getNoResultLabelText() == null) {
            this.dispose();
            return;
         }

         this.cardPanel.setSelectedSubPanel(NO_RESULTS_COMPONENT_KEY);
      } else {
         this.cardPanel.setSelectedSubPanel(LIST_COMPONENT_KEY);
      }

      IMessage stateMessage = suggestionResult.getOptionalStateMessage();
      this.optionalStateMessageModel.setValue(stateMessage);
      this.selectFirst();
      this.window.pack();
      WindowToComponentOnScreenPositioner.adjustPosition(this.hookComponent, this.window);
   }

   private void setSelectedIndexAndAssureVisible(int selectionIndex) {
      this.list.setSelectedIndex(selectionIndex);
      this.list.ensureIndexIsVisible(selectionIndex);
   }

   @Override
   public void dispose() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.window != null) {
         this.disposeListener.detachFrom(this.hookComponent);
         this.window.setVisible(false);
      }
   }

   public void moveSelectionUp() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.window != null) {
         int selectedIndex = this.list.getSelectedIndex();
         if (selectedIndex < 0) {
            this.window.getToolkit().beep();
         } else if (selectedIndex == 0) {
            this.selectLast();
         } else {
            this.setSelectedIndexAndAssureVisible(selectedIndex - 1);
         }
      }
   }

   public void selectLast() {
      this.setSelectedIndexAndAssureVisible(this.list.getModel().getSize() - 1);
   }

   public void moveSelectionDown() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.window != null) {
         int selectedIndex = this.list.getSelectedIndex();
         if (selectedIndex < 0) {
            this.window.getToolkit().beep();
         } else if (selectedIndex >= this.list.getModel().getSize() - 1) {
            this.selectFirst();
         } else {
            this.setSelectedIndexAndAssureVisible(selectedIndex + 1);
         }
      }
   }

   public void selectFirst() {
      this.setSelectedIndexAndAssureVisible(0);
   }

   public T getSelectedItem() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.busy) {
         return null;
      } else if (this.window == null) {
         return null;
      } else {
         int selectedIndex = this.list.getSelectedIndex();
         return selectedIndex == -1 ? null : this.listModel.getElementAt(selectedIndex);
      }
   }

   public boolean isVisible() {
      return this.window.isVisible();
   }
}

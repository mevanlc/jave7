package net.dizzy.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.FlowLayout;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JTextField;

import org.junit.Test;

import de.jave.jave.application.resources.JaveImageProvider;
import net.dizzy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.dizzy.commons.core.asynchronous.IJobProcessor;
import net.dizzy.commons.core.exception.IExceptionHandler;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.swing.dialog.tabbed.SmartTabbedPane;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class CommonsReplacementSmokeTest {
   @Test
   public void objectModelFiresOnlyOnChange() {
      ObjectModel<String> model = new ObjectModel<>("before");
      AtomicInteger changes = new AtomicInteger();
      model.addChangeListener(changes::incrementAndGet);

      model.setValue("before");
      model.setValue("after");

      assertEquals("after", model.getValue());
      assertEquals(1, changes.get());
   }

   @Test
   public void booleanModelDefaultsToFalse() {
      assertFalse(new BooleanModel().getValue());
   }

   @Test
   public void droppingJobProcessorCancelsSupersededWorkAndRunsNewestJob() throws Exception {
      CountDownLatch firstStarted = new CountDownLatch(1);
      CountDownLatch firstCanFinish = new CountDownLatch(1);
      CountDownLatch newestFinished = new CountDownLatch(1);
      AtomicInteger lastFinished = new AtomicInteger();

      AsynchronousDroppingJobProcessor<Integer> processor = new AsynchronousDroppingJobProcessor<>(
         new IJobProcessor<Integer>() {
            @Override
            public void process(ICancelable cancelable, Integer job) throws InterruptedException {
               if (job.intValue() == 1) {
                  firstStarted.countDown();
                  assertTrue(firstCanFinish.await(2, TimeUnit.SECONDS));
                  assertTrue(cancelable.isCanceled());
                  return;
               }
               lastFinished.set(job.intValue());
               newestFinished.countDown();
            }
         },
         new IExceptionHandler() {
            @Override
            public void handle(Throwable throwable) {
               throw new AssertionError(throwable);
            }
         }
      );

      processor.startJob(1);
      assertTrue(firstStarted.await(2, TimeUnit.SECONDS));
      processor.startJob(2);
      processor.startJob(3);
      firstCanFinish.countDown();

      assertTrue(newestFinished.await(2, TimeUnit.SECONDS));
      assertEquals(3, lastFinished.get());
   }

   @Test
   public void gridDialogLayoutAcceptsBareIntAndDataConstraints() {
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("Name"), GridDialogLayoutData.RIGHT);
      panel.add(new JTextField("value"), new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));

      panel.doLayout();

      assertEquals(2, panel.getComponentCount());
   }

   @Test
   public void imageProviderLoadsClasspathResourcesWithNormalizedBasePath() {
      assertTrue(JaveImageProvider.getInstance().getImageIcon("javeicon16.gif").getIconWidth() > 1);
   }

   @Test
   public void smartTabbedPaneUsesLeadingTabStrip() {
      SmartTabbedPane tabbedPane = new SmartTabbedPane((pane, index) -> {});
      tabbedPane.addTab("First", new JPanel());
      tabbedPane.addTab("Second", new JPanel());
      AtomicInteger selections = new AtomicInteger();
      tabbedPane.addTabSelectionChangeListener(selections::incrementAndGet);

      JPanel tabBar = (JPanel)tabbedPane.getContent().getComponent(0);
      assertEquals(FlowLayout.LEFT, ((FlowLayout)tabBar.getLayout()).getAlignment());

      tabbedPane.setSelectedTabIndex(1);
      assertEquals(1, tabbedPane.getSelectedTabIndex());

      tabbedPane.setTitleAt(1, "Renamed");
      assertEquals("Renamed", ((JToggleButton)tabBar.getComponent(1)).getText());
      assertEquals(1, selections.get());

      tabbedPane.removeTab(1);
      assertEquals(0, tabbedPane.getSelectedTabIndex());
      assertEquals(2, selections.get());
   }

   @Test
   public void smartTabbedPaneActiveTabLighterThanInactiveTab() {
      java.awt.Color activeBg = SmartTabbedPane.TabButton.getActiveBackground();
      java.awt.Color inactiveBg = SmartTabbedPane.TabButton.getInactiveBackground();

      double activeBrightness = (0.299 * activeBg.getRed() + 0.587 * activeBg.getGreen() + 0.114 * activeBg.getBlue()) / 255.0;
      double inactiveBrightness = (0.299 * inactiveBg.getRed() + 0.587 * inactiveBg.getGreen() + 0.114 * inactiveBg.getBlue()) / 255.0;

      assertTrue("Active tab background must be lighter than inactive tab background", activeBrightness > inactiveBrightness);
   }

   @Test
   public void smartTabbedPanePreventsDeselectionOfActiveTab() {
      SmartTabbedPane tabbedPane = new SmartTabbedPane((pane, index) -> {});
      tabbedPane.addTab("Tab 1", new JPanel());
      tabbedPane.addTab("Tab 2", new JPanel());

      JPanel tabBar = (JPanel)tabbedPane.getContent().getComponent(0);
      JToggleButton tab1Button = (JToggleButton) tabBar.getComponent(0);
      JToggleButton tab2Button = (JToggleButton) tabBar.getComponent(1);

      assertTrue(tab1Button.isSelected());
      assertFalse(tab2Button.isSelected());

      // Clicking already active tab must NOT deselect it
      tab1Button.doClick();
      assertTrue("Active tab must remain selected after clicking it", tab1Button.isSelected());
      assertFalse(tab2Button.isSelected());
      assertEquals(0, tabbedPane.getSelectedTabIndex());

      // Clicking inactive tab selects it
      tab2Button.doClick();
      assertFalse(tab1Button.isSelected());
      assertTrue(tab2Button.isSelected());
      assertEquals(1, tabbedPane.getSelectedTabIndex());

      // Clicking active tab again must remain selected
      tab2Button.doClick();
      assertTrue(tab2Button.isSelected());
      assertEquals(1, tabbedPane.getSelectedTabIndex());
   }

   @Test
   public void smartTabbedPaneClosingAndRecreatingTabs() {
      SmartTabbedPane tabbedPane = new SmartTabbedPane((pane, index) -> {});
      tabbedPane.addTab("Tab 1", new JPanel());
      tabbedPane.addTab("Tab 2", new JPanel());
      tabbedPane.addTab("Tab 3", new JPanel());

      assertEquals(0, tabbedPane.getSelectedTabIndex());

      // Close middle tab
      tabbedPane.removeTab(1);
      assertEquals(0, tabbedPane.getSelectedTabIndex());

      // Close remaining tabs
      tabbedPane.removeTab(0);
      tabbedPane.removeTab(0);
      assertEquals(-1, tabbedPane.getSelectedTabIndex());

      // Create new tab after emptying
      tabbedPane.addTab("New Tab", new JPanel());
      assertEquals(0, tabbedPane.getSelectedTabIndex());
      JPanel tabBar = (JPanel)tabbedPane.getContent().getComponent(0);
      assertTrue(((JToggleButton)tabBar.getComponent(0)).isSelected());
   }
}


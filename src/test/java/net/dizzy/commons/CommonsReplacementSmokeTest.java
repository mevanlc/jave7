package net.dizzy.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.Test;

import net.dizzy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.dizzy.commons.core.asynchronous.IJobProcessor;
import net.dizzy.commons.core.exception.IExceptionHandler;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.progress.ICancelable;
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
}

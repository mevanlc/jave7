package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import net.disy.commons.swing.dialog.userdialog.page.AbstractBasicDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IBasicDialogPage;
import net.disy.commons.swing.events.ICheckInputValidListener;

public abstract class AbstractAggregationDialogPage extends AbstractBasicDialogPage {
   protected final String title;

   public AbstractAggregationDialogPage(String title) {
      this.title = title;
   }

   protected abstract IBasicDialogPage[] getPages();

   @Override
   public final String getTitle() {
      return this.title;
   }

   @Override
   public final void setInputValidListener(ICheckInputValidListener inputValidListener) {
      super.setInputValidListener(inputValidListener);
      this.forAllPages(new AbstractAggregationDialogPage.IPageClosure() {
         @Override
         public void execute(IBasicDialogPage page) {
            page.setInputValidListener(AbstractAggregationDialogPage.this.getCheckInputValidListener());
         }
      });
   }

   @Override
   public void updateInputValid() {
      this.forAllPages(new AbstractAggregationDialogPage.IPageClosure() {
         @Override
         public void execute(IBasicDialogPage page) {
            page.updateInputValid();
         }
      });
   }

   protected final void forAllPages(AbstractAggregationDialogPage.IPageClosure closure) {
      for (IBasicDialogPage page : this.getPages()) {
         closure.execute(page);
      }
   }

   @Override
   public final void dispose() {
      super.dispose();

      for (IBasicDialogPage page : this.getPages()) {
         page.dispose();
      }
   }

   @Override
   public final void enter() {
      for (IBasicDialogPage page : this.getPages()) {
         page.enter();
      }
   }

   @Override
   public final void leave() {
      for (IBasicDialogPage page : this.getPages()) {
         page.leave();
      }
   }

   protected interface IPageClosure {
      void execute(IBasicDialogPage var1);
   }
}

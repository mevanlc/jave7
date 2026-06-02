package de.jave.jave.browser;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.model.AbstractChangeableModel;

public class ThumbnailsModel extends AbstractChangeableModel {
   private final List<JaveFilePreviewItem> thumbnails = new ArrayList<>();

   public JaveFilePreviewItem[] getThumbnails() {
      return this.thumbnails.toArray(new JaveFilePreviewItem[0]);
   }

   public int getSize() {
      return this.thumbnails.size();
   }

   public void clear() {
      if (this.thumbnails.size() != 0) {
         this.thumbnails.clear();
         this.fireChangeEvent();
      }
   }

   public void add(JaveFilePreviewItem dia) {
      this.thumbnails.add(dia);
      this.fireChangeEvent();
   }

   public void setPreview(JaveFilePreviewItem data, Image previewImage, Dimension originalSize) {
      data.setPreview(previewImage, originalSize);
      this.fireChangeEvent();
   }
}

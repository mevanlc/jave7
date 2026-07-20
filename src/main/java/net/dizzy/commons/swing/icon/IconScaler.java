package net.dizzy.commons.swing.icon;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Rescales the native, hand-drawn ~16px pixel-art icons to a larger preferred size on the fly,
 * so the 24px/32px variants no longer have to be packaged as pregenerated PNGs.
 *
 * <p>This helper is deliberately preference-agnostic: callers read the user's icon-size setting
 * themselves and pass the resulting {@code targetSize}. It only knows about the native pixel grid
 * the source art is authored on ({@link #BASE_ICON_SIZE}).
 *
 * <h2>Which icons get rescaled</h2>
 * Only artwork that is plausibly one of the native ~16px, roughly-square pixel icons is touched
 * (see {@link #isScalablePixelIcon}). Two guards keep everything else untouched:
 * <ul>
 *   <li><b>Upscale-only</b> &mdash; an icon is only enlarged when its longest edge is smaller than
 *       {@code targetSize}. Genuinely large artwork that shares these loading paths (the splash
 *       image, the 75&times;66 export-wizard/camelizer/quick-start dialog icons, the 95&times;72
 *       quick-start item icons) is already as large as, or larger than, the target and is returned
 *       as-is instead of being shrunk to a thumbnail.</li>
 *   <li><b>Roughly square</b> &mdash; the longest edge may be at most {@link #MAX_ASPECT_RATIO}
 *       times the shortest. Elongated art (e.g. tall tool-type strips) is left alone rather than
 *       stretched, matching the "approximately 16px, approximately square" intent of the source
 *       icons.</li>
 * </ul>
 * Aspect ratio is always preserved for the icons that do get rescaled.
 *
 * <h2>Interpolation</h2>
 * The interpolation depends on how the target relates to the {@value #BASE_ICON_SIZE}px base. A
 * whole-number multiple (32px = 2&times;) is a clean pixel doubling, so plain nearest-neighbour
 * stays crisp. A fractional multiple (24px = 1.5&times;) would leave nearest-neighbour with uneven
 * 1-vs-2px columns, so it is supersampled instead: the source is tripled with nearest-neighbour and
 * then area-averaged down to the target (3&times; / 2 = 1.5&times; for the canonical 16px icon),
 * which keeps solid interiors while smoothing the half-step edges.
 *
 * <h2>Keeping menus compact</h2>
 * A rescaled icon is returned as a {@link BaseIconImageIcon} that displays at the enlarged size but
 * also carries the original native icon as its {@link net.dizzy.commons.swing.icon.IBaseIconProvider
 * base}. Toolbars, tabs and tool palettes paint the icon directly and get the enlarged size, while
 * menus — which detect {@code IBaseIconProvider} and swap in the base — keep rendering at the native
 * ~16px, so a menu row's height never depends on whether it happens to carry an icon.
 */
public final class IconScaler {
   /**
    * The native pixel grid the source icons are authored on. This is a property of the artwork,
    * intentionally independent of any user-facing default preference that may happen to share the
    * same value.
    */
   public static final int BASE_ICON_SIZE = 16;

   private static final int SUPERSAMPLE_FACTOR = 3;

   /** The longest edge may be at most this many times the shortest for an icon to count as square. */
   private static final int MAX_ASPECT_RATIO = 2;

   private IconScaler() {
   }

   /**
    * Returns {@code icon} enlarged so its longest edge matches {@code targetSize}, or the original
    * icon unchanged when no rescaling should be applied (target at or below the base size, a
    * non-{@link ImageIcon}, or artwork that fails {@link #isScalablePixelIcon}). When it does
    * rescale, the result is a {@link BaseIconImageIcon} carrying {@code icon} as its base so menus
    * can keep rendering at the native size.
    */
   public static Icon scaleToPreferredSize(Icon icon, int targetSize) {
      if (targetSize <= BASE_ICON_SIZE || !(icon instanceof ImageIcon)) {
         return icon;
      }

      ImageIcon imageIcon = (ImageIcon)icon;
      int width = imageIcon.getIconWidth();
      int height = imageIcon.getIconHeight();
      if (!isScalablePixelIcon(width, height, targetSize)) {
         return icon;
      }

      double scale = (double)targetSize / Math.max(width, height);
      int scaledWidth = Math.max(1, (int)Math.round(width * scale));
      int scaledHeight = Math.max(1, (int)Math.round(height * scale));

      Image source = imageIcon.getImage();
      BufferedImage scaled;
      if (targetSize % BASE_ICON_SIZE == 0) {
         scaled = nearestNeighbourScale(source, scaledWidth, scaledHeight);
      } else {
         BufferedImage tripled = nearestNeighbourScale(source, width * SUPERSAMPLE_FACTOR, height * SUPERSAMPLE_FACTOR);
         scaled = areaAveragedScale(tripled, scaledWidth, scaledHeight);
      }
      // Display the enlarged image, but keep the native icon as the base so menus stay compact.
      return new BaseIconImageIcon(scaled, icon);
   }

   /**
    * Whether an icon of the given dimensions is one of the native ~16px, roughly-square pixel icons
    * this scaler is meant to enlarge to {@code targetSize}. Large artwork (longest edge already at
    * or above the target) and elongated artwork (longest edge more than {@link #MAX_ASPECT_RATIO}x
    * the shortest) are excluded so they pass through untouched.
    */
   static boolean isScalablePixelIcon(int width, int height, int targetSize) {
      int longestEdge = Math.max(width, height);
      int shortestEdge = Math.min(width, height);
      if (shortestEdge <= 0 || longestEdge >= targetSize) {
         return false;
      }
      return longestEdge <= MAX_ASPECT_RATIO * shortestEdge;
   }

   private static BufferedImage nearestNeighbourScale(Image source, int destWidth, int destHeight) {
      BufferedImage result = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = result.createGraphics();
      try {
         graphics.setComposite(AlphaComposite.Src);
         graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         graphics.drawImage(source, 0, 0, destWidth, destHeight, null);
      } finally {
         graphics.dispose();
      }
      return result;
   }

   private static BufferedImage areaAveragedScale(Image source, int destWidth, int destHeight) {
      // ImageIcon forces the asynchronously produced, area-averaged image to finish loading.
      ImageIcon averaged = new ImageIcon(source.getScaledInstance(destWidth, destHeight, Image.SCALE_AREA_AVERAGING));
      BufferedImage result = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = result.createGraphics();
      try {
         averaged.paintIcon(null, graphics, 0, 0);
      } finally {
         graphics.dispose();
      }
      return result;
   }
}

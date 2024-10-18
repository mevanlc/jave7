package de.jave.figlet.swing.ui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import net.disy.commons.swing.icon.util.IconUtilities;
import net.disy.commons.swing.resources.IIconResources;

public class FigletIcons implements IIconResources {
   public static final Icon FIGLET_ICON = Resources.getIconResource("figlet_icon16.gif");
   private static final BufferedImage ICON_IMAGE_16x16 = IconUtilities.createBufferedImage(FIGLET_ICON);
   public static final List<? extends Image> FIGLET_ICON_IMAGES = Arrays.asList(ICON_IMAGE_16x16);
   public static final Icon FONT_ICON = Resources.getIconResource("figlet_font_icon.gif");
   public static final Icon FONT_CATEGORY_ICON = Resources.getIconResource("figlet_category.gif");
   public static final Icon FONT_CATEGORY_ICON_DISABLED = Resources.getIconResource("figlet_category_disabled.gif");
   public static final Icon DYNAMICALY_GENERATED_FONT_CATEGORY_ICON = Resources.getIconResource("figlet_category_dynamically.gif");
   public static final Icon INFO_ICON = Resources.getIconResource("info.gif");
   public static final Icon FOLDER_CLOSED_ICON = Resources.getIconResource("folder.gif");
   public static final Icon FOLDER_OPEN_ICON = Resources.getIconResource("folder_open.gif");
   public static final Icon H_FIXED_WIDTH = Resources.getIconResource("h_fixed_width.gif");
   public static final Icon H_REVERSE_SUPERSMUSHING = Resources.getIconResource("h_reverse_supersmushing.gif");
   public static final Icon H_SUPERSMUSHING = Resources.getIconResource("h_supersmushing.gif");
   public static final Icon H_SPACED_KERNING = Resources.getIconResource("h_spaced_kerning.gif");
   public static final Icon H_FULL_WIDTH = Resources.getIconResource("h_full_width.gif");
   public static final Icon H_KERNING = Resources.getIconResource("h_kerning.gif");
   public static final Icon H_SMUSHING = Resources.getIconResource("h_smushing.gif");
   public static final Icon V_SMUSHING = Resources.getIconResource("v_smushing.gif");
   public static final Icon V_FITTING = Resources.getIconResource("v_fitting.gif");
   public static final Icon V_FULL_HEIGHT = Resources.getIconResource("v_full_height.gif");
   public static final Icon V_SPACED_FITTING = Resources.getIconResource("v_spaced_fitting.gif");
   public static final Icon V_SUPERSMUSHING = Resources.getIconResource("v_supersmushing.gif");
   public static final Icon V_REVERSE_SUPERSMUSHING = Resources.getIconResource("v_reverse_supersmushing.gif");
   public static final Icon H_ALIGN_CENTER = Resources.getIconResource("align_center.gif");
   public static final Icon H_ALIGN_RIGHT = Resources.getIconResource("align_right.gif");
   public static final Icon H_ALIGN_LEFT = Resources.getIconResource("align_left.gif");
   public static final Icon PRINT_DIRECTION_RIGHT = Resources.getIconResource("direction_right.gif");
   public static final Icon PRINT_DIRECTION_LEFT = Resources.getIconResource("direction_left.gif");

   private FigletIcons() {
   }
}

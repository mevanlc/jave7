# Jave artwork review

The product rename updates text, identifiers, filenames, and ordinary labels.
Image pixels, encoded clipart drawings, and 3D logo geometry are unchanged.
This inventory lists branding assets that may need manual attention; inclusion
does not mean an asset needs to change.

## Confirmed lettering to review

| Asset | Use and finding | Suggested action |
| --- | --- | --- |
| [javesplash.png](../src/main/java/de/jave/javesplash.png) (400 × 300) | Bundled splash image. Its loading message uses an uppercase final E in the product name. No direct runtime reference was found. | Change the baked-in name to **Jave** if this image is retained. |
| [javeicon64.png](../src/main/java/de/jave/javeicon64.png) (64 × 64) | Application icon loaded by `JaveIcons`. The legacy wordmark is drawn into the icon. | Review the lettering for the new capitalization, together with the smaller icon variants. |
| [logos.jcf](../clipart/logos.jcf) | Bundled clipart logo collection. The three product-logo labels now use **Jave**, but the packed drawings are unchanged. The first drawing contains the lettering `J\|a\|v\|E`. | Review the decoded drawings and adjust the final letter where appropriate. Preserve the other artwork and contributor credits. |

## Related assets to review for consistency

| Assets | Use and finding | Suggested action |
| --- | --- | --- |
| [javeicon16.gif](../src/main/java/de/jave/javeicon16.gif), [javeicon32.gif](../src/main/java/de/jave/javeicon32.gif) | 16 × 16 and 32 × 32 application icons loaded by `JaveIcons`. | Compare with the 64 × 64 wordmark before deciding whether to redraw them. |
| [javeicon.ico](../icons/javeicon.ico), [javeicon.png](../icons/javeicon.png) | Native packaging inputs for Windows and Linux. The ICO contains 16 × 16 and 32 × 32 images; the PNG is 32 × 32. | Keep these in sync with any revised application emblem. |
| [javeicon.gif](../icons/javeicon.gif), [javeicon.bmp](../icons/javeicon.bmp), [javeicon.xpm](../icons/javeicon.xpm) | Additional 32 × 32 emblem variants. No direct source reference was found. | Review whether to retain and update these variants alongside the packaging inputs. |
| [folder_jave.png](../icons/folder_jave.png) (32 × 32) | Folder image incorporating the application emblem. No direct source reference was found. | Review if the emblem changes. |
| [i2a_jave.gif](../src/main/java/de/jave/image2ascii/i2a_jave.gif) (31 × 16) | Image-to-ASCII algorithm icon loaded by `Image2AsciiIcons`. It shows an image and `abc`, with no visible product name. | No capitalization edit appears necessary; review only for broader visual consistency. |
| [Jave3dExample.java](../src/main/java/de/jave/jave/ascii3d/example/Jave3dExample.java) | Built-in logo example offered by `Render3DTool`. Its title now uses **Jave**; the model describes a stylized lowercase `a` and surrounding strokes. | No capitalization edit appears necessary. Review the rendered model if the emblem changes. |

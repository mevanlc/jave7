package net.disy.commons.swing.dialog;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class DisyCommonsSwingDialogMessages {
   private static final String BUNDLE_NAME = "net.disy.commons.swing.dialog.messages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("net.disy.commons.swing.dialog.messages", Locale.getDefault());
   public static final String LESS = getString("Dialog.LessButtonText");
   public static final String MORE = getString("Dialog.MoreButtonText");
   public static final String APPLY = getString("SmartAction.apply");
   public static final String CANCEL = getString("SmartAction.cancel");
   public static final String CLOSE = getString("SmartAction.close");
   public static final String HELP = getString("SmartAction.help");
   public static final String OK = getString("SmartAction.okay");
   public static final String YES = getString("SmartAction.Yes");
   public static final String NO = getString("SmartAction.No");
   public static final String SELECT_ALL = getString("SmartAction.selectAll");
   public static final String COPY = getString("SmartAction.copy");
   public static final String CUT = getString("SmartAction.cut");
   public static final String PASTE = getString("SmartAction.paste");
   public static final String WIZARD_NEXT = getString("Wizard.SmartNext");
   public static final String WIZARD_BACK = getString("Wizard.SmartBack");
   public static final String WIZARD_FINISH = getString("Wizard.SmartFinish");

   public static String getString(String key) {
      return RESOURCE_BUNDLE.getString(key);
   }

   public String getString(String key, Object[] arguments) {
      String value = getString(key);
      return value == null ? null : MessageFormat.format(value, arguments);
   }
}

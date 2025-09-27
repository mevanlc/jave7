package de.jave.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public abstract class NLS {
    public static boolean DEBUG_MESSAGE_BUNDLES = false;
    private static final Object[] EMPTY_ARGS = new Object[0];
    private static final String EXTENSION = ".properties";
    private static String[] nlSuffixes;
    private static final int SEVERITY_ERROR = 4;
    private static final int SEVERITY_WARNING = 2;
    static final Object ASSIGNED = new Object();

    protected NLS() {
    }

    public static String bind(String message, Object binding) {
        return internalBind(message, null, String.valueOf(binding), null);
    }

    public static String bind(String message, Object binding1, Object binding2) {
        return internalBind(message, null, String.valueOf(binding1), String.valueOf(binding2));
    }

    public static String bind(String message, Object[] bindings) {
        return internalBind(message, bindings, null, null);
    }

    @SuppressWarnings("removal")
    public static void initializeMessages(final String bundleName, final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            load(bundleName, clazz);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    NLS.load(bundleName, clazz);
                    return null;
                }
            });
        }
    }

    private static String internalBind(String message, Object[] args, String argZero, String argOne) {
        if (message == null) {
            return "No message available.";
        } else {
            if (args == null || args.length == 0) {
                args = EMPTY_ARGS;
            }

            int length = message.length();
            int bufLen = length + args.length * 5;
            if (argZero != null) {
                bufLen += argZero.length() - 3;
            }

            if (argOne != null) {
                bufLen += argOne.length() - 3;
            }

            StringBuffer buffer = new StringBuffer(bufLen < 0 ? 0 : bufLen);

            for (int i = 0; i < length; i++) {
                char c = message.charAt(i);
                switch (c) {
                    case '\'':
                        int nextIndex = i + 1;
                        if (nextIndex >= length) {
                            buffer.append(c);
                        } else {
                            char next = message.charAt(nextIndex);
                            if (next == '\'') {
                                i++;
                                buffer.append(c);
                            } else {
                                int var14 = message.indexOf(39, nextIndex);
                                if (var14 == -1) {
                                    buffer.append(c);
                                } else {
                                    buffer.append(message, nextIndex, var14);
                                    i = var14;
                                }
                            }
                        }
                        break;
                    case '{':
                        int index = message.indexOf(125, i);
                        if (index == -1) {
                            buffer.append(c);
                        } else if (++i >= length) {
                            buffer.append(c);
                        } else {
                            int number = -1;

                            try {
                                number = Integer.parseInt(message.substring(i, index));
                            } catch (NumberFormatException var13) {
                                throw new IllegalArgumentException();
                            }

                            if (number == 0 && argZero != null) {
                                buffer.append(argZero);
                            } else if (number == 1 && argOne != null) {
                                buffer.append(argOne);
                            } else {
                                if (number >= args.length || number < 0) {
                                    buffer.append("<missing argument>");
                                    i = index;
                                    continue;
                                }

                                buffer.append(args[number]);
                            }

                            i = index;
                        }
                        break;
                    default:
                        buffer.append(c);
                }
            }

            return buffer.toString();
        }
    }

    private static String[] buildVariants(String root) {
        if (nlSuffixes == null) {
            String nl = Locale.getDefault().toString();
            ArrayList<String> result = new ArrayList<>(4);

            while (true) {
                result.add('_' + nl + ".properties");
                int lastSeparator = nl.lastIndexOf(95);
                if (lastSeparator == -1) {
                    result.add(".properties");
                    nlSuffixes = result.toArray(new String[0]);
                    break;
                }

                nl = nl.substring(0, lastSeparator);
            }
        }

        root = root.replace('.', '/');
        String[] variants = new String[nlSuffixes.length];

        for (int i = 0; i < variants.length; i++) {
            variants[i] = root + nlSuffixes[i];
        }

        return variants;
    }

    private static void computeMissingMessages(String bundleName, Class clazz, Map<Object, Object> fieldMap, Field[] fieldArray, boolean isAccessible) {
        int MOD_EXPECTED = 9;
        int MOD_MASK = 25;

        for (Field field : fieldArray) {
            if ((field.getModifiers() & 25) == 9 && fieldMap.get(field.getName()) != ASSIGNED) {
                try {
                    String value = "NLS missing message: " + field.getName() + " in: " + bundleName;
                    if (DEBUG_MESSAGE_BUNDLES) {
                        System.out.println(value);
                    }

                    log(2, value, null);
                    if (!isAccessible) {
                        field.setAccessible(true);
                    }

                    field.set(null, value);
                } catch (Exception var11) {
                    log(4, "Error setting the missing message value for: " + field.getName(), var11);
                }
            }
        }
    }

    private static void load(String bundleName, Class clazz) {
        long start = System.currentTimeMillis();
        Field[] fieldArray = clazz.getDeclaredFields();
        ClassLoader loader = clazz.getClassLoader();
        boolean isAccessible = (clazz.getModifiers() & 1) != 0;
        int len = fieldArray.length;
        Map<Object, Object> fields = new HashMap<>(len * 2);

        for (int i = 0; i < len; i++) {
            fields.put(fieldArray[i].getName(), fieldArray[i]);
        }

        String[] variants = buildVariants(bundleName);

        for (int i = 0; i < variants.length; i++) {
            InputStream input = loader == null ? ClassLoader.getSystemResourceAsStream(variants[i]) : loader.getResourceAsStream(variants[i]);
            if (input != null) {
                try {
                    NLS.MessagesProperties properties = new NLS.MessagesProperties(fields, bundleName, isAccessible);
                    properties.load(input);
                } catch (IOException var21) {
                    log(4, "Error loading " + variants[i], var21);
                } finally {
                    try {
                        input.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        computeMissingMessages(bundleName, clazz, fields, fieldArray, isAccessible);
        if (DEBUG_MESSAGE_BUNDLES) {
            System.out.println("Time to load message bundle: " + bundleName + " was " + (System.currentTimeMillis() - start) + "ms.");
        }
    }

    private static void log(int severity, String message, Exception e) {
        String statusMsg;
        switch (severity) {
            case 2:
            default:
                statusMsg = "Warning: ";
                break;
            case 4:
                statusMsg = "Error: ";
        }

        if (message != null) {
            statusMsg = statusMsg + message;
        }

        if (e != null) {
            statusMsg = statusMsg + ": " + e.getMessage();
        }

        System.err.println(statusMsg);
        if (e != null) {
            e.printStackTrace();
        }
    }

    private static class MessagesProperties extends Properties {
        private static final int MOD_EXPECTED = 9;
        private static final int MOD_MASK = 25;
        private static final long serialVersionUID = 1L;
        private final String bundleName;
        private final Map<Object, Object> fields;
        private final boolean isAccessible;

        public MessagesProperties(Map<Object, Object> fieldMap, String bundleName, boolean isAccessible) {
            this.fields = fieldMap;
            this.bundleName = bundleName;
            this.isAccessible = isAccessible;
        }

        @Override
        public synchronized Object put(Object key, Object value) {
            Object fieldObject = this.fields.put(key, NLS.ASSIGNED);
            if (fieldObject == NLS.ASSIGNED) {
                return null;
            } else if (fieldObject == null) {
                String msg = "NLS unused message: " + key + " in: " + this.bundleName;
                if (NLS.DEBUG_MESSAGE_BUNDLES) {
                    System.out.println(msg);
                }

                NLS.log(2, msg, null);
                return null;
            } else {
                Field field = (Field) fieldObject;
                if ((field.getModifiers() & 25) != 9) {
                    return null;
                } else {
                    try {
                        if (!this.isAccessible) {
                            field.setAccessible(true);
                        }

                        field.set(null, value);
                    } catch (Exception var6) {
                        NLS.log(4, "Exception setting field value.", var6);
                    }

                    return null;
                }
            }
        }
    }
}

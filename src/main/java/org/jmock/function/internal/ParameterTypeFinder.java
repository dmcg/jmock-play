package org.jmock.function.internal;

import org.jmock.function.Proc1;

import static java.util.Arrays.stream;

// Forgive me Father
public class ParameterTypeFinder {

    private static final Object PROVOCATION = new Object() {};
    private static final String MARKER = "cast to ";

    public static <X extends Exception> Class[] findParameterTypes(Proc1<Object[], X> probe, int parameterCount) throws X {
        Class[] result = new Class[parameterCount];
        for (int position = 0; position < parameterCount; position++) {
            try {
                probe.call(argsWithClasses(result));
            } catch (ClassCastException x) {
                result[position] = classFromException(x);
            }
        }
        return result;
    }

    private static Class classFromException(ClassCastException x) {
        String message = x.getMessage();
        String className = message.substring(message.indexOf(MARKER) + MARKER.length(), message.length());
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] argsWithClasses(Class[] classes) {
        return stream(classes).map(ParameterTypeFinder::argForClass).toArray();
    }

    private static Object argForClass(Class aClass) {
        if (aClass == null)
            return PROVOCATION;
        if (aClass == Integer.class)
            return 0;
        if (aClass == Long.class)
            return 0L;
        if (aClass == Float.class)
            return 0F;
        if (aClass == Double.class)
            return 0D;
        if (aClass == Boolean.class)
            return false;
        if (aClass == Character.class)
            return ' ';
        if (aClass == Short.class)
            return (short) 0;
        if (aClass == Byte.class)
            return (byte) 0;
        return null;
    }
}

package ch.afterglowing.doit.business.auditing.boundary;

import javax.annotation.Nullable;

/**
 * Created by ben on 11.10.15.
 */
public class StringHelper {
    private StringHelper() {
    }

    public static ToStringHelper get(Object self) {
        return new ToStringHelper(simpleName(self.getClass()));
    }

    private static String simpleName(Class<?> clazz) {
        String name = clazz.getName();
        name = name.replaceAll("\\$[0-9]+", "\\$");
        int start = name.lastIndexOf(36);
        if (start == -1) {
            start = name.lastIndexOf(46);
        }

        return name.substring(start + 1);
    }

    public static final class ToStringHelper {
        private final StringBuilder builder;
        private boolean needsSeparator;

        private ToStringHelper(String className) {
            needsSeparator = false;
            if (className == null) {
                throw new NullPointerException("className");
            }
            builder = (new StringBuilder(32)).append(className).append('{');
        }

        public ToStringHelper add(String name, @Nullable Object value) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            maybeAppendSeparator().append(name).append('=').append(value);
            return this;
        }

        public String toString() {
            String result;
            try {
                result = builder.append('}').toString();
            } finally {
                builder.setLength(builder.length() - 1);
            }

            return result;
        }

        private StringBuilder maybeAppendSeparator() {
            if (needsSeparator) {
                return builder.append(", ");
            } else {
                needsSeparator = true;
                return builder;
            }
        }
    }
}

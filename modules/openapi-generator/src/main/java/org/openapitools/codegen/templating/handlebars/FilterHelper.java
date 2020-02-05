package org.openapitools.codegen.templating.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum FilterHelper implements Helper<Object> {
    /**
     * Filter array of objects related to value of a property
     *    if no value is given check will only check if property exists or not
     *    Supported types are Integer, Boolean, String, Float/Double
     *
     * Example:
     * array = [
     *    {name="test1", value=1, valid=true},
     *    {name="test2", value=1.0, valid=true},
     *    {name="test3", value=2, valid=false, extraProperty="extra"},
     *    {name="test4", value=1, valid=true},
     *    {name="test5", value=1.0, valid=false}
     * ]
     *
     * <pre>
     * {{#filter array property="value" value=1}}
     * {{name}}{{/filter}}
     *
     * Render:
     *   test1
     *   test4
     * </pre>
     *
     * <pre>
     * {{#filter array property="valid" value=true}}
     * {{name}}{{/filter}}
     *
     * Render:
     *   test1
     *   test2
     *   test4
     * </pre>
     *
     * <pre>
     * {{#filter array property="extraProperty"}}
     * {{name}}{{/filter}}
     *
     * Render:
     *   test3
     * </pre>
     */
    filter {
        @SuppressWarnings({"rawtypes", "unchecked" })
        @Override
        public Object apply(final Object context, final Options options) throws IOException {
            String property = options.hash("property", null);
            if ((context instanceof Iterable) && property != null) {
                Iterator<Object> loop = ((Iterable) context).iterator();
                Object value = options.hash("value", null);
                List<Object> result = new ArrayList<>();

                while (loop.hasNext()) {
                    Object it = loop.next();
                    try {
                        Field field = it.getClass().getDeclaredField(property);
                        try {
                            Object obj_value = field.get(it);
                            if (value == null) {
                                // no value give, check is then only that property exists
                                result.add(it);
                            } else {
                                if (compareFieldValueWithWanted(obj_value, value)) {
                                    result.add(it);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            // Property not readable, skipping
                        }
                    } catch (NoSuchFieldException e) {
                        // Property doesn't exist on this object, as such it will be filtered out
                    }
                }
                Options.Buffer buffer = null;
                if (result.size() > 0) {
                    buffer = options.buffer();
                    int index = 0;
                    for (Object it : result) {
                        Context itCtx = Context.newContext(options.context, it);
                        itCtx.combine("@key", index)
                                .combine("@index", index)
                                .combine("@first", index == 0 ? "first" : "")
                                .combine("@last", (index + 1) == result.size() ? "last" : "")
                                .combine("@odd", index % 2 == 0 ? "" : "odd")
                                .combine("@even", index % 2 == 0 ? "even" : "")
                                // 1-based index
                                .combine("@index_1", index + 1);
                        buffer.append(options.apply(options.fn, itCtx, Arrays.asList(it, index)));
                        index += 1;
                    }
                }

                return buffer;
            }
            return null;
        }
    };

    private static boolean compareFieldValueWithWanted(Object object_value, Object match_value) {
        if (object_value instanceof Boolean) {
            if (match_value instanceof Boolean) {
                return object_value.equals(match_value);
            }
        } else if (object_value instanceof Integer) {
            if (match_value instanceof Integer) {
                return object_value.equals(match_value);
            }
        } else if (object_value instanceof String) {
            if (match_value instanceof String) {
                return object_value.equals(match_value);
            }
        } else if (object_value instanceof Float) {
            if (match_value instanceof Float) {
                return object_value.equals(match_value);
            } else if (match_value instanceof Double) {
                return object_value.equals(((Double) match_value).floatValue());
            }
        } else if (object_value instanceof Double) {
            if (match_value instanceof Double) {
                return object_value.equals(match_value);
            } else if (match_value instanceof Float) {
                return object_value.equals(((Float) match_value).doubleValue());
            }
        }

        return false;
    }
}
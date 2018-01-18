package org.apache.flume.source.taildir.rotate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.formatter.output.BucketPath;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by penuel on 2018/1/18.
 */
public class MacroStaticInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MacroStaticInterceptor.class);

    private final boolean preserveExisting;
    private final boolean macroReplace;
    private final String key;
    private final String value;

    private MacroStaticInterceptor(boolean preserveExisting, boolean macroReplace, String key, String value) {
        this.preserveExisting = preserveExisting;
        this.macroReplace = macroReplace;
        this.key = key;
        this.value = value;
    }

    @Override public void initialize() {

    }

    @Override public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();

        if (preserveExisting && headers.containsKey(key)) {
            return event;
        }
        if (macroReplace) {
            headers.put(key, BucketPath.escapeString(value, headers));
        } else {
            headers.put(key, value);
        }
        return event;
    }

    @Override public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override public void close() {
    }

    public static class Builder implements Interceptor.Builder {

        private boolean preserveExisting;
        private boolean macroReplace;
        private String key;
        private String value;

        @Override public Interceptor build() {
            logger.info(String.format(
                "Creating StaticInterceptor: preserveExisting=%s,macroReplace=%s, key=%s,value=%s",
                preserveExisting, macroReplace, key, value));
            return new MacroStaticInterceptor(preserveExisting, macroReplace, key, value);
        }

        @Override public void configure(Context context) {
            preserveExisting = context.getBoolean(Constants.PRESERVE, Constants.PRESERVE_DEFAULT);
            macroReplace = context.getBoolean(Constants.REPLACE, Constants.REPLACE_DEFAULT);
            key = context.getString(Constants.KEY, Constants.KEY_DEFAULT);
            value = context.getString(Constants.VALUE, Constants.VALUE_DEFAULT);
        }
    }

    public static class Constants {
        public static final String KEY = "key";
        public static final String KEY_DEFAULT = "key";

        public static final String VALUE = "value";
        public static final String VALUE_DEFAULT = "value";

        public static final String PRESERVE = "preserveExisting";
        public static final boolean PRESERVE_DEFAULT = true;

        public static final String REPLACE = "macroReplace";
        public static final boolean REPLACE_DEFAULT = true;
    }
}

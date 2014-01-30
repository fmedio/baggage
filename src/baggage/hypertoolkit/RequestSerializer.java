package baggage.hypertoolkit;

import com.google.common.collect.Multimap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fmedio on 1/29/14.
 */

class IntSerializer implements ParamSerializer<Integer> {
    @Override
    public Integer parse(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public String toString(Integer tee) {
        return Integer.toString(tee);
    }
}

class LongSerializer implements ParamSerializer<Long> {
    @Override
    public Long parse(String s) {
        return Long.parseLong(s);
    }

    @Override
    public String toString(Long tee) {
        return Long.toString(tee);
    }
}

class StringSerializer implements ParamSerializer<String> {

    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String toString(String tee) {
        return tee;
    }
}

interface ParamSerializer<T> {
    public T parse(String s);
    public String toString(T tee);
}

public class RequestSerializer {
    private static Map<Class, Class<? extends ParamSerializer>> SERIALIZERS = new HashMap<>();

    static {
        SERIALIZERS.put(Integer.TYPE, IntSerializer.class);
        SERIALIZERS.put(Long.TYPE, LongSerializer.class);
        SERIALIZERS.put(String.class, StringSerializer.class);
    }

    public <T> T deserialize(Multimap<String, String> map, Class<T> klass) {
        try {
            T tee = klass.newInstance();

            Arrays.stream(klass.getFields())
                    .filter(f -> Arrays.stream(f.getAnnotations()).anyMatch(a -> a.annotationType().equals(UrlParam.class)))
                    .forEach(f -> {
                        ParamSerializer serializer = makeSerializer(f);
                        if (serializer == null)
                            throw new RuntimeException("No serializer for class " + f.getType().toString());
                        String value = map.get(f.getName()).iterator().next();
                        if (value != null) {
                            Object o = serializer.parse(value);
                            try {
                                f.set(tee, o);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            return tee;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ParamSerializer makeSerializer(Field f) {
        try {
            return SERIALIZERS.get(f.getType()).newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

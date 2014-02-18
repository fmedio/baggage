package baggage.hypertoolkit;

import baggage.Nil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

class DoubleSerializer implements ParamSerializer<Double> {
    @Override
    public Double parse(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public String toString(Double tee) {
        return tee.toString();
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

class NilSerializer implements ParamSerializer<Nil> {
    @Override
    public Nil parse(String s) {
        return new Nil();
    }

    @Override
    public String toString(Nil tee) {
        return null;
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
        SERIALIZERS.put(Integer.class, IntSerializer.class);
        SERIALIZERS.put(Long.TYPE, LongSerializer.class);
        SERIALIZERS.put(Long.class, LongSerializer.class);
        SERIALIZERS.put(Double.TYPE, DoubleSerializer.class);
        SERIALIZERS.put(Double.class, DoubleSerializer.class);
        SERIALIZERS.put(String.class, StringSerializer.class);
    }

    public <T> Multimap<String, String> serialize(T tee) {
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();
        getParamFields(tee.getClass()).forEach(f -> {
            ParamSerializer serializer = makeSerializer(f.getType());
            String name = f.getName();
            Object o = null;
            try {
                o = f.get(tee);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
            map.put(name, serializer.toString(o));
        });
        return map;
    }

    public <T> T deserialize(Multimap<String, String> map, Class<T> klass) {
        try {
            if (SERIALIZERS.keySet().contains(klass)) {
                if (map.isEmpty())
                    throw new RuntimeException("No argument to deserialize!");
                return (T) makeSerializer(klass).parse(map.values().iterator().next());
            }

            T tee = klass.newInstance();

            getParamFields(klass).forEach(f -> {
                ParamSerializer serializer = makeSerializer(f.getType());
                Collection<String> values = map.get(f.getName());
                if (values.size() != 0) {
                    Object o = serializer.parse(values.iterator().next());
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

    private <T> Stream<Field> getParamFields(Class<T> klass) {
        return Arrays.stream(klass.getFields())
                .filter(f -> Arrays.stream(f.getAnnotations()).anyMatch(a -> a.annotationType().equals(UrlParam.class)));
    }

    private ParamSerializer makeSerializer(Class klass) {
        try {
            Class serializer = SERIALIZERS.get(klass);
            if (serializer == null)
                throw new RuntimeException("No serializer for class " + klass.toString());
            return (ParamSerializer) serializer.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

package baggage.hypertoolkit;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import junit.framework.TestCase;

/**
 * Created by fmedio on 1/29/14.
 */

class CustomType {
    public int value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomType that = (CustomType) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }
}

class Panda {
    @UrlParam
    public int age;

    @UrlParam
    public long size;

    @UrlParam
    public String name;
}



public class RequestSerializerTest extends TestCase {
    public void testOptionalParameter() {
        fail("Implement me");
    }

    public void testSerialize() {
        Panda panda = new Panda();
        CustomType foo = new CustomType();
        foo.value = 1;
        panda.age = 42;
        panda.size = 153;
        panda.name = "leslie";
        Multimap<String, String> map = new RequestSerializer().serialize(panda);
        assertEquals("42", map.get("age").iterator().next());
        assertEquals("153", map.get("size").iterator().next());
        assertEquals("leslie", map.get("name").iterator().next());
    }

    public void testSingleArgument() {
        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("age", "42");
        assertEquals(42, (int) new RequestSerializer().deserialize(map, Integer.TYPE));
        assertEquals(42, (int) new RequestSerializer().deserialize(map, Integer.class));
    }

    public void testDeserialize() {
        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("age", "42");
        map.put("size", "153");
        map.put("name", "leslie");
        Panda thawed = new RequestSerializer().deserialize(map, Panda.class);
        assertEquals((int)42, thawed.age);
        assertEquals((long)153, thawed.size);
        assertEquals("leslie", thawed.name);
    }
}

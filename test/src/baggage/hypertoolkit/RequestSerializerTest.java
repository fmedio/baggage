package baggage.hypertoolkit;

import baggage.Nil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import junit.framework.TestCase;

/**
 * Created by fmedio on 1/29/14.
 */

class Panda {
    @UrlParam
    public int age;

    @UrlParam
    public long size;

    @UrlParam
    public String name;
}

public class RequestSerializerTest extends TestCase {
    public void testParametersOptionalByDefault() {
        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("age", "42");
        map.put("size", "153");
        Panda thawed = new RequestSerializer().deserialize(map, Panda.class);
        assertNull(thawed.name);
    }

    public void testSerialize() {
        Panda panda = new Panda();
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

    public void testDeserializeNil() {
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();
        Nil result = new RequestSerializer().deserialize(map, Nil.class);
        assertNotNull(result);
    }
}

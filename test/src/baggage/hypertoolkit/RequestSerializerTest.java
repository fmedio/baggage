package baggage.hypertoolkit;

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

    public void testSerialize() {
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

package clutter.hypertoolkit.security;

import clutter.BaseTestCase;
import clutter.IntegrationTest;
import junit.framework.Assert;

@IntegrationTest
public class HashTest extends BaseTestCase {
    public void testMatches() throws Exception {
        String hash = new Hash.Sum("foo").execute();
        Assert.assertTrue(new Hash.Compare(hash, "foo").execute());

    }

    public void testEncrypt() throws Exception {
        String result = new Hash.Sum("foo") {
            protected String makeRandomSalt() {
                return "abcdef";
            }
        }.execute();
        Assert.assertEquals("abcdef4AWvm/uB8EtJh12OOIhNQbZvTcg=", result);
    }

    public void testMakeRandomSalt() throws Exception {
        Hash.Sum sum = new Hash.Sum("foo");
        Assert.assertEquals(6, sum.makeRandomSalt().length());
    }
}

package baggage.hypertoolkit.security;

import baggage.Log;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.UUID;


public class Hash {
    private static final int SALT_CHARACTER_LENGTH = 6;

    public static class Sum {
        private String result;

        public Sum(String password) {
            String salt = makeRandomSalt();
            result = new SaltedSum(salt, password).execute();
        }

        protected String makeRandomSalt() {
            return UUID.randomUUID().toString().substring(0, SALT_CHARACTER_LENGTH);
        }

        public String execute() {
            return result;
        }
    }

    private static class SaltedSum {
        private String result;

        public SaltedSum(String salt, String input) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(salt);
                sb.append(StringUtils.defaultString(input));
                MessageDigest digest = MessageDigest.getInstance("SHA1");
                digest.update(sb.toString().getBytes("UTF-8"));
                byte[] hash = digest.digest();
                StringBuilder output = new StringBuilder();
                output.append(salt);
                output.append(new String(Base64.encodeBase64(hash)));
                result = output.toString();
            } catch (Exception e) {
                Log.fatal(this, "WTF - This JVM cannot do SHA1 or UTF8 or base64???", e);
                throw new RuntimeException(e);
            }
        }

        String execute() {
            return result;
        }
    }

    public static class Compare {
        private boolean result;

        public Compare(String hash, String candidate) {
            if (StringUtils.isEmpty(candidate) || StringUtils.isEmpty(hash)) {
                result = false;
            }

            if (hash.length() != 34) {
                result = false;
            }

            String salt = hash.substring(0, SALT_CHARACTER_LENGTH);
            String sum = new SaltedSum(salt, candidate).execute();
            result = sum.equals(hash);
        }

        public boolean execute() {
            return result;
        }
    }
}

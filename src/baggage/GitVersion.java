/*
 * The MIT License
 *
 * Copyright (c) 2010 Fabrice Medio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package baggage;


import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

public class GitVersion {
    private static String commit = "unknown", date = "unknown";
    public static final String GIT_BRANCH = "git.branch";
    public static final String GIT_COMMIT = "git.commit";
    public static final String GIT_DATE = "git.date";

    static {
        try {
            Properties properties = new Properties();
            properties.load(GitVersion.class.getResourceAsStream("/version.properties"));
            commit = properties.getProperty(GIT_COMMIT);
            date = properties.getProperty(GIT_DATE);
        } catch (Exception e) {

        }
    }

    public static String getCommit() {
        return StringUtils.isEmpty(commit) ? "unknown" : commit;
    }

    public static String getDate() {
        return StringUtils.isEmpty(date) ? "unknown" : date;
    }

    public static String asString() {
        return "Version: " + commit + "\nDate: " + date;
    }
}

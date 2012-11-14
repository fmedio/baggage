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

package clutter;

import java.io.*;

public class UpdateGitVersion {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: UpdateGitVersion /path/to/propfile");
            System.exit(42);
        }

        new UpdateGitVersion().execute(args[0]);
    }

    private void execute(String resourcePath) throws IOException {
        File file = new File(resourcePath);
        if (file.exists()) {
            file.delete();
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream(file));

        printBranchInfo(pw);
        printLastCommitInfo(pw);
        pw.close();
    }

    private void printBranchInfo(PrintWriter pw) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"git", "branch"});
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            if (line.contains("*")) {
                line = line.replace('*', ' ').trim();
                pw.print(GitVersion.GIT_BRANCH + "=");
                pw.println(line);
                return;
            }
        }
        pw.println(GitVersion.GIT_BRANCH + "=unknown");
    }

    private void printLastCommitInfo(PrintWriter pw) throws IOException {
        String version = "unknown";
        String date = "unknown";

        Process process = Runtime.getRuntime().exec(new String[]{"git", "log"});
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        version = bufferedReader.readLine().replaceAll("commit\\s+", "");
        bufferedReader.readLine();
        date = bufferedReader.readLine().replaceAll("Date:\\s+", "");

        pw.print(GitVersion.GIT_COMMIT + "=");
        pw.println(version);
        pw.print(GitVersion.GIT_DATE + "=");
        pw.println(date);
    }
}

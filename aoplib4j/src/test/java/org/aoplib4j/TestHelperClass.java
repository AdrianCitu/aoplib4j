/*
 *  Copyright 2008-2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.aoplib4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;

/**
 * Class containing helper methods for the unit tests.
 * 
 * 
 * @author Adrian Citu
 *
 */
public final class TestHelperClass {

 
    /**
     * On some plateform, the temporary directory returned by java.io.tmpdir do
     * not include a trailing slash. That is: <br>
     * Win NT --> C:\TEMP\ <br>
     * Win XP --> C:\TEMP <br>
     * Solaris --> /var/tmp/ <br>
     * Linux --> /var/tmp
     * 
     * @return returns value of "java.io.tmpdir" system property with a
     *         System.getProperty("file.separator") character at the and despite
     *         the platform(*NIX, Win, etc..)
     */
    public static String getJavaIoTmpDir() {

        String returnValue = System.getProperty("java.io.tmpdir");

        if (!(returnValue.endsWith("/") || returnValue.endsWith("\\"))) {
            returnValue = returnValue + System.getProperty("file.separator");
        }

        return returnValue;
    }
    
    /**
     * Method that transform the relative path of  a resource to a complete 
     * path. (/com/bobj/titi.java to c:/tpm/src-tests/com/bobj/titi.java)
     * 
     * @param relativePath String representing the relative path of the 
     * resource 
     * (ex: /com/boboj/titi/toot.java)
     * 
     * @return
     * 
     * @throws URISyntaxException
     */
    public static String getCompletePathFromRelativePath(String relativePath) 
        throws URISyntaxException {
        
        return TestHelperClass.class.getResource(relativePath).toURI()
            .getPath();
    }
    
    /**
     * Transforms an input stream into a string.
     * @param is the input stream
     * @return the content of the input stream as a string
     * @throws IOException if IO exception 
     */
    public static String inputStreamToString(InputStream is) 
        throws IOException {
        
        InputStreamReader in = new InputStreamReader(is);
        StringWriter out = new StringWriter();
        
        char[] buf = new char[4096];
        int bytesRead = -1;
        while ((bytesRead = in.read(buf)) != -1) {
            out.write(buf, 0, bytesRead);
        }
        out.flush();
        
        return out.toString();
    }
    
    /**
     * Gets the contents of a text test file as a string.
     * @param fileName test file name, relative to \src\test\resources
     * @return the contents of a text test file as a string.
     * @see TestCaseFileReader
     */
    public static String getTestFileContents(String fileName) 
        throws FileNotFoundException, IOException, URISyntaxException {
        
        return inputStreamToString(new FileInputStream(
            getCompletePathFromRelativePath(fileName)));
    }
}

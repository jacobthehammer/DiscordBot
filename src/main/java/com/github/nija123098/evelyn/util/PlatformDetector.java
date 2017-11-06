/**
 * Made by Celestialdeath99 on 11/4/2017.
 */

package com.github.nija123098.evelyn.util;

public class PlatformDetector {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static String PathEnding() {
        String ending = null;
        if (isUnix()) {
            ending = "/";
        }
        else if (isWindows()) {
            ending = "/";
        }
        return ending;
    }

    public static String ConverPath(String path) {
        StringBuffer result = new StringBuffer(path.length());
        char from = '\\';
        char to = '/';

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == from) {
                result.append(to);
            } else {
                result.append(path.charAt(i));
            }
        }
        return result.toString();
    }

}

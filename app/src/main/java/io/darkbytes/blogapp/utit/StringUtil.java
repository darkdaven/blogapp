package io.darkbytes.blogapp.utit;

public class StringUtil {

    public static String shortBody(String body, int length) {
        return (body.length() < length) ? body : body.substring(0, length - 3).concat("...");
    }
}

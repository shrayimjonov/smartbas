package uz.davrbank.userinfobase.utils;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class JsonParserInputStream extends InputStream {

    private final JsonParser parser;
    private char[] buffer;
    private int index;
    private int length;

    public JsonParserInputStream(JsonParser parser) throws IOException {
        this.parser = parser;
        this.buffer = parser.getTextCharacters();
        this.index = parser.getTextOffset();
        this.length = parser.getTextLength();
    }

    @Override
    public int read() throws IOException {
        if (index >= length) {
            return -1;
        }
        return buffer[index++];
    }
}
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.integration.ParseTest;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.*;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.jsoup.parser.ParseSettings.preserveCase;
import static org.junit.jupiter.api.Assertions.*;

public class XmlParserTest {

    private static final String XML_STRING = "<root><element1 attribute1='value1'>text1</element1><element2 attribute2='value2'>text2</element2></root>";

    @Test
    public void parseXMLStringTest() {
        Document doc = Jsoup.parse(XML_STRING, "", org.jsoup.parser.Parser.xmlParser());
        assertEquals("root", doc.childNode(0).nodeName());
    }

    @Test
    public void selectElementTest() {
        Document doc = Jsoup.parse(XML_STRING, "", org.jsoup.parser.Parser.xmlParser());
        assertEquals("text1", doc.select("element1").first().text());
    }

    @Test
    public void getAttributeTest() {
        Document doc = Jsoup.parse(XML_STRING, "", org.jsoup.parser.Parser.xmlParser());
        assertEquals("value1", doc.select("element1").first().attr("attribute1"));
    }

    @Test
    public void countElementsTest() {
        Document doc = Jsoup.parse(XML_STRING, "", org.jsoup.parser.Parser.xmlParser());
        assertEquals(1, doc.select("element1").size());
    }

    @Test
    public void retrieveAttributeValueTest() {
        Document doc = Jsoup.parse(XML_STRING, "", org.jsoup.parser.Parser.xmlParser());
        Element element1 = doc.select("element1").first();
        assertEquals("value1", element1.attr("attribute1"));
    }
}

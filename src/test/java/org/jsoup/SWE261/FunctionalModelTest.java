package org.jsoup.SWE261;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionalModelTest {
    @Test
    public void testElementSetAttributeValue() {
        String inputHtml = "<html><body><div id=\"myDiv\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.attr("class", "myClass");
        assertEquals("myClass", div.attr("class"));
    }

    @Test
    public void testElementRemoveAttribute() {
        String inputHtml = "<html><body><div id=\"myDiv\" class=\"myClass\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.removeAttr("class");
        assertEquals("", div.attr("class"));
    }

    @Test
    public void testElementSetText() {
        String inputHtml = "<html><body><div id=\"myDiv\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.text("Goodbye World");
        assertEquals("Goodbye World", div.text());
    }

    @Test
    public void testElementRemove() {
        String inputHtml = "<html><body><div id=\"myDiv\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.remove();
        assertNull(doc.getElementById("myDiv"));
    }

    @Test
    public void testElementAddClass() {
        String inputHtml = "<html><body><div id=\"myDiv\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.addClass("myClass");
        assertTrue(div.hasClass("myClass"));
    }

    @Test
    public void testElementRemoveClass() {
        String inputHtml = "<html><body><div id=\"myDiv\" class=\"myClass\">Hello World</div></body></html>";
        Document doc = Jsoup.parse(inputHtml);
        Element div = doc.getElementById("myDiv");
        div.removeClass("myClass");
        assertFalse(div.hasClass("myClass"));
    }
}

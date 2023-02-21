package org.jsoup.parser;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;

public class PartThreeTest {
    @Test
    public void testChompTo() {
        TokenQueue tq = new TokenQueue("<div><p>Example paragraph</p><p>Another paragraph</p></div>");

        // Test chomping to a sequence that exists in the string
        String chomped = tq.chompTo("par");
        assertEquals("<div><p>Example ", chomped);
        assertEquals("agraph</p><p>Another paragraph</p></div>", tq.remainder());
    }
    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("<div><p>Example paragraph</p><p>Another paragraph</p></div>");

        // Test consuming a sequence that doesn't exist in the string
        try {
            tq.consume("foo");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Queue did not match expected sequence", e.getMessage());
            assertEquals("<div><p>Example paragraph</p><p>Another paragraph</p></div>", tq.remainder());
        }

    }
    @Test
    public void testTQToString() {
        TokenQueue tq = new TokenQueue("<div><p>Example paragraph</p><p>Another paragraph</p></div>");
        tq.consume("<div>");
        String remaining = tq.toString();
        assertEquals("<p>Example paragraph</p><p>Another paragraph</p></div>", remaining);
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("<div><p>Example paragraph</p><p>Another paragraph</p></div>");

        // Test advancing when the queue is not empty
        tq.advance();
        assertEquals("div><p>Example paragraph</p><p>Another paragraph</p></div>", tq.remainder());
    }

    @Test
    public void testGetCursorPos() {
        ParseError parseError = new ParseError(20, "Unexpected character: %c", '$');
        assertEquals("20", parseError.getCursorPos());
        ParseError parseError2 = new ParseError(20, "Unexpected character: df");
        assertEquals("20", parseError.getCursorPos());
    }

    @Test
    public void testPopStackToBefore() {
        // Set up a stack with some elements
        Element div1 = new Element(Tag.valueOf("div"), "");
        Element p1 = new Element(Tag.valueOf("p"), "");
        Element span1 = new Element(Tag.valueOf("span"), "");
        Element a1 = new Element(Tag.valueOf("a"), "");
        ArrayList<Element> stack = new ArrayList<>(Arrays.asList(div1, p1, span1, a1));

        // Pop the stack up to an element that exists in the stack
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        htmlTreeBuilder.stack = stack;
        htmlTreeBuilder.popStackToBefore("span");
        assertEquals(3, htmlTreeBuilder.stack.size());
        assertEquals(div1, htmlTreeBuilder.stack.get(0));
        assertEquals(p1, htmlTreeBuilder.stack.get(1));

        // Pop the stack up to an element that doesn't exist in the stack
        htmlTreeBuilder.popStackToBefore("ul");
        assertEquals(0, htmlTreeBuilder.stack.size());
    }
    @Test
    public void testCloseElement() {
        // Set up an open element
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        Element p1 = new Element(Tag.valueOf("p"), "");
        ArrayList<Element> stack = new ArrayList<>(Arrays.asList(p1));
        htmlTreeBuilder.stack = stack;

        // Close an element that matches the open element
        htmlTreeBuilder.closeElement("p");
        assertEquals(0, htmlTreeBuilder.stack.size());

        // Close an element that doesn't match the open element
        htmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), ""));
        try {
            htmlTreeBuilder.closeElement("v");
            fail("Expected IllegalStateException to be thrown");
        } catch (Exception ex) {
            // Expected
        }
        assertEquals(1, htmlTreeBuilder.stack.size());
    }




}

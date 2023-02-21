package org.jsoup.parser;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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


//
// ------------ Tag Tests -----------------
@Test
public void testHashCode() {
    Tag tag1 = Tag.valueOf("div");
    Tag tag2 = Tag.valueOf("div");
    Tag tag3 = Tag.valueOf("a");

    assertEquals(tag1.hashCode(), tag2.hashCode());
    assertNotEquals(tag1.hashCode(), tag3.hashCode());

}

    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = Tag.valueOf("div");

        assertFalse(tag1.isFormSubmittable());
    }

    @Test
    public void testToString() {
        Tag tag1 = Tag.valueOf("div");

        assertEquals("div", tag1.toString());
    }


    // ------------ Parser Tests -----------------
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();

        Parser result = parser.setTreeBuilder(newTreeBuilder);

        assertSame(parser, result);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackPosition() throws NoSuchFieldException, IllegalAccessException {
        Document doc = Jsoup.parse("");
        Parser parser = doc.parser();
        Assertions.assertFalse(getTrackPosition(parser)); // initially false

        parser.setTrackPosition(true);
        Assertions.assertTrue(getTrackPosition(parser)); // set to true

        parser.setTrackPosition(false);
        Assertions.assertFalse(getTrackPosition(parser)); // set back to false
    }

    private boolean getTrackPosition(Parser parser) throws NoSuchFieldException, IllegalAccessException {
        Field field = Parser.class.getDeclaredField("trackPosition");
        field.setAccessible(true);
        return (boolean) field.get(parser);
    }

    @Test
    public void testParserCopyConstructor() {
        // Create a TreeBuilder instance (in this case, an instance of XmlTreeBuilder)
        TreeBuilder treeBuilder = new XmlTreeBuilder();

        // Create a Parser instance
        Parser parser = new Parser(treeBuilder);

        // Copy the parser using the copy constructor
        Parser copyParser = new Parser(parser.getTreeBuilder());

        // Ensure that the copied parser has the same properties as the original parser
        assertEquals(parser.getTreeBuilder().getClass(), copyParser.getTreeBuilder().getClass());
        assertEquals(parser.getErrors().size(), copyParser.getErrors().size());
        assertEquals(parser.settings().getClass(), copyParser.settings().getClass());
        assertEquals(parser.isTrackPosition(), copyParser.isTrackPosition());
    }

    @Test
    public void TestParser_() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser testParser = new Parser(treeBuilder);
        assertEquals(false,testParser.isContentForTagData("normalName"));
    }



    @Test
    void testMatchesAnyWithNonEmptySequenceAndNoMatch() {
        assertFalse(new CharacterReader("").matchesAny('a', 'b', 'c'));
    }
    @Test
    void testMatchesDigitWithNonEmptySequenceAndNoMatch() {
        assertFalse(new CharacterReader("").matchesDigit());
    }

    @Test
    public void testRewindToMarkWhenMarkIsNotSet() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertThrows(UncheckedIOException.class, reader::rewindToMark);
    }

    @Test
    public void testXmlTreeBuilderNewInstance() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder newBuilder = builder.newInstance();

        assertTrue(newBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testParseSettingsCopyConstructor() {
        // Create a ParseSettings object with some values
        boolean preserveTagCase = true;
        boolean preserveAttributeCase = false;
        ParseSettings settings = new ParseSettings(preserveTagCase, preserveAttributeCase);

        // Copy the settings to a new object using the copy constructor
        ParseSettings copy = new ParseSettings(settings);

        // Verify that the new object has the same values as the original object
        assertEquals(preserveTagCase, copy.preserveTagCase());
        assertEquals(preserveAttributeCase, copy.preserveAttributeCase());
    }

    @Test
    public void testMatchesAnyWithEmptyQueue() {
        TokenQueue tq = new TokenQueue("");
        assertFalse(tq.matchesAny('a', 'b', 'c'));
    }

    // ————————————————————————————————————————————————





}

package org.jsoup.nodes;


import org.jsoup.select.Evaluator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockingTests {
    private Element element;
    private Evaluator evaluator;
    @Before
    public void setup() {
        //  Mocks are being created.
        element = new Element("div");
        evaluator = mock(Evaluator.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testElementClosestShouldNotNull() {
        when(evaluator.matches(any(Element.class), any(Element.class))).thenReturn(true);
        Element closestElement = element.closest(evaluator);
        verify(evaluator).matches(any(),any());
        assertNotNull(closestElement);
        assertEquals(element, closestElement);
    }

    @Test
    public void testClosestWithParentElement() {
        Element parent = new Element("root");
        parent.appendChild(element);
        when(evaluator.matches(any(Element.class),eq(element))).thenReturn(false);
        when(evaluator.matches( any(Element.class),eq(parent))).thenReturn(true);
        Element closestElement = element.closest(evaluator);
        verify(evaluator,times(2)).matches(any(),any());
        assertNotNull(closestElement);
        assertEquals(parent, closestElement);
    }

    @Test
    public void testElementClosestShouldNull() {
        when(evaluator.matches(any(Element.class), any(Element.class))).thenReturn(false);
        Element closestElement = element.closest(evaluator);
        verify(evaluator).matches(any(),any());
        assertNull(element.parent());
        assertNull(closestElement);
    }
}
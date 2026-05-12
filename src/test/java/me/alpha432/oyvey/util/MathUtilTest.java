package me.alpha432.oyvey.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilTest {

    @Test
    void testClamp() {
        assertEquals(5, MathUtil.clamp(10, 0, 5));
        assertEquals(0, MathUtil.clamp(-1, 0, 5));
        assertEquals(3, MathUtil.clamp(3, 0, 5));
    }

    @Test
    void testRound() {
        assertEquals(3.14, MathUtil.round(3.14159, 2), 0.001);
        assertEquals(3.141, MathUtil.round(3.14159, 3), 0.0001);
        assertEquals(3.0, MathUtil.round(3.0, 0), 0.001);
    }
}

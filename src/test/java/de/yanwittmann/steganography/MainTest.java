package de.yanwittmann.steganography;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainTest() {
        Main.main(new String[]{"encode", "-f", "src/test/resources/img/nomai.png", "-o", "target/img/encoded.png", "-t", "Hello World!"});
        Main.main(new String[]{"decode", "-f", "target/img/encoded.png"});
        Main.main(new String[]{"has", "-f", "target/img/encoded.png"});
    }

    @Test
    void individualStepsTest() {
        Main.encode(new String[]{"-f", "src/test/resources/img/nomai.png", "-o", "target/img/encoded.png", "-t", "Hello World!"});
        final String decoded = Main.decode(new String[]{"-f", "target/img/encoded.png"}, "");
        assertEquals("Hello World!", decoded);
        final boolean has = Main.has(new String[]{"-f", "target/img/encoded.png"});
        assertTrue(has);

        final String decodedOriginal = Main.decode(new String[]{"-f", "src/test/resources/img/nomai.png"}, "");
        assertNull(decodedOriginal);
        final boolean hasOriginal = Main.has(new String[]{"-f", "src/test/resources/img/nomai.png"});
        assertFalse(hasOriginal);
    }
}
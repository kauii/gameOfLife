package Board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void constructor() {
        Evolution evo = new Evolution();
        Exception e = assertThrows(IllegalCallerException.class, () -> new Grid(evo, 10));

        String expectedMessage = "Only Board can create a new Grid!!!";
        String actualMessage = e.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

}
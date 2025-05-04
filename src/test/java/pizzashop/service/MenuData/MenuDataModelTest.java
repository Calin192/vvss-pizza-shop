package pizzashop.service.MenuData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.MenuDataModel;

import static org.junit.jupiter.api.Assertions.*;

class MenuDataModelTest {

    private MenuDataModel item;

    @BeforeEach
    void setUp() {

        item = new MenuDataModel("Hawaii", 2, 50.0);
    }

    @Test
    void quantityShouldBe2() {
        assertEquals(2, item.getQuantity());
    }

    @Test
    void menuItemShouldNotBeRustica() {
        assertNotEquals("Rustica", item.getMenuItem());
    }

    @Test
    void menuItemShouldBeHawaii() {
        assertEquals("Hawaii", item.getMenuItem());
    }

    @Test
    void priceShouldBe50() {
        assertEquals(50.0, item.getPrice(), 1e-6);
    }
}

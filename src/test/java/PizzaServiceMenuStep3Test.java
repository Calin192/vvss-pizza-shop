import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.MenuDataModel;
import pizzashop.repository.MenuRepository;
import pizzashop.service.PizzaService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PizzaServiceMenuStep3Test {

    private Path tempFile;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() throws Exception {
        // Creăm fișierul cu 3 linii: fiecare "nume,pret"
        tempFile = Files.createTempFile("menu-test", ".txt");
        List<String> lines = List.of(
                "Margherita,10.0",
                "Carbonara,12.5",
                "Hawaii,15.0"
        );
        Files.write(tempFile, lines);

        // Repository real încarcă datele, Service le preia
        var menuRepo = new MenuRepository(tempFile.toString());
        pizzaService = new PizzaService(menuRepo, null, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void getMenuData_sizeShouldMatchFileLines() {
        List<MenuDataModel> menu = pizzaService.getMenuData();
        assertEquals(3, menu.size(), "Trebuie 3 intrări pentru cele 3 linii din fișier");
    }

    @Test
    void menuItemsShouldHaveCorrectNameAndPrice() {
        List<MenuDataModel> menu = pizzaService.getMenuData();

        assertEquals("Margherita", menu.get(0).getMenuItem());
        assertEquals(10.0,         menu.get(0).getPrice(), 1e-6);

        assertEquals("Carbonara",  menu.get(1).getMenuItem());
        assertEquals(12.5,         menu.get(1).getPrice(), 1e-6);

        assertEquals("Hawaii",     menu.get(2).getMenuItem());
        assertEquals(15.0,         menu.get(2).getPrice(), 1e-6);
    }
}

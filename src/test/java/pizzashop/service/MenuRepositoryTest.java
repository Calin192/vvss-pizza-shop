package pizzashop.service;

import org.junit.jupiter.api.Test;
import pizzashop.repository.MenuRepository;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuRepositoryTest {
    @Test
    void getAll_sizeShouldBeNine() throws URISyntaxException {
        Path menuPath = Path.of(
                getClass().getClassLoader().getResource("menu.txt").toURI()
        );

        MenuRepository repo = new MenuRepository(menuPath.toString());
        List<?> all = repo.getAll();
        assertEquals(9, all.size(),
                "Sunt 9 rețete în fișierul inițial menu.txt");
    }
}

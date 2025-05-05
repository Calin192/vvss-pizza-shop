import org.junit.jupiter.api.Test;
import pizzashop.repository.MenuRepository;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuRepositoryTest {
    @Test
    void getAll_sizeShouldBeNine() throws URISyntaxException {

        String file ="data/menu.txt";
        MenuRepository repo = new MenuRepository(file);
        List<?> all = repo.getAll();
        assertEquals(9, all.size(),
                "Sunt 9 rețete în fișierul inițial menu.txt");
    }
}

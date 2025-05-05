import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.MenuDataModel;
import pizzashop.repository.MenuRepository;
import pizzashop.service.PizzaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceMenuStep2Test {

    @Mock
    private MenuRepository menuRepo;            // R mock-uit doar la getAll()

    @InjectMocks
    private PizzaService pizzaService;          // S real

    private MenuDataModel m1, m2, m3;

    @BeforeEach
    void setUp() {
        // E = MenuDataModel mock-uit
        m1 = mock(MenuDataModel.class);


        m2 = mock(MenuDataModel.class);

        m3 = mock(MenuDataModel.class);


        // când S apelează menuRepo.getAll(), dă lista de mock-uri

    }

    @Test
    void getMenuData_returnsAllMockedItems() {
//        when(m1.getMenuItem()).thenReturn("X");
//        when(m1.getPrice()).thenReturn(10.0);
//        when(m2.getMenuItem()).thenReturn("Y");
//        when(m2.getPrice()).thenReturn(20.0);
//        when(m3.getMenuItem()).thenReturn("Z");
//        when(m3.getPrice()).thenReturn(30.0);
        when(menuRepo.getAll()).thenReturn(List.of(m1, m2, m3));
        List<MenuDataModel> list = pizzaService.getMenuData();
        assertEquals(3, list.size(), "Trebuie să fie exact 3 elemente stub-uite");
        // verificăm că s-a chemat o dată R.getAll()
        verify(menuRepo, times(1)).getAll();
    }

    @Test
    void returnedItemsAreExactlyTheMocks() {
        when(menuRepo.getAll()).thenReturn(List.of(m1, m2, m3));
        List<MenuDataModel> list = pizzaService.getMenuData();
        assertSame(m1, list.get(0));
        assertSame(m2, list.get(1));
        assertSame(m3, list.get(2));
    }
}

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceWithMockTest {
    @Mock
    PaymentRepository  paymentRepository;
    @Mock
    MenuRepository menuRepository;
    @InjectMocks
    PizzaService paymentService;
    @Mock
    ValidatorPizzaService validatorPizzaService;
    private List<Payment>paymentsList;
    @Test
    void getMenuData_sizeShouldBeNine()  {

        // cream o lista cu 9 elemente dummy
        List<MenuDataModel> dummyMenu = IntStream.range(1,10)
                .mapToObj(i -> new MenuDataModel("Item"+i, i, i*10.0))
                .collect(Collectors.toList());

        when(menuRepository.getAll()).thenReturn(dummyMenu);
        PizzaService svc = new PizzaService(menuRepository, null, null);

        List<MenuDataModel> menu = svc.getMenuData();
        assertEquals(9, menu.size(),
                "Sunt 9 tipuri de pizza în fișierul menu.txt");
    }
    @BeforeEach
    void setUp() throws PaymentValidationException {
        paymentsList=new ArrayList<>();
        when(paymentRepository.getAll()).thenReturn(paymentsList);


    }

    @AfterEach
    void tearDown() {
        paymentRepository.getAll().clear();
        paymentRepository.writeAll();
    }

    @Test
    void getPayments() {
        List<Payment> result=paymentService.getPayments();
        assertEquals(0,result.size());
        verify(paymentRepository,times(1)).getAll();// verific daca a fost invocata o fatametoda getAll in getPayments
    }

    @Test
    void addPayment() throws PaymentValidationException {
        int table=3;
        PaymentType type =PaymentType.Cash;
        int amount=3;
        Payment payment=new Payment(3,PaymentType.Cash,3);
        doAnswer((invocation)->{
            paymentsList.add(payment);
            return null;
        }).when(paymentRepository).add(any(Payment.class));
        //imi descrie ce ar trebui sa faca mockul cand se apeleaza payRepo add
        paymentService.addPayment(payment.getTableNumber(),payment.getType(),payment.getAmount());
        verify(validatorPizzaService, times(1))
                .validateAddPayment(table, type, amount);
        verify(paymentRepository,times(1)).add(any());
        //verific ca s-a apelat o data add din repo in  la apelul addPayment din service
        List<Payment> result=paymentService.getPayments();
        assertEquals(1,result.size());
        assertEquals(payment,result.get(0));
    }
}

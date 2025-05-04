package pizzashop.service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTestWithMock {
    @Mock
    PaymentRepository  paymentRepository;
    @InjectMocks
    PizzaService paymentService;
    @Mock
    ValidatorPizzaService validatorPizzaService;
    private List<Payment>paymentsList;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.ValidatorPizzaService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PizzaServiceRepositoryIntegrationStep2Test {

    private PaymentRepository repo;
    private PizzaService pizzaService;
    private ValidatorPizzaService validator;

    @BeforeEach
    void setup() throws Exception {
        // 1) creez un fișier gol temporar ca să nu scriu în repo-ul „real”
        Path temp = Files.createTempFile("payments", ".txt");
        repo = new PaymentRepository(temp.toString());

        // 2) stub validatorul (nu vrem să arunce excepție)
        validator = mock(ValidatorPizzaService.class);
        pizzaService = new PizzaService(null, repo, validator);

        // 3) creez două Payment mock și le inserez în repo.entityList
        Payment p1 = mock(Payment.class);
        when(p1.getType()).thenReturn(PaymentType.Cash);
        when(p1.getAmount()).thenReturn(5.0);

        Payment p2 = mock(Payment.class);
        when(p2.getType()).thenReturn(PaymentType.Card);
        when(p2.getAmount()).thenReturn(7.5);

        repo.getAll().add(p1);
        repo.getAll().add(p2);
    }

    @Test
    void getPayments_returnsExactlyMocksFromRepo() {
        List<Payment> payments = pizzaService.getPayments();
        assertEquals(2, payments.size());
        // obiectele chiar sunt instanțe de Payment (mock-uri)
        assertTrue(payments.stream().allMatch(p -> p instanceof Payment));
    }

    @Test
    void getTotalAmount_sumsAmountsFromMockPayments() {
        double cashTotal = pizzaService.getTotalAmount(PaymentType.Cash);
        assertEquals(5.0, cashTotal, 1e-6);

        double cardTotal = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(7.5, cardTotal, 1e-6);
    }
}

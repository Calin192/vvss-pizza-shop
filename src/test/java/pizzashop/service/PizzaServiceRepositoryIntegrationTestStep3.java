package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceIntegrationTestStep3 {

    private Path tempFile;
    private PaymentRepository repo;
    private PizzaService pizzaService;

    @BeforeEach
    void setup() throws Exception {
        // creez un fișier gol temporar
        tempFile = Files.createTempFile("payments", ".txt");
        repo = new PaymentRepository(tempFile.toString());

        // folosesc validatorul real sau unul stub (care nu aruncă)
        ValidatorPizzaService validator = new ValidatorPizzaService();
        pizzaService = new PizzaService(null, repo, validator);
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void addPayment_andGetPayments_endToEnd() throws Exception, PaymentValidationException {
        // S + R + E: constructorul Payment e chemât, repo.add scrie în fișier
        pizzaService.addPayment(3, PaymentType.Cash, 10.0);
        List<Payment> payments = pizzaService.getPayments();
        assertEquals(1, payments.size());

        Payment p = payments.get(0);
        assertEquals(3, p.getTableNumber());
        assertEquals(PaymentType.Cash, p.getType());
        assertEquals(10.0, p.getAmount(), 1e-6);
    }

    @Test
    void getTotalAmount_afterMultipleAdds_endToEnd() throws Exception, PaymentValidationException {
        pizzaService.addPayment(1, PaymentType.Cash, 5.0);
        pizzaService.addPayment(2, PaymentType.Cash, 7.5);
        pizzaService.addPayment(3, PaymentType.Card, 3.0);

        assertEquals(12.5, pizzaService.getTotalAmount(PaymentType.Cash), 1e-6);
        assertEquals(3.0,  pizzaService.getTotalAmount(PaymentType.Card), 1e-6);
    }
}

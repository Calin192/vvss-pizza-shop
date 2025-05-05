import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest {
    PizzaService service;
    MenuRepository repoMenu;
    PaymentRepository payRepo;
    ValidatorPizzaService validatorPizzaService;
    @ParameterizedTest
    @CsvSource({
            "1, Cash, 10, true",   // BVA - min valid table
            "0, Card, 10, false",  // BVA - sub min invalid table
            "15, Card, 100, true", // BVA - max valid table
            "16, Card, 50, true", // BVA - peste max invalid table
            "5, Cash, 0, true",    // BVA - min valid amount
            "2, Card, -0.01, false", // BVA - sub min amount
            "3, Cash, 1000, true"  // BVA - valoare mare amount
    })
    void testBVA(int table, PaymentType type, double amount, boolean isValid) {
        if (isValid) {
            assertDoesNotThrow(() -> service.addPayment(table, type, amount));
        } else {
            assertThrows(PaymentValidationException.class, () -> service.addPayment(table, type, amount));
        }
    }

//    @BeforeEach
//    void setUp() {
//        repoMenu = new MenuRepository("data/menu.txt");
//        payRepo = new PaymentRepository("data/payments.txt");
//        validatorPizzaService = new ValidatorPizzaService();
//        service = new PizzaService(repoMenu, payRepo, validatorPizzaService);
//
//    }
@BeforeEach
void setUp() {
    // Rescrie fișierul pentru testare curată
    try (PrintWriter writer = new PrintWriter("data/payments.txt")) {
        writer.print(""); // șterge conținutul
    } catch (IOException e) {
        e.printStackTrace();
    }

    repoMenu = new MenuRepository("data/menu.txt");
    payRepo = new PaymentRepository("data/payments.txt");
    validatorPizzaService = new ValidatorPizzaService();
    service = new PizzaService(repoMenu, payRepo, validatorPizzaService);
}


    @AfterEach
    void tearDown() {

    }


    @Test
    @DisplayName("ECP - Valid amount")
    void testECPValidAmount() {
        assertDoesNotThrow(() -> service.addPayment(1, PaymentType.Cash, 0),
                "Expected no exception for valid data.");
    }

    @Test
    @DisplayName("Combination - Valid table and valid amount")
    void testCombinationValidTableAndAmount() {
        assertDoesNotThrow(() -> service.addPayment(1, PaymentType.Cash, 50),
                "Expected no exception for valid table and amount.");
    }

    @Test
    @DisplayName("Combination - Invalid table, valid amount")
    void testCombinationInvalidTableValidAmount() {
        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
                () -> service.addPayment(-1, PaymentType.Cash, 25.5),
                "Expected exception for invalid table.");
        assertEquals("Masa trebuie să fie cel puțin 1.", exception.getMessage());
    }

    @Test
    @DisplayName("Combination - Valid table, invalid amount")
    void testCombinationValidTableInvalidAmount() {
        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
                () -> service.addPayment(2, PaymentType.Card, -10),
                "Expected exception for invalid amount.");
        assertEquals("Valoarea achitată trebuie să fie cel puțin 0.", exception.getMessage());
    }

    @Test
    @DisplayName("Combination - Invalid table and invalid amount")
    void testCombinationInvalidTableAndAmount() {
        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
                () -> service.addPayment(-1, PaymentType.Card, -10),
                "Expected exception for invalid table and amount.");
        assertTrue(exception.getMessage().contains("Masa trebuie să fie cel puțin 1.")
                        || exception.getMessage().contains("Valoarea achitată trebuie să fie cel puțin 0."),
                "Expected exception message to contain both validation errors.");
    }
    @Test
    @DisplayName("getTotalAmount - Null payment list")
    void testGetTotalAmountWithNullPayments() {

        PaymentRepository emptyRepo = new PaymentRepository("data/empty_payments.txt"); // fișier gol
        PizzaService testService = new PizzaService(repoMenu, emptyRepo, validatorPizzaService);

        double total = testService.getTotalAmount(PaymentType.Cash);
        assertEquals(0.0, total, "Expected total to be 0.0 for null or empty list");
    }


    @Test
    @DisplayName("getTotalAmount - No matching payments")
    void testGetTotalAmountNoMatchingPayments() throws PaymentValidationException {
        service.addPayment(1, PaymentType.Cash, 20.0);
        service.addPayment(2, PaymentType.Cash, 30.0);

        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, total, "Expected total to be 0.0 when no payments match type");
    }

    @Test
    @DisplayName("getTotalAmount - Matching payments exist")
    void testGetTotalAmountMatchingPayments() throws PaymentValidationException {
        service.addPayment(1, PaymentType.Cash, 10.0);
        service.addPayment(2, PaymentType.Cash, 25.0);
        service.addPayment(3, PaymentType.Card, 5.0);

        double totalCash = service.getTotalAmount(PaymentType.Cash);
        double totalCard = service.getTotalAmount(PaymentType.Card);

        assertEquals(35.0, totalCash, "Expected total Cash amount to be 35.0");
        assertEquals(5.0, totalCard, "Expected total Card amount to be 5.0");
    }

}
package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest {
    PizzaService service;
    MenuRepository repoMenu;
    PaymentRepository payRepo;
    ValidatorPizzaService validatorPizzaService;
    @ParameterizedTest
    @CsvSource({
            "1, Cash, 25.5, true",    // valid input
            "-1, Card, 33, false",    // invalid table
            "2, Card, -10, false",    // invalid amount
            "1, Cash, 0, true",       // valid amount, boundary case
            "-1, Cash, 25.5, false",  // invalid table
            "2, Cash, -1, false"      // invalid amount
    })
    @BeforeEach
    void setUp() {
        repoMenu = new MenuRepository("data/menu.txt");
        payRepo = new PaymentRepository("data/payments.txt");
        validatorPizzaService = new ValidatorPizzaService();
        service = new PizzaService(repoMenu, payRepo, validatorPizzaService);

    }

    @AfterEach
    void tearDown() {

    }
//    @Test
//    @DisplayName("Add Payment - Valid Input")
//    void addPayment_ValidInput() {
//        assertDoesNotThrow(() -> service.addPayment(1, PaymentType.Cash, 25.5),
//                "Expected no exception for valid payment data.");
//    }
//
//    @Test
//    @DisplayName("Add Payment - Invalid Table")
//    void addPayment_InvalidTable() {
//        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
//                () -> service.addPayment(-1, PaymentType.Card, 33),
//                "Expected PaymentValidationException for invalid table.");
//        assertEquals("Masa trebuie să fie cel puțin 1.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Add Payment - Invalid Amount")
//    void addPayment_InvalidAmount() {
//        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
//                () -> service.addPayment(2, PaymentType.Card, -10),
//                "Expected PaymentValidationException for invalid amount.");
//        assertEquals("Amount nu poate fi sub 0 .", exception.getMessage());
//    }

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
                        || exception.getMessage().contains("Amount nu poate fi sub 0 ."),
                "Expected exception message to contain both validation errors.");
    }
}
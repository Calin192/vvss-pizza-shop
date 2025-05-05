import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {

    Payment payment;
    @BeforeEach
    void setUp() {
        payment=new Payment(3, PaymentType.Cash,25.5f);
    }

    @Test
    void getTableNumber() {
        assertEquals(3,payment.getTableNumber());
    }

    @Test
    void setTableNumber() {
        payment.setTableNumber(4);
        assertEquals(4,payment.getTableNumber());
    }
}

package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    PaymentRepository repository;

    @BeforeEach
    void setUp() {
        String filename="data/empty_payments.txt";
        repository=new PaymentRepository(filename);
    }

    @AfterEach
    void tearDown() {
        repository.getAll().clear();
        repository.writeAll();
    }

    @Test
    void getAll() {
        assertEquals(0,repository.getAll().size());
    }

    @Test
    void add() {
        Payment payment=getPayment(3, PaymentType.Cash,33.3);

        repository.add(payment);
        List<Payment> paymentList=repository.getAll();
        assertEquals(1,paymentList.size());
        assertEquals(payment.getTableNumber(),paymentList.get(0).getTableNumber());
        assertEquals(payment.getType(),paymentList.get(0).getType());
        assertEquals(payment.getAmount(),paymentList.get(0).getAmount());
    }

    Payment getPayment(int tableNumber,PaymentType type,double amount){
        Payment payment=mock(Payment.class);
        when(payment.getTableNumber()).thenReturn(tableNumber);
        when(payment.getType()).thenReturn(type);
        when(payment.getAmount()).thenReturn(amount);
        return payment;
    }
}

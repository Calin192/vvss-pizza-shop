package pizzashop.validator;

import pizzashop.model.PaymentType;

public class ValidatorPizzaService {
    public void validateAddPayment(int table , PaymentType type ,double amount ) throws PaymentValidationException {

        if(table<1){
            throw new PaymentValidationException("Masa trebuie să fie cel puțin 1.");
        }
        if (amount < 0) {//poate fi 0 daca e ziua cuiva
            throw new PaymentValidationException("Valoarea achitată trebuie să fie cel puțin 0.");
        }

    }
}

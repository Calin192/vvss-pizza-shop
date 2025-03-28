package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.PaymentValidationException;
import pizzashop.validator.ValidatorPizzaService;

import java.util.List;

public class PizzaService {

    private MenuRepository menuRepo;
    private PaymentRepository payRepo;
    private ValidatorPizzaService validatorPizzaService;

    public PizzaService(MenuRepository menuRepo, PaymentRepository payRepo,ValidatorPizzaService validatorPizzaService){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
        this.validatorPizzaService=validatorPizzaService;
    }

    public List<MenuDataModel> getMenuData(){return menuRepo.getAll();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public void addPayment(int table, PaymentType type, double amount) throws PaymentValidationException {
        validatorPizzaService.validateAddPayment(table, type, amount);
        Payment payment= new Payment(table, type, amount);
        payRepo.add(payment);
    }

    public double getTotalAmount(PaymentType type){
        double total=0.0f;
        List<Payment> l=getPayments();
        if ((l==null) ||(l.size()==0)) return total;
        for (Payment p:l){
            if (p.getType().equals(type))
                total+=p.getAmount();
        }
        return total;
    }

}
package pizzashop.repository;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PaymentRepository extends FileRepository<Payment>{


    public PaymentRepository(String filePath) {
        super(filePath);
    }

    private Payment getPayment(String line){
        Payment item;
        if (line==null|| line.isEmpty()) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        int tableNumber= Integer.parseInt(st.nextToken());
        String type= st.nextToken();
        double amount = Double.parseDouble(st.nextToken());
        item = new Payment(tableNumber, PaymentType.valueOf(type), amount);
        return item;
    }

    public Boolean add(Payment payment){
        entityList.add(payment);
        writeAll();
        return true;
    }

    public List<Payment> getAll(){
        return entityList;
    }

    public void writeAll(){
        //ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (Payment p: entityList) {
                System.out.println(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void readData() {
        File file = new File(filePath);

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                entityList.add(payment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
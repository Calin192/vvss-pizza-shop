package pizzashop;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pizzashop.controller.MainGUIController;
import pizzashop.gui.KitchenGUI;
import pizzashop.model.MenuDataModel;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.ValidatorPizzaService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        MenuRepository repoMenu = new MenuRepository("data/menu.txt");
        PaymentRepository payRepo= new PaymentRepository("data/payments.txt");
        ValidatorPizzaService validatorPizzaService=new ValidatorPizzaService();
        PizzaService service = new PizzaService(repoMenu, payRepo,validatorPizzaService);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainFXML.fxml"));
        //VBox box = loader.load();
        Parent box = loader.load();
        MainGUIController ctrl = loader.getController();
        ctrl.setService(service);
        primaryStage.setTitle("PizzeriaX");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setOnCloseRequest(event -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to exit the Main window?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                File restaurantDataFile = new File("data/restaurant_data.csv");
                try (BufferedReader br = new BufferedReader(new FileReader(restaurantDataFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts[0].equals("Closing Hour")){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                            LocalTime closingTime = LocalTime.parse(parts[1], formatter);
                            LocalTime currentTime = LocalTime.now();
                            if(currentTime.isBefore(closingTime)){
                                Alert cannotCloseAlert = new Alert(Alert.AlertType.ERROR, "Cannot close at this hour.", ButtonType.YES);
                                cannotCloseAlert.showAndWait();
                                event.consume();
                                return;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Stage stage = (Stage) this.getScene().getWindow();
                System.out.println("Incasari cash: "+service.getTotalAmount(PaymentType.Cash));
                System.out.println("Incasari card: "+service.getTotalAmount(PaymentType.Card));

                primaryStage.close();
            }
            // consume event
            else if (result.get() == ButtonType.NO){
                event.consume();
            }
            else {
                event.consume();

            }

        });
        primaryStage.setScene(new Scene(box));
        primaryStage.show();
        KitchenGUI kitchenGUI = new KitchenGUI();
        kitchenGUI.KitchenGUI();
    }

    public static void main(String[] args) { launch(args);
    }
}
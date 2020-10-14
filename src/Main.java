import controller.TransactionController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Money;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/Transaction.fxml"));
        primaryStage.setTitle("Transaction");
        primaryStage.setScene(new Scene(root, 850, 550));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                TransactionController transactionController = new TransactionController();
                transactionController.writeFile();
            }
        });
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        String price = nf.format(Integer.parseInt("500000"));
        System.out.println(price);
    }

}

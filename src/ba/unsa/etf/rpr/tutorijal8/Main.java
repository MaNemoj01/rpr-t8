package ba.unsa.etf.rpr.tutorijal8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader( getClass().getResource("pretrazivac.fxml") );
        Parent root = loader.load();
        loader.setController( new PretrazivacController() );
        primaryStage.setTitle("Pretraga datoteka");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

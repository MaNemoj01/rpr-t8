package ba.unsa.etf.rpr.tutorijal8;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class PretrazivacController implements Initializable  {


    public Label labelUzorak;
    public TextField textBoxPretraga;
    public Button dugmeTrazi;
    public ListView<String> listaPretrage;
    public Button dugmePrekini;
    Thread threadTrazi;
    public static File trenutniElementListe;

    public static File getTrenutniElementListe() {
        return trenutniElementListe;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dugmePrekini.setDisable(true);

        listaPretrage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (newValue != null) {
                    //Pozvati novi prozor
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("posiljka.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle("PosiljkaController");
                    //secondaryStage.setResizable(false);
                    secondaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                    secondaryStage.initModality(Modality.APPLICATION_MODAL);
                    //secondaryStage.setResizable(false);
                    if (listaPretrage.getSelectionModel().getSelectedItem() != null)
                        trenutniElementListe = new File(listaPretrage.getSelectionModel().getSelectedItem());
                    secondaryStage.show();
                }
            }
        });
    }

    public void prekiniPritisnuto(ActionEvent actionEvent) {
        dugmeTrazi.setDisable(false);
        dugmePrekini.setDisable(true);
        threadTrazi.stop();
    }

    public void pritisniEnter(KeyEvent keyEvent) {
        if( keyEvent.getCode().equals(KeyCode.ENTER) && !(dugmeTrazi.isDisable()) ){
            dugmeTrazi.requestFocus();
            traziPritisnuto(null);
        }

        if( keyEvent.getCode().equals(KeyCode.ESCAPE) ){
            dugmePrekini.requestFocus();
            prekiniPritisnuto(null);
        }
    }

    public class Pretraga implements Runnable{

        @Override
        public void run() {
            String textName = textBoxPretraga.getText();
            //displayAll( new File( System.getProperty("user.home")) , textName );
            displayAll( new File( "/Users/MaNemoj/Desktop" ) , textName );
        }
    }

    public void displayAll(File path , String name){
        if( path.isDirectory() ){
            File[] files = path.listFiles();
            if( files != null ){
                for( File f: files ){
                    displayAll( f, name );
                }
            }
        }
        if( path.isFile() ){

            if( path.getName().contains( name ) ) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    listaPretrage.getItems().add(path.getAbsolutePath());
                } );
            }
        }
        //if( path.getAbsolutePath().equals( System.getProperty("user.home") ) ) dugmeTrazi.setDisable(false);
        if( path.getAbsolutePath().equals( "/Users/MaNemoj/Desktop" ) ){
            dugmeTrazi.setDisable(false);
            dugmePrekini.setDisable(true);
        }

    }

    public void traziPritisnuto(ActionEvent actionEvent)  {
        dugmeTrazi.setDisable(true);
        dugmePrekini.setDisable(false);
        listaPretrage.getSelectionModel().clearSelection();
        listaPretrage.getItems().clear();
        Pretraga pretraga = new Pretraga();
        threadTrazi = new Thread(pretraga);
        threadTrazi.start();
    }

}

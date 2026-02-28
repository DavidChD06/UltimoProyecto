/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

/**
 *
 * @author luisf
 */
public class MenuController {

    /**
     * @todo El siguiente código permite la implementación de las acciones CRUD
     * desde el menú reutilizable de forma diferente(polimorfismo) en cada vista 
     * que lo incluya.
     */
    private MenuActionsHandler handler;
    /**
     * Este método debe ser utilizado para indicar desde cada controlador de vista que
     * incluya el menú que controlador se encargará de manejar cada acción.
     * @param handler La clase que implementa MenuActionsHandler.
     */
    public void setMenuActionsHandler(MenuActionsHandler handler) {
        this.handler = handler;
    }
    private Stage stage; 
    
    public void setStage(Stage stage) { 
        this.stage = stage; 
    } 

    @FXML
    private void handleCreate() {
        if (handler != null) {
            handler.onCreate();
        }
    }

    @FXML
    private void handleUpdate() {
        if (handler != null) {
            handler.onUpdate();
        }
    }

    @FXML
    private void handleRefresh() {
        if (handler != null) {
            handler.onRefresh();
        }
    }

    @FXML
    private void handleDelete() {
        if (handler != null) {
            handler.onDelete();
        }
    }
    
    @FXML
    public void menuExitApp(ActionEvent event) {
        //Salimos de la aplicación por completo
        Platform.exit();
    }

    @FXML
    //Correccion del LogOut para que vuelva a la ventana principal correctamente
    public void menuLogOut(ActionEvent event) {
         try { 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectoCRUD/ui/SignIn.fxml")); 

            Parent root = loader.load(); 
            SignInController controller = loader.getController(); 
            controller.init(this.stage, root); 

        } catch (IOException e) { 
            e.printStackTrace(); 
            showAlert("Error", "Log Out Error","Could not load the sign-in window."); 
        } 
    } 

    @FXML
    private void helpAccount(ActionEvent event) {
        ventanaAyuda("Help - Accounts", "/proyectoCRUD/ui/resources/helpAccount.html");
    }

    @FXML
    private void helpMovement(ActionEvent event) {
        ventanaAyuda("Help - Movements", "/proyectoCRUD/ui/resources/helpMovement.html");
    }

    @FXML
    private void helpCustomer(ActionEvent event) {
        ventanaAyuda("Help - Customers", "/proyectoCRUD/ui/resources/helpCustomer.html");
    }

    @FXML
    private void helpAboutApp(ActionEvent event) {
        ventanaAyuda("About the App", "/proyectoCRUD/ui/resources/helpAbout.html");
    }
     
      private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
      private void ventanaAyuda(String title, String resourcePath) {
        try {
            Stage helpStage = new Stage();
            helpStage.setTitle(title);

            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            java.net.URL url = getClass().getResource(resourcePath);
            
            if (url == null) {
                showAlert("Error", "File not found", "Could not find file: " + resourcePath);
                return;
            }

            webEngine.load(url.toExternalForm());

            Scene scene = new Scene(webView, 600, 400);
            helpStage.setScene(scene);
            helpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Help Error", "Could not open the help window.");
        }
    }

}

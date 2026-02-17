/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.logic.MovementRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.Customer;
import proyectoCRUD.model.Movement;

/**
 *
 * @author miguel
 */
public class MovementController {

    @FXML
    private Button btNewMovement;
    @FXML
    private Button btUndo;
    @FXML
    private Button btCancel;
    @FXML
    private Label lbIdAcount;
    @FXML
    private Label lbErrorAmount;
    @FXML
    private Label lbGeneralError;
    @FXML
    private Label lbBalance;
    @FXML
    private TextField tfAmount;
    @FXML
    private TableView<Movement> tbMovement;
    @FXML
    private TableColumn<Movement, Date> tbColDate;
    @FXML
    private TableColumn<Movement, Double> tbColAmount;
    @FXML
    private TableColumn<Movement, String> tbColType;
    @FXML
    private TableColumn<Movement, Double> tbColBalance;
    @FXML
    private ComboBox selectType;
    
    
    private Customer customer;
    private Account account;
    private Stage stage;
    private final Stage movementStage = new Stage();
    
    
    private static final Logger LOGGER = Logger.getLogger("ProyectoCRUD.ui");
    
    MovementRESTClient restClient = new MovementRESTClient();
    AccountRESTClient accClient = new AccountRESTClient();
    
    ObservableList<Movement> movements ;

    public void init(Stage stage, Parent root) {
        try {
                movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {
                    }, account.getId().toString()));        
            Scene scene = new Scene(root);
            //movementStage.initModality(Modality.APPLICATION_MODAL);
            movementStage.setScene(scene);
            movementStage.setTitle("Movements");
            movementStage.setResizable(false);
                
            btNewMovement.setDisable(false);
            
            btCancel.setDisable(false);
            
            ObservableList<String> type = FXCollections.observableArrayList("Deposit","Payment");
            selectType.setItems(type);

            selectType.focusedProperty().addListener(this::handleTypeOnFocusedChange);
                    
            btNewMovement.setOnAction(this::handlebtNewMovementOnAction);
            btUndo.setOnAction(this::handlebtUndoOnAction);
            btCancel.setOnAction(this::handlebtCancelOnAction);

            tbColDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            
            tbColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            tbColType.setCellValueFactory(new PropertyValueFactory<>("description"));
            tbColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            
            //@TODO alinear celdas de amount y balance a la derecha
            tbColAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
            tbColBalance.setStyle("-fx-alignment: CENTER-RIGHT;");
            
            //@TODO formateo de cendas
            tbColDate.setCellFactory(column -> new TableCell<Movement, Date>(){
                private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                @Override
                protected void updateItem(Date item, boolean empty){
                    super.updateItem(item,empty);
                    setText(empty || item == null ?  null : format.format(item));
                }    
            }); 
            //@TODO colocacion del simbolo € en las columnas de dinero(amount y balance)
            tbColAmount.setCellFactory(column -> new TableCell<Movement, Double>(){
                @Override
                 protected void updateItem(Double item, boolean empty){
                    super.updateItem(item, empty);
                    setText(empty || item == null ?  null : String.format("%.2f €", item));
                }
            });
            
            tbColBalance.setCellFactory(column -> new TableCell<Movement, Double>(){
                @Override
                 protected void updateItem(Double item, boolean empty){
                    super.updateItem(item, empty);
                    setText(empty || item == null ?  null : String.format("%.2f €", item));
                }
            });
            //labels de informacion
            lbIdAcount.setText(account.getId().toString());
            lbBalance.setText(account.getBalance().toString());
           
            tbMovement.setItems(movements);
            LOGGER.info(movements.toString());
            
        } catch (Exception e) {
            
            LOGGER.info(e.getMessage());
        }

        movementStage.show();

    }
    public void setAccount(Account account){
        this.account = account;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    private void handleTypeOnFocusedChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue)   {
                if(!selectType.hasProperties()){
                    lbErrorAmount.setText("You have to select the type");
                    throw new IllegalArgumentException("You have to select the type");
                }
                lbErrorAmount.setText("");
            }
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }
    //BOTONES
    /**
     *
     * @param event
     */
    private void handlebtCancelOnAction(ActionEvent event) {
        try {
            //this.stage.close();
            new Alert(AlertType.INFORMATION, "Are you sure you want to leave?").showAndWait();
            
            movementStage.close();

        } catch (InternalServerErrorException e) {
            new Alert(AlertType.INFORMATION, "Internal server error, please wait or contact your service provider").showAndWait();
        }
    }
    
     private void handlebtUndoOnAction(ActionEvent event) {
        try {
            movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {
                    }, account.getId().toString()));

            tbMovement.setItems(movements);

            //Ultimo movimiento
            Movement lastMovement = movements.stream()
                    .max(Comparator.comparing(Movement::getTimestamp)).orElse(null);

            if (lastMovement == null) {
                btUndo.setDisable(true);
                throw new Exception("No movements to undo");
            }

            String movementID = lastMovement.getId().toString();

            double amount = lastMovement.getAmount();
            String tipo = lastMovement.getDescription();

            if ("Deposit".equals(tipo)) {
                account.setBalance(account.getBalance() - amount);
            } else if ("Payment".equals(tipo)) {
                account.setBalance(account.getBalance() + amount);
            }

            accClient.updateAccount_XML(account);
            restClient.remove(movementID);
            tbMovement.getItems().remove(lastMovement);
            tbMovement.refresh();
            lbBalance.setText(account.getBalance().toString());
            lbGeneralError.setText("");
            btUndo.setDisable(true);


        } catch (ClientErrorException e) {
            LOGGER.severe("Error undoing movement: " + e.getMessage());
        } catch (Exception e) {
            lbGeneralError.setText(e.toString());
            LOGGER.severe("Unexpected error: " + e.getMessage());
        }
    }

    private void handlebtNewMovementOnAction(ActionEvent event) {
        try {
            if (tfAmount.getText().isEmpty() || selectType.getValue() == null) {
                throw new Exception("Please fill all fields");
            }
            double balanceActual = account.getBalance();
            double lineActual = account.getCreditLine();

            Movement movement = new Movement();

            String tipo = (String) selectType.getValue();
            double amount = Double.parseDouble(tfAmount.getText());

            double newBalance = balanceActual;
            double newLine = lineActual;

            //lbBalance.setText(String.valueOf(account.getBalance()));

            if ("Payment".equals(tipo)) {
                if (balanceActual + lineActual < amount) {
                    throw new Exception("You don't have enough balance");
                }
                if (balanceActual >= amount) {
                    newBalance = balanceActual - amount;
                } else {
                    double lineNecesario = amount - balanceActual;
                    newBalance = 0.0;
                    newLine = lineActual - lineNecesario;
                }
            }
            if ("Deposit".equals(tipo)) {
                newBalance = balanceActual + amount;
            }

            movement.setAmount(amount);
            movement.setDescription(tipo);
            movement.setTimestamp(new Date());
            movement.setBalance(newBalance);

            this.account.setBalance(newBalance);
            this.account.setCreditLine(newLine);
            accClient.updateAccount_XML(this.account);

            restClient.create_XML(movement, account.getId().toString());
            //lbBalance.setText(String.format("%.2f", newBalance));

            movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {
                    }, account.getId().toString()));

            tbMovement.setItems(movements);
            
            btUndo.setDisable(false);
            
            lbBalance.setText(account.getBalance().toString());
            lbBalance.setText(String.valueOf(newBalance));
            
            lbGeneralError.setText("");
            tfAmount.setText("");
            
        } catch (NumberFormatException e) {
            lbGeneralError.setText("Invalid format: Amount must be a number");
        } catch (IllegalArgumentException | ClientErrorException e) {
            LOGGER.info(e.getMessage());
        } catch (Exception e) {
            lbGeneralError.setText(e.toString());
            LOGGER.severe(e.getMessage());
        }
    }
}


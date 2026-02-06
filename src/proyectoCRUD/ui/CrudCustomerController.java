/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.logic.CustomerRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.Customer;
import proyectoCRUD.ui.MenuController;

/**
 * Controller class for the Customer CRUD view.
 * Handles the interaction between the UI and the logic layer for managing Customers.
 *
 * @author david
 */
public class CrudCustomerController {
    

    @FXML private Window menuCustomer;
    @FXML private MenuController menuController;
    
    @FXML
    private TableColumn tbID;
    @FXML
    private TableColumn<Customer, String> tbName;
    @FXML
    private TableColumn<Customer, String> tbMidInit;
    @FXML
    private TableColumn<Customer, String> tbSurname;
    @FXML
    private TableColumn<Customer, String> tbStreet;
    @FXML
    private TableColumn<Customer, String> tbCity;
    @FXML
    private TableColumn<Customer, String> tbState;
    @FXML
    private TableColumn<Customer, Integer> tbZip;
    @FXML
    private TableColumn<Customer, String> tbEmail;
    @FXML
    private TableColumn<Customer, Long> tbPhone;
    @FXML
    private TableColumn<Customer, String> tbPassw;
    @FXML
    private TableView<Customer> tbCustomers;
    @FXML
    private Button bAdd;
    @FXML
    private Button bExit;
    @FXML
    private Button bDelete;
    @FXML
    private Button bRefresh;
    
    private final Stage CustomerStage = new Stage();
    private Scene scene;
     
    private final CustomerRESTClient clientManager = new CustomerRESTClient();
    
    private static final Logger LOGGER=Logger.getLogger("projectinterfaces.ui");
    /**
     * Initializes the controller, sets up the stage, and configures the table columns.
     * Defines cell factories and edit commit handlers for data validation and updates.
     *
     * @param stage The stage where the scene will be displayed.
     * @param root  The root node of the FXML hierarchy.
     */
    
    public void init(Stage stage, Parent root) {
        //Creating logger
        LOGGER.info("Initializing window");
        
        //Loading window
        scene = new Scene(root); 
        CustomerStage.setScene(scene);
        CustomerStage.setTitle("Customers administration");
        CustomerStage.setResizable(false);
        CustomerStage.show();
        
        bDelete.setDisable(true);
        
        reloadTable();
        
        tbCustomers.setEditable(true);
        
        //Creating handler methods
        bExit.setOnAction(this::handleBtExitOnAction);
        bDelete.setOnAction(this::handleBtDeleteOnAction);
        bRefresh.setOnAction(this::handleBtRefreshOnAction);
        bAdd.setOnAction(this::handleBtAddOnAction);
        
        CustomerStage.setOnCloseRequest(this::handleBtExitOnAction);
        tbCustomers.getSelectionModel().selectedItemProperty().addListener(this::handleCustomerTableSelectionChanged);
        
        tbID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbName.setEditable(false);
        
        //Creating both cellValuefactory and cellFactory for handling tableview
        
        
        //Field name handler
        tbName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbName.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbName.setEditable(true);
        //Lambda method for validating field name on change via cell editing
        tbName.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("Name field not valid");
                    }
                    //3)
                    item.setFirstName(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setFirstName(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field middle initial handler
        tbMidInit.setCellValueFactory(new PropertyValueFactory<>("middleInitial"));
        tbMidInit.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbMidInit.setEditable(true);
        //Lambda method for validating field middle initial on change via cell editing
        tbMidInit.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getMiddleInitial();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>1 || !newValue.trim().matches("[A-Z]+")){
                        throw new Exception("Middle initial field not valid");
                    }
                    //3)
                    item.setMiddleInitial(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setMiddleInitial(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field surname handler
        tbSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbSurname.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbSurname.setEditable(true);
        //Lambda method for validating field surname on change via cell editing
        tbSurname.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getLastName();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("Surname field not valid");
                    }
                    //3)
                    item.setLastName(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setLastName(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field street handler
        tbStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tbStreet.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbStreet.setEditable(true);
        //Lambda method for validating field street on change via cell editing
        tbStreet.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[0-9a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("Street field not valid");
                    }
                    //3)
                    item.setStreet(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setStreet(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field city handler
        tbCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tbCity.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbCity.setEditable(true);
        //Lambda method for validating field city on change via cell editing
        tbCity.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getCity();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("City field not valid");
                    }
                    //3)
                    item.setCity(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setCity(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field state handler
        tbState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbState.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbState.setEditable(true);
        //Lambda method for validating field state on change via cell editing
        tbState.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getState();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("State field not valid");
                    }
                    //3)
                    item.setState(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setState(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field zip handler
        tbZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        tbZip.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tbZip.setEditable(true);
        //Lambda method for validating field zip on change via cell editing
        tbZip.setOnEditCommit(
                (CellEditEvent<Customer, Integer> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    Integer newValue = t.getNewValue();
                    Integer oldValue = item.getZip();
                try{
                    //1)
                    
                    //2)
                    if(newValue.toString().trim().isEmpty() || newValue.toString().trim().length()>5 || newValue.toString().trim().length()<5 || !newValue.toString().trim().matches("[0-9]+")){
                        throw new Exception("Zip field not valid");
                    }
                    //3)
                    item.setZip(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setZip(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
    
        //Field email handler
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbEmail.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbEmail.setEditable(true);
        //Lambda method for validating field email on change via cell editing
        tbEmail.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getEmail();
                try{
                    //1)
                    
                    //2)
                    if(!newValue.trim().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || newValue.trim().isEmpty() || newValue.trim().length()>100){
                        throw new Exception("Email field not valid");
                    }
                    //3)
                    item.setEmail(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setEmail(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field phone handler
        tbPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbPhone.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        tbPhone.setEditable(true);
                //Lambda method for validating field phone on change via cell editing
        tbPhone.setOnEditCommit(
                (CellEditEvent<Customer, Long> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    Long newValue = t.getNewValue();
                    Long oldValue = item.getPhone();
                try{
                    //1)
                    
                    //2)
                    if(!newValue.toString().trim().matches("[0-9 +]+") || newValue.toString().trim().length()>15 || newValue.toString().trim().isEmpty()){
                        throw new Exception("Phone field not valid");
                    }
                    //3)
                    item.setPhone(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setPhone(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        //Field password handler
        tbPassw.setCellValueFactory(new PropertyValueFactory<>("password"));
        tbPassw.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbPassw.setEditable(true);
        //Lambda method for validating field password on change via cell editing
        tbPassw.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getPassword();
                try{
                    //1)
                    
                    //2)
                    if(newValue.trim().isEmpty() || !newValue.trim().matches("^(?=.*[A-Z])(?=.*\\d).{5,30}$")){
                        throw new Exception("Password field not valid");
                    }
                    //3)
                    item.setPassword(newValue);
                    //4)
                    clientManager.edit_XML(item, item.getId());
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    item.setPassword(oldValue);
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                    new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
                }
                
        });
        
        
       
      
    } 
    /**
     * Handles the action event when the exit button is clicked or the window is closed.
     * Shows a confirmation alert before closing the stage.
     *
     * @param event The event that triggered this action.
     */
    private void handleBtExitOnAction(Event event){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                          "Are you sure you want to exit?",
                           ButtonType.YES, ButtonType.NO);
            alert.setTitle("¡Confirm Exit!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                //Lanzamos la ventana emergente para pedir confirmación de salida
                CustomerStage.close();
            }
            event.consume();
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
            new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
        }
    }
    /**
     * Listener for selection changes in the customer table.
     * Enables or disables the delete button based on whether an item is selected.
     *
     * @param observable The `ObservableValue` which value changed.
     * @param oldValue   The old value.
     * @param newValue   The new value.
     */
    private void handleCustomerTableSelectionChanged(ObservableValue observable, Object oldValue, Object newValue){
        try{
            if(newValue != null){
                bDelete.setDisable(false);
            }
            else{
                bDelete.setDisable(true); 
            }
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
        }
    }
    
    /**
     * Handles the action event when the delete button is clicked.
     * Validates if the customer has associated accounts or is an administrator before deletion.
     * Asks for user confirmation before removing the customer from the database and the table.
     *
     * @param event The event that triggered this action.
     */
    private void handleBtDeleteOnAction(Event event){
        try{
            Customer customer = (Customer) tbCustomers.getSelectionModel().getSelectedItem();
            AccountRESTClient accountClient = new AccountRESTClient();
            
            List<Account> account = accountClient.findAccountsByCustomerId_XML(
                    new GenericType<List<Account>>(){
                    },
                    customer.getId().toString()
                    
            );

            if(account != null &&  !account.isEmpty()){
                throw new InternalServerErrorException("The user has associated accounts");
            }
            if (customer.getEmail().equals("admin@admin.com")){
                throw new Exception("Selected user is an administrator");
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this Customer?",ButtonType.YES,ButtonType.NO);
            alert.setTitle("Deletion prompt");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) { 
                clientManager.remove(customer.getId().toString());
                tbCustomers.getItems().remove(customer);
                bDelete.setDisable(true);
            }
            event.consume();
            
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
            new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
        }
        
    }
    /**
     * Handles the action event when the refresh button is clicked.
     * Reloads the customer data from the server into the table.
     *
     * @param event The event that triggered this action.
     */
    private void handleBtRefreshOnAction(Event event){
        try{
            reloadTable();
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
            new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
        }
    }
    /**
     * Handles the action event when the add button is clicked.
     * Creates a new empty customer in the database, adds it to the table, and focuses on the new row.
     *
     * @param event The event that triggered this action.
     */
    private void handleBtAddOnAction(Event event){
        try{
            Customer customer = new Customer();
            clientManager.create_XML(customer);
            tbCustomers.getItems().add(0, customer);
            reloadTable();
            
            ObservableList<Customer> items = tbCustomers.getItems();
            
            for (Customer c : items) {
                    if (c.getFirstName() == null || c.getFirstName().trim().isEmpty()) {
                    tbCustomers.getSelectionModel().select(c);
                    tbCustomers.scrollTo(c);
                    tbCustomers.requestFocus();
                    break; 
                }
            }


            
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
            new Alert(Alert.AlertType.INFORMATION,e.getMessage()).showAndWait();
        }
    }
    /**
     * Fetches all customers from the server using the REST client and updates the TableView items.
     */
    private void reloadTable(){
        
        GenericType<List<Customer>> customers = new GenericType<List<Customer>>() {};
        
        List<Customer> customerList = clientManager.findAll_XML(customers);

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList(customerList);
        
        tbCustomers.setItems(allCustomers);
    }
        
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import proyectoCRUD.SignInApplication;
import proyectoCRUD.model.Customer;

/**
 *
 * @author david
 * @fixme Añadir un método de test que compruebe que los datos que presenta la tabla son objetos Customer.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrudCustomerControllerTest extends ApplicationTest{
    private TableView<Customer> table;
    private Button bDelete;
    private Button bAdd;
    private Button bExit;
    String correo = "ejemplo@"+System.currentTimeMillis()+".com";
    
    
    @Override
    public void start(Stage stage) throws Exception {
        //Method to start the application
        //new AppCRUD().start(stage);
        new SignInApplication().start(stage);

    }
    
    @Before
    public void test1_init_window() {
        clickOn("#tfEmail");
        write("admin@admin.com");
        clickOn("#pfPassword");
        write("admin");
        clickOn("#btnLogin");      
        
        table = lookup("#tbCustomers").queryTableView();

        
    }

    @Test
    public void test2_create_customer_success() {
        int rowsCount = table.getItems().size();
        verifyThat("#bAdd", isEnabled());
        clickOn("#bAdd");
        assertEquals("The row has not been added!!!", rowsCount + 1, table.getItems().size());
        //FIXME El assert anterior es insuficiente. Añadir uno que compruebe que el nuevo Customer 
        //FIXME con los datos iniciales está entre los items de la tabla.
        Customer customer = (Customer) table.getSelectionModel().getSelectedItem();
        
    }
    
    @Test
    public void test3_modify_customer_info() {
        
        int datos = 0;

        int selectedRowIndex = table.getItems().size() / 2;


        interact(() -> table.scrollTo(selectedRowIndex));
        


        List<String> nuevosDatos = Arrays.asList(
            "Ejemplo", "T", "Ejemplo", "calleEjemplo", "ciudadEjemplo", 
            "estadoEjemplo", "63491", correo, "644119353", "Abcd*1234"
        );
        

        for (int colIndex = 1; colIndex < nuevosDatos.size()+1; colIndex++) {


            Node cell = getCell(selectedRowIndex, colIndex);

            doubleClickOn(cell);
            push(KeyCode.SHORTCUT, KeyCode.A); 
            push(KeyCode.BACK_SPACE);          
            write(nuevosDatos.get(datos));
            push(KeyCode.ENTER);
            datos++;
        }

        Customer customerActualizado = table.getItems().get(selectedRowIndex);
        
        assertEquals("El nombre no se actualizó", "Ejemplo", customerActualizado.getFirstName());
        assertEquals("El apellido no se actualizó", "Ejemplo", customerActualizado.getLastName());
        assertEquals("La inicial no se actualizó", "T", customerActualizado.getMiddleInitial());
        assertEquals("La ciudad no se actualizó", "ciudadEjemplo", customerActualizado.getCity());
        assertEquals("La calle no se actualizó", "calleEjemplo", customerActualizado.getStreet());
        assertEquals("El estado no se actualizó", "estadoEjemplo", customerActualizado.getState());
        assertEquals("El zip no se actualizó", "63491", customerActualizado.getZip().toString());
        assertEquals("El email no se actualizó", correo, customerActualizado.getEmail());
        assertEquals("El telefono no se actualizó", "644119353", customerActualizado.getPhone().toString());
        assertEquals("La contraseña no se actualizó", "Abcd*1234", customerActualizado.getPassword());

}


    @Test
    public void test4_Delete_Customer() {
        int rowsCount = table.getItems().size();
        int selectedRowIndex = table.getItems().size() -1;
        interact(() -> table.scrollTo(selectedRowIndex));
        Node cell = getCell(selectedRowIndex, 1);
        clickOn(cell);
        clickOn("#bDelete");
        verifyThat("Are you sure you want to delete this Customer?", isVisible());
        clickOn("Sí"); 
        assertEquals("The row has not been deleted", rowsCount - 1, table.getItems().size());
        //FIXME El assert anterior es insuficiente. Añadir uno que compruebe que el Customer seleccionado 
        //FIXME para borrar NO está entre los items de la tabla.
        
    }

    
    @Test
    public void test5_Delete_Customer_w_Acc(){
        // 1. Buscar el índice del primer cliente que se pueda borrar que tenga cuentas para que salte el mensaje de fallo
        int rowIndex = 1;
        for (int i = 0; i < table.getItems().size(); i++) {
            Customer customer = table.getItems().get(i);
            if ((customer.getAccounts() != null && !customer.getAccounts().isEmpty())) {
                rowIndex = i;
                break;
            }
        }
        verifyThat(bDelete, isDisabled());
        int rowsCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test", 0, rowsCount);
        Node row = lookup(".table-row-cell").nth(rowIndex).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat(bDelete, isEnabled());
        clickOn(bDelete);
        verifyThat("The user has associated accounts", isVisible());

    }




    private Node getCell(int rowIndex, int colIndex) {

        Node row = lookup(".table-row-cell").nth(rowIndex).query();

        return from(row).lookup(".table-cell").nth(colIndex).query();
    }
    
    
}

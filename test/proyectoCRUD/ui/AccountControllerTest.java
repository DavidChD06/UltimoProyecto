/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;
import proyectoCRUD.SignInApplication;
import proyectoCRUD.model.Account;

/**
 *
 * @author felipe
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create,Update y Delete sobre la
 * tabla de Cuentas que verifiquen sobre los items de la tabla cada caso de uso.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest extends ApplicationTest {

    private TableView tbvAccounts;
    private TableColumn tcDescription;

    @Override
    public void start(Stage stage) throws Exception {
        new SignInApplication().start(stage);
    }

    @Before
    public void test_SignInCorrect() {
        clickOn("#tfEmail");
        write("jsmith@enterprise.net");
        clickOn("#pfPassword");
        write("abcd*1234");
        verifyThat("#btnLogin", isEnabled());
        clickOn("#btnLogin");
        verifyThat("¡Welcome John!", isVisible());
        clickOn("Aceptar");

    }

    @Ignore
    @Test
    public void test1_Read() {
        TableView<Account> tbvAccounts = lookup("#tbvAccounts").queryTableView();

        ObservableList<Account> accounts = tbvAccounts.getItems();
        assertTrue("Some data in the table is not a account", accounts.stream().allMatch(a -> a instanceof Account));
    }

    @Ignore
    @Test
    public void test2_Create() {

        TableView<Account> tbvAccounts = lookup("#tbvAccounts").queryTableView();
        int rowCount = tbvAccounts.getItems().size();

        String description = "Test Accounts";
        //Cuento primera las cuentas que habian
        long cuentasIniciales = tbvAccounts.getItems().stream()
            .filter(a -> a.getDescription().equals(description)).count();
        clickOn("#btnAdd");
        type(KeyCode.END);
        Node celdaDescripcion = lookup(".table-row-cell:selected .table-cell").nth(1).query();
        doubleClickOn(celdaDescripcion);
        write(description);
        push(KeyCode.ENTER);
        Node celdaBeginBalance = lookup(".table-row-cell:selected .table-cell").nth(3).query();
        doubleClickOn(celdaBeginBalance);
        write("2000");
        push(KeyCode.ENTER);
        Node celdaType = lookup(".table-row-cell:selected .table-cell").nth(2).query();
        doubleClickOn(celdaType);
        type(KeyCode.DOWN).type(KeyCode.ENTER);
        Node celdaCreditLine = lookup(".table-row-cell:selected .table-cell").nth(5).query();
        doubleClickOn(celdaCreditLine);
        write("1000").push(KeyCode.ENTER);
        clickOn("#btnAdd");
        assertEquals("The row has not been added!!!", rowCount + 1, tbvAccounts.getItems().size());
        //busca la cuenta en el modelo de datos de la tabla
        List<Account> accounts = tbvAccounts.getItems();
        assertEquals("The account has not been added!!!",
                cuentasIniciales + 1,
                accounts.stream().filter(a -> a.getDescription().equals(description)).count());

    }

    @Ignore
    @Test
    public void test3_Update() {

        TableView<Account> tbvAccounts = lookup("#tbvAccounts").queryTableView();
        int rowCount = tbvAccounts.getItems().size();

        //Recojo una cuenta que tiene credito para poder modificar el creditLine
        List<Account> accounts = tbvAccounts.getItems();
        int rowIndex = -1;
        int i = 0;
        for (Account account : accounts) {

            if (account.getType().toString().equalsIgnoreCase("CREDIT")) {
                rowIndex = i;
                break;
            }
            i++;
        }
        assertNotEquals("No existen cuentas con credito.", -1, rowIndex);

        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        Node row = lookup(".table-row-cell").nth(rowIndex).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        Node celdaDescripcion = from(row).lookup(".table-row-cell:selected .table-cell").nth(1).query();
        clickOn(celdaDescripcion);
        write("Modification Description");
        push(KeyCode.ENTER);
        Node celdaCreditLine = from(row).lookup(".table-row-cell:selected .table-cell").nth(5).query();
        doubleClickOn(celdaCreditLine);
        write("1500").push(KeyCode.ENTER);
        assertEquals("The row count should not change on update!", rowCount, tbvAccounts.getItems().size());
        //Utilizo get(rowIndex) para coger la fila modificada
        Account account = tbvAccounts.getItems().get(rowIndex);

        assertEquals("The description has not been updated!!!",
                "Modification Description",
                account.getDescription());

    }

    @Ignore
    @Test
    public void test4_Delete() {

        TableView<Account> tbvAccounts = lookup("#tbvAccounts").queryTableView();
        int rowCount = tbvAccounts.getItems().size();

        List<Account> accounts = tbvAccounts.getItems();
        int rowIndex = -1;
        int i = 0;
        for (Account account : accounts) {
            if (account.getMovements() == null || account.getMovements().isEmpty()) {
                rowIndex = i;
                break;
            }
            i++;
        }
        assertNotEquals("No hay cuentas sin movimientos", -1, rowIndex);
        //Busca la primera linea de la tabla y haz click en ella
        Node row = lookup(".table-row-cell").nth(rowIndex).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");
        verifyThat("Are you sure you want to delete this account?",
                isVisible());
        clickOn("Aceptar");
        assertEquals("The row has not been deleted!!!",
                rowCount - 1, tbvAccounts.getItems().size());
    }

    @Ignore
    @Test
    public void test5_NoDelete() {

        TableView<Account> tbvAccounts = lookup("#tbvAccounts").queryTableView();
        int rowCount = tbvAccounts.getItems().size();

        //obligo que el robot coja una cuenta que tiene movimientos para poder comprobar que no se puede borrar
        List<Account> accounts = tbvAccounts.getItems();
        int rowIndex = -1;
        int i = 0;

        for (Account account : accounts) {
            if (account.getMovements() != null && !account.getMovements().isEmpty()) {
                rowIndex = i;
                break;
            }
            i++;
        }
        Node row = lookup(".table-row-cell").nth(rowIndex).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");
        verifyThat("You cannot delete the account\nbecause it still has movements",
                isVisible());
        clickOn("Aceptar");
        assertEquals("The row HAS been deleted and it shouldn't!!!",
                rowCount, tbvAccounts.getItems().size());
    }

    @Ignore
    @Test
    public void test6_AccountCancel() {
        clickOn("#btnAdd");
        clickOn("#btnDelete");
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import proyectoCRUD.SignInApplication;

/**
 *
 * @author felipe
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create,Update y Delete sobre la tabla de Cuentas que verifiquen sobre los items de la tabla cada caso de uso.

 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new SignInApplication().start(stage);
    }

    @Before
    public void test1_SignInCorrect() {
        clickOn("#tfEmail");
        write("jsmith@enterprise.net");
        clickOn("#pfPassword");
        write("abcd*1234");
        verifyThat("#btnLogin", isEnabled());
        clickOn("#btnLogin");
        verifyThat("¡Welcome John!", isVisible());
        clickOn("Aceptar");
        
    }

   @Test
    public void test2_AddAccount() {
        clickOn("#btnAdd");
        Node celdaDescripcion = lookup(".table-row-cell:selected .table-cell").nth(1).query();
        doubleClickOn(celdaDescripcion);
        write("New Account");
        push(KeyCode.ENTER);
        Node celdaBeginBalance = lookup(".table-row-cell:selected .table-cell").nth(3).query();
        doubleClickOn(celdaBeginBalance);
        write("3000");
        push(KeyCode.ENTER);
        clickOn("#btnAdd");
    }

    @Test
    public void test3_AccountCancel(){
        clickOn("#btnAdd");
        clickOn("#btnDelete");
    }

    @Test
    public void test4_TableEdit() {
        clickOn(".table-row-cell");
        Node celdaDescripcion = lookup(".table-row-cell:selected .table-cell").nth(1).query();
        doubleClickOn(celdaDescripcion);
        write("Modification Description");
        push(KeyCode.ENTER);
        
    }
   @Test
    public void test5_Delete() {
        clickOn("New Modification Description");
        clickOn("#btnDelete");
        verifyThat("Are you sure you want to delete this account?", isVisible());
        clickOn("Aceptar");
    } 

}

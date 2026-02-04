/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import proyectoCRUD.SignInApplication;

/**
 *
 * @author felipe
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
        write("david23@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        verifyThat("#btnLogin", isEnabled());
        clickOn("#btnLogin");
        verifyThat("¡Welcome David!", isVisible());
        clickOn("Aceptar");
    }

    @Test
    public void test2_AddAccount() {
        clickOn("#btnAdd");
        Node celdaDescripcion = lookup(".table-row-cell:selected .table-cell").nth(1).query();
        doubleClickOn(celdaDescripcion);
        write("New Account Test");
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
    public void test4_Movement() {
        clickOn("New Account");
        clickOn("#btnMovement");
        
    }
    @Test
    public void test5_Delete() {
        clickOn("New Account");
        clickOn("#btnDelete");
        verifyThat("Are you sure you want to delete this account?", isVisible());
        clickOn("#Aceptar");
    }

    @Test
    public void test6_Exit() {
        clickOn("#btnExit");
        verifyThat("Are you sure you want to go out?", isVisible());
        clickOn("#Aceptar");
    }
    @After
    public void test7_CloseWindow() throws Exception{
        FxToolkit.cleanupStages();
    }
    

}

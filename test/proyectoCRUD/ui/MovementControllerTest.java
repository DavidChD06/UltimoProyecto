/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import proyectoCRUD.SignInApplication;

/**
 *
 * @author miguel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovementControllerTest extends ApplicationTest {
    
    @Override
    public void start(Stage stage) throws Exception {
        new SignInApplication().start(stage);
    }

    @Before
    public void test1_SignIn() {
        
        clickOn("#tfEmail").write("jsmith@enterprise.net");
        clickOn("#pfPassword").write("Asd4Asd");
        clickOn("#btnLogin");
        
        clickOn("Aceptar");
        
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        clickOn("#btnMovement");

        verifyThat("#movementViewPane", isVisible());
        
    }
    
    //@Test
    @Ignore
    public void test2_UndoMovement() {
       // verifyThat("#movementViewPane", isVisible());
        verifyThat("#btUndo", isVisible());
        clickOn("#btUndo");
        verifyThat("#btUndo", isDisabled());
        //verifyThat("")
    }
    @Test
    //@Ignore
    public void test4_NewMovement() {
        clickOn("#tfAmount");
        write("200");
        clickOn("#selectType");
        clickOn("Deposit");
        
        clickOn("#btNewMovement");
        verifyThat("#btUndo", isEnabled());
    }
    //@Test
    //@Ignore
    public void test5_UndoAfterNewMovement() {
        clickOn("#btUndo");
        verifyThat("#btUndo", isDisabled());
    }
    /*@Test
    //@Ignore
    public void test9_ExitMovement() {
        clickOn("#btCancel");
    }
    
    
    public MovementControllerTest() {
        
    }*/
}

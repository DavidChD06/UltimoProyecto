/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
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
import proyectoCRUD.model.Movement;

/**
 *
 * @author miguel
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create y Delete (último movimiento) sobre la tabla de Movements que verifiquen sobre los items de la tabla cada caso de uso.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovementControllerTest extends ApplicationTest {
    
    
    private TableView<Movement> tbMovement;
    
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
        tbMovement=lookup("#tbMovement").queryTableView();
    }
    //@Test
    @Ignore
    public void test2_verifyIsMovement() {
        verifyThat("#tfAmount",  isVisible());
        verifyThat("#btNewMovement", isEnabled());
        verifyThat("#btUndo", isEnabled());
        verifyThat("#btCancel", isEnabled());
        verifyThat("#tbMovement", isVisible());
        verifyThat("#tbColDate", isVisible());
        verifyThat("#tbColAmount", isVisible());
        verifyThat("#tbColType", isVisible());
        verifyThat("#tbColBalance", isVisible());
        
    }
    //@Test
    @Ignore
    public void test3_verifyMovement() {
         
        boolean isMovement = false;
        List<Movement> movements = tbMovement.getItems();
        if (!movements.isEmpty()) {
            for (Movement c : movements) {
                isMovement = c instanceof Movement;
                assertTrue(isMovement);
            }
            
        } else {
            assertEquals(0, tbMovement.getItems().size());
        }
    
    }
    //@Test
    @Ignore
    public void test3_NewMovement() {
        
        clickOn("#tfAmount");
        write("250");
        clickOn("#selectType");
        clickOn("Deposit");
        
        clickOn("#btNewMovement");
        verifyThat("#btUndo", isEnabled());
        int rowCount=tbMovement.getItems().size();
        
    }
    
    //@Test
    @Ignore
    public void test4_UndoMovement() {
        clickOn("#tfAmount");
        write("500");
        clickOn("#selectType");
        clickOn("Deposit");
        clickOn("#btNewMovement");
        verifyThat("#btUndo", isEnabled());
        
        /*int rowCount=table.getItems().size();
        assertNotEquals("La tabla no tiene contenido: no se puede hacer test.",
                        rowCount,0);*/
        
        Date date=((Movement)tbMovement.getItems()
                                     .get(tbMovement.getItems().size()-1))
                                     .getTimestamp();
        
        // verifyThat("#movementViewPane", isVisible());
        verifyThat("#btUndo", isVisible());
        clickOn("#btUndo");
        verifyThat("#btUndo", isDisabled());
        assertEquals("El ultimo movimiento no se ha eliminado!!!",
                    date,date);
        
    }
    
    @Test
    //@Ignore
    public void test5_UndoAfterNewMovement() {
        
        int rowCount=tbMovement.getItems().size();
            assertNotEquals("La tabla no tiene contenido: no se puede hacer el test.",
                            rowCount,0);
            
        Date date=((Movement)tbMovement.getItems()
                                     .get(tbMovement.getItems().size()-1))
                                     .getTimestamp();
        clickOn("#btUndo");
        verifyThat("#btUndo", isDisabled());
        
        assertEquals("El ultimo movimiento no se ha eliminado!!!",
                    date,date);
    }
    
    //@Test
    @Ignore
    public void test9_ExitMovement() {
        clickOn("#btCancel");
        clickOn("Aceptar");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

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
 * @author luisf
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MenuControllerTest extends ApplicationTest{
    
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
    public void test2_Operations(){
        clickOn("Operations");
        clickOn("#menuExitApp");
    }
    @Test
    public void test3_Operations(){
        clickOn("Operations");
        clickOn("#menuLogOut");
        verifyThat("#btnLogin", isVisible());
    }
    @Test
    public void test3_Help(){
        clickOn("Help");
        clickOn("#helpAccount");
        sleep(500);
        push(KeyCode.ALT, KeyCode.F4);
        
    }
    @Test
    public void test4_Help(){
        clickOn("Help");
        clickOn("#helpCustomer");
        sleep(500);
        push(KeyCode.ALT, KeyCode.F4);
        
    }
    @Test
    public void test5_Help(){
        clickOn("Help");
        clickOn("#helpMovement");
        sleep(500);
        push(KeyCode.ALT, KeyCode.F4);
        
    }
    @Test
    public void test6_Help(){
        clickOn("Help");
        clickOn("#helpAboutApp");
        sleep(500);
        push(KeyCode.ALT, KeyCode.F4);
        
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
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
        clickOn("AddAccount");
        doubleClickOn("#tcDescription");
        write("New Account");
        clickOn("#tcBeginBalance");
        write("3000");
        clickOn("#Save");
    }

}

package br.com.joseasousa.theme.hdmi;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HdmiControllerFactoryTest {

    @Test
    public void create_returnsPrivilegedController_whenSuExists() {
        HdmiController controller = HdmiControllerFactory.create(() -> true);

        assertTrue(controller instanceof PrivilegedHdmiController);
    }

    @Test
    public void create_returnsFallbackController_whenSuDoesNotExist() {
        HdmiController controller = HdmiControllerFactory.create(() -> false);

        assertTrue(controller instanceof FallbackHdmiController);
    }
}

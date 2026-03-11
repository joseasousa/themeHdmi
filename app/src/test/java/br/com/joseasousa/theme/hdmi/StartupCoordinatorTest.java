package br.com.joseasousa.theme.hdmi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StartupCoordinatorTest {

    @Test
    public void isSetupRequired_isTrue_whenNoSavedPort() {
        FakePreferences preferences = new FakePreferences(false, HdmiPort.HDMI1);
        StartupCoordinator coordinator = new StartupCoordinator(preferences, new SuccessController());

        assertTrue(coordinator.isSetupRequired());
    }

    @Test
    public void savePreferredPort_updatesPreferences() {
        FakePreferences preferences = new FakePreferences(false, HdmiPort.HDMI1);
        StartupCoordinator coordinator = new StartupCoordinator(preferences, new SuccessController());

        coordinator.savePreferredPort(HdmiPort.HDMI3);

        assertFalse(coordinator.isSetupRequired());
        assertEquals(HdmiPort.HDMI3, coordinator.getPreferredPort());
    }

    @Test
    public void attemptSwitch_usesSavedPort() {
        FakePreferences preferences = new FakePreferences(true, HdmiPort.HDMI2);
        CapturingController controller = new CapturingController();
        StartupCoordinator coordinator = new StartupCoordinator(preferences, controller);

        HdmiSwitchResult result = coordinator.attemptSwitch();

        assertTrue(result.isSuccess());
        assertEquals(HdmiPort.HDMI2, controller.lastPort);
    }

    @Test
    public void attemptSwitch_propagatesFailureReason() {
        FakePreferences preferences = new FakePreferences(true, HdmiPort.HDMI1);
        StartupCoordinator coordinator = new StartupCoordinator(
                preferences,
                port -> HdmiSwitchResult.failure("No privileged access")
        );

        HdmiSwitchResult result = coordinator.attemptSwitch();

        assertFalse(result.isSuccess());
        assertEquals("No privileged access", result.getReason());
    }

    private static final class FakePreferences implements StartupPreferences {
        private boolean hasPort;
        private HdmiPort port;

        private FakePreferences(boolean hasPort, HdmiPort port) {
            this.hasPort = hasPort;
            this.port = port;
        }

        @Override
        public boolean hasSelectedPort() {
            return hasPort;
        }

        @Override
        public HdmiPort getSelectedPort() {
            return port;
        }

        @Override
        public void setSelectedPort(HdmiPort port) {
            this.hasPort = true;
            this.port = port;
        }
    }

    private static final class SuccessController implements HdmiController {
        @Override
        public HdmiSwitchResult trySwitchToHdmi(HdmiPort port) {
            return HdmiSwitchResult.success();
        }
    }

    private static final class CapturingController implements HdmiController {
        private HdmiPort lastPort;

        @Override
        public HdmiSwitchResult trySwitchToHdmi(HdmiPort port) {
            this.lastPort = port;
            return HdmiSwitchResult.success();
        }
    }
}

package br.com.joseasousa.theme.hdmi;

public final class StartupCoordinator {
    private final StartupPreferences preferences;
    private final HdmiController hdmiController;

    public StartupCoordinator(StartupPreferences preferences, HdmiController hdmiController) {
        this.preferences = preferences;
        this.hdmiController = hdmiController;
    }

    public boolean isSetupRequired() {
        return !preferences.hasSelectedPort();
    }

    public void savePreferredPort(HdmiPort port) {
        preferences.setSelectedPort(port);
    }

    public HdmiPort getPreferredPort() {
        return preferences.getSelectedPort();
    }

    public HdmiSwitchResult attemptSwitch() {
        HdmiPort port = preferences.getSelectedPort();
        return hdmiController.trySwitchToHdmi(port);
    }
}

package br.com.joseasousa.theme.hdmi;

public interface StartupPreferences {
    boolean hasSelectedPort();

    HdmiPort getSelectedPort();

    void setSelectedPort(HdmiPort port);
}

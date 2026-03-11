package br.com.joseasousa.theme.hdmi;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesStartupPreferences implements StartupPreferences {
    private static final String FILE_NAME = "startup_prefs";
    private static final String KEY_HDMI_PORT = "hdmi_port";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesStartupPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean hasSelectedPort() {
        return sharedPreferences.contains(KEY_HDMI_PORT);
    }

    @Override
    public HdmiPort getSelectedPort() {
        String value = sharedPreferences.getString(KEY_HDMI_PORT, HdmiPort.HDMI1.name());
        try {
            return HdmiPort.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return HdmiPort.HDMI1;
        }
    }

    @Override
    public void setSelectedPort(HdmiPort port) {
        sharedPreferences.edit().putString(KEY_HDMI_PORT, port.name()).apply();
    }
}

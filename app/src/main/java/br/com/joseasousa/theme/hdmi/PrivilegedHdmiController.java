package br.com.joseasousa.theme.hdmi;

import android.util.Log;

import java.io.IOException;

public final class PrivilegedHdmiController implements HdmiController {
    private static final String TAG = "PrivilegedHdmiCtrl";

    @Override
    public HdmiSwitchResult trySwitchToHdmi(HdmiPort port) {
        String command = buildCommand(port);
        Process process = null;
        try {
            process = new ProcessBuilder("su", "-c", command).start();
            int code = process.waitFor();
            if (code == 0) {
                return HdmiSwitchResult.success();
            }
            return HdmiSwitchResult.failure("System command failed with exit code: " + code);
        } catch (IOException | InterruptedException e) {
            Log.w(TAG, "Could not execute HDMI switch command", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return HdmiSwitchResult.failure("System access is not available for HDMI switch.");
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    // Key event opens the input/source picker on many OEM TV firmwares.
    private String buildCommand(HdmiPort port) {
        return "input keyevent KEYCODE_TV_INPUT";
    }
}

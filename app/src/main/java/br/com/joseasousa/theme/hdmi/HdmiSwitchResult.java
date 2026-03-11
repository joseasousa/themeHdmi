package br.com.joseasousa.theme.hdmi;

public final class HdmiSwitchResult {
    private final boolean success;
    private final String reason;

    private HdmiSwitchResult(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public static HdmiSwitchResult success() {
        return new HdmiSwitchResult(true, null);
    }

    public static HdmiSwitchResult failure(String reason) {
        return new HdmiSwitchResult(false, reason);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}

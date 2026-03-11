package br.com.joseasousa.theme.hdmi;

public final class FallbackHdmiController implements HdmiController {
    private final String reason;

    public FallbackHdmiController(String reason) {
        this.reason = reason;
    }

    @Override
    public HdmiSwitchResult trySwitchToHdmi(HdmiPort port) {
        return HdmiSwitchResult.failure(reason);
    }
}

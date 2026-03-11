package br.com.joseasousa.theme.hdmi;

public final class FallbackHdmiController implements HdmiController {
    @Override
    public HdmiSwitchResult trySwitchToHdmi(HdmiPort port) {
        return HdmiSwitchResult.failure("Automatic HDMI switch requires privileged system access.");
    }
}

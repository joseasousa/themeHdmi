package br.com.joseasousa.theme.hdmi;

public final class HdmiControllerFactory {
    private HdmiControllerFactory() {
    }

    public static HdmiController create() {
        return create(new SystemRootChecker());
    }

    static HdmiController create(RootChecker rootChecker) {
        if (rootChecker.hasSuBinary()) {
            return new PrivilegedHdmiController();
        }
        return new FallbackHdmiController();
    }
}

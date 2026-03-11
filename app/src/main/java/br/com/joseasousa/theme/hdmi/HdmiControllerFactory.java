package br.com.joseasousa.theme.hdmi;

import android.os.Build;

public final class HdmiControllerFactory {
    private static final String REASON_EMULATOR_UNSUPPORTED =
            "Automatic HDMI switch is not supported on emulator: no real HDMI hardware and no privileged access.";
    private static final String REASON_PRIVILEGE_REQUIRED =
            "Automatic HDMI switch requires privileged system access (root/system app).";

    private HdmiControllerFactory() {
    }

    public static HdmiController create() {
        return create(new SystemRootChecker());
    }

    static HdmiController create(RootChecker rootChecker) {
        if (rootChecker.hasSuBinary()) {
            return new PrivilegedHdmiController();
        }
        if (isLikelyEmulator()) {
            return new FallbackHdmiController(REASON_EMULATOR_UNSUPPORTED);
        }
        return new FallbackHdmiController(REASON_PRIVILEGE_REQUIRED);
    }

    private static boolean isLikelyEmulator() {
        String fingerprint = Build.FINGERPRINT == null ? "" : Build.FINGERPRINT;
        String model = Build.MODEL == null ? "" : Build.MODEL;
        String product = Build.PRODUCT == null ? "" : Build.PRODUCT;
        String manufacturer = Build.MANUFACTURER == null ? "" : Build.MANUFACTURER;

        return fingerprint.contains("generic")
                || fingerprint.contains("emulator")
                || model.contains("Emulator")
                || model.contains("Android SDK")
                || manufacturer.contains("Genymotion")
                || product.contains("sdk")
                || product.contains("emulator")
                || product.contains("simulator");
    }
}

package br.com.joseasousa.theme.hdmi;

import android.content.Context;

import androidx.annotation.Nullable;

public final class HdmiControllerProvider {
    private static volatile HdmiController testController;

    private HdmiControllerProvider() {
    }

    public static HdmiController getController(Context context) {
        HdmiController local = testController;
        if (local != null) {
            return local;
        }
        return HdmiControllerFactory.create();
    }

    static void setTestController(@Nullable HdmiController controller) {
        testController = controller;
    }

    static void clearTestController() {
        testController = null;
    }
}

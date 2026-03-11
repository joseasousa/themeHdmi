package br.com.joseasousa.theme.hdmi;

import java.io.File;

public final class SystemRootChecker implements RootChecker {
    @Override
    public boolean hasSuBinary() {
        String[] paths = {"/system/bin/su", "/system/xbin/su", "/sbin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }
}

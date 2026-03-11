package br.com.joseasousa.theme.hdmi;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WriteSettingsPermissionManagerInstrumentedTest {

    @Test
    public void buildManageWriteSettingsIntent_usesAppPackageUri() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Intent intent = WriteSettingsPermissionManager.buildManageWriteSettingsIntent(context);

        assertEquals(Settings.ACTION_MANAGE_WRITE_SETTINGS, intent.getAction());
        assertEquals(Uri.parse("package:" + context.getPackageName()), intent.getData());
    }
}

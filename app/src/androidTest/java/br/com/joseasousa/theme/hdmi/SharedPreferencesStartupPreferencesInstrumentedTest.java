package br.com.joseasousa.theme.hdmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesStartupPreferencesInstrumentedTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("startup_prefs", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    @Test
    public void hasSelectedPort_isFalse_whenEmpty() {
        SharedPreferencesStartupPreferences preferences = new SharedPreferencesStartupPreferences(context);

        assertFalse(preferences.hasSelectedPort());
    }

    @Test
    public void setAndGetSelectedPort_persistsValue() {
        SharedPreferencesStartupPreferences preferences = new SharedPreferencesStartupPreferences(context);

        preferences.setSelectedPort(HdmiPort.HDMI2);

        assertTrue(preferences.hasSelectedPort());
        assertEquals(HdmiPort.HDMI2, preferences.getSelectedPort());
    }

    @Test
    public void getSelectedPort_returnsDefault_whenStoredValueIsInvalid() {
        SharedPreferences preferences = context.getSharedPreferences("startup_prefs", Context.MODE_PRIVATE);
        preferences.edit().putString("hdmi_port", "INVALID").commit();

        SharedPreferencesStartupPreferences startupPreferences = new SharedPreferencesStartupPreferences(context);

        assertEquals(HdmiPort.HDMI1, startupPreferences.getSelectedPort());
    }
}

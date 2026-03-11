package br.com.joseasousa.theme.hdmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BootReceiverInstrumentedTest {

    @Test
    public void onReceive_startsLauncher_onBootCompleted() {
        Context baseContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CapturingContext context = new CapturingContext(baseContext);
        BootReceiver receiver = new BootReceiver();

        receiver.onReceive(context, new Intent(Intent.ACTION_BOOT_COMPLETED));

        ComponentName component = context.getLastStartedIntent().getComponent();
        assertEquals(LauncherActivity.class.getName(), component.getClassName());
    }

    @Test
    public void onReceive_doesNothing_forIrrelevantAction() {
        Context baseContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CapturingContext context = new CapturingContext(baseContext);
        BootReceiver receiver = new BootReceiver();

        receiver.onReceive(context, new Intent(Intent.ACTION_TIME_CHANGED));

        assertNull(context.getLastStartedIntent());
    }

    private static final class CapturingContext extends ContextWrapper {
        private Intent lastStartedIntent;

        private CapturingContext(Context base) {
            super(base);
        }

        @Override
        public void startActivity(Intent intent) {
            this.lastStartedIntent = intent;
        }

        private Intent getLastStartedIntent() {
            return lastStartedIntent;
        }
    }
}

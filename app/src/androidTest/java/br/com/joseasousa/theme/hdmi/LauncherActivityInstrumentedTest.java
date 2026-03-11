package br.com.joseasousa.theme.hdmi;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LauncherActivityInstrumentedTest {

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.getSharedPreferences("startup_prefs", Context.MODE_PRIVATE).edit().clear().commit();
        HdmiControllerProvider.clearTestController();
    }

    @After
    public void tearDown() {
        HdmiControllerProvider.clearTestController();
    }

    @Test
    public void firstRun_showsSetupAndDisablesGoButton() {
        try (ActivityScenario<LauncherActivity> ignored = ActivityScenario.launch(LauncherActivity.class)) {
            onView(withId(R.id.setupContainer)).check(matches(isDisplayed()));
            onView(withId(R.id.buttonGoHdmi)).check(matches(org.hamcrest.Matchers.not(isEnabled())));
        }
    }

    @Test
    public void savePort_hidesSetupAndEnablesGoButton() {
        try (ActivityScenario<LauncherActivity> ignored = ActivityScenario.launch(LauncherActivity.class)) {
            onView(withId(R.id.radioHdmi2)).perform(click());
            onView(withId(R.id.buttonSavePort)).perform(click());

            onView(withId(R.id.setupContainer)).check(matches(withEffectiveVisibility(androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.buttonGoHdmi)).check(matches(isEnabled()));
            onView(withId(R.id.textStatus)).check(matches(withText(containsString("HDMI2"))));
        }
    }

    @Test
    public void switchFailure_showsGuidanceAndRetry() {
        HdmiControllerProvider.setTestController(port -> HdmiSwitchResult.failure("No system permission"));

        try (ActivityScenario<LauncherActivity> ignored = ActivityScenario.launch(LauncherActivity.class)) {
            onView(withId(R.id.radioHdmi1)).perform(click());
            onView(withId(R.id.buttonSavePort)).perform(click());
            onView(withId(R.id.buttonGoHdmi)).perform(click());

            onView(withId(R.id.textStatus)).check(matches(withText(containsString("No system permission"))));
            onView(withId(R.id.textGuidance)).check(matches(isDisplayed()));
            onView(withId(R.id.buttonRetry)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void switchSuccess_hidesGuidanceAndRetry() {
        HdmiControllerProvider.setTestController(port -> HdmiSwitchResult.success());

        try (ActivityScenario<LauncherActivity> ignored = ActivityScenario.launch(LauncherActivity.class)) {
            onView(withId(R.id.radioHdmi3)).perform(click());
            onView(withId(R.id.buttonSavePort)).perform(click());
            onView(withId(R.id.buttonGoHdmi)).perform(click());

            onView(withId(R.id.textStatus)).check(matches(withText("HDMI switch command sent successfully.")));
            onView(withId(R.id.textGuidance)).check(matches(withEffectiveVisibility(androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.buttonRetry)).check(matches(withEffectiveVisibility(androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE)));
        }
    }
}

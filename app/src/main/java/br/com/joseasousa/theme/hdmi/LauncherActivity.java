package br.com.joseasousa.theme.hdmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public final class LauncherActivity extends AppCompatActivity {
    private StartupCoordinator startupCoordinator;

    private View setupContainer;
    private RadioGroup radioGroupPorts;
    private Button buttonSavePort;
    private Button buttonGoHdmi;
    private Button buttonRetry;
    private TextView textStatus;
    private TextView textGuidance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        startupCoordinator = new StartupCoordinator(
                new SharedPreferencesStartupPreferences(this),
                HdmiControllerProvider.getController(this)
        );

        bindViews();
        initActions();
        renderInitialState();
    }

    private void bindViews() {
        setupContainer = findViewById(R.id.setupContainer);
        radioGroupPorts = findViewById(R.id.radioGroupPorts);
        buttonSavePort = findViewById(R.id.buttonSavePort);
        buttonGoHdmi = findViewById(R.id.buttonGoHdmi);
        buttonRetry = findViewById(R.id.buttonRetry);
        textStatus = findViewById(R.id.textStatus);
        textGuidance = findViewById(R.id.textGuidance);
    }

    private void initActions() {
        buttonSavePort.setOnClickListener(v -> {
            HdmiPort selectedPort = getSelectedPortFromUi();
            if (selectedPort == null) {
                Toast.makeText(this, R.string.select_port_validation, Toast.LENGTH_SHORT).show();
                return;
            }
            startupCoordinator.savePreferredPort(selectedPort);
            setupContainer.setVisibility(View.GONE);
            buttonGoHdmi.setEnabled(true);
            textStatus.setText(getString(R.string.selected_port_status, selectedPort.name()));
        });

        View.OnClickListener switchListener = v -> performHdmiSwitch();
        buttonGoHdmi.setOnClickListener(switchListener);
        buttonRetry.setOnClickListener(switchListener);
    }

    private void renderInitialState() {
        if (startupCoordinator.isSetupRequired()) {
            setupContainer.setVisibility(View.VISIBLE);
            buttonGoHdmi.setEnabled(false);
            textStatus.setText(R.string.setup_required_status);
        } else {
            setupContainer.setVisibility(View.GONE);
            buttonGoHdmi.setEnabled(true);
            textStatus.setText(getString(R.string.selected_port_status, startupCoordinator.getPreferredPort().name()));
        }
        textGuidance.setVisibility(View.GONE);
        buttonRetry.setVisibility(View.GONE);
    }

    private void performHdmiSwitch() {
        HdmiSwitchResult result = startupCoordinator.attemptSwitch();
        if (result.isSuccess()) {
            textStatus.setText(R.string.switch_success);
            textGuidance.setVisibility(View.GONE);
            buttonRetry.setVisibility(View.GONE);
            return;
        }

        textStatus.setText(getString(R.string.switch_failure, result.getReason()));
        textGuidance.setVisibility(View.VISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
    }

    @Nullable
    private HdmiPort getSelectedPortFromUi() {
        int checkedId = radioGroupPorts.getCheckedRadioButtonId();
        if (checkedId == View.NO_ID) {
            return null;
        }
        RadioButton checkedButton = findViewById(checkedId);
        Object tag = checkedButton.getTag();
        if (tag == null) {
            return null;
        }
        try {
            return HdmiPort.valueOf(tag.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

package com.example.esp.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.esp.CurrentUser;
import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.DeviceService;
import com.example.esp.api.param.QueryInfraTypeAbilityParam;
import com.example.esp.api.result.QueryInfraTypeAbilityResult;
import com.example.esp.model.InfraAbility;
import com.example.esp.model.UnitedDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设备控制面板
 *
 * Created by Ryan Hu on 2020/3/28.
 */
public class DeviceControlFragment extends Fragment {

    private static final String ARG_DEVICE = "device";

    private enum Status {
        LOADING,
        COMPLETE,
        ERROR;
    }

    private UnitedDevice unitedDevice;
    private QueryInfraTypeAbilityResult deviceAbilities;
    private Status status;

    private LinearLayout containerView;
    private View loadingView;
    private View errorView;

    public static DeviceControlFragment create (@NonNull UnitedDevice unitedDevice) {
        DeviceControlFragment fragment = new DeviceControlFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEVICE, unitedDevice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            unitedDevice = (UnitedDevice)args.getSerializable(ARG_DEVICE);
            loadDeviceAbility();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_control, container, false);
        initViews(root);
        return root;
    }

    private void initViews(View root) {
        containerView = root.findViewById(R.id.container);
        loadingView = root.findViewById(R.id.loading);
        errorView = root.findViewById(R.id.error);
    }

    private void setStatus(Status status) {
        if (this.status != status) {
            this.status = status;
            updateStatus(status);
        }
    }

    private void updateStatus(Status status) {
        if (getView() == null) {
            return;
        }
        switch (status) {
            case LOADING:
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                containerView.setVisibility(View.GONE);
                break;
            case COMPLETE:
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                containerView.setVisibility(View.GONE);
                break;
        }
    }

    private void setAbilities(QueryInfraTypeAbilityResult deviceAbilities) {
        if (this.deviceAbilities != deviceAbilities) {
            this.deviceAbilities = deviceAbilities;
            updateAbilities(deviceAbilities);
        }
    }

    private void updateAbilities(QueryInfraTypeAbilityResult deviceAbilities) {
        if (getView() == null) {
            return;
        }
        containerView.removeAllViews();
        if (deviceAbilities.keyRowNum != null && deviceAbilities.keysSet.size() > 0) {
            InfraAbility[] abilities = new InfraAbility[deviceAbilities.keysSet.size()];
            deviceAbilities.keysSet.values().toArray(abilities);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            int count = 0;
            for (int i = 0; i < deviceAbilities.keyRowNum.size(); i++) {
                int col = deviceAbilities.keyRowNum.get(i);
                ViewGroup row = (ViewGroup)inflater.inflate(R.layout.item_row, containerView, false);
                for (int j = 0; j < col; j++) {
                    TextView key = (TextView)inflater.inflate(R.layout.item_key, row, false);
                    key.setText(abilities[count++].text);
                    row.addView(key);
                }
                containerView.addView(row);
            }
        }
    }

    /**
     * 查询当前设备能力
     */
    private void loadDeviceAbility() {
        QueryInfraTypeAbilityParam param = new QueryInfraTypeAbilityParam();
        param.sessionID = CurrentUser.sessionId;
        param.infraTypeID = unitedDevice.infraTypeID;
        Api.create(DeviceService.class).queryInfraTypeAbility(param).enqueue(new Callback<QueryInfraTypeAbilityResult>() {
            @Override
            public void onResponse(Call<QueryInfraTypeAbilityResult> call, Response<QueryInfraTypeAbilityResult> response) {
                if (response.body() != null && response.body().success()) {
                    setStatus(Status.COMPLETE);
                    setAbilities(response.body());
                } else {
                    setStatus(Status.ERROR);
                }
            }

            @Override
            public void onFailure(Call<QueryInfraTypeAbilityResult> call, Throwable t) {
                setStatus(Status.ERROR);
            }
        });
    }
}

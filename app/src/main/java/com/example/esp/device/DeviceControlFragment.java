package com.example.esp.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.esp.CurrentUser;
import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.DeviceService;
import com.example.esp.api.param.QueryInfraTypeAbilityParam;
import com.example.esp.api.result.DevicecontrolResult;
import com.example.esp.api.result.QueryInfraTypeAbilityResult;
import com.example.esp.model.InfraAbility;
import com.example.esp.model.UnitedDevice;
import com.example.esp.view.StateView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设备控制面板
 * 传入参数为{@link com.example.esp.model.Device},然后向服务端查询设备能力，并展示出对应的功能按钮
 *
 * Created by Ryan Hu on 2020/3/28.
 */
public class DeviceControlFragment extends Fragment {

    private static final String ARG_DEVICE = "device";
    private static final String ARG_TOKEN = "token";

    private String token;
    private UnitedDevice unitedDevice;
    private QueryInfraTypeAbilityResult deviceAbilities;

    private LinearLayout containerView;
    private StateView stateView;

    public static DeviceControlFragment create (@NonNull UnitedDevice unitedDevice, @NonNull String token) {
        DeviceControlFragment fragment = new DeviceControlFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEVICE, unitedDevice);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            unitedDevice = (UnitedDevice)args.getSerializable(ARG_DEVICE);
            token = args.getString(ARG_TOKEN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_control, container, false);
        initViews(root);
        loadDeviceAbility();
        return root;
    }

    private void initViews(View root) {
        containerView = root.findViewById(R.id.container);
        stateView = root.findViewById(R.id.state);
    }

    private void setAbilities(QueryInfraTypeAbilityResult deviceAbilities) {
        if (this.deviceAbilities != deviceAbilities) {
            this.deviceAbilities = deviceAbilities;
            updateAbilities(deviceAbilities);
        }
    }

    /**
     * 更新设备能力
     * @param deviceAbilities
     */
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
            //为每一个功能创建一个按钮
            for (int i = 0; i < deviceAbilities.keyRowNum.size(); i++) {
                int col = deviceAbilities.keyRowNum.get(i);
                ViewGroup row = (ViewGroup)inflater.inflate(R.layout.item_row, containerView, false);
                for (int j = 0; j < col; j++) {
                    TextView key = (TextView)inflater.inflate(R.layout.item_key, row, false);
                    InfraAbility ability = abilities[count++];
                    //按钮名称
                    key.setText(ability.text.get(0));
                    key.setTag(ability);
                    //按钮点击逻辑
                    key.setOnClickListener(this::onKeyClick);
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
        stateView.showLoading();
        QueryInfraTypeAbilityParam param = new QueryInfraTypeAbilityParam();
        param.sessionID = CurrentUser.sessionId;
        param.infraTypeID = unitedDevice.infraTypeID;
        Api.create(DeviceService.class).queryInfraTypeAbility(param).enqueue(new Callback<QueryInfraTypeAbilityResult>() {
            @Override
            public void onResponse(Call<QueryInfraTypeAbilityResult> call, Response<QueryInfraTypeAbilityResult> response) {
                if (response.body() != null && response.body().success()) {
                    //查询成功
                    stateView.hide();
                    setAbilities(response.body());
                } else {
                    //查询失败
                    stateView.showError("重试", () -> loadDeviceAbility());
                }
            }

            @Override
            public void onFailure(Call<QueryInfraTypeAbilityResult> call, Throwable t) {
                //查询失败
                stateView.showError("重试", () -> loadDeviceAbility());
            }
        });
    }

    private void onKeyClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof InfraAbility) {
            sendKeyInstruction((InfraAbility)tag);
        }
    }

    /**
     * 向服务器发送设备指令
     */
    private void sendKeyInstruction(InfraAbility ability) {
        Api.create(DeviceService.class).deviceControl("6", ability.inst.get(0), token, unitedDevice.infraTypeID).enqueue(new Callback<DevicecontrolResult>() {
            @Override
            public void onResponse(Call<DevicecontrolResult> call, Response<DevicecontrolResult> response) {
                DevicecontrolResult result = response.body();
                if (result != null && result.success()) {
                    Toast.makeText(getContext(), "指令发送成功:" + result.lastInst, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "指令发送失败:" + (result != null ? result.errmsg : ""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DevicecontrolResult> call, Throwable t) {
                Toast.makeText(getContext(), "指令发送失败:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

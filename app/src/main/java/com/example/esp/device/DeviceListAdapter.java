package com.example.esp.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp.R;
import com.example.esp.model.Device;

import java.util.List;

/**
 * Created by Ryan Hu on 2020/3/25.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    private List<Device> deviceList;

    public void update(List<Device> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(deviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceList != null ? deviceList.size() : 0;
    }

    public void removeDevice(String deviceId) {
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceId.equals(deviceList.get(i).devID)) {
                deviceList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;
        private TextView deviceType;
        private TextView deviceStatus;
        private View deleteButton;

        public DeviceViewHolder(View itemView){
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceType = itemView.findViewById(R.id.device_type);
            deviceStatus = itemView.findViewById(R.id.device_status);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            deleteButton.setOnClickListener(clickListener);
            itemView.setOnClickListener(itemListener);
        }

        public void bind(Device device) {
            deviceName.setText(device.devName);
            deviceType.setText(device.devType);
            deviceStatus.setText(device.devStatus);
            deleteButton.setTag(device);
            itemView.setTag(device);
        }
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof Device) {
                if (listener != null) {
                    listener.onDeleteDevice((Device)tag);
                }
            }
        }
    };

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof Device) {
                if (listener != null) {
                    listener.onClickDevice((Device)tag);
                }
            }
        }
    };

    public interface Listener {
        void onClickDevice(Device device);
        void onDeleteDevice(Device device);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}

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

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public DeviceViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        public void bind(Device device) {
            title.setText(device.devName);
        }
    }
}

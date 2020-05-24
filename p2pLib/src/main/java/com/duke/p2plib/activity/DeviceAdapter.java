package com.duke.p2plib.activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.p2plib.R;
import com.duke.p2plib.p2phelper.WifiP2PHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    public interface OnItemClickListener {
        void onItemClick(WifiP2pDevice wifiP2pDevice);
    }

    private OnItemClickListener clickListener;
    private List<WifiP2pDevice> wifiP2pDeviceList = new ArrayList<>();

    public void setWifiP2pDeviceList(Collection<WifiP2pDevice> wifiP2pDeviceList) {
        this.wifiP2pDeviceList.clear();
        if (wifiP2pDeviceList != null && wifiP2pDeviceList.size() > 0) {
            this.wifiP2pDeviceList.addAll(wifiP2pDeviceList);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceHolder holder, final int position) {
        holder.tv_deviceName.setText(wifiP2pDeviceList.get(position).deviceName);
        holder.tv_deviceAddress.setText(wifiP2pDeviceList.get(position).deviceAddress);
        holder.tv_deviceDetails.setText(WifiP2PHelper.getInstance(holder.tv_deviceDetails.getContext()).getDeviceStatus(wifiP2pDeviceList.get(position).status));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(wifiP2pDeviceList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wifiP2pDeviceList.size();
    }

    static class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView tv_deviceName;
        private TextView tv_deviceAddress;
        private TextView tv_deviceDetails;

        DeviceHolder(View itemView) {
            super(itemView);
            tv_deviceName = itemView.findViewById(R.id.tv_deviceName);
            tv_deviceAddress = itemView.findViewById(R.id.tv_deviceAddress);
            tv_deviceDetails = itemView.findViewById(R.id.tv_deviceDetails);
        }
    }
}

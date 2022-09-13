package hcmute.nhom13.detaicuoiki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.models.busStopModel;

//Adapter hiển thị list chi tiết cách đi
public class directionMapsDetailAdapter extends BaseAdapter {
    //Khởi tạo các biến cần dùng
    final Context context;
    final int layout;
    final ArrayList<busStopModel> busStopModels;

    //Gán các biến vừa khởi tạo
    public directionMapsDetailAdapter(Context context, int layout, ArrayList<busStopModel> busStopModels) {
        this.context = context;
        this.layout = layout;
        this.busStopModels = busStopModels;
    }

    @Override
    public int getCount() {
        return busStopModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        //Khởi tạo các biến giao diện
        TextView txtBeginDetail, txtBeginDetailStep1, txtBeginDetailStep2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        directionMapsDetailAdapter.ViewHolder holder;

        //Khởi tạo và kiểm tra view
        if(view == null) {
            holder = new directionMapsDetailAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //Gán giá trị cho các biến khởi tạo trong giao diện
            holder.txtBeginDetail = view.findViewById(R.id.txtBeginDetail);
            holder.txtBeginDetailStep1 = view.findViewById(R.id.txtBeginDetailStep1);
            holder.txtBeginDetailStep2 = view.findViewById(R.id.txtBeginDetailStep2);
            view.setTag(holder);
        }else {
            holder = (directionMapsDetailAdapter.ViewHolder) view.getTag();
        }

        //Duyệt các phần tử trong routeList tiến hành render ra giao diện
        holder.txtBeginDetail.setText("Đón tuyến " + busStopModels.get(i).getRoute().getRouteId() + " metro");
        holder.txtBeginDetailStep1.setText("Xuất phát từ: " + busStopModels.get(i).getStation().getStationAddress());
        holder.txtBeginDetailStep2.setText("Đón tuyến " + busStopModels.get(i).getRoute().getRouteId() + " tại " + busStopModels.get(i).getStation().getStationName());

        return view;
    }
}

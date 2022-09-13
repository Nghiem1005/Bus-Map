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

//Adapter hiển thị list các trạm đi qua khi tìm kiếm bằng địa chỉ
public class directionMapsStationAdapter extends BaseAdapter {

    //Khởi tạo các biến cần dùng
    private Context context;
    private int layout;
    ArrayList<busStopModel> busStopList;

    public  directionMapsStationAdapter(Context context, int layout, ArrayList<busStopModel> busStopList) {
        //Gán các biến vừa khởi tạo
        this.context = context;
        this.layout = layout;
        this.busStopList = busStopList;
    }
    @Override
    public int getCount() {
        return busStopList.size();
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
        TextView txtStationName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        directionMapsStationAdapter.ViewHolder holder;

        //Khởi tạo và kiểm tra view
        if(view == null) {
            holder = new directionMapsStationAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //Gán giá trị cho các biến khởi tạo trong giao diện
            holder.txtStationName = view.findViewById(R.id.txtStationName);

            view.setTag(holder);
        }else {
            holder = (directionMapsStationAdapter.ViewHolder) view.getTag();
        }

        //Duyệt các phần tử trong routeList tiến hành render ra giao diện
        busStopModel busStop = busStopList.get(i);
        holder.txtStationName.setText(busStop.getStation().getStationName());

        return view;
    }
}

package hcmute.nhom13.detaicuoiki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.daos.busStopDAO;
import hcmute.nhom13.detaicuoiki.daos.savedRouteDAO;
import hcmute.nhom13.detaicuoiki.interfacee.ItransmitData;
import hcmute.nhom13.detaicuoiki.models.busStopModel;

//Adapter hiển thị list đường đi khi tìm kiếm bằng địa chỉ
public class homeFindDirectionAdapter extends BaseAdapter {
    //Khởi tạo các biến cần dùng
    final Context context;
    final int layout;
    final ArrayList<busStopModel> busList1;
    final ArrayList<busStopModel> busList2;
    final ArrayList<busStopModel> busList3;
    final ArrayList<busStopModel> busList4;
    savedRouteDAO savedRDAO;
    hcmute.nhom13.detaicuoiki.daos.busStopDAO busStopDAO;
    String userEmail;
    ItransmitData passDataToInfoBus;
    String stationStart;
    String stationEnd;

    //Gán các biến vừa khởi tạo
    public homeFindDirectionAdapter(Context context, int layout, ArrayList<busStopModel> busList1, ArrayList<busStopModel> busList2, ArrayList<busStopModel> busList3, ArrayList<busStopModel> busList4, String userEmail, ItransmitData passDataToInfoBus, String stationStart, String stationEnd) {
        this.context = context;
        this.layout = layout;
        this.busList2 = busList2;
        this.busList1 = busList1;
        this.busList3 = busList3;
        this.busList4 = busList4;
        this.userEmail = userEmail;
        this.passDataToInfoBus = passDataToInfoBus;
        this.stationStart = stationStart;
        this.stationEnd = stationEnd;
    }

    @Override
    public int getCount() {
        return busList1.size();
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
        TextView routeNumber, routeSpace, routeActive, routePrice, routeTime;
        LinearLayout busBtn;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        homeFindDirectionAdapter.ViewHolder holder;

        busStopDAO = new busStopDAO(context);

        //Khởi tạo và kiểm tra view
        if(view == null) {
            holder = new homeFindDirectionAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //Gán giá trị cho các biến khởi tạo trong giao diện
            holder.routeNumber = view.findViewById(R.id.routeNumber);
            holder.routeSpace = view.findViewById(R.id.routeSpace);
            holder.routeActive = view.findViewById(R.id.routeActive);
            holder.routePrice = view.findViewById(R.id.routePrice);
            holder.routeTime = view.findViewById(R.id.savedRoute);
            holder.busBtn = view.findViewById(R.id.busBtn);
            view.setTag(holder);
        }else {
            holder = (homeFindDirectionAdapter.ViewHolder) view.getTag();
        }

        if(busList2.size() == 0 && busList3.size() == 0){
            //Duyệt các phần tử trong routeList tiến hành render ra giao diện
            busStopModel bus = busList1.get(i);
            holder.routeNumber.setText("Tuyến số " + bus.getRoute().getRouteId() + " metro");
            holder.routeSpace.setText(stationStart + " - " + stationEnd);
            //holder.routeActive.setText(bus.getRoute().getRouteOperationTime());
            holder.routePrice.setText(bus.getRoute().getRoutePrice() + " VNĐ");

            //Bắt sự kiện busBtn khi ấn thì hiện thị tuyến đi lên google map
            holder.busBtn.setOnClickListener(view1 -> {
                passDataToInfoBus.passDataRouteDetail(bus.getRoute().getRouteId(), "-1", "-1", -1, -1);
            });
        } else if(busList3.size() == 0){
            //Duyệt các phần tử trong routeList tiến hành render ra giao diện
            busStopModel bus1 = busList1.get(i);
            busStopModel bus2 = busList2.get(i);
            if(bus1.getStation().getStationId() == bus2.getStation().getStationId()){
                holder.routeNumber.setText("Tuyến số " + bus1.getRoute().getRouteId() + ", " + bus2.getRoute().getRouteId() + " metro");
                holder.routeSpace.setText(stationStart + " - " + stationEnd);
                int price = bus1.getRoute().getRoutePrice() + bus2.getRoute().getRoutePrice();
                holder.routePrice.setText(price + " VNĐ");
            }


            //Bắt sự kiện busBtn khi ấn thì hiện thị tuyến đi lên google map
            holder.busBtn.setOnClickListener(view1 -> {
                passDataToInfoBus.passDataRouteDetail(bus1.getRoute().getRouteId(), bus2.getRoute().getRouteId(), "-1", bus1.getOrdinal(), -1);
            });
        } else {
            //Duyệt các phần tử trong routeList tiến hành render ra giao diện
            busStopModel bus1 = busList1.get(i);
            busStopModel bus2 = busList2.get(i);
            busStopModel bus3 = busList3.get(i);
            //busStopModel bus4 = busList4.get(i);
            //if(bus1.getStation().getStationId() == bus3.getStation().getStationId()){
                holder.routeNumber.setText("Tuyến số " + bus1.getRoute().getRouteId() + ", " + bus3.getRoute().getRouteId() + ", " + bus2.getRoute().getRouteId() + " metro");
                holder.routeSpace.setText(stationStart + " - " + stationEnd);
                int price = bus1.getRoute().getRoutePrice() + bus2.getRoute().getRoutePrice() + bus3.getRoute().getRoutePrice();
                holder.routePrice.setText(price + " VNĐ");
            //}


            //Bắt sự kiện busBtn khi ấn thì hiện thị tuyến đi lên google map
            holder.busBtn.setOnClickListener(view1 -> {
                passDataToInfoBus.passDataRouteDetail(bus1.getRoute().getRouteId(), bus2.getRoute().getRouteId(), bus3.getRoute().getRouteId(), busStopDAO.getBusByStationIdAndRoute(bus3.getRoute().getRouteId(), bus1.getStation().getStationId()).getOrdinal(), bus3.getOrdinal());
            });
        }

        return view;
    }
}

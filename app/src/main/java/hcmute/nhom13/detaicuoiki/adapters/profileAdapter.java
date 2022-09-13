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
import hcmute.nhom13.detaicuoiki.daos.savedRouteDAO;
import hcmute.nhom13.detaicuoiki.interfacee.ItransmitData;
import hcmute.nhom13.detaicuoiki.models.savedRouteModel;

//Adapter hiển thị danh sách các tuyến đã lưu trong thông tin người dùng
public class profileAdapter extends BaseAdapter {
    //Khởi tạo các biến cần dùng
    private Context context;
    private int layout;
    ArrayList<savedRouteModel> routeList;
    String userEmail;
    savedRouteDAO savedRDAO;
    ItransmitData passDataToInfoBus;

    public profileAdapter(Context context, int layout, ArrayList<savedRouteModel> routeList, String userEmail, ItransmitData passDataToInfoBus) {
        //Gán các biến vừa khởi tạo
        this.context = context;
        this.layout = layout;
        this.routeList = routeList;
        this.userEmail = userEmail;
        this.passDataToInfoBus = passDataToInfoBus;
    }
    @Override
    public int getCount() {
        return routeList.size();
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
        TextView routeNumber, routeSpace, routeActive, routePrice, unSavedRoute;
        LinearLayout busBtn;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        profileAdapter.ViewHolder holder;

        savedRDAO = new savedRouteDAO(context);

        //Khởi tạo và kiểm tra view
        if(view == null) {
            holder = new profileAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //Gán giá trị cho các biến khởi tạo trong giao diện
            holder.routeNumber = view.findViewById(R.id.routeNumber);
            holder.routeSpace = view.findViewById(R.id.routeSpace);
            holder.routeActive = view.findViewById(R.id.routeActive);
            holder.routePrice = view.findViewById(R.id.routePrice);
            holder.unSavedRoute = view.findViewById(R.id.unSavedRoute);
            holder.busBtn = view.findViewById(R.id.busBtn);
            view.setTag(holder);
        }else {
            holder = (profileAdapter.ViewHolder) view.getTag();
        }

        //Duyệt các phần tử trong routeList tiến hành render ra giao diện
        savedRouteModel savedRoute = routeList.get(i);
        holder.routeNumber.setText("Tuyến số " + savedRoute.getRouteModel().getRouteId() + " metro");
        holder.routeSpace.setText(savedRoute.getRouteModel().getStationStart().getStationName() + " - " + savedRoute.getRouteModel().getStationStart().getStationName());
        holder.routeActive.setText(savedRoute.getRouteModel().getRouteOperationTime());
        holder.routePrice.setText(savedRoute.getRouteModel().getRoutePrice() + "VNĐ");

        // Bắt sự kiện khi user ấn hủy lưu
        holder.unSavedRoute.setOnClickListener(view1 -> {
            try {
                //Tiến hành hủy lưu route này
                savedRDAO.deleteSavedRoute(savedRoute.getRouteModel().getRouteId(), userEmail);
                passDataToInfoBus.passDataToProfile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Bắt sự kiện busBtn khi ấn thì hiện thị tuyến đi lên google map
        holder.busBtn.setOnClickListener(view1 -> {
            passDataToInfoBus.passDataRouteData(savedRoute.getRouteModel().getRouteId());
        });

        return view;
    }
}

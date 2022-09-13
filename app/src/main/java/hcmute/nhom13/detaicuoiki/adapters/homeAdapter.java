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
import hcmute.nhom13.detaicuoiki.models.routeModel;

//Adapter hiển thị list các tuyến đi
public class homeAdapter extends BaseAdapter {
    //Khởi tạo các biến cần dùng
    final Context context;
    final int layout;
    final ArrayList<routeModel> routeList;
    savedRouteDAO savedRDAO;
    String userEmail;
    ItransmitData passDataToInfoBus;

    //Gán các biến vừa khởi tạo
    public homeAdapter(Context context, int layout, ArrayList<routeModel> routeList, String userEmail, ItransmitData passDataToInfoBus) {
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
        TextView routeNumber, routeSpace, routeActive, routePrice, savedRoute;
        LinearLayout busBtn;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        savedRDAO = new savedRouteDAO(context);

        //Khởi tạo và kiểm tra view
        if(view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //Gán giá trị cho các biến khởi tạo trong giao diện
            holder.routeNumber = view.findViewById(R.id.routeNumber);
            holder.routeSpace = view.findViewById(R.id.routeSpace);
            holder.routeActive = view.findViewById(R.id.routeActive);
            holder.routePrice = view.findViewById(R.id.routePrice);
            holder.savedRoute = view.findViewById(R.id.savedRoute);
            holder.busBtn = view.findViewById(R.id.busBtn);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        //Duyệt các phần tử trong routeList tiến hành render ra giao diện
        routeModel savedRoute = routeList.get(i);
        holder.routeNumber.setText("Tuyến số " + savedRoute.getRouteId() + " metro");
        holder.routeSpace.setText(savedRoute.getStationStart().getStationName() + " - " + savedRoute.getStationEnd().getStationName());
        holder.routeActive.setText(savedRoute.getRouteOperationTime());
        holder.routePrice.setText(savedRoute.getRoutePrice() + " VNĐ");

        //Kiểm tra tuyền đường nào đã lưu rồi thì k có lưu nữa
        if(savedRDAO.checkIsSavedRoute(savedRoute.getRouteId(), userEmail)) {
            holder.savedRoute.setText("Hủy lưu");
        } else {
            holder.savedRoute.setText("Lưu");
        }

        // Bắt sự kiện khi user ấn lưu
        holder.savedRoute.setOnClickListener(view1 -> {
            try {
                //Kiểm tra nếu chưa lưu thì tiến hành lưu route này
                if(!savedRDAO.checkIsSavedRoute(savedRoute.getRouteId(), userEmail)) {
                    savedRDAO.insertSavedRoute(savedRoute.getRouteId(), userEmail);
                    holder.savedRoute.setText("Hủy lưu");
                } else {
                    savedRDAO.deleteSavedRoute(savedRoute.getRouteId(), userEmail);
                    holder.savedRoute.setText("Lưu");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        //Bắt sự kiện busBtn khi ấn thì hiện thị tuyến đi lên google map
        holder.busBtn.setOnClickListener(view1 -> {
            passDataToInfoBus.passDataRouteData(savedRoute.getRouteId());
        });

        return view;
    }

}

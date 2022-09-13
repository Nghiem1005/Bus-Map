package hcmute.nhom13.detaicuoiki.daos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.routeModel;
import hcmute.nhom13.detaicuoiki.models.savedRouteModel;
import hcmute.nhom13.detaicuoiki.models.userModel;

public class savedRouteDAO {
    databaseHelper database;
    ArrayList<savedRouteModel> savedRouteList;
    userDAO uDAO;
    routeDAO rDAO;
    userModel userModel;
    routeModel routeModel;

    public savedRouteDAO (@NonNull Context context) {
        database = new databaseHelper(context);
        uDAO = new userDAO(context);
        rDAO = new routeDAO(context);
    }

    //Hàm lấy danh sách lưu xe buýt yêu thích theo từng user
    public ArrayList<savedRouteModel> getSavedRouteByUser(String userEmail) {
        savedRouteList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT * from savedroute where user_gmail='"+ userEmail + "'");
        while (cursor.moveToNext()){
            String route = cursor.getString(0);
            String user = cursor.getString(1);
            routeModel = rDAO.getRouteById(route);
            userModel = uDAO.getUserByGmail(user);
            savedRouteList.add(new savedRouteModel(routeModel, userModel));
        }
        return savedRouteList;
    }

    //Hàm lưu xe buýt yêu thích theo từng user
    public void insertSavedRoute(String routeId, String userEmail) {
        database.QueryData("insert into savedroute values('"+ routeId  +"','"+ userEmail + "')");
    }

    //Hàm xóa xe buýt yêu thích theo từng user
    public void deleteSavedRoute(String routeId, String userEmail) {
        database.QueryData("delete from  savedroute where route_id='"+ routeId  +"' and user_gmail='"+ userEmail + "'");
    }

    //Hàm kiểm tra xem có buýt yêu thích theo từng user
    public boolean checkIsSavedRoute(String routeId, String userEmail) {
        Cursor cursor = database.GetData("SELECT * from savedroute where " +
                "route_id='"+ routeId +"' and user_gmail='"+ userEmail + "'");
        if (cursor.getCount() > 0 ) return true;
        return false;
    }
}

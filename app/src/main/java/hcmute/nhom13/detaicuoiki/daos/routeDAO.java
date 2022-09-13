package hcmute.nhom13.detaicuoiki.daos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.routeModel;
import hcmute.nhom13.detaicuoiki.models.stationModel;

public class routeDAO {
    databaseHelper database;
    routeModel routeModel;
    stationDAO sDAO;
    stationModel sModelStart, sModelEnd;
    ArrayList<routeModel> arrayListRoute;

    public routeDAO (@NonNull Context context) {
        database = new databaseHelper(context);
        sDAO = new stationDAO(context);
    }

    //Hàm lấy danh sách tất cả xe buýt
    public ArrayList<routeModel> getRouteAll() {
        arrayListRoute = new ArrayList<>();
        Cursor cursor =  database.GetData("SELECT * from route");
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            int stationStart = cursor.getInt(1);
            int stationEnd = cursor.getInt(2);
            int price = cursor.getInt(3);
            String type = cursor.getString(4);
            String operation = cursor.getString(5);
            String cycleTime = cursor.getString(6);
            String unit = cursor.getString(7);
            int repeatTime = cursor.getInt(8);
            int perDay = cursor.getInt(9);
            int distance = cursor.getInt(10);
            sModelStart = sDAO.getStationById(stationStart);
            sModelEnd = sDAO.getStationById(stationEnd);
            arrayListRoute.add(new routeModel(id, sModelStart, sModelEnd, price, type, operation, cycleTime, unit, repeatTime, perDay, distance));
        }
        return  arrayListRoute;
    }

    //Hàm lấy danh sách tất cả xe buýt theo kết quả tìm kiếm
    public ArrayList<routeModel> getRouteSearch(String value) {
        arrayListRoute = new ArrayList<>();
        Cursor cursor =  database.GetData("SELECT * from route WHERE id like '%" + value + "'");
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            int stationStart = cursor.getInt(1);
            int stationEnd = cursor.getInt(2);
            int price = cursor.getInt(3);
            String type = cursor.getString(4);
            String operation = cursor.getString(5);
            String cycleTime = cursor.getString(6);
            String unit = cursor.getString(7);
            int repeatTime = cursor.getInt(8);
            int perDay = cursor.getInt(9);
            int distance = cursor.getInt(10);
            sModelStart = sDAO.getStationById(stationStart);
            sModelEnd = sDAO.getStationById(stationEnd);
            arrayListRoute.add(new routeModel(id, sModelStart, sModelEnd, price, type, operation, cycleTime, unit, repeatTime, perDay, distance));
        }
        return  arrayListRoute;
    }

    //Hàm lấy danh sách xe buýt theo mã xe buýt
    public routeModel getRouteById (String routeId) {
        routeModel = new routeModel();
        Cursor cursor = database.GetData("SELECT * from route WHERE id='"+ routeId +"'");
        cursor.moveToFirst();
        String id = cursor.getString(0);
        int stationStart = cursor.getInt(1);
        int stationEnd = cursor.getInt(2);
        int price = cursor.getInt(3);
        String type = cursor.getString(4);
        String operation = cursor.getString(5);
        String cycleTime = cursor.getString(6);
        String unit = cursor.getString(7);
        int repeatTime = cursor.getInt(8);
        int perDay = cursor.getInt(9);
        int distance = cursor.getInt(10);
        sModelStart = sDAO.getStationById(stationStart);
        sModelEnd = sDAO.getStationById(stationEnd);
        routeModel.setRouteId(id);
        routeModel.setStationStart(sModelStart);
        routeModel.setStationEnd(sModelEnd);
        routeModel.setRoutePrice(price);
        routeModel.setRouteType(type);
        routeModel.setRouteOperationTime(operation);
        routeModel.setRouteCycleTime(cycleTime);
        routeModel.setRouteUnit(unit);
        routeModel.setRouteRepeatTime(repeatTime);
        routeModel.setRoutePerDay(perDay);
        routeModel.setRouteDistance(distance);

        return routeModel;

    }
}

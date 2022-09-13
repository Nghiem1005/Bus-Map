package hcmute.nhom13.detaicuoiki.daos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.busStopModel;
import hcmute.nhom13.detaicuoiki.models.routeModel;
import hcmute.nhom13.detaicuoiki.models.stationModel;

public class busStopDAO {
    databaseHelper database;
    ArrayList<busStopModel> busList;
    routeDAO rDAO;
    stationDAO sDAO;
    routeModel routeModel;
    stationModel stationModel;
    busStopModel busStopModel;

    public busStopDAO (@NonNull Context context) {
        database = new databaseHelper(context);
        rDAO = new routeDAO(context);
        sDAO = new stationDAO(context);
    }

    //Hàm lấy danh sách xe buýt theo mã đường
    public ArrayList<busStopModel> getBusList(String routeId) {
        busList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT * from busstop WHERE route_id='"+ routeId +"'");
        while (cursor.moveToNext()) {
            String route = cursor.getString(0);
            int station = cursor.getInt(1);
            int ordinal = cursor.getInt(2);
            stationModel = sDAO.getStationById(station);
            routeModel = rDAO.getRouteById(route);
            busList.add(new busStopModel(routeModel, stationModel, ordinal));
        }
        return busList;
    }

    //Hàm lấy danh sách xe buýt theo địa chỉ trạm dừng
    public ArrayList<busStopModel> getBusListByAddress(String address) {
        busList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT busstop.* from busstop INNER JOIN station on busstop.station_id = station.id where station.address= '"+ address +"'");
        while (cursor.moveToNext()) {
            String route = cursor.getString(0);
            int station = cursor.getInt(1);
            int ordinal = cursor.getInt(2);
            stationModel = sDAO.getStationById(station);
            routeModel = rDAO.getRouteById(route);
            busList.add(new busStopModel(routeModel, stationModel, ordinal));
        }
        return busList;
    }

    //Hàm lấy xe buýt theo địa chỉ trạm dừng và tuyến
    public busStopModel getBusListByAddressAndRoute(String routeId, String address) {
        Cursor cursor = database.GetData("SELECT busstop.* from busstop INNER JOIN station on busstop.station_id = station.id where station.address= '"+ address +"' AND busstop.route_id='" + routeId + "'");
        cursor.moveToFirst();
        String route = cursor.getString(0);
        int station = cursor.getInt(1);
        int ordinal = cursor.getInt(2);
        stationModel = sDAO.getStationById(station);
        routeModel = rDAO.getRouteById(route);
        busStopModel = new busStopModel(routeModel, stationModel, ordinal);

        return busStopModel;
    }

    //Hàm lấy xe buýt theo id trạm dừng và tuyến
    public busStopModel getBusByStationIdAndRoute(String routeId, int statitonId) {
        Cursor cursor = database.GetData("SELECT * FROM busstop WHERE route_id='" + routeId + "' AND station_id=" + statitonId);
        cursor.moveToFirst();
        String route = cursor.getString(0);
        int station = cursor.getInt(1);
        int ordinal = cursor.getInt(2);
        stationModel = sDAO.getStationById(station);
        routeModel = rDAO.getRouteById(route);
        busStopModel = new busStopModel(routeModel, stationModel, ordinal);

        return busStopModel;
    }

    //Hàm lấy xe buýt theo id trạm dừng và tuyến
    public busStopModel getBusByOrderAndRoute(String routeId, int order) {
        Cursor cursor = database.GetData("SELECT * FROM busstop WHERE route_id='"+ routeId +"' AND orderStation="+ order);
        cursor.moveToFirst();
        String route = cursor.getString(0);
        int station = cursor.getInt(1);
        int ordinal = cursor.getInt(2);
        stationModel = sDAO.getStationById(station);
        routeModel = rDAO.getRouteById(route);
        busStopModel = new busStopModel(routeModel, stationModel, ordinal);

        return busStopModel;
    }

    //Hàm lấy danh sách xe buýt theo tuyến, trạm bắt đầu và trạm kết thúc đã được sắp xếp theo thứ tự các trạm trong tuyến
    //Trạm ở điểm xuất phát thì có thứ tự nhỏ hơn
    public ArrayList<busStopModel> getBusListStartAndEndStationAndRoute(String routeId, int StationStart, int StationEnd, int sort) {
        busList = new ArrayList<>();
        Cursor cursor;
        if(sort == 0){
            //Sắp xếp theo thứ tự tăng dần
            cursor = database.GetData("SELECT * FROM busstop WHERE route_id = '" + routeId + "' AND orderStation>=" + StationStart + " AND orderStation<=" + StationEnd + " ORDER by\n" +
                    "orderStation");
        } else {
            //Sắp xếp theo thứ tự giảm dần
            cursor = database.GetData("SELECT * FROM busstop WHERE route_id = '" + routeId + "' AND orderStation>=" + StationStart + " AND orderStation<=" + StationEnd + " ORDER by\n" +
                    "orderStation DESC");
        }
        while (cursor.moveToNext()) {
            String route = cursor.getString(0);
            int station = cursor.getInt(1);
            int ordinal = cursor.getInt(2);
            stationModel = sDAO.getStationById(station);
            routeModel = rDAO.getRouteById(route);
            busList.add(new busStopModel(routeModel, stationModel, ordinal));
        }
        return busList;
    }
}

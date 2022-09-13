package hcmute.nhom13.detaicuoiki.daos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.stationModel;

public class stationDAO {
    databaseHelper database;
    stationModel stationModel;
    ArrayList<stationModel> stationList;

    public stationDAO (@NonNull Context context) {
        database = new databaseHelper(context);
    }

    //Hàm lấy trạm xe buýt theo mã trạm xe buýt
    public stationModel getStationById (int stationId) {
        stationModel = new stationModel();
        Cursor cursor = database.GetData("SELECT * from station WHERE id=" + stationId);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String address = cursor.getString(2);
        Double latitude = cursor.getDouble(3);
        Double longitude = cursor.getDouble(4);

        stationModel.setStationId(id);
        stationModel.setStationName(name);
        stationModel.setStationAddress(address);
        stationModel.setStationLatitude(latitude);
        stationModel.setStationLongitude(longitude);
        return stationModel;
    }

    //Hàm lấy danh sách tất cả trạm xe buýt theo mã trạm xe buýt
    public ArrayList<stationModel> getStationAll () {
        stationList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT * from station ");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            Double latitude = cursor.getDouble(3);
            Double longitude = cursor.getDouble(4);
            stationList.add(new stationModel(id, name, address, latitude, longitude));
        }
        return stationList;
    }
}

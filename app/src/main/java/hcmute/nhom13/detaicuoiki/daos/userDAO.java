package hcmute.nhom13.detaicuoiki.daos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.userModel;

public class userDAO {
    databaseHelper  database;
    userModel userModel;

    public userDAO (@NonNull Context context) {
        database = new databaseHelper(context);
    }

    //Hàm thêm tài khoản người dùng
    public void insert(String name, String email, String birthday, String phone, String password) {
        database.QueryData("INSERT INTO user VALUES('"+ email +"','"+ password + "','"
                + name + "','"+ phone +"'," + 0 + ",'"+ birthday  + "','none')");
    }

    //Hàm lấy tài khoản người dùng theo số điện thoại
    public userModel getUserByPhone(String phone) {
        Cursor dataUSer = database.GetData("SELECT * FROM user where phone ='" + phone + "'");
        dataUSer.moveToFirst();
        String userEmail = dataUSer.getString(0);
        String userName = dataUSer.getString(2);
        String userPhone = dataUSer.getString(3);
        String birthday = dataUSer.getString(5);
        userModel = new userModel();
        userModel.setName(userName);
        userModel.setEmail(userEmail);
        userModel.setPhone(userPhone);
        userModel.setBirthday(birthday);
        return userModel;
    }

    //Hàm lấy tài khoản người dùng theo email
    public userModel getUserByGmail(String email) {
        Cursor dataUSer = database.GetData("SELECT * FROM user where email ='" + email + "'");
        dataUSer.moveToFirst();
        String userEmail = dataUSer.getString(0);
        String userName = dataUSer.getString(2);
        String userPhone = dataUSer.getString(3);
        String birthday = dataUSer.getString(5);
        userModel = new userModel();
        userModel.setName(userName);
        userModel.setEmail(userEmail);
        userModel.setPhone(userPhone);
        userModel.setBirthday(birthday);
        return userModel;
    }

    //Hàm cập nhật thông tin tài khoản người dùng theo email
    public void updateUser(String name, String prevEmail, String newEmail, String phone, String birthday) {
        database.QueryData("UPDATE user SET email ='"+ newEmail +
                "', name ='" + name + "',phone='"+ phone + "',date_of_birth ='"+ birthday + "' where email='" + prevEmail +"'");
    }

    //Hàm kiểm tra tài khoản người dùng có chưa
    public boolean checkExistUser (String phone) {
        Cursor dataUser = database.GetData("SELECT * from user where phone ='" + phone + "'");
        if (dataUser.getCount() > 0 ) return true;
        return false;
    }

    //Hàm đăng nhập tài khoản người dùng
    public userModel login (String phone, String password) {
        Cursor dataUSer = database.GetData("SELECT * from user where " +
                "phone ='" + phone + "' and password='" + password + "' LIMIT 1");
        dataUSer.moveToFirst();
        String userEmail = dataUSer.getString(0);
        String userName = dataUSer.getString(2);
        String userPhone = dataUSer.getString(3);
        String birthday = dataUSer.getString(5);
        userModel = new userModel();
        userModel.setName(userName);
        userModel.setEmail(userEmail);
        userModel.setPhone(userPhone);
        userModel.setBirthday(birthday);
        return userModel;
    }

}

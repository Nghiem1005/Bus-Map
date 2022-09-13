package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.adapters.profileAdapter;
import hcmute.nhom13.detaicuoiki.daos.savedRouteDAO;
import hcmute.nhom13.detaicuoiki.daos.userDAO;
import hcmute.nhom13.detaicuoiki.interfacee.ItransmitData;
import hcmute.nhom13.detaicuoiki.models.savedRouteModel;
import hcmute.nhom13.detaicuoiki.models.userModel;

//Activity trang thông tin người dùng
public class ProfileActivity extends AppCompatActivity implements ItransmitData {

    //Khởi tạo các giá trị cần dùng
    TextView txtName, txtEmail, txtPhone, txtBirthday, txtProfileMessage;
    Button btnLogout, btnEdit;
    ImageView backBtn;
    ListView listSavedRoute;

    String userEmail;
    userModel userModel;
    userDAO uDAO;
    savedRouteDAO saveRDAO;
    ArrayList<savedRouteModel> savedRouteList;
    profileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Khởi tạo hàm DAO
        saveRDAO = new savedRouteDAO(this);
        uDAO = new userDAO(this);

        //Nhận dữ liệu userPhone từ các form khác trả về để lấy thông tin user
        Intent i = this.getIntent();
        if (i != null) {
            userEmail = i.getStringExtra("id");
        }

        //Thấy thông tin phone của user để lấy ra tất cả những route được lưu
        savedRouteList = saveRDAO.getSavedRouteByUser(userEmail);
        listSavedRoute = findViewById(R.id.listSavedRoute);


        //Kiểm tra nếu có route được lưu rồi thì hiển thị
        //Không có route nào được lưu thì thông báo chưa có route được lưu
        if(savedRouteList != null && savedRouteList.size() > 0  ) {
            profileAdapter = new profileAdapter(ProfileActivity.this, R.layout.save_route_item, savedRouteList, userEmail, ProfileActivity.this);
            listSavedRoute.setAdapter(profileAdapter);
        } else {
            txtProfileMessage = findViewById(R.id.txtProfileMessage);
            txtProfileMessage.setText("Bạn chưa lưu tuyến xe nào");
        }

        //Gán giá trị cho biến giao diện
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);

        txtName = findViewById(R.id.txtProfileName);
        txtEmail = findViewById(R.id.txtProfileEmail);
        txtPhone = findViewById(R.id.txtProfilePhone);
        txtBirthday = findViewById(R.id.txtProfileBirthday);
        backBtn = findViewById(R.id.backBtn);

        //Lấy dữ liệu của user và hiển thị ra giao diện
        userModel = uDAO.getUserByGmail(userEmail);
        txtName.setText("Họ tên: " + userModel.getName());
        txtEmail.setText("Email: " + userModel.getEmail());
        txtPhone.setText("Số điện thoại: " + userModel.getPhone());
        txtBirthday.setText("Sinh nhật: " + userModel.getBirthday());

        //Bắt sự kiện btnLogout khi ấn đăng xuất thì thoát ra trang đăng nhập
        btnLogout.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        });

        //Bắt sự kiện btnEdit khi ấn thì chỉnh sửa thông tin cá nhân
        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("name", userModel.getName());
            intent.putExtra("id", userModel.getEmail());
            intent.putExtra("phone", userModel.getPhone());
            intent.putExtra("birthday", userModel.getBirthday());
            startActivity(intent);
        });

        //Bắt sự kiện backBtn khi ấn thì quay lại trang chủ
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("id", userModel.getEmail());
            startActivity(intent);
        });

    }

    //Truyền dữ liệu từ con ra cha thông qua 1 interface
    @Override
    public void passDataRouteData(String routeId) {
        Intent i1 = new Intent(this, ShowInfoDestinationAndMapsActivity.class);
        i1.putExtra("routeId", routeId);
        i1.putExtra("id", userEmail);
        startActivity(i1);
    }

    //Truyền dữ liệu từ con ra cha thông qua 1 interface
    @Override
    public void passDataToProfile() {
        Intent i1 = new Intent(this, ProfileActivity.class);
        i1.putExtra("id", userEmail);
        startActivity(i1);
    }

    @Override
    public void passDataRouteDetail(String routeId1, String routeId2, String routeId3, int station1, int station2) {

    }





    /*//Truyền dữ liệu từ con ra cha thông qua 1 interface
    @Override
    public void passDataRouteDetail(String routeIds) {

    }*/
}
package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.daos.userDAO;
import hcmute.nhom13.detaicuoiki.database.databaseHelper;
import hcmute.nhom13.detaicuoiki.models.userModel;

//Activity trang đăng nhập
public class MainActivity extends AppCompatActivity {

    //Khởi tạo các giá trị cần dùng
    databaseHelper database;

    userDAO userDAO;
    userModel userModel;
    TextView btnSwitchRegister, txtMessage;
    EditText txtPhoneLogin, txtPasswordLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Nạp dữ liệu data khi lần đầu khởi động chương trình
        //Khởi tạo hàm DAO
        userDAO = new userDAO(this);
        database = new databaseHelper(this);
        database.createDataBase();

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Gán giá trị các biến giao diện
        txtPhoneLogin = findViewById(R.id.txtPhoneLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        txtMessage = findViewById(R.id.txtMessage);

        //Bắt sự kiện btnSwitchRegister khi ấn thì đổi giữa đăng nhập và đăng kí
        btnSwitchRegister = findViewById(R.id.btnSwitchRegister);
        btnSwitchRegister.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        //Bắt sự kiện btnLogin khi đăng nhập thì kiểm tra xem có hợp lệ không
        //Nếu hợp lệ thì lưu thông tin đăng nhập và vào trang chủ,
        //Không hợp lệ thì thông báo lỗi
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            //Kiểm tra xem có tài khoản hợp lệ
            if(userDAO.checkExistUser(String.valueOf(txtPhoneLogin.getText()).trim())) {
                //Hợp lệ thì lấy dữ liệu người dùng truyền đi
                userModel = userDAO.login(String.valueOf(txtPhoneLogin.getText()).trim(), String.valueOf(txtPasswordLogin.getText()).trim());
                if (userModel != null ) {
                    //Truyền dữ liệu
                    Toast.makeText(this, "Chào mừng quý khách", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("id", userModel.getEmail());
                    startActivity(intent);
                } else {
                    txtMessage.setText("Mật khẩu không đúng!");
                }
            } else {
                txtMessage.setText("Số điện thoại không tồn tại!");
            }
        });
    }
}
package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.daos.userDAO;

//Acitivity trang đăng ký
public class RegisterActivity extends AppCompatActivity {

    //Khởi tạo các giá trị cần dùng
    TextView btnSwitchLogin, txtMessage;
    Button btnRegister;
    EditText txtName, txtEmail, txtPhone, txtBirthday, txtPassword, txtConfirmPassword;
    userDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Khởi tạo hàm DAO
        userDAO = new userDAO(this);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Gán giá trị các biến giao diện
        txtName = findViewById(R.id.txtRegisterFullName);
        txtEmail = findViewById(R.id.txtRegisterEmail);
        txtPhone = findViewById(R.id.txtRegisterPhone);
        txtBirthday = findViewById(R.id.txtRegisterBirthday);
        txtPassword = findViewById(R.id.txtRegisterPassword);
        txtConfirmPassword = findViewById(R.id.txtRegisterConfirmPassword);
        txtMessage = findViewById(R.id.txtMessage);

        //Bắt sự kiện btnSwitchLogin khi ấn thì đổi giữa đăng nhập và đăng kí
        btnSwitchLogin = findViewById(R.id.btnSwitchLogin);
        btnSwitchLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });


        //Bắt sự kiện btnRegister khi đăng kí với hàm xử lí registerUser()
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> registerUser(String.valueOf(txtName.getText()).trim(),
                String.valueOf(txtPhone.getText()).trim(),
                String.valueOf(txtEmail.getText()).trim(),
                String.valueOf(txtBirthday  .getText()).trim(),
                String.valueOf(txtPassword.getText()).trim(),
                String.valueOf(txtConfirmPassword.getText()).trim()));
    }

    //Bắt sự kiện btnRegister khi đăng kí thì kiểm tra xem có hợp lệ không
    //Nếu hợp lệ thì lưu thông tin đăng đăng kí và vào trang đăng nhập,
    //Không hợp lệ thì thông báo lỗi
    public void registerUser(String name, String phone, String email, String birthday, String password, String confirmPassword) {
        try {
            //Kiểm tra giá trị đầu vào các trường có rỗng không
            //Nếu rỗng thì thông báo "trường rỗng"
            if(name.equals("") || email.equals("") || phone.equals("") || birthday.equals("") || password.equals("") || confirmPassword.equals("")) {
                txtMessage.setText("Vui lòng nhập hết các trường!");
            } else {
                //Kiểm tra mật khẩu với xác nhận lại mật khẩu có giống nhau không
                //Nếu mật khẩu với xác nhận lại mật khẩu không giống thì thông báo "mật khẩu không khớp"
                if (password.equals(confirmPassword)) {
                    //Kiểm tra số điện thoại đã được đăng kí chưa
                    //Nếu có rồi thì thông báo "số điện thoại đã tồn tại"
                    if(!userDAO.checkExistUser(phone)) {
                        userDAO.insert(name, email, birthday, phone, password);
                        //Đăng kí thành công lưu thông tin và vào trang đăng nhập
                        Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        //Thông báo lỗi
                        txtMessage.setText("Số điện thoại đã tồn tại!");
                    }
                } else {
                    //Thông báo lỗi
                    txtMessage.setText("Mật khẩu không khớp!");
                }
            }
        } catch (Exception e) {
            Log.d("TAG", "Error: "+ e.getMessage());
        }
    }
}
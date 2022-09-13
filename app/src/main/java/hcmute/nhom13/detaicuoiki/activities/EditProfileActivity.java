package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import hcmute.nhom13.detaicuoiki.daos.userDAO;

import hcmute.nhom13.detaicuoiki.R;

//Activity trang chỉnh sửa thông tin người dùng
public class EditProfileActivity extends AppCompatActivity {

    //Khởi tạo các giá trị cần dùng
    userDAO uDAO;
    TextView txtEditName, txtEditEmail, txtEditPhone, txtEditBirthday;
    Button btnSave, btnCancel;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Khởi tạo hàm DAO
        uDAO = new userDAO(this);

        //Gán giá trị cho biến giao diện
        txtEditName = findViewById(R.id.editTextName);
        txtEditEmail = findViewById(R.id.editTextEmail);
        txtEditPhone = findViewById(R.id.editTextSdt);
        txtEditBirthday = findViewById(R.id.editTextBirthday);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        //Nhận dữ liệu userPhone từ các form khác trả về để lấy thông tin user
        Intent i = this.getIntent();
        if (i != null) {
            userEmail = i.getStringExtra("id");
            String name = i.getStringExtra("name");
            String phone = i.getStringExtra("phone");
            String birthday = i.getStringExtra("birthday");

            txtEditName.setText(name);
            txtEditEmail.setText(userEmail);
            txtEditPhone.setText(phone);
            txtEditBirthday.setText(birthday);
        }

        //Bắt sự kiện khi ấn btnSave lưu thông tin cập nhật mới và quay lại trang cá nhân
        btnSave.setOnClickListener(view -> {
            uDAO.updateUser(
                    String.valueOf(txtEditName.getText()),
                    userEmail,
                    String.valueOf(txtEditEmail.getText()),
                    String.valueOf(txtEditPhone.getText()),
                    String.valueOf(txtEditBirthday.getText()));

            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("id", userEmail);
            startActivity(intent);
        });

        //Bắt sự kiện khi ấn btnCancel hủy cập nhật mới và quay lại trang cá nhân
        btnCancel.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("id", userEmail);
            startActivity(intent);
        });
    }
}
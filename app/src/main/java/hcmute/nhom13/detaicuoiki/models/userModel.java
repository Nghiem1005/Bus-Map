package hcmute.nhom13.detaicuoiki.models;

//Model chứa thông tin người dùng
public class userModel {
    //Email
    private String email;
    //Mật khẩu
    private  String password;
    //Tên
    private String name;
    //Số điện thoại
    private String phone;
    //Giới tính
    private int gender;
    //Ngày sinh
    private String birthday;
    //Hình ảnh
    private String image;

    public userModel() {
    }

    //Tạo model userModel với các trường như dưới database để hứng dữ liệu
    public userModel(String email, String password, String name, String phone, int gender, String birthday, String image) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.image = image;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package hcmute.nhom13.detaicuoiki.models;

//Model lưu các trạm
public class savedStationModel {
    //Email người dùng
    private userModel userGmail;
    //Trạm
    private stationModel stationModel;

    public savedStationModel() {
    }
    //Tạo model savedStationModel với các trường như dưới database để hứng dữ liệu
    public savedStationModel(stationModel stationModel, userModel userGmail) {
        this.userGmail = userGmail;
        this.stationModel = stationModel;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public userModel getUserGmail() {
        return userGmail;
    }

    public void setUserGmail(userModel userGmail) {
        this.userGmail = userGmail;
    }

    public hcmute.nhom13.detaicuoiki.models.stationModel getStationModel() {
        return stationModel;
    }

    public void setStationModel(hcmute.nhom13.detaicuoiki.models.stationModel stationModel) {
        this.stationModel = stationModel;
    }
}

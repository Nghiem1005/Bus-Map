package hcmute.nhom13.detaicuoiki.models;

//Model lưu các tuyến
public class savedRouteModel {
    //Email người dùng
    private userModel userGmail;
    //Tuyến
    private routeModel routeModel;

    public savedRouteModel() {
    }

    //Tạo model savedRouteModel với các trường như dưới database để hứng dữ liệu
    public savedRouteModel(routeModel  routeModel, userModel userGmail) {
        this.userGmail = userGmail;
        this.routeModel = routeModel;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public userModel getUserGmail() {
        return userGmail;
    }

    public void setUserGmail(userModel userGmail) {
        this.userGmail = userGmail;
    }

    public hcmute.nhom13.detaicuoiki.models.routeModel getRouteModel() {
        return routeModel;
    }

    public void setRouteModel(hcmute.nhom13.detaicuoiki.models.routeModel routeModel) {
        this.routeModel = routeModel;
    }
}

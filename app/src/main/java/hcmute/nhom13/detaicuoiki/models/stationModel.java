package hcmute.nhom13.detaicuoiki.models;

//Model chứa thông tin trạm
public class stationModel {
    //Id trạm
    private int stationId;
    //Tên trạm
    private String stationName;
    //Địa chỉ trạm
    private String stationAddress;
    //Tọa độ của trạm (Vĩ độ)
    private double stationLatitude;
    //Tọa độ của trạm (Kinh độ)
    private double stationLongitude;

    public stationModel() {
    }

    //Tạo model stationModel với các trường như dưới database để hứng dữ liệu
    public stationModel(int stationId, String stationName, String stationAddress, double stationLatitude, double stationLongitude) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationAddress = stationAddress;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public double getStationLatitude() {
        return stationLatitude;
    }

    public void setStationLatitude(double stationLatitude) {
        this.stationLatitude = stationLatitude;
    }

    public double getStationLongitude() {
        return stationLongitude;
    }

    public void setStationLongitude(double stationLongitude) {
        this.stationLongitude = stationLongitude;
    }
}

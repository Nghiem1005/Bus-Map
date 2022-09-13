package hcmute.nhom13.detaicuoiki.models;

//Model sử dụng để chứa thông tin của trạm nào của tuyến nào
public class busStopModel {
    //Tuyến
    private routeModel route;
    //Trạm của tuyến đó
    private stationModel station;
    //Thứ tự của trạm trong tuyến
    private int ordinal;

    public busStopModel() {
    }

    //Tạo model busStopModel với các trường như dưới database để hứng dữ liệu
    public busStopModel(routeModel route, stationModel station, int ordinal) {
        this.route = route;
        this.station = station;
        this.ordinal = ordinal;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public routeModel getRoute() {
        return route;
    }

    public void setRoute(routeModel route) {
        this.route = route;
    }

    public stationModel getStation() {
        return station;
    }

    public void setStation(stationModel station) {
        this.station = station;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}

package hcmute.nhom13.detaicuoiki.models;

//Model chứa thông tin tuyến
public class routeModel {
    //Id tuyến
    private String routeId;
    //Trạm bắt đầu
    private stationModel stationStart;
    //Trạm kết thúc
    private stationModel stationEnd;
    //Giá của tuyến
    private int routePrice;
    //Loại tuyến
    private String routeType;
    //Thời gian hoạt động của tuyến xe
    private String routeOperationTime;
    //Thời gian hết 1 vòng tuyến
    private String routeCycleTime;
    //Đơn vị chịu trách nhiệm của tuyến
    private String routeUnit;
    //Thười gian giữa các xe
    private int routeRepeatTime;
    //Số chuyến trong ngày
    private int routePerDay;
    //Quãng đường của tuyến
    private int routeDistance;

    public routeModel() {
    }
    //Tạo model routeModel với các trường như dưới database để hứng dữ liệu
    public routeModel(String routeId, stationModel stationStart, stationModel stationEnd, int routePrice, String routeType, String routeOperationTime, String routeCycleTime, String routeUnit, int routeRepeatTime, int routePerDay, int routeDistance) {
        this.routeId = routeId;
        this.stationStart = stationStart;
        this.stationEnd = stationEnd;
        this.routePrice = routePrice;
        this.routeType = routeType;
        this.routeOperationTime = routeOperationTime;
        this.routeCycleTime = routeCycleTime;
        this.routeUnit = routeUnit;
        this.routeRepeatTime = routeRepeatTime;
        this.routePerDay = routePerDay;
        this.routeDistance = routeDistance;
    }

    //Khởi tạo hàm setter getter để lấy là nhận dữ liệu
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public stationModel getStationStart() {
        return stationStart;
    }

    public void setStationStart(stationModel stationStart) {
        this.stationStart = stationStart;
    }

    public stationModel getStationEnd() {
        return stationEnd;
    }

    public void setStationEnd(stationModel stationEnd) {
        this.stationEnd = stationEnd;
    }

    public int getRoutePrice() {
        return routePrice;
    }

    public void setRoutePrice(int routePrice) {
        this.routePrice = routePrice;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteOperationTime() {
        return routeOperationTime;
    }

    public void setRouteOperationTime(String routeOperationTime) {
        this.routeOperationTime = routeOperationTime;
    }

    public String getRouteCycleTime() {
        return routeCycleTime;
    }

    public void setRouteCycleTime(String routeCycleTime) {
        this.routeCycleTime = routeCycleTime;
    }

    public String getRouteUnit() {
        return routeUnit;
    }

    public void setRouteUnit(String routeUnit) {
        this.routeUnit = routeUnit;
    }

    public int getRouteRepeatTime() {
        return routeRepeatTime;
    }

    public void setRouteRepeatTime(int routeRepeatTime) {
        this.routeRepeatTime = routeRepeatTime;
    }

    public int getRoutePerDay() {
        return routePerDay;
    }

    public void setRoutePerDay(int routePerDay) {
        this.routePerDay = routePerDay;
    }

    public int getRouteDistance() {
        return routeDistance;
    }

    public void setRouteDistance(int routeDistance) {
        this.routeDistance = routeDistance;
    }
}

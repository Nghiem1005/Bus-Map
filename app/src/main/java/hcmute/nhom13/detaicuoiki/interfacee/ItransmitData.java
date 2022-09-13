package hcmute.nhom13.detaicuoiki.interfacee;

//Interface truyền dữ liệu
public interface ItransmitData {

    //Hàm truyền dữ liệu qua trang ShowInfoDestinationAndMapsActivity
    void passDataRouteData(String routeId);

    //Hàm truyền dữ liệu qua trang ProfileActivity
    void passDataToProfile();

    //Hàm truyền dữ liệu qua trang DirectionMapsActivity
    void passDataRouteDetail(String routeId1, String routeId2, String routeId3, int station1, int station2);
}

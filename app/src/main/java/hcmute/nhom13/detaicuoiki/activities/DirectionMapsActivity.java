package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.adapters.directionMapsDetailAdapter;
import hcmute.nhom13.detaicuoiki.adapters.directionMapsStationAdapter;
import hcmute.nhom13.detaicuoiki.daos.busStopDAO;
import hcmute.nhom13.detaicuoiki.daos.routeDAO;
import hcmute.nhom13.detaicuoiki.daos.stationDAO;
import hcmute.nhom13.detaicuoiki.models.busStopModel;


//Activity của trang thông tin chi tiết chuyến đi khi tìm kiếm bằng địa chỉ
public class DirectionMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Khởi tạo các giá trị cần dùng
    private GoogleMap mMap;
    ImageView userBtn, backRouteBtn;
    TextView txtDirectionRouteName;
    RadioGroup radioGroup;
    RadioButton showDirectionDetailRadio, showBusStationRadio, showMapRadio;
    ListView rcv_busListDirectionDetail, rcv_busListDetailStation;
    LinearLayout formDirectionDetail, formStationBus, formMapBus;

    String locationStart, locationEnd, numberBusRoute, userEmail;
    directionMapsDetailAdapter dDetailAdapter;
    directionMapsStationAdapter dStationAdapter;

    busStopDAO bDAO;
    routeDAO rDAO;
    stationDAO sDAO;

    String routeId1, routeId2, routeId3, stationStartData, getStationEndData;
    int stationBetween1, stationBetween2;
    busStopModel stationStart, stationEnd;
    ArrayList<busStopModel> busStopList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_maps);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Nhận dữ liệu userPhone từ các form khác trả về để lấy thông tin user
        Intent intent = this.getIntent();

        routeId1 = intent.getStringExtra("routeId1");
        routeId2 = intent.getStringExtra("routeId2");
        routeId3 = intent.getStringExtra("routeId3");
        userEmail = intent.getStringExtra("id");
        numberBusRoute = intent.getStringExtra("numberBusRoute");
        stationStartData = intent.getStringExtra("locationStart");
        getStationEndData = intent.getStringExtra("locationEnd");
        stationBetween1 = intent.getIntExtra("station1", -1);
        stationBetween2 = intent.getIntExtra("station2", -1);

        //Gán giá trị cho biến giao diện
        userBtn = findViewById(R.id.userBtn);
        backRouteBtn = findViewById(R.id.backRouteBtn);
        radioGroup = findViewById(R.id.radioGroup);
        txtDirectionRouteName = findViewById(R.id.txtDirectionRouteName);
        showDirectionDetailRadio = findViewById(R.id.showDirectionDetailRadio);
        showBusStationRadio = findViewById(R.id.showBusStationRadio);
        showMapRadio = findViewById(R.id.showMapRadio);
        rcv_busListDirectionDetail = findViewById(R.id.rcv_busListDirectionDetail);
        rcv_busListDetailStation = findViewById(R.id.rcv_busListDetailStation);
        formDirectionDetail = findViewById(R.id.formDirectionDetail);
        formStationBus = findViewById(R.id.formStationBus);
        formMapBus = findViewById(R.id.formMapBus);

        //Khởi tạo hàm DAO
        bDAO = new busStopDAO(this);
        rDAO = new routeDAO(this);
        sDAO = new stationDAO(this);

        //Bắt sự kiện khi ấn userBtn thì vào trang cá nhân
        userBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(DirectionMapsActivity.this, ProfileActivity.class);
            intent1.putExtra("id", userEmail);
            startActivity(intent1);
        });

        findStation();
        handleDirectionDetail();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDirectionBus);
        mapFragment.getMapAsync(this);

        //Bắt sự kiện lựa chọn option tìm kiếm hoặc tìm đường thì chuyển form tương ứng
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                //Khi lựa chọn chi tiết cách đi thì hiển thị giao diện chi tiết cách đi
                case R.id.showDirectionDetailRadio:
                    formDirectionDetail.setVisibility(View.VISIBLE);
                    formStationBus.setVisibility(View.INVISIBLE);
                    formMapBus.setVisibility(View.INVISIBLE);

                    handleDirectionDetail();
                    break;
                //Khi lựa chọn các trạm đi qua thì hiển thị giao diện các trạm đi qua
                case R.id.showBusStationRadio:
                    formDirectionDetail.setVisibility(View.INVISIBLE);
                    formStationBus.setVisibility(View.VISIBLE);
                    formMapBus.setVisibility(View.INVISIBLE);

                    handleStationDetail();
                    break;
                //Khi lựa chọn bản đồ thì hiển thị giao diện bản đồ
                case R.id.showMapRadio:
                    formDirectionDetail.setVisibility(View.INVISIBLE);
                    formStationBus.setVisibility(View.INVISIBLE);
                    formMapBus.setVisibility(View.VISIBLE);

                default:
                    break;
            }
        });

        //Khi ấn nút backRouteBtn thì quay lại trang chủ
        backRouteBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(DirectionMapsActivity.this, HomeActivity.class);
            intent2.putExtra("id", userEmail);
            intent2.putExtra("locationStart", locationStart);
            intent2.putExtra("locationEnd", locationEnd);
            intent2.putExtra("numberBusRoute", numberBusRoute);
            startActivity(intent2);
        });
    }

    //Hàm xử lí sự kiện khi thao tác với form chi tiết cách đi
    //Lấy list các trạm mà khách hàng đón xe
    public void handleDirectionDetail() {
        //Ở đây sử dụng directionMapsDetailAdapter cho listview chi tiết cách đi
        ArrayList<busStopModel> busStopModels;

        if(numberBusRoute.equals("1")){
            busStopModels = new ArrayList<>();
            //List chứa busStopModel của tuyến đi qua điểm đến và trạm đi
            busStopModels.add(stationStart);
        } else if(numberBusRoute.equals("2")){
            busStopModels = new ArrayList<>();
            //List chứa busStopModel của tuyến đi qua điểm đến và trạm đi
            busStopModels.add(stationStart);
            //List chứa busStopModel của tuyến tiếp theo khách cần chuyển tuyến và trạm đón xe
            busStopModels.add(bDAO.getBusByStationIdAndRoute(routeId2, Integer.valueOf(stationBetween1)));
        } else {
            busStopModels = new ArrayList<>();
            //List chứa busStopModel của tuyến đi qua điểm đến và trạm đi
            busStopModels.add(stationStart);
            //List chứa busStopModel của tuyến tiếp theo khách cần chuyển tuyến và trạm đón xe
            busStopModels.add(bDAO.getBusByOrderAndRoute(routeId3, Integer.valueOf(stationBetween1)));
            //List chứa busStopModel của tuyến tiếp theo khách cần chuyển tuyến và trạm đón xe
            busStopModels.add(bDAO.getBusByStationIdAndRoute(routeId2, bDAO.getBusByOrderAndRoute(routeId3, Integer.valueOf(stationBetween2)).getStation().getStationId()) );

        }

        dDetailAdapter = new directionMapsDetailAdapter(this, R.layout.detail_go_bus_item, busStopModels);
        rcv_busListDirectionDetail.setAdapter(dDetailAdapter);
    }

    //Hàm xử lí sự kiện khi thao tác với form các trạm đi qua
    public void handleStationDetail() {
        //Ở đây sử dụng directionMapsStationAdapter cho listview các trạm dừng
        dStationAdapter = new directionMapsStationAdapter(this, R.layout.station_item, busStopList);
        rcv_busListDetailStation.setAdapter(dStationAdapter);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        findStation();
        loadRouteOnMap(googleMap);
    }

    //Hiển thị tuyến đường đi lên map
    public void loadRouteOnMap(@NonNull GoogleMap googleMap){
        mMap = googleMap;

        //Biến chứa chuỗi các đường thẳng được kết nối từ các điểm
        PolylineOptions polylineOptions = new PolylineOptions();

        //Lặp qua các điểm dừng
        //Tạo địa điểm và thêm vào đường đi được vẽ ra
        for (busStopModel i : busStopList){
            //Tạo địa điểm
            LatLng station = new LatLng(i.getStation().getStationLatitude(), i.getStation().getStationLongitude());
            mMap.addMarker(new MarkerOptions().position(station).title("Marker in Station").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_station_50)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(station, 15));

            //Thêm địa điểm vừa tạo vào tuyến đường sẽ được vẽ
            polylineOptions.add(station);
        }

        //Vẽ đường đi trên map
        mMap.addPolyline(polylineOptions.width(14).color(Color.GREEN));

    }

    //Hàm tìm kiếm tất cả trạm của chuyến đi cần đi qua
    public void findStation(){
        busStopList = new ArrayList<>();

        //Tuyến và trạm bắt đầu
        stationStart = bDAO.getBusListByAddressAndRoute(routeId1, stationStartData);

        if(numberBusRoute.equals("1")){
            //Gán giá trị các tuyến xe buýt và render ra giao diện
            txtDirectionRouteName.setText("Đi tuyến số " + routeId1 + " metro");
            stationEnd = bDAO.getBusListByAddressAndRoute(routeId1, getStationEndData);
            int sort=0;

            //Kiểm tra 2 trạm bắt đầu và kết thúc trong tuyến, trạm nào có số thứ tự lớn hơn
            //Nếu trạm bắt đầu số thứ tự lớn hơn thì sort = 1, sắp xếp list các trạm theo thứ tự giảm dần và ngược lại
            if(stationStart.getOrdinal() > stationEnd.getOrdinal()){
                sort = 1;
            }
            //Lấy list các trạm từ điểm bắt đầu đến điểm kết thúc
            busStopList = bDAO.getBusListStartAndEndStationAndRoute(routeId1, stationStart.getOrdinal(), stationEnd.getOrdinal(), sort);
        } else if(numberBusRoute.equals("2")){
            //Gán giá trị các tuyến xe buýt và render ra giao diện
            txtDirectionRouteName.setText("Đi tuyến số "+ routeId1 + ", " + routeId2 + " metro");
            stationEnd = bDAO.getBusListByAddressAndRoute(routeId2, getStationEndData);
            //List chứa danh sách các trạm của tuyến thứ bắt đầu
            ArrayList<busStopModel> busStopModels1 = new ArrayList<>();
            //List chứa danh sách các trạm của tuyến thứ cuối cùng
            ArrayList<busStopModel> busStopModels2 = new ArrayList<>();

            //Kiểm tra 2 trạm trong tuyến, trạm nào có số thứ tự lớn hơn
            //Nếu trạm bắt đầu số thứ tự lớn hơn thì sort = 1, sắp xếp list các trạm theo thứ tự giảm dần và ngược lại

            //Kiểm tra số thứ tự giữa trạm bắt đầu và trạm trung gian
            if(stationStart.getOrdinal() < stationBetween1){
                busStopModels1 = bDAO.getBusListStartAndEndStationAndRoute(routeId1, stationStart.getOrdinal(), stationBetween1, 0);
            } else {
                busStopModels1 = bDAO.getBusListStartAndEndStationAndRoute(routeId1, stationBetween1, stationStart.getOrdinal(), 1);
            }

            //Kiểm tra số thứ tự giữa trạm kết thúc và trạm trung gian
            if(stationBetween1 < stationEnd.getOrdinal()){
                busStopModels2 = bDAO.getBusListStartAndEndStationAndRoute(routeId2, stationBetween1, stationEnd.getOrdinal(), 0);
            } else {
                busStopModels2 = bDAO.getBusListStartAndEndStationAndRoute(routeId2, stationEnd.getOrdinal(), stationBetween1, 1);
            }

            //Thêm tất cả các trạm vào cùng 1 list
            for(busStopModel i : busStopModels1){
                busStopList.add(i);
            }
            for(busStopModel i : busStopModels2){
                busStopList.add(i);
            }
        } else{
            //Gán giá trị các tuyến xe buýt và render ra giao diện
            txtDirectionRouteName.setText("Đi tuyến số "+ routeId1 + ", " + routeId3 + ", " + routeId2 + " metro");
            stationEnd = bDAO.getBusListByAddressAndRoute(routeId2, getStationEndData);

            //List chứa danh sách các trạm của tuyến thứ 1
            ArrayList<busStopModel> busStopModels1 = new ArrayList<>();
            //List chứa danh sách các trạm của tuyến kết thúc
            ArrayList<busStopModel> busStopModels2 = new ArrayList<>();
            //List chứa danh sách các trạm của tuyến trung gian
            ArrayList<busStopModel> busStopModels3 = new ArrayList<>();

            //Kiểm tra 2 trạm trong tuyến, trạm nào có số thứ tự lớn hơn
            //Nếu trạm bắt đầu số thứ tự lớn hơn thì sort = 1, sắp xếp list các trạm theo thứ tự giảm dần và ngược lại

            //Kiểm tra số thứ tự giữa trạm bắt đầu và trạm trung gian thứ nhất trong tuyến qua trạm bắt đầu
            if(stationStart.getOrdinal() < stationBetween1){
                busStopModels1 = bDAO.getBusListStartAndEndStationAndRoute(routeId1, stationStart.getOrdinal(), stationBetween1, 0);
            } else {
                busStopModels1 = bDAO.getBusListStartAndEndStationAndRoute(routeId1, stationBetween1, stationStart.getOrdinal(), 1);
            }

            //Kiểm tra số thứ tự giữa hai trạm trung gian trong tuyến trung gian
            if(stationBetween1 < stationBetween2){
                busStopModels3 = bDAO.getBusListStartAndEndStationAndRoute(routeId3, stationBetween1, stationBetween2, 0);
            } else {
                busStopModels3 = bDAO.getBusListStartAndEndStationAndRoute(routeId3, stationBetween2, stationBetween1, 1);
            }

            //Kiểm tra số thứ tự giửa trạm kết thúc và trạm trung gian thứ hai trong tuyến qua trạm kết thúc
            if(stationBetween2 < stationEnd.getOrdinal()){
                busStopModels2 = bDAO.getBusListStartAndEndStationAndRoute(routeId2, stationBetween2, stationEnd.getOrdinal(), 0);
            } else {
                busStopModels2 = bDAO.getBusListStartAndEndStationAndRoute(routeId2, stationEnd.getOrdinal(), stationBetween2, 1);
            }

            //Thêm tất cả các trạm vào cùng 1 list
            for(busStopModel i : busStopModels1){
                busStopList.add(i);
            }
            for(busStopModel i : busStopModels3){
                busStopList.add(i);
            }
            for(busStopModel i : busStopModels2){
                busStopList.add(i);
            }
        }
    }

}
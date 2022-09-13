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
import java.util.List;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.adapters.showDestinationAndMapsAdapter;
import hcmute.nhom13.detaicuoiki.daos.busStopDAO;
import hcmute.nhom13.detaicuoiki.daos.routeDAO;
import hcmute.nhom13.detaicuoiki.daos.stationDAO;
import hcmute.nhom13.detaicuoiki.models.busStopModel;
import hcmute.nhom13.detaicuoiki.models.routeModel;

//Activity trang thông tin chi tiết của từng tuyến xe buýt
public class ShowInfoDestinationAndMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Khởi tạo các giá trị cần dùng
    private GoogleMap mMap;
    ImageView backRouteBtn, userBtn;
    TextView txtRouteName, txtRouteTime;
    RadioGroup radioGroup;
    RadioButton showTimeRadio, showBusStationRadio, showMapRadio;
    ListView rcv_busListStation;
    LinearLayout formTimeBus, formStationBus, formMapBus;
    showDestinationAndMapsAdapter showAdapter;
    busStopDAO bDAO;
    routeDAO rDAO;
    stationDAO sDAO;
    ArrayList<busStopModel> busStopList;
    String routeId, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_and_map);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Nhận dữ liệu userPhone từ các form khác trả về để lấy thông tin user
        Intent intent = this.getIntent();
        routeId = intent.getStringExtra("routeId");
        userEmail = intent.getStringExtra("id");

        //Gán giá trị cho biến giao diện
        backRouteBtn = findViewById(R.id.backRouteBtn);
        userBtn = findViewById(R.id.userBtn);
        txtRouteName = findViewById(R.id.txtRouteName);
        txtRouteTime = findViewById(R.id.txtRouteTime);
        radioGroup = findViewById(R.id.radioGroup);
        showTimeRadio = findViewById(R.id.showTimeRadio);
        showBusStationRadio = findViewById(R.id.showBusStationRadio);
        showMapRadio = findViewById(R.id.showMapRadio);
        formTimeBus = findViewById(R.id.formTimeBus);
        formStationBus = findViewById(R.id.formStationBus);
        formMapBus = findViewById(R.id.formMapBus);
        rcv_busListStation = findViewById(R.id.rcv_busListStation);

        //Khởi tạo hàm DAO
        bDAO = new busStopDAO(this);
        rDAO = new routeDAO(this);
        sDAO = new stationDAO(this);

        //Gán text cho tiêu đề tuyến đi
        txtRouteName.setText("Tuyến số " + routeId + " metro");

        //Lấy tuyến theo id
        routeModel routeModel = rDAO.getRouteById(routeId);
        //Show thời gian tuyến
        txtRouteTime.setText(routeModel.getRouteOperationTime());

        //Bắt sự kiện khi ấn thay đổi các option xem giờ chạy, trạm nghỉ, bản đồ để hiển thị tương ứng
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    //Khi lựa chọn xem giờ thì hiển thị giờ tuyến đầu - tuyến cuối
                    case R.id.showTimeRadio:
                        formTimeBus.setVisibility(View.VISIBLE);
                        formStationBus.setVisibility(View.INVISIBLE);
                        formMapBus.setVisibility(View.INVISIBLE);

                        break;
                    //Khi lựa chọn xem trạm nghỉ thì hiển thị danh sách trạm nghỉ của xe bus đó
                    case R.id.showBusStationRadio:
                        formTimeBus.setVisibility(View.INVISIBLE);
                        formStationBus.setVisibility(View.VISIBLE);
                        formMapBus.setVisibility(View.INVISIBLE);
                        busStopList = new ArrayList<>();
                        busStopList = bDAO.getBusList(routeId);
                        showAdapter = new showDestinationAndMapsAdapter(ShowInfoDestinationAndMapsActivity.this,
                                R.layout.station_item,
                                busStopList);
                        rcv_busListStation.setAdapter(showAdapter);
                        break;
                    //Khi chọn bản đồ thì hiển trị quãng đường của xe bus đó
                    case R.id.showMapRadio:
                        formTimeBus.setVisibility(View.INVISIBLE);
                        formStationBus.setVisibility(View.INVISIBLE);
                        formMapBus.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });

        //Khi ấn nút backRouteBtn thì quay lại trang chủ
        backRouteBtn.setOnClickListener(view -> {
            Intent i = new Intent(ShowInfoDestinationAndMapsActivity.this, HomeActivity.class);
            i.putExtra("id", userEmail);
            startActivity(i);
        });

        //Bắt sự kiện khi ấn userBtn thì vào trang cá nhân
        userBtn.setOnClickListener(view -> {
            Intent i1 = new Intent(ShowInfoDestinationAndMapsActivity.this, ProfileActivity.class);
            i1.putExtra("id", userEmail);
            startActivity(i1);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        loadRouteOnMap(googleMap);
    }

    //Hiển thị tuyến đường đi lên map
    public void loadRouteOnMap(@NonNull GoogleMap googleMap){
        mMap = googleMap;

        //Biến chứa chuỗi các đường thẳng được kết nối từ các điểm
        PolylineOptions polylineOptions = new PolylineOptions();

        //Lấy danh sách các điểm dừng theo id của route được chọn
        List<busStopModel> busStopModelList = bDAO.getBusList(routeId);

        //Lặp qua các điểm dừng
        //Tạo địa điểm và thêm vào đường đi được vẽ ra
        for (busStopModel i : busStopModelList){
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
}
package hcmute.nhom13.detaicuoiki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hcmute.nhom13.detaicuoiki.R;
import hcmute.nhom13.detaicuoiki.adapters.homeAdapter;
import hcmute.nhom13.detaicuoiki.adapters.homeFindDirectionAdapter;
import hcmute.nhom13.detaicuoiki.daos.busStopDAO;
import hcmute.nhom13.detaicuoiki.daos.routeDAO;
import hcmute.nhom13.detaicuoiki.daos.stationDAO;
import hcmute.nhom13.detaicuoiki.interfacee.ItransmitData;
import hcmute.nhom13.detaicuoiki.models.busStopModel;
import hcmute.nhom13.detaicuoiki.models.routeModel;
import hcmute.nhom13.detaicuoiki.models.stationModel;

//Activity trang chủ
public class HomeActivity extends AppCompatActivity implements ItransmitData {

    //Khởi tạo các giá trị cần dùng
    RadioGroup radioGroup, radioBusRouteNumber;
    ImageView userBtn;
    EditText txtSearchBus;
    String userEmail;
    ListView rcv_busList, rcv_busListMentor;
    LinearLayout formSearchBus, formFindBus;
    homeAdapter hAdapter;
    homeFindDirectionAdapter homeFindDirectionAdapter;
    ArrayList<routeModel> routeList;
    ArrayList<busStopModel> busList;
    routeDAO rDAO;
    busStopDAO busDAO;

    //Khởi tạo các giá trị cần dùng
    ArrayList<String> station;
    ArrayList<stationModel> stationList;
    AutoCompleteTextView txtStationStart, txtStationEnd;
    ArrayAdapter<String> adapterItems;
    stationDAO sDAO;
    String busRouteNumber, stationStartData, getStationEndData;
    List<busStopModel> routeStartList, routeEndList;

    //List các bus stop
    ArrayList<busStopModel> route1, route2, routeTest;
    ArrayList<busStopModel> route3, route4;

    //Tuyến 1 là tuyến có đi qua trạm đi
    //Tuyến 2 là tuyến có đi qua trạm đến
    //Tuyến 3 là tuyến trung gian nối tuyến 1 và 2 khi đi 3 tuyến
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Ẩn thanh actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Nhận dữ liệu userPhone từ các form khác trả về để lấy thông tin user
        Intent i = this.getIntent();
        userEmail = i.getStringExtra("id");

        //Gán giá trị cho biến giao diện
        userBtn = findViewById(R.id.userBtn);
        txtSearchBus = findViewById(R.id.txtSearchBus);
        radioGroup = findViewById(R.id.radioGroup);
        rcv_busList = findViewById(R.id.rcv_busList);
        formSearchBus = findViewById(R.id.formSearchBus);
        formFindBus = findViewById(R.id.formFindBus);

        //Gán giá trị cho biến giao diện
        userBtn = findViewById(R.id.userBtn);
        radioGroup = findViewById(R.id.radioGroup);
        radioBusRouteNumber = findViewById(R.id.radioBusRouteNumber);
        txtStationStart = findViewById(R.id.txtStationStart);
        txtStationEnd = findViewById(R.id.txtStationEnd);
        rcv_busListMentor = findViewById(R.id.rcv_busListMentor);

        //Khởi tạo hàm DAO
        rDAO = new routeDAO(this);
        busDAO = new busStopDAO(this);
        routeList = new ArrayList<>();

        // Khởi tạo khi mới vô trang home sẽ hiện tất cả các chuyến xe bus
        if(String.valueOf(txtSearchBus.getText()).equals("") ) {
            routeList = rDAO.getRouteAll();

            // Truyền tham số cho homeAdapter tiến hành render
            hAdapter = new homeAdapter(HomeActivity.this, R.layout.bus_item, routeList, userEmail, this);
            rcv_busList.setAdapter(hAdapter);
        }

        //Bắt sự kiện khi ấn userBtn thì vào trang cá nhân
        userBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("id", userEmail);
            startActivity(intent);
        });
        handleFindBus();
        //Bắt sự kiện lựa chọn option tìm kiếm hoặc tìm đường thì chuyển form tương ứng
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                //Khi lựa chọn tìm kiếm thì hiển thị giao diện tìm kiếm
                case R.id.searchBusStationBtn:
                    formSearchBus.setVisibility(View.VISIBLE);
                    formFindBus.setVisibility(View.INVISIBLE);
                    handleFindBus();
                    break;
                //Khi lựa chọn tìm đường thì hiển thị giao diện tìm đường
                case R.id.findBusStationBtn:
                    formSearchBus.setVisibility(View.INVISIBLE);
                    formFindBus.setVisibility(View.VISIBLE);
                    handleFindDestination();
                    break;
                default:
                    break;
            }
        });
    }

    //Hàm xử lí sự kiện khi thao tác với form tìm kiếm
    public  void handleFindBus() {
        // Bắt sự kiện mỗi khi ô tìm kiếm thay đổi giá trị để truy vấn realtime
        txtSearchBus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Kiểm tra chuỗi tìm kiếm không rỗng thì truy vấn keo chuỗi
                if(!String.valueOf(txtSearchBus.getText()).equals("")) {
                    routeList.clear();
                    routeList = rDAO.getRouteSearch(String.valueOf(txtSearchBus.getText()));
                    //Chuỗi rỗng xuất toàn bộ chuyến xe bus
                } else {
                    routeList.clear();
                    routeList = rDAO.getRouteAll();
                }
                //Ở đây sử dụng homeAdapter cho list view tìm kiếm

                // Truyền tham số cho homeAdapter tiến hành render
                hAdapter = new homeAdapter(HomeActivity.this, R.layout.bus_item, routeList, userEmail,HomeActivity.this);
                rcv_busList.setAdapter(hAdapter);
            }
        });
    }

    //Hàm xử lí sự kiện khi thao tác với form tìm đường
    public void handleFindDestination() {

        //Khởi tạo hàm DAO và lấy dữ liệu địa chỉ của các tuyến xe bus cho người dùng lựa chọn
        sDAO = new stationDAO(this);
        stationList = sDAO.getStationAll();
        station = new ArrayList<>();
        for (stationModel stationModel : stationList) {
            station.add(stationModel.getStationAddress());
        }

        //Cài đặt adapter cho dropdown list dữ liệu địa chỉ
        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_item, station);
        txtStationStart.setAdapter(adapterItems);
        txtStationEnd.setAdapter(adapterItems);

        //Bắt sự kiện txtStationStart khi ấn thì lưu giá trị được chọn
        txtStationStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stationStartData = parent.getItemAtPosition(position).toString();
            }
        });

        //Bắt sự kiện txtStationEnd khi ấn thì lưu giá trị được chọn
        txtStationEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getStationEndData = parent.getItemAtPosition(position).toString();
            }
        });

        //Bắt sự kiện khi ấn radioBusRouteNumber thì lấy dữ liệu số thuyến khách hàng lựa chọn

        //Ở đây sử dụng homeFindDirectionAdapter cho list view tìm đường
        radioBusRouteNumber.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            busList = new ArrayList<>();
            ArrayList<busStopModel> busList1 = new ArrayList<>();
            ArrayList<busStopModel> busList2 = new ArrayList<>();
            ArrayList<busStopModel> busList3 = new ArrayList<>();
            ArrayList<busStopModel> busList4 = new ArrayList<>();

            switch (checkedId) {
                //Khi lựa chọn 1 tuyến thì lưu giá trị busRouteNumber = 1 và hiển thị kết quả hướng dẫn
                case R.id.oneBusRoute:
                    busRouteNumber = "1";
                    //Lấy danh sách các trạm 1 tuyến
                    busList1 = findRoute1(stationStartData, getStationEndData);

                    break;
                //Khi lựa chọn 1 tuyến thì lưu giá trị busRouteNumber = 1 và hiển thị kết quả hướng dẫn
                case R.id.twoBusRoute:
                    busRouteNumber = "2";
                    //Lấy danh sách các busStopModel 2 tuyến
                    findRoute2();
                    //List các busStopModel có thông tin của tuyến thứ 1 và trạm dừng trung gian
                    busList1 = route1;
                    System.out.println(busList1.size());
                    //List các busStopModel có thông tin của tuyến thứ 2 và trạm dừng trung gian
                    busList2 = route2;
                    System.out.println(route2.size());

                    break;
                //Khi lựa chọn 1 tuyến thì lưu giá trị busRouteNumber = 1 và hiển thị kết quả hướng dẫn
                case R.id.threeBusRoute:
                    busRouteNumber = "3";
                    //Lấy danh sách các busStopModel 3 tuyến
                    findRoute3();
                    //List các busStopModel có thông tin của tuyến thứ 1 và trạm dừng trung gian
                    busList1 = route1;
                    //List các busStopModel có thông tin của tuyến thứ 3 và trạm dừng trung gian
                    busList2 = route2;
                    //List các busStopModel có thông tin của tuyến thứ 2 và trạm dừng trung gian giữa tuyến thứ 1 và thứ 2
                    busList3 = route3;
                    //List các busStopModel có thông tin của tuyến thứ 2 và trạm dừng trung gian giữa tuyến thứ 3 và thứ 2
                    busList4 = route4;

                    break;
                default:
                    break;
            }

            //Tạo adapter hiển thị danh sách các tuyến xe buýt
            homeFindDirectionAdapter = new homeFindDirectionAdapter(HomeActivity.this, R.layout.mentor_bus_item, busList1, busList2, busList3, busList4, userEmail, HomeActivity.this, stationStartData, getStationEndData);
            rcv_busListMentor.setAdapter(homeFindDirectionAdapter);
        });
    }

    //Truyền dữ liệu từ con ra cha thông qua 1 interface
    //Truyền dữ liệu đến ShowInfoDestinationAndMapsActivity
    @Override
    public void passDataRouteData(String routeId) {
        Intent i1 = new Intent(this, ShowInfoDestinationAndMapsActivity.class);
        i1.putExtra("routeId", routeId);
        i1.putExtra("id", userEmail);
        startActivity(i1);
    }

    //Truyền dữ liệu từ con ra cha thông qua 1 interface
    @Override
    public void passDataToProfile() {

    }

    //Truyền dữ liệu từ con ra cha thông qua 1 interface
    //Truyền dữ liệu đến DirectionMapsActivity
    //Dữ liệu bao gầm id tuyến bắt đầu, kết thúc
    //Và tuyến trung gian, và thứ tự của của 2 trạm trung gian trong tuyến trung gian (nếu có)
    //Những giá trị không có thì được gán bằng -1
    @Override
    public void passDataRouteDetail(String routeId1, String routeId2, String routeId3, int station1, int station2) {
        Intent i1 = new Intent(this, DirectionMapsActivity.class);
        //Id tuyến bắt đầu
        i1.putExtra("routeId1", routeId1);
        //Id tuyến kết thúc
        i1.putExtra("routeId2", routeId2);
        //Id tuyến trung gian
        i1.putExtra("routeId3", routeId3);
        //Email người dùng
        i1.putExtra("id", userEmail);
        //Địa chỉ trạm bắt đầu
        i1.putExtra("locationStart", stationStartData);
        //Địa chỉ trạm kết thúc
        i1.putExtra("locationEnd", getStationEndData);
        //Số tuyến được chọn
        i1.putExtra("numberBusRoute", busRouteNumber);
        //Thứ tự của trạm trung gian thứ nhất trong tuyến trung gian
        i1.putExtra("station1", station1);
        //Thứ tự của trạm trung gian thứ hai trong tuyến trung gian
        i1.putExtra("station2", station2);
        startActivity(i1);
    }

    //Hàm tìm các đường đi gồm 1 tuyến
    public ArrayList<busStopModel> findRoute1(String stationStart, String stationEnd){

        //Lấy list các busStopModel có chứa điểm bắt đầu
        List<busStopModel> routeStarts = busDAO.getBusListByAddress(stationStart);
        //Lấy list các busStopModel có chứa điểm kết thúc
        List<busStopModel> routeEnds = busDAO.getBusListByAddress(stationEnd);

        ArrayList<busStopModel> listRoute = new ArrayList<>();

        //Lặp qua list các busStopModel
        //Kiểm tra các busStopModel nào có tuyến giống nhau
        //Lấy busStopModel có thông tin của tuyến tìm được đi qua điểm đi và điểm đến

        //Lặp qua các busStopModel của có tuyến khác nhau và cùng một trạm là điểm đi
        for (busStopModel i : routeStarts){
            //Lặp qua các busStopModel của có tuyến khác nhau và cùng một trạm là điểm đến
            for (busStopModel j : routeEnds){
                //Kiểm tra các trạm giốn nhau
                if(i.getRoute().getRouteId().equals(j.getRoute().getRouteId())){
                    //Thêm vào list các busStopModel có thông tin của tuyến mà đi qua điểm đi và đến
                    listRoute.add(j);

                }
            }
        }
        return listRoute;
    }

    //Hàm tìm các đường đi gồm 2 tuyến
    public void findRoute2(){

        //List busStopModel có các tuyến có điểm đi
        List<busStopModel> routeStartList = busDAO.getBusListByAddress(stationStartData);
        //List busStopModel có các tuyến có điểm đi
        List<busStopModel> routeEndList = busDAO.getBusListByAddress(getStationEndData);

        route1 = new ArrayList<>();
        route2 = new ArrayList<>();


        for (busStopModel i : routeStartList){
            //Tạo list các busStopModel có cùng một tuyến có chứa điểm đi nhưng khác trạm
            List<busStopModel> busStopModelList1 = busDAO.getBusList(i.getRoute().getRouteId());
            for (busStopModel j : routeEndList){
                //Tạo list các busStopModel có cùng một tuyến có chứa điểm đến nhưng khác trạm
                List<busStopModel> busStopModelList2 = busDAO.getBusList(j.getRoute().getRouteId());
                //Kiểm tra các 2 tuyến đang lặp có bị trùng không
                if(!i.getRoute().getRouteId().equals(j.getRoute().getRouteId())){
                    //Thực hiện thêm các busStopModel có thông tin tuyến và điểm trung gian
                    //Truyền vào list các trạm của tuyến có chứa điểm đi và list các trạm của tuyến có chứa điểm đến
                    addBusStop2(busStopModelList1, busStopModelList2);
                }
            }
        }
    }

    //Hàm thực hiện thêm các busStopModel có thông tin tuyến và điểm trung gian khi tìm đường đi có 2 tuyến
    public void addBusStop2(List<busStopModel> busStopModelList1, List<busStopModel> busStopModelList2){
        //Cờ kiểm tra khi nào thì thoát vòng lặp
        int flag = 0;

        for (busStopModel i : busStopModelList1){
            for (busStopModel j : busStopModelList2){
                //Kiểm tra 2 trạm có phải là cùng 1 trạm không. Nếu cùng là 1 trạm thì thực hiện bên trong
                if(i.getStation().getStationId() == j.getStation().getStationId()){
                    //Thêm busStopModel có trạm trung gian và tuyến của tuyến thứ 1 trong 2 tuyến
                    route1.add(i);
                    //Thêm busStopModel có trạm trung gian và tuyến của tuyến thứ 2 trong 2 tuyến
                    route2.add(j);
                    flag = 1;
                }
            }
            //Thực hiện thoát vòng lặp sau khi tìm được trạm trung gian
            if(flag == 1){
                break;
            }
        }
    }

    public void findRoute3(){

        //List busStopModel có các tuyến có điểm đi
        List<busStopModel> routeStartList = busDAO.getBusListByAddress(stationStartData);
        //List busStopModel có các tuyến có điểm đi
        List<busStopModel> routeEndList = busDAO.getBusListByAddress(getStationEndData);

        route1 = new ArrayList<>();
        route2 = new ArrayList<>();
        route3 = new ArrayList<>();
        route4 = new ArrayList<>();

        List<busStopModel> busStopModelList1 = new ArrayList<>();
        List<busStopModel> busStopModelList2 = new ArrayList<>();

        for (busStopModel i : routeStartList){
            //Tạo list các busStopModel có cùng một tuyến có chứa điểm đi nhưng khác trạm
            List<busStopModel> busStopModelList = busDAO.getBusList(i.getRoute().getRouteId());

            for (busStopModel j : busStopModelList){
                //List các busStopModel chứa thông tin tuyến qua điểm đi và các trạm của tuyến đó
                busStopModelList1.add(j);
            }
        }
        for (busStopModel j : routeEndList){

            //Tạo list các busStopModel có cùng một tuyến có chứa điểm đến nhưng khác trạm
            List<busStopModel> busStopModelList = busDAO.getBusList(j.getRoute().getRouteId());
            for (busStopModel k : busStopModelList){
                //List các busStopModel chứa thông tin tuyến qua điểm đến và các trạm của tuyến đó
                busStopModelList2.add(k);
            }
        }
        addBusStop3(busStopModelList1, busStopModelList2);
    }

    //Hàm tìm kiếm và lưu các tuyến và trạm của đường đi gồm 3 tuyến
    public void addBusStop3(List<busStopModel> busStopModelList1, List<busStopModel> busStopModelList2) {

        //Tạo list chứa id của các tuyến đã được kiểm tra trong list các tuyến qua điểm đi
        List<String> routeID1 = new ArrayList<>();
        routeID1.add("-1");
        //Cờ lưu vị trí của tuyến vừa được thêm vào list chứa id của các tuyến đã được kiểm tra trong list các tuyến qua điểm đi
        int flag1 = 0;

        for (busStopModel k : busStopModelList1) {

            //Cờ lưu vị trí của tuyến vừa được thêm vào list chứa id của các tuyến đã được kiểm tra trong list các tuyến qua điểm đến
            int flag2 = 0;

            //Kiểm tra tuyến đó đã được kiểm tra chưa
            if (!k.getRoute().getRouteId().equals(routeID1.get(flag1))) {

                //Tạo list chứa id của các tuyến đã được kiểm tra trong list các tuyến qua điểm đến
                List<String> routeID2 = new ArrayList<>();
                routeID2.add("-1");

                for (busStopModel l : busStopModelList2) {

                    //Kiểm tra 2 trạm có phải là cùng 1 trạm không và tuyến đó đã được kiểm tra chưa và hai tuyến qua điểm đi và điểm đến đang kiểm tra có phải cùng 1 tuyến không.
                    if (k.getStation().getStationId() != l.getStation().getStationId() && !l.getRoute().getRouteId().equals(routeID2.get(flag2)) && !k.getRoute().getRouteId().equals(l.getRoute().getRouteId())) {

                        //Thực hiện việc tìm những station nào của 2 tuyến chứa điểm đi và đến có cùng nằm trong 1 tuyến thứ 3
                        //Nếu trùng thì thêm vào list các busStopModel có thông tin của tuyến trung gian thứ 2 và trạm trung gian giữa tuyến thứ 3 và thứ 2
                        //Và trả về list các busStopModel có thông tin của tuyến trung gian thứ 2 và trạm trung gian giữa tuyến thứ 3 và thứ 1
                        ArrayList<busStopModel> busList = findRoute1(k.getStation().getStationAddress(), l.getStation().getStationAddress());
                        //busList = findRoute1(k.getStation().getStationAddress(), l.getStation().getStationAddress());
                        if (busList.size() != 0) {
                            for (int m = 0; m < busList.size(); m++) {
                                //Kiểm tra tuyến thứ 3 tìm được có bị trùng với tuyến 1 và 2 không.
                                if (!k.getRoute().getRouteId().equals(busList.get(m).getRoute().getRouteId()) && !l.getRoute().getRouteId().equals(busList.get(m).getRoute().getRouteId())) {
                                    int flag3 = 0;
                                    //Kiểm tra tuyến vừa tìm được đã có chưa
                                    for (busStopModel i : route3) {
                                        if (i.getRoute().getRouteId().equals(busList.get(m).getRoute().getRouteId())) {
                                            flag3 = 1;
                                            break;
                                        }
                                    }
                                    if (flag3 == 1) {
                                        break;
                                    }
                                    //Thêm busStopModel có thông tin tuyến thứ 3 trạm trung gian giữa tuyến 2 và 3
                                    route3.add(busList.get(m));
                                    //Thêm busStopModel có thông tin tuyến thứ 1 trạm trung gian giữa tuyến 1 và 3
                                    route1.add(k);
                                    //Thêm busStopModel có thông tin tuyến thứ 2 trạm trung gian giữa tuyến 2 và 3
                                    route2.add(l);
                                    //Thêm tuyến vừa tìm được vào danh sách các tuyến đã kiểm tra
                                    routeID2.add(l.getRoute().getRouteId());
                                    routeID1.add(k.getRoute().getRouteId());
                                    flag2++;
                                    flag1++;
                                    break;

                                }

                            }
                        }

                    }
                }


            }

        }
    }
}

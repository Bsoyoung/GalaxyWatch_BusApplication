package com.wheremybus.galaxywatch_busapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class ListBusItemAdapter extends BaseAdapter {

    public static String tagAdapter = "tagAdapter";
    ArrayList<ListBusItem> items = new ArrayList<ListBusItem>();
    Context context;

    TextView txt_busID ;
    TextView txt_busStation;
    TextView txt_firstBus;
    TextView txt_secondBus;
    RequestQueue requestQueue;

    public ListBusItemAdapter(){
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListBusItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        final ListBusItem listBusItem = items.get(position);
        final int pos = position;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_busitem,parent,false);

        }
        txt_busID = convertView.findViewById(R.id.busID);
        txt_firstBus = convertView.findViewById(R.id.firstBus);
        txt_secondBus = convertView.findViewById(R.id.secondBus);
        txt_busStation = convertView.findViewById(R.id.busStation);

        txt_busID.setText(listBusItem.getBusID());
        txt_busStation.setText(listBusItem.getStationName());
        txt_firstBus.setText(listBusItem.getFirstBus());
        txt_secondBus.setText(listBusItem.getSecondBus());

        // 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBusItem busItem = getItem(pos);
                BusArriveTask(busItem.getStationID(),busItem.getRouteID(),busItem);

                Log.d("mainTag", "getStationID :"+busItem.getStationID());
                Log.d("mainTag", "getRouteID :"+busItem.getRouteID());
/*                if(pos ==0){
                    Log.d("mainTag", "pos :"+pos);
                    //BusArriveTask("210000285","234001237","29",busItem);
                    BusArriveTask("210000285","234001237",busItem);
                }*/
            }

        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnMyItemCheckedChanged != null)
                    mOnMyItemCheckedChanged.onItemCheckedChanged (getItem(pos), pos);
                return true;
            }
        });
        return convertView;
    }


    private void BusArriveTask(String stationId, final String routeID, final ListBusItem listBusitem){
        requestQueue = Volley.newRequestQueue(context);
        String url = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList?serviceKey="+"mnQdrkY5SwsM7qjbButDVmNq47d6ACbXemDTg3m1R6PnCiSYSNACrpA0c4E5ucqNqy5tFOsGO%2FKDsQkPdGFIbw%3D%3D"+
                "&stationId="+stationId+"";
        Log.d(tagAdapter, "URL:"+url);
        StringRequest request= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        XMLtoJSONData(response,routeID,listBusitem);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);
    }

    private void XMLtoJSONData(String xml,String routID,ListBusItem listBusitem){
        // https://androidfreetutorial.wordpress.com/2016/11/28/how-to-convert-xml-to-json-for-android/        \
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        // convert to a JSONObject       
        JSONObject jsonObject = xmlToJson.toJson();
        Log.d(tagAdapter, "jsonObject:"+jsonObject);
        // JSON 에서 배열은 [] 대괄호 사용, Objext 는 {} 중괄호 사용       
        try {
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject msgHeader = response.getJSONObject("msgHeader");
            String resultCode = msgHeader.optString("resultCode");
            Log.d(tagAdapter, "String resultCode :"+resultCode);

            if(resultCode.equals("0")){
                JSONObject msgBody = response.getJSONObject("msgBody");
                Log.d(tagAdapter, "jsonObject msgBody :"+msgBody);
                JSONArray array = msgBody.getJSONArray("busArrivalList");
                for(int i=0; i < array.length();i++){
                    JSONObject obj = array.getJSONObject(i);
// optString which returns the value mapped by name if it exists    
                    String locationNo1 =obj.optString("locationNo1"); // 첫번째 차량 번호
                    String locationNo2 =obj.optString("locationNo2"); // 두번째 차량 위치 정보            
                    String predictTime1 =obj.optString("predictTime1"); // 첫번째 차량 번호
                    String predictTime2 =obj.optString("predictTime2"); // 두번째 차량 위치 정보            
                    String remainSeat1 =obj.optString("remainSeatCnt1"); // 두번째 차량 번호                   
                    String remainSeat2 =obj.optString("remainSeatCnt2"); // 두번째 차량 위치 정보               
                    String routeId =obj.optString("routeId"); // 두번째 차량 위치 정보                   
                    Log.d(tagAdapter, "jString locationNo1 :"+locationNo1);
                    Log.d(tagAdapter, "jString locationNo2 :"+locationNo2);
                    Log.d(tagAdapter, "jString predictTime1 :"+predictTime1);
                    Log.d(tagAdapter, "jString predictTime1 :"+predictTime2);
                    Log.d(tagAdapter, "jString remainSeat1 :"+remainSeat1);
                    Log.d(tagAdapter, "jString remainSeat2 :"+remainSeat2);

                    if(routeId.equals(routeId)){
                        listBusitem.setFirstBus(locationNo1 + "번째 전 / "+predictTime1+"분 전 / "+ remainSeat1 + "석");
                        listBusitem.setSecondBus(locationNo2 + "번째 전 / "+predictTime2+"분 전 / "+ remainSeat2 + "석");
                    }

                }
                refreshTxt(listBusitem);
            }
            else if(resultCode.equals("1")){
                Toast.makeText(context, "시스템 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("4")){
                listBusitem.setFirstBus("결과가 존재하지 않습니다");
                listBusitem.setSecondBus("결과가 존재하지 않습니다");
                refreshTxt(listBusitem);
                Toast.makeText(context, "결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("8")){
                Toast.makeText(context, "요청 제한을 초과하였습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("23")){
                Toast.makeText(context, "버스 도착 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void BusArriveTask(String stationId, String routerId, String staOrder, final ListBusItem listBusitem){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalItem?serviceKey="+"mnQdrkY5SwsM7qjbButDVmNq47d6ACbXemDTg3m1R6PnCiSYSNACrpA0c4E5ucqNqy5tFOsGO%2FKDsQkPdGFIbw%3D%3D"+
                "&stationId="+stationId+"&routeId="+routerId+"&staOrder="+staOrder+"";
        Log.d(tagAdapter, "URL:"+url);
        StringRequest request= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        XMLtoJSONData(response,listBusitem);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);
    }

    private void XMLtoJSONData(String xml,ListBusItem listBusitem){
        // https://androidfreetutorial.wordpress.com/2016/11/28/how-to-convert-xml-to-json-for-android/        \
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        // convert to a JSONObject       
        JSONObject jsonObject = xmlToJson.toJson();
        Log.d(tagAdapter, "jsonObject:"+jsonObject);
        String strResult = "";
        // JSON 에서 배열은 [] 대괄호 사용, Objext 는 {} 중괄호 사용       
        try {
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject msgHeader = response.getJSONObject("msgHeader");
            String resultCode = msgHeader.optString("resultCode");
            Log.d(tagAdapter, "String resultCode :"+resultCode);
            if(resultCode.equals("0")){
                JSONObject msgBody = response.getJSONObject("msgBody");
                JSONObject msgInfo = msgBody.getJSONObject("busArrivalItem");
                Log.d(tagAdapter, "jsonObject msgBody :"+msgBody);
                // optString which returns the value mapped by name if it exists
                String locationNo1 =msgInfo.getString("locationNo1"); // 첫번째 차량 번호
                String locationNo2 =msgInfo.optString("locationNo2"); // 두번째 차량 위치 정보            
                String predictTime1 =msgInfo.getString("predictTime1"); // 첫번째 차량 번호
                String predictTime2 =msgInfo.optString("predictTime2"); // 두번째 차량 위치 정보            
                String remainSeat1 =msgInfo.optString("remainSeatCnt1"); // 두번째 차량 번호                   
                String remainSeat2 =msgInfo.optString("remainSeatCnt2"); // 두번째 차량 위치 정보                          
                Log.d(tagAdapter, "jString locationNo1 :"+locationNo1);
                Log.d(tagAdapter, "jString locationNo2 :"+locationNo2);
                Log.d(tagAdapter, "jString predictTime1 :"+predictTime1);
                Log.d(tagAdapter, "jString predictTime1 :"+predictTime2);
                Log.d(tagAdapter, "jString remainSeat1 :"+remainSeat1);
                Log.d(tagAdapter, "jString remainSeat2 :"+remainSeat2);
                listBusitem.setFirstBus(locationNo1 + "번째 전 / "+predictTime1+"분 전 / "+ remainSeat1 + "석");
                listBusitem.setSecondBus(locationNo2 + "번째 전 / "+predictTime2+"분 전 / "+ remainSeat2 + "석");
                refreshTxt(listBusitem);
            }else{
                switch(resultCode){
                    case "1":strResult = "시스템 에러가 발생하였습니다.";break;
                    case "2":strResult = "필수 요청 Parameter 가 존재하지 않습니다..";break;
                    case "3":strResult = "필수 요청 Parameter 가 잘못되었습니다";break;
                    case "4":strResult = "결과가 존재하지 않습니다.";break;
                    case "5":strResult = "필수 요청 Parameter(인증키) 가 존재하지 않습니다.";break;
                    case "6":strResult = "등록되지 않은 키입니다.";break;
                    case "7":strResult = "사용할 수 없는(등록은 되었으나, 일시적으로 사용 중지된) 키입니다.";break;
                    case "8":strResult = "요청 제한을 초과하였습니다.";break;
                    case "20":strResult = "잘못된 위치로 요청하였습니다. 위경도 좌표값이 정확한지 확인하십시오.";break;
                    case "21":strResult = "노선번호는 1자리 이상 입력하세요.";break;
                    case "22":strResult = "정류소명/번호는 1자리 이상 입력하세요.";break;
                    case "23":strResult = "버스 도착 정보가 존재하지 않습니다.";break;
                    case "31":strResult = "존재하지 않는 출발 정류소 아이디(ID)/번호입니다.";break;
                    case "32":strResult = "존재하지 않는 도착 정류소 아이디(ID)/번호입니다.";break;
                    case "99":strResult = "API 서비스 준비중입니다.";break;

                }
                if(!strResult.isEmpty()){
                    listBusitem.setFirstBus(strResult);
                    listBusitem.setSecondBus(strResult);
                    refreshTxt(listBusitem);
                    Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
                }
            }
            /*else if(resultCode.equals("1")){
                listBusitem.setFirstBus("시스템 에러가 발생하였습니다");
                listBusitem.setSecondBus("시스템 에러가 발생하였습니다");
                refreshTxt(listBusitem);
                Toast.makeText(context, "시스템 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("4")){
                listBusitem.setFirstBus("결과가 존재하지 않습니다");
                listBusitem.setSecondBus("결과가 존재하지 않습니다");
                refreshTxt(listBusitem);
                Toast.makeText(context, "결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("8")){
                listBusitem.setFirstBus("요청 제한을 초과하였습니다");
                listBusitem.setSecondBus("요청 제한을 초과하였습니다");
                refreshTxt(listBusitem);
                Toast.makeText(context, "요청 제한을 초과하였습니다", Toast.LENGTH_SHORT).show();}
            else if(resultCode.equals("23")){
                listBusitem.setFirstBus("버스 도착 정보가 존재하지 않습니다");
                listBusitem.setSecondBus("버스 도착 정보가 존재하지 않습니다");
                refreshTxt(listBusitem);
                Toast.makeText(context, "버스 도착 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshTxt(ListBusItem busItem){

        txt_busID.setText(busItem.getBusID());
        txt_firstBus.setText(busItem.getFirstBus());
        txt_secondBus.setText(busItem.getSecondBus());

        notifyDataSetChanged();
    }

    public void setListView(String temp){
        String[] str = temp.split("/");

        //Log.d(tagAdapter,"busNUM : "+ busNum+"/ busID : " + busID + "/ stationNum : " + stationNum);
        String busNum = "";
        String stationNum = "";
        String busID = "";
        String stationName = "";
        for(int i = 0; i < str.length/4;i++){
            busNum = str[4*i+0];
            stationName = str[4*i+1];
            busID = str[4*i+2];
            stationNum = str[4*i+3];

            Log.d(tagAdapter,"busNUM : "+ busNum+"/stationName : " + stationName+ "/ busID : " + busID + "/ stationNum : " + stationNum);

            boolean b_check = true;
            Log.d(tagAdapter,""+items.size());
            for(int j=0;j<items.size();j++)
                if(items.get(j).getBusID().equals(busNum) && items.get(j).getStationName().equals(stationName))
                    b_check = false;

            ListBusItem tempBusItem = new ListBusItem(busNum,stationName,busID,stationNum);
            if(b_check)
                addItem(tempBusItem);
        }

    }

    public void addItem(ListBusItem busItem)
    {
        items.add(busItem);
    }

    public void removeItem(int pos)
    {
        Log.d(tagAdapter,"remove: " + pos);
        items.remove(pos);
    }

    public interface OnMyItemCheckedChanged {
        public void onItemCheckedChanged(ListBusItem item, int pos);
    }
    private OnMyItemCheckedChanged mOnMyItemCheckedChanged;

    public void setOnMyItemCheckedChanged(OnMyItemCheckedChanged  onMyItemCheckedChanged){
        this.mOnMyItemCheckedChanged = onMyItemCheckedChanged;
    }


}

package com.lulu.weichatshake.door;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Request {
	
	private static final String SERVER_URL = "https://www.doormaster.me:9099/doormaster/connection";
	
	private static final String REQ_DEV_URL = "https://www.doormaster.me:9099/doormaster/users/userinfo/rid";
	
	//请求登录
	public static JSONObject login(String username, String pwd) throws JSONException
	{
		final JSONObject login_json = new JSONObject();
		login_json.put("username", username);
		login_json.put("password", pwd);
		
		JSONObject json_ret = DemoHttps.connectPost(SERVER_URL, login_json);
		return json_ret;
	}
	
	//请求所有的设备信息
	public static ArrayList<DeviceBean> reqDeviceList(String client_id) throws JSONException
	{
		String param = "client_id=" +client_id;
		JSONObject req_dev_ret = DemoHttps.connectGet( REQ_DEV_URL, param);
		if (req_dev_ret == null || req_dev_ret.isNull("ret"))
		{
			return null;
		}
		int ret = req_dev_ret.getInt("ret");		
		Log.e("Doormaster sdk", "reqDeviceList error [ " + req_dev_ret.toString() + " ]");
		
		if (ret == 0 ) 
		{
			JSONArray data = req_dev_ret.getJSONArray("data");
			if ( data!=null )
			{
				return resolveData(data); 
			}
			else 
			{
				Log.e("Doormaster sdk", "reqDeviceList  data null ");
				return null;		//data is null
			}
		}
		else 
		{
			return null;
		}
		
	}
	
	//解析服务器 请求的设备列表数据
	private static ArrayList<DeviceBean> resolveData(JSONArray data) throws JSONException
	{
		ArrayList<DeviceBean> deviceList = new ArrayList<DeviceBean>();
		for (int i = 0;i <data.length();i++) 
		{
			JSONArray readerArray = data.getJSONObject(i).getJSONArray("reader");
			for(int j = 0;j<readerArray.length();j++)
			{
				JSONObject readerObj = readerArray.getJSONObject(j);
				if (readerObj.isNull("reader_sn") 
						|| readerObj.isNull("reader_mac") 
						|| readerObj.isNull("dev_type")
						|| readerObj.isNull("privilege") 
						|| readerObj.isNull("verified")
						|| readerObj.isNull("open_type")
						|| readerObj.isNull("ekey")) 
				{
					continue;
				}
				
				int privilege = readerObj.getInt("privilege");
				String reader_sn = readerObj.getString("reader_sn");
				String dev_mac = readerObj.getString("reader_mac");
				String ekey = readerObj.getString("ekey");
				int dev_type = readerObj.getInt("dev_type");
				//设备操作/有效时间/距离/操作方式
				String start_date = readerObj.getString("start_date");
				String end_date = readerObj.getString("end_date");
				int use_count =  readerObj.getInt("use_count");
				int verified = readerObj.getInt("verified");
				int open_type = readerObj.getInt("open_type");
		
				DeviceBean device = new DeviceBean();
				device.setDevSn(reader_sn)  ;
				device.setDevMac( dev_mac.toUpperCase());
				device.setDevType(dev_type);
				device.seteKey( ekey) ;
				device.setPrivilege(privilege);
				device.setOpenType(open_type);
				device.setVerified(verified);
				device.setStartDate(start_date);
				device.setEndDate(end_date);
				device.setUseCount(use_count);
				deviceList.add(device);
			}
		}
		
		return deviceList;
	}
}
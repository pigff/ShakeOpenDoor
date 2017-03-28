package com.lulu.weichatshake.door;

import java.io.Serializable;

public class DeviceBean implements Serializable {

    /*设备类型
     (1, (u'门禁读头')),
    (2, (u'门禁一体机')),
    (3, (u'梯控读头(离线)')),
    (4, (u'无线锁')),
    (5, (u'蓝牙遥控模块')),
    (6, (u'门禁控制器')),
    (7, (u'触摸开关门禁')),
    (8, (u'可视对讲')),
    (9, (u'二维码设备')),
    */

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private static final String DEVSN = "0350335203";
    private static final String DEV_MAC = "33:34:14:E1:B0:E3";

	private static final  int DEV_TYPE = 8;
	private static final  int PRIVILEGE = 1;
	private static final  int OPEN_TYPE = 3;
	private static final  int VERIFIED = 1;
	private static final  int USE_COUNT = 0;
	private static final  int ENCRYTION = 0;
	private static final  String START_DATE = "";
	private static final  String END_DATE = "";
	private static final  String E_KEY = "b46f591f31c33d7355561c4323cfa149000000000000000000000000000000001000";


    private String devMac = null;
    private String devSn = null;
    private int devType;
	private int privilege;
	private int openType;
	private int verified;
	private String startDate = null;
	private String endDate = null;
	private int useCount;
	private String eKey = null;
	private int encrytion = 0x00;
	
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	
	public String getDevMac() {
		return devMac;
	}
	public void setDevMac(String devMac) {
		this.devMac = devMac;
	}
	
	public int getDevType() {
		return devType;
	}
	public void setDevType(int devType) {
		this.devType = devType;
	}
	
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	
	public int getVerified() {
		return verified;
	}
	public void setVerified(int verified) {
		this.verified = verified;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	
	public String geteKey() {
		return eKey;
	}
	public void seteKey(String eKey) {
		this.eKey = eKey;
	}
	
	public int getEncrytion() {
		return encrytion;
	}
	public void setEncrytion(int encrytion) {
		this.encrytion = encrytion;
	}

    public static DeviceBean getDefault() {
        DeviceBean bean = new DeviceBean();
        bean.setDevSn(DEVSN);
        bean.setDevMac(DEV_MAC);
        bean.setDevType(DEV_TYPE);
        bean.setPrivilege(PRIVILEGE);
        bean.setOpenType(OPEN_TYPE);
        bean.setVerified(VERIFIED);
        bean.setStartDate(START_DATE);
        bean.setEndDate(END_DATE);
        bean.setUseCount(USE_COUNT);
        bean.seteKey(E_KEY);
        bean.setEncrytion(ENCRYTION);
        return bean;
    }

	@Override
	public String toString() {
		return "DeviceBean{" +
				"devSn='" + devSn + '\'' +
				", devMac='" + devMac + '\'' +
				", devType=" + devType +
				", privilege=" + privilege +
				", openType=" + openType +
				", verified=" + verified +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", useCount=" + useCount +
				", eKey='" + eKey + '\'' +
				", encrytion=" + encrytion +
				'}';
	}
}

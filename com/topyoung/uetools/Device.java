package com.topyoung.uetools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * 属性：UE对应的设备
 * 对应通道支持的UE数和CELL数
 * 功能：判断设备版本号
 * 通过版本号判断设备可支持的UE数和CELL数
 * 判断设备类型，支持的通道数
 * 
 */
public class Device {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private BufferedInputStream bufin;
	private BufferedOutputStream bufout;
	public static String deviceType = "";// 设备类型，用来匹配设备
	private int count_UE;// 支持的UE数
	private int count_CELL;// 支持的CELL数

	/**
	 * 读取IDN
	 * 
	 * @return IDN信息
	 * @throws IOException
	 */
	public String readIDN() throws IOException {

		return readMessage("*IDN?\r\n");
	}

	/**
	 * 打开与设备的连接
	 * 
	 * @param strIP
	 *            （设备IP）
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void open(String strIP) throws UnknownHostException, IOException {
		socket = new Socket(strIP, 1000);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}

	/**
	 * 读取信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readMessage(String mes) throws IOException {
		byte[] b = new byte[1024];
		out.writeBytes(mes);
		int count = in.read(b);
		String strRes = new String(b);
		return count != -1 ? strRes : String.valueOf(count);
	}

	/**
	 * 设备初始化 判断是否多用户
	 * 
	 * @param ip
	 */
	public int init(String ip) {
		try {
			open(ip);
			String version = readVersion();
			// version.substring(30, 31);
			String type = version.substring(21, 24);
			out.writeBytes("*SETREMOTE?\r\n");
			if (version.contains("L")) {
				// you should login before apply
				out.writeBytes("LOGIN:zwx\r\n");
				apply(type);
				System.out.println("是多用户,设备类型是：" + type);
				return 1;

			} else
				System.out.println("不是多用户,设备类型是：" + type);
			deviceType = type;
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 匹配UE数 根据设备类型判断支持UE数 通过支持的UE数来判读该如何设置通道值
	 * 建立标志位来模拟UE运动是否达到目标
	 */
	void runATT(int count_UE, boolean flag) {
		switch (count_UE) {
		case 1:
			int attValue1 = 1;// =MathProc.getRunPoint(StartPoint, EndPoint,
								// CurrentPoint, stepLong);
			int attValue2 = 2;
			//当达到目标时，flag置为flase
//			flag=
			if(flag){
				switchDevice(attValue1, attValue2);
			}
			break;
		case 2:

			break;
		default:
			break;
		}
	}

	/**
	 * 根据设备类型判断设备支持的UE和CELL数
	 * 
	 * @param type
	 */
	void count(String type) {
		deviceType = type;
		switch (type) {
		case "4B2":
			count_UE = 1;
			count_CELL = 1;
			break;
		case "4B4":
			count_UE = 2;
			count_CELL = 1;
			break;
		case "8B8":
			count_UE = 2;
			count_CELL = 2;
			break;
		default:
			break;
		}

	}

	/**
	 * 根据设备类型申请设备通道
	 * 
	 * @param type
	 */
	public void apply(String type) {
		try {

			deviceType = type;
			String strToSend = "";
			switch (type) {
			case "4B2":
				strToSend = toWord(8);
				break;
			case "4B4":
				strToSend = toWord(16);
				break;
			case "8B8":
				strToSend = toWord(64);
				break;
			default:
				System.out.println("没有匹配的设备！");
				break;
			}
			out.writeBytes(strToSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将申请设备的序号
	 * 
	 * @param j
	 * @return
	 */
	public String toWord(int j) {
		StringBuffer s = new StringBuffer();
		for (int i = 1; i <= j; i++) {
			s.append(i).append(',');
		}
		s.deleteCharAt(s.length() - 1);
		return "ROUT:APPLY:" + s.toString() + "\r\n";
	}

	/**
	 * 读取版本信息
	 * 
	 * @throws IOException
	 * 
	 */
	public String readVersion() throws IOException {

		return readMessage("SYSTEM:VERSION\r\n");
	}

	/**
	 * 读取通道值
	 * 
	 * @param channelNo
	 * @return The value of ATT
	 * @throws IOException
	 */
	public String readATT(int channelNo) throws IOException {
		String strToSend = "READ:" + String.valueOf(channelNo) + "\r\n";
		return readMessage(strToSend);
	}

	/**
	 * 设置通道值
	 * 
	 * @param channelNo
	 * @param attValue
	 */
	public void setATT(int channelNo, int attValue) {
		try {
			String send = "SET:" + String.valueOf(channelNo) + ":"
					+ String.valueOf(attValue) + "\r\n";
			out.writeBytes(send);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param attValue1
	 * @param attValue2
	 * @param device
	 */
	private void switchDevice(int attValue1, int attValue2) {
		if (attValue1 < 0)
			attValue1 = 0;
		if (attValue1 > 127)
			attValue1 = 127;
		if (attValue2 < 0)
			attValue2 = 0;
		if (attValue2 > 127)
			attValue2 = 127;

		switch (Device.deviceType) {
		case "4B2":
			setATT(1, attValue1);
			setATT(4, attValue1);
			setATT(5, attValue2);
			setATT(8, attValue2);
			break;
		case "4B4":
			setATT(1, attValue1);
			setATT(6, attValue1);
			setATT(9, attValue2);
			setATT(14, attValue2);
			break;
		case "8B8":
			setATT(1, attValue1);
			try {
				System.out.println(readATT(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setATT(10, attValue1);
			try {
				System.out.println(readATT(10));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setATT(17, attValue2);
			try {
				System.out.println(readATT(17));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setATT(26, attValue2);
			try {
				System.out.println(readATT(26));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
}

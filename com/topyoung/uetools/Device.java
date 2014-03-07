package com.topyoung.uetools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * ���ԣ�UE��Ӧ���豸
 * ��Ӧͨ��֧�ֵ�UE����CELL��
 * ���ܣ��ж��豸�汾��
 * ͨ���汾���ж��豸��֧�ֵ�UE����CELL��
 * �ж��豸���ͣ�֧�ֵ�ͨ����
 * 
 */
public class Device {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private BufferedInputStream bufin;
	private BufferedOutputStream bufout;
	public static String deviceType = "";// �豸���ͣ�����ƥ���豸
	private int count_UE;// ֧�ֵ�UE��
	private int count_CELL;// ֧�ֵ�CELL��

	/**
	 * ��ȡIDN
	 * 
	 * @return IDN��Ϣ
	 * @throws IOException
	 */
	public String readIDN() throws IOException {

		return readMessage("*IDN?\r\n");
	}

	/**
	 * �����豸������
	 * 
	 * @param strIP
	 *            ���豸IP��
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void open(String strIP) throws UnknownHostException, IOException {
		socket = new Socket(strIP, 1000);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}

	/**
	 * ��ȡ��Ϣ
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
	 * �豸��ʼ�� �ж��Ƿ���û�
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
				System.out.println("�Ƕ��û�,�豸�����ǣ�" + type);
				return 1;

			} else
				System.out.println("���Ƕ��û�,�豸�����ǣ�" + type);
			deviceType = type;
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * ƥ��UE�� �����豸�����ж�֧��UE�� ͨ��֧�ֵ�UE�����ж����������ͨ��ֵ
	 * ������־λ��ģ��UE�˶��Ƿ�ﵽĿ��
	 */
	void runATT(int count_UE, boolean flag) {
		switch (count_UE) {
		case 1:
			int attValue1 = 1;// =MathProc.getRunPoint(StartPoint, EndPoint,
								// CurrentPoint, stepLong);
			int attValue2 = 2;
			//���ﵽĿ��ʱ��flag��Ϊflase
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
	 * �����豸�����ж��豸֧�ֵ�UE��CELL��
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
	 * �����豸���������豸ͨ��
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
				System.out.println("û��ƥ����豸��");
				break;
			}
			out.writeBytes(strToSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �������豸�����
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
	 * ��ȡ�汾��Ϣ
	 * 
	 * @throws IOException
	 * 
	 */
	public String readVersion() throws IOException {

		return readMessage("SYSTEM:VERSION\r\n");
	}

	/**
	 * ��ȡͨ��ֵ
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
	 * ����ͨ��ֵ
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

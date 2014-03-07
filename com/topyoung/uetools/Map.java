package com.topyoung.uetools;
/*
 * ��Ա��UE,CELL
 * ���ܣ����UE���˶��켣��CELL�Ĺ�ϵ
 * 
 */
public class Map implements Runnable{
	private Device device;
	UE uePoint;
	CELL cellPoint;
	PointF startPoint;//UE��ʼ����
	PointF endPoint;//UE��������
	
	String type;//�豸����
	float speed;
	int hight;
	int fre;
	float stepLong;
	int count_UE;//�豸֧�ֵ�UE��
	
	//���ñ߽�
	float maxX=0;
	float maxY=0;
	float minX=0;
	float minY=0;
	//��ʼ������
	public PointF initUE(float x,float y){
		uePoint=new UE(x,y);
		startPoint=new UE(x, y);
		return uePoint;
	}
	public PointF initCell(float x,float y){
		cellPoint=new CELL(x, y);
		return cellPoint;
	}
	//���ý�������
	public PointF setEndPoint(float x,float y){
		endPoint=new PointF(x, y);
		return endPoint;
	}
	//�����ٶ�
	public void initSpeed(float speed){
		uePoint.setSpeed(speed);
		this.speed=uePoint.getSpeed();
	}
	//���û�վ�߶�
	public void initHight(int hight){
		cellPoint.setHight(hight);
		this.hight=cellPoint.getHight();
	}
	//����Ƶ��
	public void initFreq(int frq){
		cellPoint.setFrq(frq);
		this.fre=cellPoint.getFrq();
	}
	//���ò�������
	public void initStepLong(float stepLong){
		cellPoint.setStepLong(stepLong);
		this.stepLong=cellPoint.getStepLong();
	}
	//��ǰ����
	public PointF getCurrentPoint(){
		uePoint=(UE)MathProc.getRunPoint(startPoint, endPoint, uePoint, stepLong);
		return uePoint;
	}
	//��ʼ���豸
	public void initDevice(String ip){
		device.init(ip);
	}
	
	@Override
	public void run() {//�豸������ģ��UE���˶�
		boolean flag=true;//Ĭ��Ϊ��
		device.runATT(count_UE,flag);
		//���ﵽ����ʱ��flag=false,��ֹ�߳�
		flag=uePoint.getX() < minX || uePoint.getX() > maxX || uePoint.getY() < minY || uePoint.getY() > maxY;
		if(flag==false){
			Thread.currentThread().stop();
		}
		System.out.println("UE.....RUN.....");
	}
	

}

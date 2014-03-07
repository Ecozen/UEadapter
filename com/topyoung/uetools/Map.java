package com.topyoung.uetools;
/*
 * 成员：UE,CELL
 * 功能：检测UE的运动轨迹与CELL的关系
 * 
 */
public class Map implements Runnable{
	private Device device;
	UE uePoint;
	CELL cellPoint;
	PointF startPoint;//UE起始坐标
	PointF endPoint;//UE结束坐标
	
	String type;//设备类型
	float speed;
	int hight;
	int fre;
	float stepLong;
	int count_UE;//设备支持的UE数
	
	//设置边界
	float maxX=0;
	float maxY=0;
	float minX=0;
	float minY=0;
	//初始化坐标
	public PointF initUE(float x,float y){
		uePoint=new UE(x,y);
		startPoint=new UE(x, y);
		return uePoint;
	}
	public PointF initCell(float x,float y){
		cellPoint=new CELL(x, y);
		return cellPoint;
	}
	//设置结束坐标
	public PointF setEndPoint(float x,float y){
		endPoint=new PointF(x, y);
		return endPoint;
	}
	//设置速度
	public void initSpeed(float speed){
		uePoint.setSpeed(speed);
		this.speed=uePoint.getSpeed();
	}
	//设置基站高度
	public void initHight(int hight){
		cellPoint.setHight(hight);
		this.hight=cellPoint.getHight();
	}
	//设置频率
	public void initFreq(int frq){
		cellPoint.setFrq(frq);
		this.fre=cellPoint.getFrq();
	}
	//设置步进长度
	public void initStepLong(float stepLong){
		cellPoint.setStepLong(stepLong);
		this.stepLong=cellPoint.getStepLong();
	}
	//当前坐标
	public PointF getCurrentPoint(){
		uePoint=(UE)MathProc.getRunPoint(startPoint, endPoint, uePoint, stepLong);
		return uePoint;
	}
	//初始化设备
	public void initDevice(String ip){
		device.init(ip);
	}
	
	@Override
	public void run() {//设备被操作模拟UE的运动
		boolean flag=true;//默认为真
		device.runATT(count_UE,flag);
		//当达到条件时，flag=false,终止线程
		flag=uePoint.getX() < minX || uePoint.getX() > maxX || uePoint.getY() < minY || uePoint.getY() > maxY;
		if(flag==false){
			Thread.currentThread().stop();
		}
		System.out.println("UE.....RUN.....");
	}
	

}

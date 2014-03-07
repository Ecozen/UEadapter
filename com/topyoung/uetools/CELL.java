package com.topyoung.uetools;
/*
 * 
 * 功能：接收UE运动数据
 * 
 * 属性：坐标 ，高度，发射频率
 */
public class CELL extends PointF {

	private int hight;
	private int frq;
	private float stepLong;
	private int timeDelay;
	public int getHight() {
		return hight;
	}
	public void setHight(int hight) {
		this.hight = hight;
	}
	public int getFrq() {
		return frq;
	}
	public void setFrq(int frq) {
		this.frq = frq;
	}
	public float getStepLong() {
		return stepLong;
	}
	public void setStepLong(float stepLong) {
		this.stepLong = stepLong;
	}
	public int getTimeDelay() {
		return timeDelay;
	}
	public void setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}
	public CELL() { 
		super();
	}
	public CELL(float x, float y) {//初始化cell
		super(x, y);
	}
				
}

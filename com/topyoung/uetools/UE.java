package com.topyoung.uetools;

import java.io.IOException;

/*
 * 名称：移动终端
 * 功能：运动，向基站发射信号
 * 类型：统一
 * 属性：坐标，运动速度
 */
public class UE extends PointF {
	String type;//类型
	private float speed;//运动速度
	
	public UE() {
		super();
	}
	public UE(float x, float y) {//初始化UE
		super(x, y);
	}
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	
	public void setPoint(float x,float y){//设置位置
	setX(x);
	setY(y);
	}
	
	
	

}

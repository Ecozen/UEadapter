package com.topyoung.uetools;

import java.io.IOException;

/*
 * ���ƣ��ƶ��ն�
 * ���ܣ��˶������վ�����ź�
 * ���ͣ�ͳһ
 * ���ԣ����꣬�˶��ٶ�
 */
public class UE extends PointF {
	String type;//����
	private float speed;//�˶��ٶ�
	
	public UE() {
		super();
	}
	public UE(float x, float y) {//��ʼ��UE
		super(x, y);
	}
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	
	public void setPoint(float x,float y){//����λ��
	setX(x);
	setY(y);
	}
	
	
	

}

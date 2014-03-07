package com.topyoung.uetools;


public class MathProc
{
	public static PointF rePointF;
  	/************************************************************************************
    **函数名称：GetDistanceFromPoints(PointF fPointA, PointF fPointB)
    **函数功能：计算两点之间的距离
    **函数参数：fPointA  A点
    **          fPointB  B点
    **返回值：  两点之间的距离
    *************************************************************************************/
    public static double getDistanceFromPoints(PointF fPointA, PointF fPointB)
    {
        double dRe = Math.sqrt(Math.pow((fPointA.getX() - fPointB.getX()), 2) + Math.pow((fPointA.getY() - fPointB.getY()), 2));
        return dRe;
    }
    
    /************************************************************************************
    **函数名称：GetAttValue(PointF cellPointF, PointF uePointF,int bsHeight,int freq) 
    **函数功能：得到从小区的信号源到UE所在的位置的衰减值
    **函数参数：cellPointF  cell的中心点坐标
    **          uePointF    ue的当前坐标
    **          bsHeight    基站高度
    **          freq        信号频率，单位为MHz
    **返回值：  衰减值，为整数，单位为dB
    *************************************************************************************/
    public static int getAttValue(PointF cellPointF, PointF uePointF,int bsHeight,int freq)  
    {
        double distance = getDistanceFromPoints(uePointF, cellPointF);    //小区中心点距离UE的距离
        //double temp = Math.sqrt((double)bsHeight * (double)bsHeight + dDistance * dDistance);
        //System.out.println("Distance:");
        //System.out.println(temp);
        int attValue = (int)(20 * log((Math.sqrt((double)bsHeight * (double)bsHeight + distance * distance) / 1000),10) + 20 * log(freq,10) + 0.5);
        //int attValue = (int)(20 * Logarithm.log(temp / 1000,10) + 20 * Logarithm.log(freq,10) + 0.5);
//        System.out.println("attValue:"+attValue);
        return attValue;
    }
    
    //得到在自动跑过程中UE的坐标
    public static PointF getRunPoint(PointF StartPoint, PointF EndPoint, PointF CurrentPoint,float stepLong)
    {
        //region**一条直线奔跑，横着移动**
        if (StartPoint.getY() == EndPoint.getY())
        {
            //region**起始坐标小于终止坐标，说明直线往前跑**
            if (StartPoint.getX() < EndPoint.getX())
            {
            	float x = CurrentPoint.getX()+stepLong;
            	float y=StartPoint.getY();
            	rePointF.setX(x);
            	rePointF.setY(y) ;
            }

            //region**起始坐标大于终止坐标，说明直线返回**
            if (StartPoint.getX() > EndPoint.getX())
            {
            	float x = CurrentPoint.getX() -stepLong;
            	float y=StartPoint.getY();
            	rePointF.setX(x);
            	rePointF.setY(y) ;
            }
        }


        //region**一条直线奔跑，竖着移动**
        else if (StartPoint.getX() == EndPoint.getX())
        {
            //region**起始坐标小于终止坐标，说明直线往下跑**
            if (StartPoint.getY() < EndPoint.getY())
            {
            	float y= CurrentPoint.getY() + stepLong;
            	float x= StartPoint.getX();
            	rePointF.setX(x);
            	rePointF.setY(y);
            }

            //region**起始坐标大于终止坐标，说明直线往上跑**
            else if (StartPoint.getY() > EndPoint.getY())
            {
            	float y= CurrentPoint.getY() - stepLong;
            	float x= StartPoint.getX();
            	rePointF.setX(x);
            	rePointF.setY(y);
            }
        }

        //region**奔跑的线路更改为斜着的一条线**
        else if (StartPoint.getX() != EndPoint.getX() && StartPoint.getY() != EndPoint.getY())
        {
            float fK = getLineK(StartPoint,EndPoint);
            float fB = getLineB(StartPoint,fK);
            float fN = getN(fK);
            double dM = getM(fN, stepLong);
            //region**当从上往下的路线的时候，为负数**
            if (StartPoint.getY() > EndPoint.getY())
            {
                rePointF = getMovePoint(-dM, fK, fB, CurrentPoint);
            }

            //region**从下往上的路线的时候，为正数**
            else if (StartPoint.getY() < EndPoint.getY())
            {
                rePointF = getMovePoint(dM, fK, fB, CurrentPoint);
            }
        }

        return rePointF;
    }

    //得到直线斜率，K= (Y2-Y1)/(X2/X1)
    private static float getLineK(PointF pointFA, PointF pointFB)
    {
        return (pointFB.getY() - pointFA.getY()) / (pointFB.getX() - pointFA.getX());
    }

    // 得到直线系数中的b参量
    private static float getLineB(PointF pointFA, float K)
    {
        return pointFA.getY() - (pointFA.getX() * K);
    }

    //得到坐标X轴
    private static float getPointX(float Y, float K, float B)
    {
        return (Y - B) / K;
    }

    //得到坐标Y轴
    private static float getPointY(float X, float K, float B)
    {
        return X * K + B;
    }

    //得到方程式中N的值，N为（（Y2-Y1）/（X2-X1））^2
    private static float getN(float K)
    {
        return K * K;
    }

    //得到方程式中的M参数，M = （N*D^2）/(1+N)
    private static double getM(float N, float D)
    {
        return  Math.sqrt((double)((N * (D * D)) / (1 + N)));
    }

    //得到需要移动到的坐标
    private static PointF getMovePoint(double M, float K, float B,PointF pointFCurrent)
    {
        float x = getPointX(rePointF.getY(), K, B);
        float y = (float)(pointFCurrent.getY() + M);
        rePointF.setX(x);
        rePointF.setY(y);
        return rePointF;
    }
    
    static public double log(double value,double base)
	{
		return Math.log(value) / Math.log(base);
	}
    
    
    public static float getMin(float a,float i)
    {
    	
    	return a>i?i:a;
    }
    
    public static float getMax(float a,float i)
    {
    	
    	return a>i?a:i;
    }
}
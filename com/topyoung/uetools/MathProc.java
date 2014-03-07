package com.topyoung.uetools;


public class MathProc
{
	public static PointF rePointF;
  	/************************************************************************************
    **�������ƣ�GetDistanceFromPoints(PointF fPointA, PointF fPointB)
    **�������ܣ���������֮��ľ���
    **����������fPointA  A��
    **          fPointB  B��
    **����ֵ��  ����֮��ľ���
    *************************************************************************************/
    public static double getDistanceFromPoints(PointF fPointA, PointF fPointB)
    {
        double dRe = Math.sqrt(Math.pow((fPointA.getX() - fPointB.getX()), 2) + Math.pow((fPointA.getY() - fPointB.getY()), 2));
        return dRe;
    }
    
    /************************************************************************************
    **�������ƣ�GetAttValue(PointF cellPointF, PointF uePointF,int bsHeight,int freq) 
    **�������ܣ��õ���С�����ź�Դ��UE���ڵ�λ�õ�˥��ֵ
    **����������cellPointF  cell�����ĵ�����
    **          uePointF    ue�ĵ�ǰ����
    **          bsHeight    ��վ�߶�
    **          freq        �ź�Ƶ�ʣ���λΪMHz
    **����ֵ��  ˥��ֵ��Ϊ��������λΪdB
    *************************************************************************************/
    public static int getAttValue(PointF cellPointF, PointF uePointF,int bsHeight,int freq)  
    {
        double distance = getDistanceFromPoints(uePointF, cellPointF);    //С�����ĵ����UE�ľ���
        //double temp = Math.sqrt((double)bsHeight * (double)bsHeight + dDistance * dDistance);
        //System.out.println("Distance:");
        //System.out.println(temp);
        int attValue = (int)(20 * log((Math.sqrt((double)bsHeight * (double)bsHeight + distance * distance) / 1000),10) + 20 * log(freq,10) + 0.5);
        //int attValue = (int)(20 * Logarithm.log(temp / 1000,10) + 20 * Logarithm.log(freq,10) + 0.5);
//        System.out.println("attValue:"+attValue);
        return attValue;
    }
    
    //�õ����Զ��ܹ�����UE������
    public static PointF getRunPoint(PointF StartPoint, PointF EndPoint, PointF CurrentPoint,float stepLong)
    {
        //region**һ��ֱ�߱��ܣ������ƶ�**
        if (StartPoint.getY() == EndPoint.getY())
        {
            //region**��ʼ����С����ֹ���꣬˵��ֱ����ǰ��**
            if (StartPoint.getX() < EndPoint.getX())
            {
            	float x = CurrentPoint.getX()+stepLong;
            	float y=StartPoint.getY();
            	rePointF.setX(x);
            	rePointF.setY(y) ;
            }

            //region**��ʼ���������ֹ���꣬˵��ֱ�߷���**
            if (StartPoint.getX() > EndPoint.getX())
            {
            	float x = CurrentPoint.getX() -stepLong;
            	float y=StartPoint.getY();
            	rePointF.setX(x);
            	rePointF.setY(y) ;
            }
        }


        //region**һ��ֱ�߱��ܣ������ƶ�**
        else if (StartPoint.getX() == EndPoint.getX())
        {
            //region**��ʼ����С����ֹ���꣬˵��ֱ��������**
            if (StartPoint.getY() < EndPoint.getY())
            {
            	float y= CurrentPoint.getY() + stepLong;
            	float x= StartPoint.getX();
            	rePointF.setX(x);
            	rePointF.setY(y);
            }

            //region**��ʼ���������ֹ���꣬˵��ֱ��������**
            else if (StartPoint.getY() > EndPoint.getY())
            {
            	float y= CurrentPoint.getY() - stepLong;
            	float x= StartPoint.getX();
            	rePointF.setX(x);
            	rePointF.setY(y);
            }
        }

        //region**���ܵ���·����Ϊб�ŵ�һ����**
        else if (StartPoint.getX() != EndPoint.getX() && StartPoint.getY() != EndPoint.getY())
        {
            float fK = getLineK(StartPoint,EndPoint);
            float fB = getLineB(StartPoint,fK);
            float fN = getN(fK);
            double dM = getM(fN, stepLong);
            //region**���������µ�·�ߵ�ʱ��Ϊ����**
            if (StartPoint.getY() > EndPoint.getY())
            {
                rePointF = getMovePoint(-dM, fK, fB, CurrentPoint);
            }

            //region**�������ϵ�·�ߵ�ʱ��Ϊ����**
            else if (StartPoint.getY() < EndPoint.getY())
            {
                rePointF = getMovePoint(dM, fK, fB, CurrentPoint);
            }
        }

        return rePointF;
    }

    //�õ�ֱ��б�ʣ�K= (Y2-Y1)/(X2/X1)
    private static float getLineK(PointF pointFA, PointF pointFB)
    {
        return (pointFB.getY() - pointFA.getY()) / (pointFB.getX() - pointFA.getX());
    }

    // �õ�ֱ��ϵ���е�b����
    private static float getLineB(PointF pointFA, float K)
    {
        return pointFA.getY() - (pointFA.getX() * K);
    }

    //�õ�����X��
    private static float getPointX(float Y, float K, float B)
    {
        return (Y - B) / K;
    }

    //�õ�����Y��
    private static float getPointY(float X, float K, float B)
    {
        return X * K + B;
    }

    //�õ�����ʽ��N��ֵ��NΪ����Y2-Y1��/��X2-X1����^2
    private static float getN(float K)
    {
        return K * K;
    }

    //�õ�����ʽ�е�M������M = ��N*D^2��/(1+N)
    private static double getM(float N, float D)
    {
        return  Math.sqrt((double)((N * (D * D)) / (1 + N)));
    }

    //�õ���Ҫ�ƶ���������
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
package com.topyoung.uetools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * ��Ҫ�����������豸 ����UE��CELL��Ϣ  ��������
 * @author Administrator
 *
 */
public class AutoRun {
	Map map=new Map();
	ExecutorService pool=Executors.newCachedThreadPool();
	
	@Test
	public void runUE(){
		pool.execute(map);
	}
}

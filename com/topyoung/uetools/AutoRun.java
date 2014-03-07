package com.topyoung.uetools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 主要工作：连接设备 设置UE和CELL信息  启动运行
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

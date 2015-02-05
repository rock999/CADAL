package com.cadal.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.cadal.common.LogTools;
import com.cadal.model.OperationStatus;

public class CADALCrawler {
	private static Logger log = Logger.getLogger(LogTools.class);

	public static void main(String[] args) {
		CADALCrawler cb = new CADALCrawler();
		cb.demo();
	}

	private void demo() {
		int a1[] = { 1, 2, 3 };
		int a2[] = { 4, 5, 6 };
		int a3[] = { 7, 8, 9 };
		int a4[] = { 10, 11, 12 };
		int a5[] = { 13, 14, 15 };
		int aa[][] = { a1, a2, a3, a4, a5 };
		new CADALScheduler(aa);
	}
}

class CADALScheduler {
	int data[][];
	int ThreadMaxSize = 100;
	CyclicBarrier barrier = null;

	public CADALScheduler(int[][] data) {
		this.data = data;
		ThreadMaxSize = 100;
		ExecutorService ex = Executors.newFixedThreadPool(ThreadMaxSize);

		List<String> bookIDSet = null;

		// for(String bookID:bookIDSet)
		// {
		//
		// }

		barrier = new CyclicBarrier(ThreadMaxSize);
		Callable<Integer> task = null;
		List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
		for (int i = 0; i < ThreadMaxSize; i++) {
			task = new Worker("", "", barrier);
			Future<Integer> result = ex.submit(task);
			resultList.add(result);
		}

		int flagNum = 0;
		boolean invalidIP = false;
		boolean invalidAccount = false;

		for (Future<Integer> futrueTask : resultList) {
			try {
				flagNum = futrueTask.get(15, TimeUnit.MINUTES);
				if (OperationStatus.INVALIDIP == flagNum)
					invalidIP = true;
				if (OperationStatus.INVALIDACCOUNT == flagNum)
					invalidAccount = true;

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Is Barrier broken : " + barrier.isBroken());
		System.out.println("Grand total is : " + flagNum);
		ex.shutdown();
	}
}

/**
 * 
 * return :
 * 
 * outOfBound:1 operationFailed:-1 InvalidAccount:-100 NogLogin:-2
 * 
 */

class Worker implements Callable<Integer> {
	CyclicBarrier barrier;
	String bookInfo;
	String sessionInfos;

	public Worker(String bookInfo, String sessionInfos, CyclicBarrier barrier) {
		this.bookInfo = bookInfo;
		this.barrier = barrier;
		this.sessionInfos = sessionInfos;
	}

	public Integer call() throws Exception {
		Random random = new Random();
		try {
			Thread.sleep((random.nextInt(10) * 1000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			System.out
					.println("Here is barrier, Waiting here for other threads to complete.");
			barrier.await();

		} catch (InterruptedException e) {
			e.printStackTrace();

		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

		System.out.println("Barrier opened");
		return 1;
	}
}

package com.cadal.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.cadal.common.FileHelper;
import com.cadal.common.LogTools;
import com.cadal.common.TimeUtility;
import com.cadal.model.OperationStatus;

public class CADALCrawler {
	private static Logger log = Logger.getLogger(LogTools.class);

	public static void main(String[] args) {
		CADALCrawler cb = new CADALCrawler();
		cb.demo();
	}

	private void demo() {
		new CADALScheduler();
	}
}

class CADALScheduler {
	int ThreadMaxSize = 3;
	CyclicBarrier barrier = null;
	static FileHelper fileHelper = new FileHelper();

	public CADALScheduler() {

		// record startup time
		fileHelper.writeDataToFile("log", "run.log",
				TimeUtility.getCurrentTimeStr(), "UTF-8");

		ExecutorService ex = Executors.newFixedThreadPool(ThreadMaxSize);

		barrier = new CyclicBarrier(ThreadMaxSize);
		Callable<OperationStatus> task = null;
		List<Future<OperationStatus>> resultList = new ArrayList<Future<OperationStatus>>();
		for (int i = 0; i < ThreadMaxSize; i++) {
			task = new Worker(barrier);
			Future<OperationStatus> result = ex.submit(task);
			resultList.add(result);
		}

		System.out.println("Is Barrier broken : " + barrier.isBroken());
		System.out.println("endtime:" + TimeUtility.getCurrentTimeStr());
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

class Worker implements Callable<OperationStatus> {
	CyclicBarrier barrier;
	String bookID;
	String sessionInfos;

	public Worker(CyclicBarrier barrier) {

		this.barrier = barrier;
	}

	public OperationStatus call() throws Exception {
		OperationStatus status = null;
		try {
			BookFetcher fetcher = new BookFetcher();
			status = fetcher.execute();

		} catch (Exception e1) {
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
		return status;
	}
}

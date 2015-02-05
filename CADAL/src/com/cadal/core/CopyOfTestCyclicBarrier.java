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

public class CopyOfTestCyclicBarrier {

	public static void main(String[] args) {
		CopyOfTestCyclicBarrier cb = new CopyOfTestCyclicBarrier();
		cb.demo();
	}

	private void demo() {
		int a1[] = { 1, 2, 3 };
		int a2[] = { 4, 5, 6 };
		int a3[] = { 7, 8, 9 };
		int a4[] = { 10, 11, 12 };
		int a5[] = { 13, 14, 15 };
		int aa[][] = { a1, a2, a3, a4, a5 };
		//new Solver(aa);
	}
}

//class Solver {
//	int data[][];
//	int N;
//	CyclicBarrier barrier = null;
//
//	public Solver(int[][] data) {
//		this.data = data;
//		N = data.length;
//		barrier = new CyclicBarrier(N, new WhenLastOneInBarrier());
//		ExecutorService ex = Executors.newFixedThreadPool(N);
//		Callable<Integer> task = null;
//		List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
//		for (int i = 0; i < N; i++) {
//			task = new Worker(data, i, barrier);
//			Future<Integer> result = ex.submit(task);
//
//			resultList.add(result);
//		}
//
//		int grandTotal = 0;
//		for (Future<Integer> f : resultList) {
//			try {
//				grandTotal += f.get(200, TimeUnit.SECONDS);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			} catch (TimeoutException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		System.out.println("Is Barrier broken : " + barrier.isBroken());
//		System.out.println("Grand total is : " + grandTotal);
//		ex.shutdown();
//	}
//}
//
//class Worker implements Callable<Integer> {
//	CyclicBarrier barrier;
//	int data[][];
//	int rowNum;
//
//	public Worker(int[][] data, int rowNum, CyclicBarrier barrier) {
//		this.data = data;
//		this.barrier = barrier;
//		this.rowNum = rowNum;
//	}
//
//	@Override
//	public Integer call() throws Exception {
//		Random random = new Random();
//		try {
//			Thread.sleep((random.nextInt(10) * 1000));
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//
//		int d[] = data[rowNum];
//		int total = 0;
//		for (int i : d) {
//			total += i;
//		}
//
//		System.out.println("processed..total for the row number " + rowNum
//				+ " is : " + total);
//		try {
//			System.out
//					.println("Here is barrier, Waiting here for other threads to complete.");
//			barrier.await();
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//
//		} catch (BrokenBarrierException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Barrier opened");
//		return total;
//	}
//}
//
//class WhenLastOneInBarrier implements Runnable {
//	@Override
//	public void run() {
//		System.out.println("Last worker in the barrier...");
//	}
//}
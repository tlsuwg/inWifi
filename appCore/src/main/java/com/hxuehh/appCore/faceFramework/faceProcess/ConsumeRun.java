/**
 * 
 */
package com.hxuehh.appCore.faceFramework.faceProcess;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ConsumeRun<T> implements Runnable {

	public ConsumeRun(ConcurrentLinkedQueue<T> inQueue) {
		this.inQueue = inQueue;
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
		synchronized (inQueue) {
			inQueue.notifyAll();
		}
	}

	private ConcurrentLinkedQueue<T> inQueue;
	private boolean isRun = true;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			T oo = getPoll();
			if (oo == null) {
				break;
			}
			if (isRun)
				doOne(oo);
		}
	}

	public abstract void doOne(T oo);

	private T getPoll() {
		synchronized (inQueue) {
			T oo = inQueue.poll();
			if (oo == null) {
				try {
					inQueue.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return getPoll();
			} else {
				return oo;
			}
		}
	}


	public void add(T t){
		synchronized (inQueue){
			inQueue.add(t);
			inQueue.notifyAll();
		}
	}


	
}

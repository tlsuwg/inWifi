package com.hxuehh.rebirth.capacity;

public interface DataListener<T> {
	
	boolean onDate(T... bs);
	
	void onErr(Object err);

}

package com.shixels.printer;

public interface SerialPortDataReceived {
	public void onDataReceivedListener(final byte[] buffer, final int size); 

}

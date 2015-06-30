package com.eoe.demovideoplayer;

import com.avos.avoscloud.AVOSCloud;

import android.app.Application;

public class MyApplication extends Application{

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		AVOSCloud.initialize(this, "bdynme6i8z7lbgdpnudbny1pdl1bk5y8g8x6i2zcdej47qp1", "gdhyqweysu2hlbnou5hbtsf90ulg5v7atnttohn7ya5j991i");
	}
	
}

package com.socket.activity;

import android.app.Activity;
import android.os.Bundle;

import com.socket.R;
import com.socket.business.BusinessLogic;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 20:57:48 24/01/2013
 */
public class MainActivity extends Activity{
	
	private BusinessLogic businessLogic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        businessLogic = new BusinessLogic(this);
        businessLogic.configView();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	businessLogic.closeCommunication();
    }
    
}
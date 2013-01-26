package com.socket.business;

import java.net.Socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.socket.R;
import com.socket.manager.SocketCommunication;
import com.socket.notice.Notice;
import com.socket.task.SocketClientTask;
import com.socket.task.SocketServerTask;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 17:52:44 25/01/2013
 */
@SuppressLint("HandlerLeak")
public class BusinessLogic implements OnClickListener{
	
	private EditText edMsg;
	private Button btnSend;
	private Button btnService;
	private Button btnClient;
	private ListView lstHistoric;
	
	private Activity context;
	private Notice notice;
	private ArrayAdapter<String> historic;
	
	private SocketCommunication communication;
	
	public BusinessLogic(Activity activity){
		this.context = activity;
		
		notice = new Notice(activity);
		edMsg = (EditText) activity.findViewById(R.id.edtMsg);
        btnSend = (Button) activity.findViewById(R.id.btnSend);
        btnClient = (Button) activity.findViewById(R.id.btnClient);
        btnService = (Button) activity.findViewById(R.id.btnService);
        lstHistoric = (ListView) activity.findViewById(R.id.lstHistoric);
	}
	
	public void configView(){
		historic = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		lstHistoric.setAdapter(historic);
		
		edMsg.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnClient.setOnClickListener(this);
		btnService.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
	        case R.id.btnSend:
	        		if(isNetworkConnected()){
	        			sendMsg();
	        		}
	                break;
	        case R.id.btnService:
		        	if(isNetworkConnected()){
		        		popupServer();
		        	}
	                break;
	        case R.id.btnClient:
		        	if(isNetworkConnected()){
		        		popupClient();
		        	}
	                break;
		}
	}
	
	private void sendMsg(){
		if(communication != null){
			String msg = edMsg.getText().toString(); 
			
			if(msg.trim().length() > 0){
				edMsg.setText(""); 
				
				communication.sendMsg(msg);
				
				historic.add("Eu: " + msg); 
				historic.notifyDataSetChanged();							
			}else{
				notice.showToast("Escreva alguma mensagem");
			}
		}else{
			notice.showToast("Sem conexão com outro dispositivo");
		}
	}
	
	private void popupServer(){
		AlertDialog.Builder alertConfig = new AlertDialog.Builder(context);
        alertConfig.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        alertConfig.setTitle("Servidor");
        
        final EditText input = new EditText(context);
        input.setHint("porta");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertConfig.setView(input);	

        alertConfig.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	closeCommunication();
            	SocketServerTask serverTask = new SocketServerTask(context, handler, 1);
            	serverTask.execute(new String[]{input.getText().toString()});
            }
        });
            
        alertConfig.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        alertConfig.create().show();
	}
	
	@SuppressLint("HandlerLeak")
	private void popupClient(){
		AlertDialog.Builder alertConfig = new AlertDialog.Builder(context);
        alertConfig.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        alertConfig.setTitle("Cliente");
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.popup_client, null);
        
        final EditText editText = (EditText)convertView.findViewById(R.id.editText1);
        final EditText editText2 = (EditText)convertView.findViewById(R.id.editText2);
        
        alertConfig.setView(convertView);	
        
        alertConfig.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	closeCommunication();
            	SocketClientTask clientTask = new SocketClientTask(context, handler, 1);
            	clientTask.execute(new String[]{editText.getText().toString(), editText2.getText().toString()});
            }
        });
            
        alertConfig.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        alertConfig.create().show();
	}
	
	public void closeCommunication(){
		if(communication != null){
			communication.stopComunication();
		}
	}
	
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null) {
			notice.showToast("Sem conexão");
			return false;
		} else{
			return true;
		}
	}
	
	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (msg) {
                switch (msg.what) {
                	case 1:
                		Socket socket = (Socket)(msg.obj);
                		communication = new SocketCommunication(socket, handler, 3, 2);
            			communication.start();
                		break;
            			
                	case 2:
                		String message = (String)(msg.obj);
                		
                		notice.showToast(message);
                		break;
                	
                	case 3:
                		String messageBT = (String)(msg.obj);
                		
                		historic.add(messageBT);
       				 	historic.notifyDataSetChanged();
       				 	break;
                }
            }
        };
    };
	
}
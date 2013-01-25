package com.socket.business;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.socket.R;
import com.socket.manager.SocketCallback;
import com.socket.manager.SocketClient;
import com.socket.manager.SocketCommunication;
import com.socket.manager.SocketServer;
import com.socket.notice.Notice;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 17:52:44 25/01/2013
 */
public class BusinessLogic implements OnClickListener, SocketCallback{
	
	private EditText edMsg;
	private Button btnSend;
	private Button btnService;
	private Button btnClient;
	private ListView lstHistoric;
	
	private Activity context;
	private Notice notice;
	private ArrayAdapter<String> historic;
	
	private SocketServer server;
	private SocketClient client;
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
	            	String msg = edMsg.getText().toString(); 
					
					if(msg.trim().length() > 0){
						edMsg.setText(""); 
						
						historic.add("Eu: " + msg); 
						historic.notifyDataSetChanged();							
					}else{
						notice.showToast("Escreva alguma mensagem");
					}
	                break;
	        case R.id.btnService:
	        		popupServer();
	                break;
	        case R.id.btnClient:
	        		popupClient();
	                break;
		}
	}
	
	private void popupServer(){
		AlertDialog.Builder alertConfig = new AlertDialog.Builder(context);
        alertConfig.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        alertConfig.setTitle("Servidor");
        alertConfig.setMessage("Porta de comunicação");
        
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertConfig.setView(input);	

        alertConfig.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
            
        alertConfig.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        alertConfig.create().show();
	}
	
	private void popupClient(){
		AlertDialog.Builder alertConfig = new AlertDialog.Builder(context);
        alertConfig.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        alertConfig.setTitle("Cliente");
        alertConfig.setMessage("Porta de comunicação");
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.popup_client, null);
        alertConfig.setView(convertView);	

        alertConfig.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
            
        alertConfig.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        alertConfig.create().show();
	}

	@Override
	public void onSocketReceiverMsg(byte[] msg) {
		historic.add(new String(msg));
		historic.notifyDataSetChanged();
	}

}
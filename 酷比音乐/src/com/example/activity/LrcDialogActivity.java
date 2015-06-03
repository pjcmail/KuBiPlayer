package com.example.activity;
import com.example.kubimusic.R;
import com.example.lrc.GetLrc;
import com.example.util.LrcUtil;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LrcDialogActivity extends ActivityBase implements OnClickListener{
	EditText nameText,singerText;
	Button ok_btn,cancel_btn;
	 String name;
	 String singer;
	 private ToastHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lrc_dialog);
		
		singerText = (EditText)findViewById(R.id.artist_tv);
		nameText = (EditText)findViewById(R.id.music_tv);
		ok_btn = (Button)findViewById(R.id.ok_btn);
		cancel_btn = (Button)findViewById(R.id.cancel_btn);
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("MusicName");
		singer = bundle.getString("singer");
		nameText.setText(name);
		singerText.setText(singer);
		ok_btn.setOnClickListener(this) ;
		cancel_btn.setOnClickListener(this);
		handler = new ToastHandler();
		
		
	}

	/**
	 * º∆À„∫¡√Î
	 */
	public int time2Long(String timestr){
		String s[] = timestr.split(":");
		int min = Integer.parseInt(s[0]);
		String ss[] =s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		
		return min*60*1000 + sec*1000 + mill*10;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(cancel_btn))
			finish();
		else if(v.equals(ok_btn)){
			downLoadAndSaveLrc();
		}
	}

	private void downLoadAndSaveLrc() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				List<String> _LrcList  =GetLrc.getLrc(nameText.getText().toString(), singerText.getText().toString());
				if(_LrcList==null){
					message.what=0;
					handler.sendMessage(message);
				}
				else{
					message.what=1;
					handler.sendMessage(message);
					boolean isSaveOk = LrcUtil.saveLrcFile(_LrcList, name);
					if(isSaveOk){
					}
					else{
						message.what=-1;
						handler.sendMessage(message);
					}
				}
				finish();
			}
		}).start();;
	}
	
	class ToastHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0)
			{
				Toast.makeText(LrcDialogActivity.this, "∏Ë¥ œ¬‘ÿ ß∞‹",1000).show();
			}
			else if(msg.what==-1)
				Toast.makeText(LrcDialogActivity.this, "∏Ë¥ ±£¥Ê ß∞‹",1000).show();
			else if(msg.what==1)
				Toast.makeText(LrcDialogActivity.this, "∏Ë¥ œ¬‘ÿ≥…π¶",1000).show();
		}
		
	}
	
}

package com.fhl.customwidget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.fhl.customwidget.view.GuideMapViewGroup2;
import com.fhl.customwidget.view.GuideMapViewGroup2.OnMeanItemClick;

public class MainActivity2 extends Activity {

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				vg.setMessageCount(10);
				break;
			case 2:
				vg.setMessageCount(-10);
				break;

			default:
				break;
			}
		};
	};
	
	
	
	private View bing_car_driver_ll;
	
	private int resIds[] = new int[]{
			R.drawable.selector_jgyw_zt,
            R.drawable.selector_jgyw_tsxx,
            R.drawable.selector_jgyw_wzcx,
            R.drawable.selector_jgyw_rf,
            R.drawable.selector_jgyw_rj,
            R.drawable.selector_jgyw_wfjl
    };
	private static final String WFLS = "Υ����ʷ ";
	private static final String YWXX = "ҵ����Ϣ";
	private static final String WZCX = "Υ�²�ѯ";
	private static final String ZZCF = "��������";
	private static final String WFCX = "Υ���ɷ� ";
	private static final String WFJL = "Υ����¼ ";
    private String texts[] = new String[]{
    		WFLS, YWXX, WZCX,
    		ZZCF, WFCX,WFJL
    };



	private GuideMapViewGroup2 vg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);
		
		init();
	}

	private void init() {
		vg = (GuideMapViewGroup2) findViewById(R.id.vg);
		vg.setMeanItemIconAndText(resIds, texts,8);
		vg.setOnMeanItemClickListener(new OnMeanItemClick() {
			
			@Override
			public void onMeanItemClick(int position) {
				Toast.makeText(MainActivity2.this, "�������:"+texts[position], 0).show();
				
			}
		});
		
		
		findViewById(R.id.id_bind_ll).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity2.this, "�󶨻������ͼ�ʻ֤��", 0).show();
			}
		});
		
//		handler.sendEmptyMessageDelayed(1, 1000 * 5);
//		handler.sendEmptyMessageDelayed(2, 1000 * 10);
		
	}
}

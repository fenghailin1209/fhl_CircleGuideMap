package com.fhl.customwidget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.fhl.customwidget.view.GuideMapViewGroup;
import com.fhl.customwidget.view.GuideMapViewGroup.setOnMeanItemSelectListenner;

public class MainActivity extends Activity {

	private View bing_car_driver_ll;
	
	private int resIds[] = new int[]{
            R.drawable.selector_jgyw_tsxx,
            R.drawable.selector_jgyw_wzcx,
            R.drawable.selector_jgyw_rf,
            R.drawable.selector_jgyw_rj,
            R.drawable.selector_jgyw_zt
    };
    private String texts[] = new String[]{"推送信息 ", "违章查询", "认罚",
            "认缴", "状态"
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		init();
	}

	private void init() {
		GuideMapViewGroup vg = (GuideMapViewGroup) findViewById(R.id.vg);
		//可以再代码里面设置下面的参数，也可以在布局中设置
//		vg.setPaintColor(getResources().getColor(R.color.orange));
//		vg.setPaintWidth(8);
//		vg.setArrowsWidth(20);
//		vg.setDashedSpace(15);
		vg.setMeanItemIconsAndTexts(resIds, texts);
		vg.setOnMeanItemSelectListenner(new setOnMeanItemSelectListenner() {
			
			@Override
			public void onMeanSelect(int position) {
				// TODO Auto-generated method stub
				if(position != -1)
				Toast.makeText(MainActivity.this, texts[position] + "",0).show();
			}
		});
		
		bing_car_driver_ll = findViewById(R.id.bing_car_driver_ll);
		bing_car_driver_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "onClick", 0).show();
			}
		});
		
	}
}

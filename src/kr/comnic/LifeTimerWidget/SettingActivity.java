package kr.comnic.LifeTimerWidget;

import java.text.DateFormat;
import java.util.Calendar;

import net.daum.mobilead.AdHttpListener;
import net.daum.mobilead.MobileAdView;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cauly.android.ad.AdInfo;
import com.cauly.android.ad.AdListener;
import com.cauly.android.ad.AdView;

public class SettingActivity extends Activity implements AdListener, AdHttpListener{
	private static final String CAULY_AD_ID = "IO9RVJHKj";
	private static final String DAUM_AD_ID = "bafZ03T13132502d31";
	
	private RelativeLayout m_adLayout = null;
	private AdView m_caulyADView = null;
	private MobileAdView m_adamADView = null;	
	
	private LTWDBHelper m_db;
	private LTWInfomation m_info;
	/*
	//세팅값에 사용할 멤버 변수들
	String m_birthYear;
	String m_birthMonth;
	String m_birthDay;	
	int m_expAge;
	boolean m_isChk1, m_isChk2;
	String m_text1, m_text2;
	int m_kind, m_color;
	*/
	
	//Date 처리에 사용
	DateFormat fmDateAndTime = DateFormat.getDateTimeInstance();
	Calendar dateAndTime = Calendar.getInstance();

	
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			setInputDate(android.text.format.DateFormat.format("yyyy-MM-dd", dateAndTime.getTime()).toString());
		}
	};	
	
	private void setInputDate(String _date){
		Button et = (Button) findViewById(R.id.btnBirth);
		et.setText(_date);
	}

	private void clickDate(){
		new DatePickerDialog(this, d, dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH) ).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

        m_adLayout = (RelativeLayout)findViewById(R.id.adLayout);        
        AdInfo ads_info = new AdInfo();
        //ads_info.initData("", adtype, gender, age, gps, effect, allowcall, reloadInterval)
        //irmWTe609U
        ads_info.initData(CAULY_AD_ID, "CPC", "all", "all", "off", "circle", "no", 30);
       
        m_caulyADView = new AdView(this);
        m_caulyADView.setAdListener(this);
        
        m_adLayout.addView(m_caulyADView);
		
        //visibleDaumAD();
		
		m_db = new LTWDBHelper(this);

		m_info = m_db.select();
		
    	init();
    	
    	final CheckBox m_chk1 = (CheckBox) findViewById(R.id.chkText1);
    	final CheckBox m_chk2 = (CheckBox) findViewById(R.id.chkText2);

		findViewById(R.id.chkText1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!m_chk1.isChecked() && !m_chk2.isChecked()){
					Toast.makeText(SettingActivity.this, "둘 중 하나는 활성화 되어야 합니다.", Toast.LENGTH_SHORT).show();
					((CheckBox)v).setChecked(true);
				}
			}
		});
		
		findViewById(R.id.chkText2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!m_chk1.isChecked() && !m_chk2.isChecked()){
					Toast.makeText(SettingActivity.this, "둘 중 하나는 활성화 되어야 합니다.", Toast.LENGTH_SHORT).show();
					((CheckBox)v).setChecked(true);
				}
			}
		});
		
		findViewById(R.id.btnBirth).setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickDate();
			}
		});
	}
	
	private void init(){
		LinearLayout _layput = (LinearLayout) findViewById(R.id.mainLayout);
		
		Button _btnSave = (Button) _layput.findViewById(R.id.btnSave);
		_btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateDB();
				Toast t = Toast.makeText(SettingActivity.this, "저장되었습니다", Toast.LENGTH_SHORT);
				t.show();
			}
		});
		
		
		Button _btnBirth = (Button) _layput.findViewById(R.id.btnBirth);
		if(m_info.birth_year.equals("0000") || m_info.birth_month.equals("00") || m_info.birth_day.equals("00")){
			_btnBirth.setText("생년월일");
		}else{
			dateAndTime.set(Calendar.YEAR, Integer.valueOf(m_info.birth_year));
			dateAndTime.set(Calendar.MONTH, Integer.valueOf(m_info.birth_month)-1);
			dateAndTime.set(Calendar.DAY_OF_MONTH, Integer.valueOf(m_info.birth_day));
			
			_btnBirth.setText(String.format("%s-%s-%s", m_info.birth_year, m_info.birth_month, m_info.birth_day));
		}
		
		EditText _expAge = (EditText) _layput.findViewById(R.id.etExpAge);
		_expAge.setText(String.valueOf(m_info.expAge));
		
		CheckBox _chk1 = (CheckBox) _layput.findViewById(R.id.chkText1);
		_chk1.setChecked(m_info.isChk1);
	
		CheckBox _chk2 = (CheckBox) _layput.findViewById(R.id.chkText2);
		_chk2.setChecked(m_info.isChk2);
		
		EditText _text1 = (EditText) _layput.findViewById(R.id.etText1);
		_text1.setText(m_info.text1);

		EditText _text2 = (EditText) _layput.findViewById(R.id.etText2);
		_text2.setText(m_info.text2);
		
		Spinner _spKind = (Spinner) _layput.findViewById(R.id.spKind);
		_spKind.setSelection(m_info.kind);
	
		Spinner _spColor = (Spinner) _layput.findViewById(R.id.spColor);
		_spColor.setSelection(m_info.color);

	}
	
	private void updateDB(){
		LinearLayout _layput = (LinearLayout) findViewById(R.id.mainLayout);
		
		Button _btnBirth = (Button) _layput.findViewById(R.id.btnBirth);
		String birth = _btnBirth.getText().toString();
		if(birth.equals("생년월일")){
			Toast.makeText(SettingActivity.this, "생년월일을 선택하세요!", Toast.LENGTH_SHORT)
			.show();
			return;
		}else{
			m_db.update("birth_year", birth.substring(0, 4), "");
			m_db.update("birth_month", birth.substring(5, 7), "");
			m_db.update("birth_day", birth.substring(8, 10), "");
		}
		
		EditText _expAge = (EditText) _layput.findViewById(R.id.etExpAge);
		String strExpAge = _expAge.getText().toString();
		if(strExpAge.length() == 0){
			Toast.makeText(SettingActivity.this, "기대 수명을 입력하셔야 합니다!", Toast.LENGTH_SHORT)
			.show();
			return;
		}else{
			m_db.update("exp_age", strExpAge, "");
		}

		CheckBox _chk1 = (CheckBox) _layput.findViewById(R.id.chkText1);
		if(_chk1.isChecked()){
			m_db.update("chk1", "1", "");
		}else{
			m_db.update("chk1", "0", "");
		}
		
		CheckBox _chk2 = (CheckBox) _layput.findViewById(R.id.chkText2);
		if(_chk2.isChecked()){
			m_db.update("chk2", "1", "");
		}else{
			m_db.update("chk2", "0", "");
		}
		
		EditText _text1 = (EditText) _layput.findViewById(R.id.etText1);
		String strText1 = _text1.getText().toString();
		if(strText1.length() == 0){
			Toast.makeText(SettingActivity.this, "지난날 제목을 입력하셔야 합니다!", Toast.LENGTH_SHORT)
			.show();
			_text1.requestFocus();
			return;
		}else{
			m_db.update("text1", strText1, "");
		}
		
		EditText _text2 = (EditText) _layput.findViewById(R.id.etText2);
		String strText2 = _text2.getText().toString();
		if(strText2.length() == 0){
			Toast.makeText(SettingActivity.this, "남은날 제목을 입력하셔야 합니다!", Toast.LENGTH_SHORT)
			.show();
			_text2.requestFocus();
			return;
		}else{
			m_db.update("text2", strText2, "");
		}
		
		Spinner _spKind = (Spinner) _layput.findViewById(R.id.spKind);
		int nSelectKind = _spKind.getSelectedItemPosition();
		m_db.update("kind", String.valueOf(nSelectKind), "");
		
		Spinner _spColor = (Spinner) _layput.findViewById(R.id.spColor);
		int nSelectColor = _spColor.getSelectedItemPosition();
		m_db.update("color", String.valueOf(nSelectColor), "");
		
	}

	@Override
	public void onFailedToReceiveAd(boolean arg0) {
		// TODO Auto-generated method stub
		visibleDaumAD();
	}

	@Override
	public void onReceiveAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didDownloadAd_AdListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failedDownloadAd_AdListener(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private void visibleDaumAD(){
		net.daum.mobilead.AdConfig.setClientId(DAUM_AD_ID);
		m_adamADView = new MobileAdView(this);
		
		m_adamADView.setAdListener(this);
		m_adamADView.setVisibility(View.VISIBLE);
		
		m_caulyADView.setVisibility(View.GONE);
		m_adLayout.addView(m_adamADView);
	}

}

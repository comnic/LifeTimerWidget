package kr.comnic.LifeTimerWidget;

public class LTWInfomation {
	String 	birth_year;
	String 	birth_month;
	String 	birth_day;
	int		expAge;
	boolean	isChk1;
	boolean isChk2;
	String	text1;
	String	text2;
	int		kind;
	int		color;
	
	public LTWInfomation(String _birth_year, String _birth_month, String _birth_day, int _expAge, boolean _isChk1, boolean _isChk2, String _text1, String _text2, int _kind, int _color){
		birth_year = _birth_year;
		birth_month = _birth_month;
		birth_day = _birth_day;
		expAge = _expAge;
		isChk1 = _isChk1;
		isChk2 = _isChk2;
		text1 = _text1;
		text2 = _text2;
		kind = _kind;
		color = _color;
	}
}

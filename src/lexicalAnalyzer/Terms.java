package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.Queue;

public class Terms {
	
	static public void main(String[] args){
		System.out.println(IsNumber("-9555.333E-199"));
	}
	static public boolean IsKeyWord(String str){
		if (IsInArrayList(Grammar_C.KeyWord,str)){
			return true;
		}
		return false;
	}
	static public boolean IsDelimiter(String str){
		if (IsInArrayList(Grammar_C.Delimiter,str)){
			return true;
		}
		return false;
	}
	static public boolean IsOperator(String str){
		if (IsInArrayList(Grammar_C.Operator,str)){
			return true;
		}
		return false;
	}
	static public boolean IsNumber(String str){
		//return str.matches("^[-+]?(([0-9]+))+(.[0-9]+)?$");
		return str.matches("^[-+]?(([0-9]+))+(.[0-9]+)?+([eE]-?[0-9]+)?$");
		//return str.matches("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
		//return str.matches("^-?\\d+(.\\d)?");
	}
	
	static public boolean IsString(String str){
		return str.matches("\"([^\"]*)\"");
	}
	static public boolean IsVariable(String str){
		if (str.length() == 0){
			return false;
		}
		int index;
		for (index = 0;index < str.length();index ++){
			char TempChar;
			TempChar = str.charAt(index);
			if (!((TempChar >= 'a' && TempChar <= 'z')||
					(TempChar >= 'A' && TempChar <= 'Z')||
					(TempChar >= '0' && TempChar <= '9')||
					(TempChar == '_'))){
				return false;
			}
		}
		if (str.charAt(0) >= '0' && str.charAt(0) <= '9'){
			return false;
		}
		return true;
	}
	static public boolean IsInNumber(String str){
		int index;
		for (index = 0;index < str.length();index ++){
			char TempChar;
			TempChar = str.charAt(index);
			if (!((TempChar >= '0' && TempChar <= '9')||
					(TempChar == '.')||
					(TempChar == 'e')||
					(TempChar == 'E')||
					(TempChar == '-'))){
				return false;
			}
		}
		int oi = -1;
		int i = str.indexOf("-",0);
		while ( i != -1){
			oi = i;
			i = str.indexOf("-", i + 1);
		}
		if (oi == -1){
			return true;
		}
		else{
			if (oi != 0){
				return false;
			}
			else{
				return true;
			}
		}
		
	}
	
	static public boolean IsInArrayList(ArrayList<String> Array,String word){
		if (word.length() == 0){
			return false;
		}
		for (String TempStr:Array){
			if (TempStr.equals(word)){
				return true;
			}
		}
		return false;
	}
	
	static public int IsInArrayList(Queue<String> Array,String word){
		if (word.length() == 0){
			return -1;
		}
		int index = 0;
		for (String TempStr:Array){
			if (TempStr.equals(word)){
				return index;
			}
			index ++;
		}
		return -1;
	}
}

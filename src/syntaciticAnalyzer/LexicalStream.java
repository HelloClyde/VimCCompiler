package syntaciticAnalyzer;

import java.util.ArrayList;

public class LexicalStream {
	static public ArrayList<String> Article = new ArrayList<String>();
	static public void Show(){
		for (String TempStr:Article){
			System.out.print(TempStr + " ");
		}
		System.out.println();
	}
}

package syntaciticAnalyzer;

import java.util.HashSet;

public class SingleFirstSet {
	String LeftName;
	HashSet<String> Set = new HashSet<String>();
	
	public void Show(){
		System.out.print(LeftName + " : ");
		for (String Str:this.Set){
			System.out.print(Str + " ");
		}
		System.out.println();
	}
}

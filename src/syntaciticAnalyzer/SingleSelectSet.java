package syntaciticAnalyzer;

import java.util.HashSet;

public class SingleSelectSet {
	String LeftName;
	Produce CProduce;
	HashSet<String> Set = new HashSet<String>();
	public void Show() {
		System.out.print(LeftName + " ¡· ");
		for (String Str:this.CProduce.Elems){
			System.out.print(Str + " ");
		}
		System.out.print(" : ");
		for (String Str:this.Set){
			System.out.print(Str + " ");
		}
		System.out.println();
	}
}

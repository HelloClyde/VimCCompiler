package syntaciticAnalyzer;

import java.util.ArrayList;

public class Produce {
	ArrayList<String> Elems;
	
	public Produce(){
		Elems = new ArrayList<String>();
	}

	public void Show() {
		for (int i = 0;i < this.Elems.size() - 1;i ++){
			System.out.print(this.Elems.get(i) + " ");
		}
		System.out.print(this.Elems.get(this.Elems.size() - 1));
	}
	
	public boolean IsProduce$(CGramClass SrcGramClass){
		for (String Str:this.Elems){
			if (!SrcGramClass.CFinalTable.IsProduce$(Str)){
				return false;
			}
		}
		return true;
	}
	
	public int GetElemPosition(String ElemName){
		for (int i = 0;i < this.Elems.size();i ++){
			if (this.Elems.get(i).equals(ElemName)){
				return i;
			}
		}
		return -1;
	}
}

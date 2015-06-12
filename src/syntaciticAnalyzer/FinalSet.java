package syntaciticAnalyzer;

public class FinalSet {
	String ElemName;
	/*
	 * 1:能推出$,0：不能推出$，-1：未知
	 */
	int IsProduce$;
	public FinalSet(String LeftName){
		this.ElemName = LeftName;
		this.IsProduce$ = -1;
	}
}

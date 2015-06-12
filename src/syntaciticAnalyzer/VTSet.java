package syntaciticAnalyzer;

import java.util.HashSet;

public class VTSet {
	HashSet<String> CVTSet;

	public VTSet(ProduceType CProduceType) {
		this.CVTSet = new HashSet<String>();
		
		/*
		 * 遍历产生式的所有字符串
		 */
		for (SingleProduceType TempSPT:CProduceType.CSingleProduceType){
			//产生式左部不会是终结符，所以不扫描
			for (Produce TempProduce:TempSPT.RightProduce){
				for (String Str:TempProduce.Elems){
					if (Str.charAt(0) != '【'){
						this.CVTSet.add(Str);
					}
				}
			}
		}
	}
	
	public void Show(){
		for (String Str:this.CVTSet){
			System.out.println(Str);
		}
	}
	
}

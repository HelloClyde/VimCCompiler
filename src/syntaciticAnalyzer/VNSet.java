package syntaciticAnalyzer;

import java.util.HashSet;

public class VNSet {
	HashSet<String> CVNSet;

	public VNSet(ProduceType CProduceType) {
		this.CVNSet = new HashSet<String>();
		
		/*
		 * 遍历产生式的所有字符串
		 */
		for (SingleProduceType TempSPT:CProduceType.CSingleProduceType){
			//扫描产生式左部
			this.CVNSet.add(TempSPT.LeftName);
			for (Produce TempProduce:TempSPT.RightProduce){
				for (String Str:TempProduce.Elems){
					if (Str.charAt(0) == '【'){
						this.CVNSet.add(Str);
					}
				}
			}
		}
	}

	public void Show(){
		for (String Str:this.CVNSet){
			System.out.println(Str);
		}
	}
}

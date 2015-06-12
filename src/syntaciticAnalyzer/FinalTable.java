package syntaciticAnalyzer;

import java.util.ArrayList;

public class FinalTable {
	ArrayList<FinalSet> CFinalTable;
	
	public void AddFinalElem(FinalSet SrcFinalSet){
		
	}

	public FinalTable(CGramClass SrcGramClass) {
		this.CFinalTable = new ArrayList<FinalSet>();
		/*
		 * FinalSet的全部元素为VN交VT
		 */
		/*
		 * 添加VT,VT都不能推出$
		 */
		for (String TempStr:SrcGramClass.CVTSet.CVTSet){
			FinalSet NewFinalSet = new FinalSet(TempStr);
			NewFinalSet.IsProduce$ = 0;
			this.CFinalTable.add(NewFinalSet);
		}
		/*
		 * 添加VN，VN都不确定
		 */
		for (String TempStr:SrcGramClass.CVNSet.CVNSet){
			FinalSet NewFinalSet = new FinalSet(TempStr);
			NewFinalSet.IsProduce$ = -1;
			this.CFinalTable.add(NewFinalSet);
		}
		
		boolean IsModify = true;
		while (IsModify){
			IsModify = false;
			/*
			 * 如果这个非终结符有一个产生式的全部元素都能推出$则这个非终结符能推出$
			 */
			for (int index = 0;index < this.CFinalTable.size();index ++){
				FinalSet SrcFinalSet = this.CFinalTable.get(index);
				if (SrcFinalSet.IsProduce$ == -1){
					for (Produce TempProduce:SrcGramClass.CProduceType.GetProduces(SrcFinalSet.ElemName)){
						boolean IsAllProduce = true;
						for (String Str:TempProduce.Elems){
							if (!this.IsProduce$(Str)){
								IsAllProduce = false;
								break;
							}
						}
						if (IsAllProduce){
							SrcFinalSet.IsProduce$ = 1;
							IsModify = true;
							break;
						}
					}
				}
			}
		}
		
		/*
		 * 剩余的都是不能导出$的
		 */
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.IsProduce$ == -1){
				TempFinalSet.IsProduce$ = 0;
			}
		}
	}
	
	public void SetValue(String Name,int Value){
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.ElemName.equals(Name)){
				TempFinalSet.IsProduce$ = Value;
			}
		}
	}
	
	public boolean IsProduce$(String Name){
		if (Name.equals("$")){
			return true;
		}
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.ElemName.equals(Name)){
				if (TempFinalSet.IsProduce$ == 1){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}

	public void Show(){
		for (FinalSet TempFinalSet:this.CFinalTable){
			System.out.println(TempFinalSet.ElemName + (TempFinalSet.IsProduce$ == 1 ? "能导出$" : "不能导出$"));
		}
	}
	
}

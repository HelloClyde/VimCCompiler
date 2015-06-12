package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;

public class FirstSet {
	
	ArrayList<SingleFirstSet> CSingleFirstSet = new ArrayList<SingleFirstSet>();
	
	public FirstSet(CGramClass SrcGramClass) {
		/*
		 * First集合初始都是VN和VT
		 */
		for (String Str:SrcGramClass.CVNSet.CVNSet){
			SingleFirstSet NewFirstSet = new SingleFirstSet();
			NewFirstSet.LeftName = Str;
			//情况3:VN集合凡事能推出$的First集合都有$
			if (SrcGramClass.CFinalTable.IsProduce$(Str)){
				NewFirstSet.Set.add("$");
			}
			this.CSingleFirstSet.add(NewFirstSet);
		}
		for (String Str:SrcGramClass.CVTSet.CVTSet){
			SingleFirstSet NewFirstSet = new SingleFirstSet();
			NewFirstSet.LeftName = Str;
			//情况1：VT集合的FIRST集合就是它本身
			NewFirstSet.Set.add(Str);
			this.CSingleFirstSet.add(NewFirstSet);
		}
		//this.Show();
		/*
		 * 情况4:若X,Y1,Y2,...Yn都属于VN，而有产生式X->Y1Y2...Yn。当Y1,Y2,...Yi-1都能推出$时，则First(Y1)-{$},first(Y2)-{$},...,First(Yi-1)-{$},First(Yi)都包含在First(X)中
		 * 情况5：当4中所有的Yi都能推出$，(i=1,2,...,n)，则FIRST(X)=FIRST(Y1)并FIRST(Y2)并...并FIRST(Yn)并{$}
		 */
		boolean IsModify = true;
		while (IsModify){
			IsModify = false;
			for (SingleFirstSet TempSFS:this.CSingleFirstSet){
				//System.out.println(TempSFS.LeftName);
				if (SrcGramClass.CProduceType.GetProduces(TempSFS.LeftName) == null){
					continue;
				}
				for (Produce TempProduce:SrcGramClass.CProduceType.GetProduces(TempSFS.LeftName)){
					
					int CanProduceNum;
					CanProduceNum = 0;
					//判断能推出$的个数
					for (int i = 0;i < TempProduce.Elems.size();i ++){
						if (SrcGramClass.CFinalTable.IsProduce$(TempProduce.Elems.get(i))){
							CanProduceNum ++;
						}
						else{
							break;
						}
					}
					//System.out.println(TempSFS.LeftName + "-------" + CanProduceNum);
					if (CanProduceNum == TempProduce.Elems.size()){
						int OldSize = TempSFS.Set.size();
						for (int i = 0;i < CanProduceNum;i ++){
							String TempStr = TempProduce.Elems.get(i);
							TempSFS.Set.addAll(this.GetSet(TempStr));
						}
						this.GetSet(TempSFS.LeftName).add("$");
						if (TempSFS.Set.size() != OldSize){
							IsModify = true;
						}
					}
					else{
						int OldSize = TempSFS.Set.size();
						for (int i = 0;i < CanProduceNum;i ++){
							HashSet<String> TempHashSet = new HashSet<String>();
							String TempStr = TempProduce.Elems.get(i);
							TempHashSet.addAll(this.GetSet(TempStr));
							TempHashSet.remove("$");
							TempSFS.Set.addAll(TempHashSet);
						}
						TempSFS.Set.addAll(this.GetSet(TempProduce.Elems.get(CanProduceNum)));
						
						if (TempSFS.Set.size() != OldSize){
							IsModify = true;
						}
					}
				}
			}
		}
		
	}
	
	public boolean AddSet(String Name,String Str){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				int OldSet = TempSFS.Set.size();
				TempSFS.Set.add(Str);
				if (TempSFS.Set.size() == OldSet){
					return false;
				}
				else{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param Name
	 * @return
	 */
	public HashSet<String> GetSet(String Name){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				return TempSFS.Set;
			}
		}
		return null;
	}
	
	public void Show(){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			TempSFS.Show();
		}
	}
	
	public SingleFirstSet GetSFS(String Name){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				return TempSFS;
			}
		}
		return null;
	}
}

package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;

public class FollowSet {
	ArrayList<SingleFollowSet> CSingleFollowSet = new ArrayList<SingleFollowSet>();
	public FollowSet(CGramClass SrcGramClass) {
		/*
		 * Follow集合初值都是VN
		 */
		for (String Str:SrcGramClass.CVNSet.CVNSet){
			SingleFollowSet NewFollowSet = new SingleFollowSet();
			NewFollowSet.LeftName = Str;
			if (Str.equals("【函数定义】")){
				NewFollowSet.Set.add("#");
			}
			this.CSingleFollowSet.add(NewFollowSet);
		}
		boolean IsModify = true;
		while (IsModify){
			IsModify = false;
			for (SingleFollowSet TempSFS:this.CSingleFollowSet){
				int OldSize = TempSFS.Set.size();
				//搜索右式包含它的产生式
				for (SingleProduceType TempSPT:SrcGramClass.CProduceType.CSingleProduceType){
					for (Produce TempProduce:TempSPT.RightProduce){
						int ElemPostion = TempProduce.GetElemPosition(TempSFS.LeftName);
						if (ElemPostion != -1){
							boolean IsAllBlock = true;
							for (int i = ElemPostion + 1;i < TempProduce.Elems.size();i ++){
								//添加First-$
								HashSet<String> TempHashSet = new HashSet<String>();
								TempHashSet.addAll(SrcGramClass.CFirstSet.GetSet(TempProduce.Elems.get(i)));
								TempHashSet.remove("$");
								TempSFS.Set.addAll(TempHashSet);
								if (!SrcGramClass.CFinalTable.IsProduce$(TempProduce.Elems.get(i))){
									IsAllBlock = false;
									break;
								}
							}
							if (IsAllBlock){
								TempSFS.Set.addAll(this.GetSet(TempSPT.LeftName));
							}
						}
					}
				}
				if (TempSFS.Set.size() != OldSize){
					IsModify = true;
				}
			}
		}
	}
	
	public HashSet<String> GetSet(String Name){
		for (SingleFollowSet TempSFS:this.CSingleFollowSet){
			if (TempSFS.LeftName.equals(Name)){
				return TempSFS.Set;
			}
		}
		return null;
	}
	
	public void Show(){
		for (SingleFollowSet TempSFS:this.CSingleFollowSet){
			TempSFS.Show();
		}
	}

}

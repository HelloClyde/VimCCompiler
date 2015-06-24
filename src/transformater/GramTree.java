package transformater;

import java.util.ArrayList;

import syntaciticAnalyzer.GramTreeNode;
import syntaciticAnalyzer.Produce;

public class GramTree {
	public boolean IsLegal;
	public GramTreeNode GramTreeRoot;
	public GramTreeNode GramTreeNow;
	
	public GramTree(){
		GramTreeRoot = new GramTreeNode("¡¾º¯Êý¶¨Òå¡¿");
		GramTreeNow = GramTreeRoot;
	}

	public void AddProduce(Produce TempProduce) {
		//
		boolean IsProduceChild = false;
		for (String TempStr:TempProduce.Elems){
			if (!TempStr.equals("$")){
				GramTreeNow.AddChild(new GramTreeNode(TempStr));
				IsProduceChild = true;
			}
		}
		if (IsProduceChild) {
			GramTreeNow = GramTreeNow.Child;
		}
		else{
			GramTreeNow = GramTreeNow.FindNext();
		}
	}

	public void Show(){
		this.GramTreeRoot.Show();
	}
	
	public ArrayList<GramTreeNode> SearchNode(String NodeValue){
		ArrayList<GramTreeNode> DesArrayList = new ArrayList<GramTreeNode>();
		this.GramTreeRoot.Search(NodeValue, DesArrayList);
		return DesArrayList;
	}
}

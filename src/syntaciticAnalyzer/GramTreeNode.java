package syntaciticAnalyzer;

import java.util.ArrayList;

import transformater.Cwords;

public class GramTreeNode {
	public String Value;
	public Cwords Words;
	public GramTreeNode Parent;
	public GramTreeNode next;
	public GramTreeNode Child;
	
	public GramTreeNode(String SrcValue){
		this.Value = SrcValue;
		this.Parent = null;
		this.next = null;
		this.Child = null;
	}
	
	public GramTreeNode FindNext(){
		GramTreeNode TempNode = this;
		while (TempNode.next == null){
			if (TempNode.Parent == null){
				return null;
			}
			else{
				TempNode = TempNode.Parent;
			}
		}
		return TempNode.next;
	}
	
	public void AddChild(GramTreeNode SrcNode){
		SrcNode.Parent = this;
		if (this.Child == null){
			this.Child = SrcNode;
		}
		else{
			GramTreeNode TempNode = this.Child;
			while (TempNode.next != null){
				TempNode = TempNode.next;
			}
			TempNode.next = SrcNode;
		}
	}
	
	public void Show(){
		System.out.println("Value:" + this.Value);
		GramTreeNode TempNode = this.Child;
		if (TempNode != null) System.out.print("Child:");
		while (TempNode != null){
			TempNode.Show();
			TempNode = TempNode.next;
		}
	}
	
	public void Search(String DesValue,ArrayList<GramTreeNode> DesArray){
		if (this.Value.equals(DesValue)){
			DesArray.add(this);
		}
		GramTreeNode TempGramTreeNode = this.Child;
		while (TempGramTreeNode != null){
			TempGramTreeNode.Search(DesValue, DesArray);
			TempGramTreeNode = TempGramTreeNode.next;
		}
	}
}

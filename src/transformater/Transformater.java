package transformater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.GramTreeNode;

public class Transformater {

	public Transformater(GramTree MainGramTree, SourceStream MainSourceStream) {
		try {
			File ObjFile = new File("D:/HCCCompiler/BBasicObj.txt");
			PrintWriter ObjWriter = new PrintWriter(ObjFile);
			ObjWriter.println("CCompiler Ver 1.0");
			ObjWriter.println("Author: HelloClyde");
			ObjWriter.println(";======MainProg======");
			ObjWriter.println("call fint_vint_main");
			ObjWriter.println("exit");
			ObjWriter.println(";======FuncProg======");
			
			//搜索所有函数,进行登记
			ArrayList<GramTreeNode> FunctionNode = MainGramTree.SearchNode("【单个函数】");
			for (GramTreeNode TempNode:FunctionNode){
				//System.out.println(FindFunctionName(TempNode).Words.Value);
				
			}
			ObjWriter.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/*
	 * 搜索函数名node
	 */
	GramTreeNode FindFunctionName(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("【变量】")){
				return TempGramTreeNode.Child.Child;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
}

package transformater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.GramTreeNode;

public class Transformater {
	String FunctionName;
	ArrayList<String> TotalVarTable;
	ArrayList<VarTable> VarTableList;
	int LabelIndex = 0;

	public Transformater(GramTree MainGramTree, SourceStream MainSourceStream) {
		try {
			File ObjFile = new File("D:/HCCCompiler/BBasicObj.txt");
			PrintWriter ObjWriter = new PrintWriter(ObjFile);
			ObjWriter.println(";CCompiler Ver 1.0");
			ObjWriter.println(";Author: HelloClyde");
			ObjWriter.println(";======MainProg======");
			ObjWriter.println("call fint_vint_main");
			ObjWriter.println("exit");
			
			ObjWriter.println(";======FuncProg======");
			//搜索所有函数,进行登记
			ArrayList<GramTreeNode> FunctionNode = MainGramTree.SearchNode("【单个函数】");
			//变量表
			this.TotalVarTable = new ArrayList<String>();
			for (GramTreeNode FunNode:FunctionNode){
				//获取函数名
				this.FunctionName = String.valueOf(FindFunctionName(FunNode).Words.Value);
				//将函数名加入变量表,用于返回值
				TotalVarTable.add("vint_" + FunctionName);
				//获取参数数据
				this.VarTableList = this.GetVarTable(this.FindFunVarDeclare(FunNode));
				//保存堆栈状态，并对堆栈初始化
				ObjWriter.println("fint_vint_" + FunctionName + ":");
				ObjWriter.println("push rb");
				ObjWriter.println("ld int rb,rs");
				ObjWriter.println("cal int add rs,-4");
				//获取函数块
				GramTreeNode FunBlock = this.FindFunBlock(FunNode);
				GramTreeNode FunVarDelare = FunBlock.Child;
				GramTreeNode FunDoBlock = FunBlock.Child.next;
				//把声明写入变量表中
				ArrayList<VarTable> TempVarTableList = this.GetVarTable(FunVarDelare);
				for (VarTable TempVarTable:TempVarTableList){
					TotalVarTable.add("vint_" + FunctionName + "_" + TempVarTable.VarName);
				}
				//函数块闭包处理
				FunDo(ObjWriter,FunDoBlock,FunctionName);
				//函数退出
				ObjWriter.println("vint_" + FunctionName + "_exit:");
				//恢复堆栈状态
				ObjWriter.println("ld int rs,rb");
				ObjWriter.println("pop rb");
				ObjWriter.println("ret");
			}
			//写变量表
			ObjWriter.println(";======BinData======");
			for (String Str:TotalVarTable){
				ObjWriter.println("data " + Str + " int 0");
			}
			ObjWriter.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//【函数块】
	void FunBlock(PrintWriter DesPWriter,GramTreeNode FunBlockBlock){
		//声明语句不处理
		//函数块闭包
		this.FunDo(DesPWriter, FunBlockBlock.Child.next, this.FunctionName);
	}
	
	void FunDo(PrintWriter DesPWriter,GramTreeNode FunDoBlock,String FunName){
		if (FunDoBlock.Child == null){
			return;
		}
		else{
			if (FunDoBlock.Child.Value.equals("【赋值函数】")){
				VarTo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("【for循环】")){
				ForDo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("【条件语句】")){
				IfDo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("【函数返回】")){
				ReturnDo(DesPWriter,FunDoBlock.Child,FunName);
			}
			FunDo(DesPWriter,FunDoBlock.Child.next,FunName);
		}
	}
	
	//表达式
	void CalDo(PrintWriter DesWriter,GramTreeNode CalDoBlock){
		CalA(DesWriter,CalDoBlock.Child);
		CalB(DesWriter,CalDoBlock.Child.next);
	}
	
	//项
	void CalB(PrintWriter DesWriter,GramTreeNode CalBBlock){
		if (CalBBlock.Child != null){
			//备份r0至r1
			DesWriter.println("ld int r1,r0");
			CalA(DesWriter,CalBBlock.Child.next);
			if (CalBBlock.Child.Value.equals("+")){
				DesWriter.println("cal int add r1,r0");
			}
			else if (CalBBlock.Child.Value.equals("-")){
				DesWriter.println("cal int sub r1,r0");
			}
			DesWriter.println("ld int r0,r1");
			CalB(DesWriter,CalBBlock.Child.next.next);
		}
	}
	
	//因子
	void CalA(PrintWriter DesWriter,GramTreeNode CalABlock){
		CalS(DesWriter,CalABlock.Child);
		CalSa(DesWriter,CalABlock.Child.next);
	}
	
	//因式递归
	void CalSa(PrintWriter DesWriter,GramTreeNode CalSaBlock){
		if (CalSaBlock.Child != null){
			//备份r0至r1
			DesWriter.println("ld int r1,r0");
			//计算下一个因式,存在r0
			CalS(DesWriter,CalSaBlock.Child.next);
			if (CalSaBlock.Child.Value.equals("*")){
				DesWriter.println("cal int mul r1,r0");
			}
			else if (CalSaBlock.Child.Value.equals("/")){
				DesWriter.println("cal int div r1,r0");
			}
			else if (CalSaBlock.Child.Value.equals("%")){
				DesWriter.println("cal int mod r1,r0");
			}
			//存至r0
			DesWriter.println("ld int r0,r1");
		}
	}
	
	void VarOrFun(PrintWriter DesWriter,GramTreeNode VarOrFunBlock){
		if (VarOrFunBlock.Child.next.Child == null){
			//变量
			DesWriter.println("ld int r0," + VarOrFunBlock.Child.Child.Child.Words.Value);
		}
		else{
			//函数调用
			//压参数
			PushVar(DesWriter,VarOrFunBlock.Child.next.Child.next);
			//Call指令
			DesWriter.println("call fint_vint_" + VarOrFunBlock.Child.Child.Child.Words.Value);
		}
	}
	
	/*
	 * 参数列表,压参数入栈
	 */
	void PushVar(PrintWriter DesWriter,GramTreeNode VarList){
		ArrayList<GramTreeNode> GramTreeNodeArray = new ArrayList<GramTreeNode>();
		VarList.Search("【参数】",GramTreeNodeArray);
		for (GramTreeNode TempTreeNode:GramTreeNodeArray){
			if (TempTreeNode.Child.Value.equals("【标志符】")){
				String VarName = TempTreeNode.Child.Child.Words.Value;
				int VarIndex = this.IsInVarTableList(VarName);
				if (VarIndex == -1){
					DesWriter.println("push " + "[vint_" + this.FunctionName + "_" + VarName + "]");
				}
				else{
					//调整栈
					DesWriter.println("ld int r0,rb");
					DesWriter.println("cal int add r0," + (VarIndex - 1));
					//压栈
					DesWriter.println("push " + "[r0]");
				}
			}
			else if (TempTreeNode.Child.Value.equals("【数字】")){
				DesWriter.println("push " + TempTreeNode.Child.Words.Value);
			}
			//字符串处理并不正确
			else if (TempTreeNode.Child.Value.equals("【字符串】")){
				DesWriter.println("push " + TempTreeNode.Child.Child.Words.Value);
			}
		}
	}
	
	int IsInVarTableList(String Str){
		for (VarTable TempVarTable:this.VarTableList){
			if (TempVarTable.VarName.equals(Str)){
				return TempVarTable.Index;
			}
		}
		return -1;
	}
	
	//因式
	void CalS(PrintWriter DesWriter,GramTreeNode CalSBlock){
		GramTreeNode CalNode = CalSBlock;
		if (CalNode.Child.Value.equals("(")){
			//计算表达式
			CalDo(DesWriter,CalNode.Child.next);
		}
		else if (CalNode.Child.Value.equals("【变量或函数调用】")){
			VarOrFun(DesWriter,CalNode.Child);
		}
		else if (CalNode.Child.Value.equals("【数字】")){
			DesWriter.println("ld int r0," + CalNode.Child.Child.Words.Value);
		}
	}
	
	void ReturnDo(PrintWriter DesWriter,GramTreeNode ReturnDoBlock,String FunName){
		//计算因式
		//计算完的结果都保存在r0中
		CalS(DesWriter,ReturnDoBlock.Child.next);
		DesWriter.println("ld int vint_" + FunName + ",r0");
		DesWriter.println("jmp " + "vint_" + FunName + "_exit");
	}
	
	//【多重逻辑表达式】
	void MulLogicExpression(PrintWriter DesWriter,GramTreeNode MulLogicExpressionBlock){
		this.LogicExpression(DesWriter, MulLogicExpressionBlock.Child);
		this.MulLogicExpressionRecursive(DesWriter, MulLogicExpressionBlock.Child.next);
	}
	
	//【逻辑表达式】
	void LogicExpression(PrintWriter DesWriter,GramTreeNode LogicExpressionBlock){
		//表达式
		this.CalDo(DesWriter, LogicExpressionBlock.Child);
		this.LogicExpressionSuffix(DesWriter, LogicExpressionBlock.Child.next);
	}
	
	//【逻辑表达式后缀】
	void LogicExpressionSuffix(PrintWriter DesWriter,GramTreeNode LogicExpressionSuffixBlock){
		if (LogicExpressionSuffixBlock.Child != null){
			//备份r0至r1
			DesWriter.println("ld int r1,r0");
			this.CalDo(DesWriter, LogicExpressionSuffixBlock.Child.next);
			DesWriter.println("cmp int r0,r1");
			if (LogicExpressionSuffixBlock.Child.Child.Value.equals("<")){
				DesWriter.println("ld int r0,be");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals(">")){
				DesWriter.println("ld int r0,ae");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("==")){
				DesWriter.println("ld int r0,z");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("!=")){
				DesWriter.println("ld int r0,nz");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals(">=")){
				DesWriter.println("ld int r0,b");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("<=")){
				DesWriter.println("ld int r0,a");
			}
		}
	}
	
	//【多重逻辑表达式递归】
	void MulLogicExpressionRecursive(PrintWriter DesWriter,GramTreeNode MulLogicExpressionRecursiveBlock){
		if (MulLogicExpressionRecursiveBlock.Child != null){
			//备份r0至r1
			DesWriter.println("ld int r1,r0");
			this.LogicExpression(DesWriter, MulLogicExpressionRecursiveBlock.Child.next);
			if (MulLogicExpressionRecursiveBlock.Child.Child.Value.equals("&&")){
				DesWriter.println("cal int mul r0,r1");
			}
			else if (MulLogicExpressionRecursiveBlock.Child.Child.Value.equals("||")){
				DesWriter.println("cal int add r0,r1");
			}
			this.MulLogicExpressionRecursive(DesWriter, MulLogicExpressionRecursiveBlock.Child.next.next);
		}
	}
	
	//【条件语句】
	void IfDo(PrintWriter DesWriter,GramTreeNode IfDoBlock){
		//【多重逻辑表达式】,结果存r0
		this.MulLogicExpression(DesWriter, IfDoBlock.Child.next.next);
		DesWriter.println("cmp int r0,1");
		//跳转至else
		DesWriter.println("jpc nz label" + this.LabelIndex + "_else");
		//运行函数块
		this.FunBlock(DesWriter, IfDoBlock.Child.next.next.next.next.next);
		DesWriter.println("jmp label" + this.LabelIndex + "_end");
		this.ElseDo(DesWriter, IfDoBlock.Child.next.next.next.next.next.next.next);
		//结束标签
		DesWriter.println("label" + this.LabelIndex + "_end:");
		this.LabelIndex ++;
	}
	
	//【否则语句】
	void ElseDo(PrintWriter DesWriter,GramTreeNode ElseDoBlock){
		//写标签
		DesWriter.println("label" + this.LabelIndex + "_else:");
		if (ElseDoBlock.Child != null){
			this.FunBlock(DesWriter, ElseDoBlock.Child.next.next);
		}
	}
	
	//【for循环】
	void ForDo(PrintWriter DesWriter,GramTreeNode ForDoBlock){
		this.VarTo(DesWriter,ForDoBlock.Child.next.next);
		DesWriter.println("for" + this.LabelIndex + "_start:");
		//计算循环条件
		this.LogicExpression(DesWriter, ForDoBlock.Child.next.next.next);
		DesWriter.println("cmp int r0,1");
		DesWriter.println("jpc nz for" + this.LabelIndex + "_end");
		this.FunBlock(DesWriter, ForDoBlock.Child.next.next.next.next.next.next.next.next);
		this.SuffixExpression(DesWriter, ForDoBlock.Child.next.next.next.next.next);
		DesWriter.println("jmp for" + this.LabelIndex + "_start:");
		DesWriter.println("for" + this.LabelIndex + "_end:");
		this.LabelIndex ++;
	}
	
	//【后缀表达式】
	void SuffixExpression(PrintWriter DesWriter,GramTreeNode SuffixExpressionBlock){
		if (SuffixExpressionBlock.Child.next.Child.Words.Value.equals("++")){
			DesWriter.println("cal int add [vint_" + this.FunctionName + "_" + SuffixExpressionBlock.Child.Child.Child.Words.Value + "],1");
		}
		else if (SuffixExpressionBlock.Child.next.Child.Words.Value.equals("--")){
			DesWriter.println("cal int sub [vint_" + this.FunctionName + "_" + SuffixExpressionBlock.Child.Child.Child.Words.Value + "],1");
		}
	}
	
	//【赋值函数】
	void VarTo(PrintWriter DesWriter,GramTreeNode VarToBlock){
		if (VarToBlock.Child.next.Child.Value.equals("=")){
			//赋值
			CalDo(DesWriter,VarToBlock.Child.next.Child.next.Child);
			//判断是否在栈中
			int VarIndex = this.IsInVarTableList(VarToBlock.Child.Child.Child.Value);
			if (VarIndex == -1){
				DesWriter.println("ld int [vint_" + this.FunctionName + "_" + VarToBlock.Child.Child.Child.Value + "],r0");
			}
			else{
				//备份r0至r1
				DesWriter.println("ld int r1,r0");
				//调整栈
				DesWriter.println("ld int r0,rb");
				DesWriter.println("cal int add r0," + (VarIndex - 1));
				DesWriter.println("ld int [r0],r1");
			}
		}
		else if (VarToBlock.Child.next.Child.Value.equals("(")){
			//函数调用
			//关键字vasm处理，直接导入汇编
			if (VarToBlock.Child.Child.Child.Words.Value.toLowerCase().equals("vasm")){
				ArrayList<GramTreeNode> TempArrayList = new ArrayList<GramTreeNode>();
				VarToBlock.Child.next.Child.next.Search("string", TempArrayList);
				String TempStr = TempArrayList.get(0).Words.Value;
				DesWriter.println(TempStr.substring(1,TempStr.length() - 1).toLowerCase());
			}
			else{
				//压参数
				PushVar(DesWriter,VarToBlock.Child.next.Child.next);
				//Call指令
				DesWriter.println("call fint_vint_" + VarToBlock.Child.Child.Child.Words.Value);
			}
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
	
	/*
	 * 搜索参数声明
	 */
	GramTreeNode FindFunVarDeclare(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("【参数声明】")){
				return TempGramTreeNode;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
	
	/*
	 * 搜索函数块
	 */
	GramTreeNode FindFunBlock(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("【函数块】")){
				return TempGramTreeNode;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
	
	/*
	 * 获取所有参数，并存表中返回
	 */
	ArrayList<VarTable> GetVarTable(GramTreeNode DesNode){
		ArrayList<VarTable> VarTableArray = new ArrayList<VarTable>();
		ArrayList<GramTreeNode> GramTreeNodeArray = new ArrayList<GramTreeNode>();
		DesNode.Search("【声明】", GramTreeNodeArray);
		int VarIndex = 0;
		for (GramTreeNode TempGramTreeNode:GramTreeNodeArray){
			VarTable TempVarTable = new VarTable();
			TempVarTable.VarName = TempGramTreeNode.Child.next.next.Child.Child.Words.Value;
			//System.out.println("Var:" + TempVarTable.VarName);
			TempVarTable.Index = VarIndex;
			VarTableArray.add(TempVarTable);
			VarIndex ++;
		}
		return VarTableArray;
	}
}

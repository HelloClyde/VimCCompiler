package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class CGramClass {
	/**
	 * 测试用
	 * @param args
	 */
	static public void main(String[] args){
		CGramClass TestCGramClass = new CGramClass("D:\\HCCCompiler\\grammar.txt");
		TestCGramClass.Show();
	}
	/*
	 * 储存产生式
	 */
	ProduceType CProduceType;
	/*
	 * 储存VT终结符表
	 */
	VTSet CVTSet;
	/*
	 * 储存VN非终结符表
	 */
	VNSet CVNSet;
	/*
	 * 储存能否推出$的表
	 */
	FinalTable CFinalTable;
	/*
	 * 储存First集合
	 */
	FirstSet CFirstSet;
	/*
	 * 储存Follow集合
	 */
	FollowSet CFollowSet;
	/*
	 * 储存Select集合
	 */
	SelectSet CSelectSet;
	/*
	 * 储存预测分析表
	 */
	PreciateTable CPreciateTable;
	public CGramClass(String FilePath){
		this.CProduceType = new ProduceType(FilePath);
		this.CVTSet = new VTSet(this.CProduceType);
		this.CVNSet = new VNSet(this.CProduceType);
		this.CFinalTable = new FinalTable(this);
		this.CFirstSet = new FirstSet(this);
		this.CFollowSet = new FollowSet(this);
		this.CSelectSet = new SelectSet(this);
		this.CPreciateTable = new PreciateTable(this);
	}
	
	public void Show(){
		System.out.println("\n语法：");
		this.CProduceType.Show();
		System.out.println("\n终结符：");
		this.CVTSet.Show();
		System.out.println("\n非终结符：");
		this.CVNSet.Show();
		System.out.println("\n$符推导表");
		this.CFinalTable.Show();
		System.out.println("\nFirst集合");
		this.CFirstSet.Show();
		System.out.println("\nFollow集合");
		this.CFollowSet.Show();
		System.out.println("\nSelect集合");
		this.CSelectSet.Show();
		System.out.println("\n预测分析表");
		this.CPreciateTable.Show();
	}

	public boolean IsLegal(ArrayList<String> Article) {
		Stack<String> AnlyStack = new Stack<String>();
		Stack<String> StrStack = new Stack<String>();
		//压入S
		AnlyStack.push("#");
		AnlyStack.push("【函数定义】");
		//压入Article
		StrStack.push("#");
		for (int i = Article.size() - 1;i >= 0;i --){
			StrStack.push(Article.get(i));
		}
		//步骤计数器
		int StepsCounts;
		StepsCounts = 1;
		while (!(AnlyStack.isEmpty() && StrStack.isEmpty())){
			if (AnlyStack.isEmpty() || StrStack.isEmpty()){
				System.out.println("预测分析发生错误。分析栈或者剩余串为空");
				return false;
			}
			else{
				System.out.print(StepsCounts + "\t");
				//输出分析栈
				for (String TempStr:AnlyStack){
					System.out.print(TempStr + " ");
				}
				System.out.print("\t");
				//输出剩余串
				for (String TempStr:StrStack){
					System.out.print(TempStr + " ");
				}
				System.out.print("\t");
				String AnlyStr = AnlyStack.pop();
				String StrStr = StrStack.peek();
				if (AnlyStr.equals(StrStr)){
					System.out.println(StrStr + "匹配");
					StrStack.pop();
				}
				else{
					Produce TempProduce;
					TempProduce = this.CPreciateTable.GetProduce(AnlyStr, StrStr);
					if (TempProduce == null){
						System.out.println(StrStr + "匹配失败");
						return false;
					}
					else{
						TempProduce.Show();
						System.out.println();
						for (int i = TempProduce.Elems.size() - 1; i >= 0;i --){
							String TempStr = TempProduce.Elems.get(i);
							if (!TempStr.equals("$")){
								AnlyStack.push(TempProduce.Elems.get(i));
							}
						}
					}
				}
				StepsCounts ++;
			}
		}
		System.out.println("匹配成功!");
		return true;
	}
	
	
	public boolean IsLL1(){
		for (SingleProduceType TempSPT:this.CProduceType.CSingleProduceType){
			for (Produce TempProduce:TempSPT.RightProduce){
				for (Produce TempProduceOthers:TempSPT.RightProduce){
					if (TempProduce != TempProduceOthers){
						HashSet<String> TempHashSet = new HashSet<String>();
						TempHashSet.addAll(this.CSelectSet.GetSet(TempProduce));
						TempHashSet.retainAll(this.CSelectSet.GetSet(TempProduceOthers));
						if (!TempHashSet.isEmpty()){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}

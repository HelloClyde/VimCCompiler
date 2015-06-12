package syntaciticAnalyzer;

import java.util.ArrayList;

public class SingleProduceType {
	String LeftName;
	ArrayList<Produce> RightProduce;
	
	public SingleProduceType(String Name){
		this.LeftName = Name;
		this.RightProduce = new ArrayList<Produce>();
	}
	
	/**
	 * 
	 * @param StrArray
	 * 添加不判断是否重复
	 */
	public void AddRightProduce(String[] StrArray){
		Produce TempProduce = new Produce();
		for (String TempStr:StrArray){
			TempProduce.Elems.add(TempStr);
		}
		this.RightProduce.add(TempProduce);
	}

	public void Show() {
		System.out.print(LeftName + "》 ");
		for (int i = 0;i < this.RightProduce.size() - 1;i ++){
			this.RightProduce.get(i).Show();
			System.out.print(" | ");
		}
		this.RightProduce.get(this.RightProduce.size() - 1).Show();
		System.out.println();
	}
}

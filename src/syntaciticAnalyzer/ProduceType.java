package syntaciticAnalyzer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ProduceType {
	/**
	 * 测试用
	 * @param args
	 */
	static public void main(String[] args){
		ProduceType TestProduceType = new ProduceType("D:\\HCCCompiler\\grammar.txt");
		TestProduceType.Show();
	}
	ArrayList<SingleProduceType> CSingleProduceType = new ArrayList<SingleProduceType>();

	public ProduceType(String FilePath) {
		Scanner MyScanner;
		try {
			MyScanner = new Scanner(new FileReader(FilePath));
		
			while (MyScanner.hasNextLine()){
				/*
				 * 使用空格分离获得字符串数组
				 */
				String[] TempStrArray = MyScanner.nextLine().split(" +");
				//获得产生式左部名字
				String GramElemName = TempStrArray[0];
				//添加
				this.AddSingleProduceType(GramElemName, TempStrArray);
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param LeftName
	 * @param SrcStrArray
	 */
	public void AddSingleProduceType(String LeftName,String[] SrcStrArray){
		/*
		 * debug
		 */
		//System.out.println(LeftName + "," + SrcStrArray);
		SingleProduceType TempSingleProduceType = null;
		for (SingleProduceType TempSPY:CSingleProduceType){
			if (TempSPY.LeftName.equals(LeftName)){
				TempSingleProduceType = TempSPY;
				break;
			}
		}
		/*
		 * 未找到新建
		 */
		if (TempSingleProduceType == null){
			TempSingleProduceType = new SingleProduceType(LeftName);
			this.CSingleProduceType.add(TempSingleProduceType);
		}
		String[] TempStrArray = new String[SrcStrArray.length - 2];
		/*
		 * 复制String数组,从2开始
		 */
		for (int i = 0;i < TempStrArray.length;i ++){
			TempStrArray[i] = SrcStrArray[i + 2];
		}
		TempSingleProduceType.AddRightProduce(TempStrArray);
		
	}
	
	public void Show(){
		for (SingleProduceType TempSPT:this.CSingleProduceType){
			TempSPT.Show();
		}
	}
	
	/**
	 * 
	 * @param SrcLeftName
	 * @return 如果返回null表示没有这个符号的产生式
	 */
	public ArrayList<Produce> GetProduces(String SrcLeftName){
		for (SingleProduceType TempSPT:this.CSingleProduceType){
			if (TempSPT.LeftName.equals(SrcLeftName)){
				return TempSPT.RightProduce;
			}
		}
		return null;
	}

}

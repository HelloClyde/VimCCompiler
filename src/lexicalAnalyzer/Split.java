package lexicalAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Split {
	static public void main(String[] args){
		try {
			ArrayList<TermsTable> Article;
			ArrayList<String> mainVariable = new ArrayList<String>();
			Article = FromStream("D:/HCCCompiler/test.tx2");
			File desFile = new File("D:/HCCCompiler/TempResult.txt");
			PrintWriter pw;
			pw = new PrintWriter(desFile);
			File desFile2 = new File("D:/HCCCompiler/mainVariable.txt");
			PrintWriter pw2;
			pw2 = new PrintWriter(desFile2);
			
			for (TermsTable Str:Article){
				if (!Str.Str.trim().isEmpty())
					pw.println(Str.Str + "\t\t\t\t:" + Str.Kind);
			}
	        pw.close();
	        
	        //标识符表
	        for (TermsTable TempTable:Article){
	        	if (TempTable.Kind.equals("变量")){
	        		if (Terms.IsInArrayList(VariableTable.data, TempTable.Str) == -1){
	        			VariableTable.data.add(TempTable.Str);
	        		}
	        	}
	        }
	        //保存标识符表
	        int printIndex = 0;
	        for (String Str:VariableTable.data){
				pw2.println(Str + "\t" + printIndex);
				printIndex ++;
			}
	        pw2.close();
	        //最后处理结果
	        File desFile3 = new File("D:/HCCCompiler/LexiclaAnalysisResult.txt");
	        PrintWriter pw3 = new PrintWriter(desFile3);
	        for (TermsTable TempTable:Article){
	        	if (!TempTable.Str.trim().isEmpty()){
		        	int VariableIndex;
		        	VariableIndex = Terms.IsInArrayList(VariableTable.data, TempTable.Str);
		        	if (VariableIndex != -1){
		        		pw3.println(TempTable.Str + "\t\t\t\t:" + TempTable.Kind + "\t\t\t\t" + VariableIndex);
		        	}
		        	else{
		        		pw3.println(TempTable.Str + "\t\t\t\t:" + TempTable.Kind);
		        	}
	        	}
	        }
	        pw3.close();
        } catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//System.out.println(Terms.IsKeyWord("void"));
	}
	
	/**
	 * 扫描文件内容，对其进行分词，分词结果返回，词汇信息存入ClassValue表。
	 * @param FilePath
	 * @return 分词结果队列
	 */
	static public ArrayList<TermsTable> FromStream(String FilePath){
		try {
			ArrayList<TermsTable> desTable = new ArrayList<TermsTable>();
			ArrayList<String> desString = new ArrayList<String>();
			Scanner s;
			s = new Scanner(new FileReader(FilePath));
			while (s.hasNextLine()){
				String TempStr;
				TempStr = s.nextLine();
				int SplitStart;//分词状态，1正分词，2字符串处理
				SplitStart = 1;
				StringBuffer TempString = new StringBuffer("");
				for (int CharIndex = 0;CharIndex < TempStr.length();CharIndex ++){
					char TempChar;
					TempChar = TempStr.charAt(CharIndex);
					//分词进行时
					if (SplitStart == 1){
						if (TempChar == '\"'){
							desString.add(String.valueOf(TempString));
							TempString = new StringBuffer("");
							TempString.append(TempChar);
							SplitStart = 2;
						}
						else{
							String TTStr;
							TTStr = String.valueOf(TempString) + TempChar;
							if (Terms.IsString(TTStr)||
									Terms.IsDelimiter(TTStr) ||
									Terms.IsInNumber(TTStr)||
									Terms.IsOperator(TTStr)||
									Terms.IsVariable(TTStr)
									){
								TempString.append(TempChar);
							}
							else{
								desString.add(String.valueOf(TempString));
								TempString = new StringBuffer("");
								TempString.append(TempChar);
							}
						}
					}
					else if(SplitStart == 2){
						if (TempChar == '\"'){
							TempString.append(TempChar);
							SplitStart = 1;
						}
						else{
							TempString.append(TempChar);
						}
					}
				}
				desString.add(String.valueOf(TempString));
				TempString = new StringBuffer("");
				//
				/*
				System.out.println(TempStr);
				String[] TempStrA;
				TempStrA = TempStr.split("\\b");
				*/
			}
			/*
			for (String Str:desString){
				System.out.println(Str);
			}
			*/
			for (String Str:desString){
				if (Terms.IsKeyWord(Str)){
					desTable.add(new TermsTable(Str,"关键字"));
				}
				else if (Terms.IsNumber(Str)){
					desTable.add(new TermsTable(Str,"数字常量"));
				}
				else if (Terms.IsOperator(Str)){
					desTable.add(new TermsTable(Str,"运算符"));
				}
				else if (Terms.IsDelimiter(Str)){
					desTable.add(new TermsTable(Str,"界符"));
				}
				else if (Terms.IsVariable(Str)){
					desTable.add(new TermsTable(Str,"变量"));
				}
				else if (Terms.IsString(Str)){
					desTable.add(new TermsTable(Str,"字符串常量"));
				}
				else{
					/*
					for (int i = 0;i < Str.length();i ++){
						char TempChar;
						TempChar = Str.charAt(i);
						String TempStr;
						TempStr = String.valueOf(TempChar);
						if (Terms.IsOperator(TempStr)){
							desTable.add(new TermsTable(TempStr,"运算符"));
						}
						else if (Terms.IsDelimiter(TempStr)){
							desTable.add(new TermsTable(TempStr,"界符"));
						}
						else{
							if (!TempStr.trim().isEmpty())
								desTable.add(new TermsTable(TempStr,"其他"));
						}
					}
					*/
					desTable.add(new TermsTable(Str,"其他"));
				}
			}
			return desTable;
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
	}
}

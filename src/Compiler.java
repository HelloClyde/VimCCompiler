import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.*;
import transformater.GramTree;
import transformater.SourceStream;
import transformater.Transformater;
import lexicalAnalyzer.*;
import macroProcessing.*;

public class Compiler {
	static public void main(String[] args){
		try {
			PrintStream PsFile = new PrintStream("D:/HCCCompiler/Result.txt");  
            System.setOut(PsFile);
			
			String FilePath;
			//文件处理路径
			FilePath = "D:/HCCCompiler/test.txt";
			//进行微处理，（导入头文件、替换define定义、清除注释)）
			FilePath = MacroProcessing.Deal(FilePath);
			
			//分离单词，信息存入article中
			ArrayList<TermsTable> Article;
			Article = Split.FromStream(FilePath);
			
			File desFile2 = new File("D:/HCCCompiler/mainVariable.txt");
			PrintWriter pw2;
			pw2 = new PrintWriter(desFile2);
	        
	        //讲计算出的标识符表保存至VariableTable类中
	        for (TermsTable TempTable:Article){
	        	if (TempTable.Kind.equals("变量")){
	        		if (Terms.IsInArrayList(VariableTable.data, TempTable.Str) == -1){
	        			VariableTable.data.add(TempTable.Str);
	        		}
	        	}
	        }
	        
	        //保存标识符表到文件
	        int printIndex = 0;
	        for (String Str:VariableTable.data){
				pw2.println(Str + "\t" + printIndex);
				printIndex ++;
			}
	        pw2.close();
	        
	        //保存词法分析结果至文件
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
	        
	        //将分词结果转换成语法分析要用的句子保存
	        SourceStream MainSourceStream = new SourceStream(Article);
	        //LexicalStream.Show();
	        //
	        
	        /*
	         * 初始化语法分析器，生成预测表
	         */
	        CGramClass MainCGramClass = new CGramClass("D:\\HCCCompiler\\grammar.txt");
	        MainCGramClass.Show();
	        if (MainCGramClass.IsLL1()){
	        	System.out.println("语法是LL(1)！");
	        }
	        else{
	        	System.out.println("语法不是LL(1)！");
	        }
	        GramTree MainGramTree = MainCGramClass.IsLegal(MainSourceStream);
	        MainGramTree.Show();
	        //翻译成BB汇编过程
	        Transformater MainTransformater = new Transformater(MainGramTree,MainSourceStream);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

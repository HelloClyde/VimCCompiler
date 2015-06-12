package macroProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CleanExplain {
	static public void main(String[] args){
		Clean("D:/HCCCompiler/test.txt");
	}
	/**
	 * 清除代码中的注释，注释不支持嵌套
	 * */
	static public String Clean(String FilePath){
		try {
			Scanner s = new Scanner(new FileReader(FilePath));
			ArrayList<String> desString = new ArrayList<String>();
			boolean IsExplain;
			IsExplain = false;
			while (s.hasNextLine()){
				String LineString;
				LineString = s.nextLine();
				if (IsExplain){
					desString.add("");
					if (LineString.indexOf("*/") != -1){
						IsExplain = false;
					}
				}
				else{
					if (LineString.indexOf("/*") != -1){
						desString.add("");
						IsExplain = true;
					}
					else if (LineString.indexOf("//") != -1){
						desString.add(LineString.substring(0, LineString.indexOf("//")));
					}
					else{
						desString.add(LineString);
					}
				}
				
			}
			File desFile = new File(FilePath.substring(0, FilePath.length() - 4) + ".tx2");
			PrintWriter pw = new PrintWriter(desFile);
	        for (String LineString:desString){
	        	pw.println(LineString);
	        }
	        pw.close();
	        return desFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		
	}
}

package macroProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class importHead {
	static public void main(String[] args){
		importH("D:/HCCCompiler/test.txt");
		/*
		String LineString = "for (outN = 1;outN <= Max;outN += 2){";
		System.out.println(LineString.replaceAll("Max", "9"));
		System.out.println(LineString);
		*/
	}
	static public String importH(String FilePath){
		Scanner s;
		try {
			s = new Scanner(new FileReader(FilePath));
		
			ArrayList<String> desString = new ArrayList<String>();
			while (s.hasNextLine()){
				String LineString;
				LineString = s.nextLine();
				if (LineString.startsWith("#include")){
					String HeadString;
					HeadString = LineString.split(" ")[1];
					HeadString = HeadString.substring(1,HeadString.length() - 1);
					Scanner sHead = new Scanner(new FileReader("d:/HCCCompiler/include/" + HeadString));
					while (sHead.hasNextLine()){
						desString.add(sHead.nextLine());
					}
				}
				else{
					desString.add(LineString);
				}
			}
			File desFile = new File(FilePath.substring(0, FilePath.length() - 4) + ".tx1");
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
	static public String define(String FilePath){
		try {
			Scanner s = new Scanner(new FileReader(FilePath));
			ArrayList<String> desString = new ArrayList<String>();
			while (s.hasNextLine()){
				String LineString;
				LineString = s.nextLine();
				desString.add(LineString);
			}
			//建立对应表
			ArrayList<String> srcA = new ArrayList<String>();
			ArrayList<String> srcB = new ArrayList<String>();
			for (String LineString:desString){
				if (LineString.startsWith("#define")){
					srcA.add(LineString.split(" +")[1]);
					srcB.add(LineString.split(" +")[2]);
				}
			}
			for (int index = 0;index < srcA.size();index ++){
				int i;
				for (i = 0;i < desString.size();i ++){
					String LineString;
					LineString = desString.get(i);
					if (!LineString.startsWith("#define")){
						String modString;
						modString = LineString.replaceAll("\\b" + srcA.get(index) + "\\b", srcB.get(index));
						desString.remove(i);
						desString.add(i, modString);
					}
					else{
						desString.remove(i);
						desString.add(i, "");
					}
				}
			}
			File desFile = new File(FilePath.substring(0, FilePath.length() - 4) + ".tx0");
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

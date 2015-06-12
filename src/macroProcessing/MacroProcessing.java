package macroProcessing;

public class MacroProcessing {
	static public void main(String[] args){
		Deal("D:/HCCCompiler/test.txt");
	}
	static public String Deal(String FilePath){
		String File;
		File = FilePath;
		File = importHead.define(File);
		File = importHead.importH(File);
		File = CleanExplain.Clean(File);
		return File;
	}
}

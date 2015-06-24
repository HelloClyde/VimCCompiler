package transformater;

public class Cwords {
	//单词类型，在词法分析中扮演的角色
	public String Kind;
	//单词语法分析中扮演的角色
	public String GramKind;
	//单词原型，用于翻译
	public String Value;
	
	public Cwords(String SrcKind,String SrcValue){
		this.Kind = SrcKind;
		this.Value = SrcValue;
	}

	public Cwords() {
		
	}
}

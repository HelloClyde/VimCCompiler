package lexicalAnalyzer;

import java.util.ArrayList;

public class Grammar_C {
	static public ArrayList<String> KeyWord = new ArrayList<String>(){{
	    add("char");
	    add("double");
	    add("else");
	    add("float");
	    add("for");
	    add("if");
	    add("int");
	    add("long");
	    add("return");
	    add("void");
	}};
	static public ArrayList<String> Delimiter = new ArrayList<String>(){{
		add("(");
		add(")");
		add("{");
		add("}");
		add("\"");
		add(",");
		add("<");
		add(">");
		add(";");
		
	}};
	static public ArrayList<String> Operator = new ArrayList<String>(){{
			add("+");
			add("-");
			add("*");
			add("/");
			add("%");
			add("(");
			add(")");
			add("[");
			add("]");
			add("->");
			add("!");
			add("~");
			add("++");
			add("--");
			add("&");
			add("<<");
			add(">>");
			add("<");
			add(">");
			add("<=");
			add(">=");
			add("==");
			add("!=");
			add("^");
			add("|");
			add("||");
			add("?");
			add(":");
			add(",");
			add("");
			add(".");
			add("&&");
			add("+=");
			add("=");
			add("-=");
			add("*=");
			add("/=");
			add("%=");
			add("&=");
			add("^=");
			add("|=");
			add("<<=");
			add(">>=");
			
	}};
}

package syntaciticAnalyzer;

public class SinglePreciateTable {
	String VNName;
	String VTName;
	Produce DesProduce;
	
	public void Show() {
		System.out.print(this.VNName + this.VTName + ":");
		if (this.DesProduce == null){
			System.out.println("null");
		}
		else{
			this.DesProduce.Show();
			System.out.println();
		}
	}
}

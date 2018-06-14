package Code;

public class Poly {
	
	private int[] terms=new int[1000000];//По
	private int deg;//ЅЧ
	
	public Poly(int deg){
		for(int i=0;i<1000000;i++){
			terms[i]=1000000;
		}
		this.deg=deg;
	}
	
	public void set_deg(int deg){
		this.deg=deg;
	}
	
	public void set_coeff(int c,int n){
		this.terms[n]=c;
		if(n>this.deg){
			this.deg=n;
		}
	}
	
	public int get_deg(){
		return deg;
	}

	public int get_coeff(int d){
		return terms[d];
	}

	public void Add(Poly q){
		if(this.deg<=q.get_deg()){
			this.deg=q.get_deg();
		}
		for(int i=0;i<=this.deg;i++){
			if(q.get_coeff(i)!=1000000){
				this.terms[i]+=q.get_coeff(i);
			}
		}
	}
	
	public void Sub(Poly q){
		if(this.deg<=q.get_deg()){
			this.deg=q.get_deg();
		}
		for(int i=0;i<=this.deg;i++){
			if(q.get_coeff(i)!=1000000){
				this.terms[i]-=q.get_coeff(i);
			}
		}
	}
}

package Code;

public class ComputePoly {
	
	private Poly[] PolyList;
	private char[] OpList;
	public int error;
	public int num;
	private Poly p;
	
	//构造器//
	public ComputePoly(){
		p = new Poly(0);
		for(int i=0;i<1000000;i++){
			p.set_coeff(0, i);
		}
		PolyList = new Poly[20];
		for(int i=0;i<20;i++){
			PolyList[i] = new Poly(0);
		}
		OpList = new char[20];
		error = 0;
		num = 0;
	}
	
	public void set_Poly(String s){
		for(int i=0;i<s.length();i++){
			if(i==0){
				if(s.charAt(0)=='-'){
					OpList[num]='-';
				}
				else{
					OpList[num]='+';
				}
			}
			if(s.charAt(i)=='}'){
				if(s.charAt(i+1)!='\0'){
					OpList[num]=s.charAt(i+1);
				}
			}
			else if(s.charAt(i)=='{'){
				while(s.charAt(i+1)!='}'){
					int a=0;
					int coeff=0;
					int degree=0;
					if(s.charAt(i)=='('){
						i++;
						int coeff_minus=0;
						int degree_minus=0;
						while(s.charAt(i+a)!=','){
							if(s.charAt(i+a)=='-'){
								coeff_minus=1;
								a++;
							}
							else if(s.charAt(i+a)>='0'&&s.charAt(i+a)<='9'){
								coeff=coeff*10+(s.charAt(i+a)-'0');
								a++;
							}
							else{
								a++;
							}
						}
						i=i+a+1;
						a=0;
						while(s.charAt(i+a)!=')'){
							if(s.charAt(i+a)=='-'){
								degree_minus=1;
								a++;
							}
							else if(s.charAt(i+a)>='0'&&s.charAt(i+a)<='9'){
								degree=degree*10+(s.charAt(i+a)-'0');
								a++;
							}
							else{
								a++;
							}
						}
						i=i+a-1;
						if(coeff_minus==1){
							if(degree_minus==1&&degree!=0){
								error=2;
								break;
							}
							else{
								if(PolyList[num].get_coeff(degree)==1000000){
									PolyList[num].set_coeff(-coeff, degree);
								}
								else{
									error=1;
									break;
								}
							}
						}
						else{
							if(degree_minus==1&&degree!=0){
								error=2;
								break;
							}
							else{
								if(PolyList[num].get_coeff(degree)==1000000){
									PolyList[num].set_coeff(coeff, degree);
								}
								else{
									error=1;
									break;
								}
							}
						}
					}
					i++;
				}
				if(error==1||error==2){
					break;
				}
				num++;
			}	
		}
	}
	
	public void Compute(){
		char op;
		for(int i=0;i<num;i++){
			op = OpList[i];
			if(op=='+'){
				p.Add(PolyList[i]);
			}
			else if(op=='-'){
				p.Sub(PolyList[i]);
			}
		}
	}
	
	public void print(){
		int none=1;
		for(int i=0;i<=p.get_deg();i++){
			if(p.get_coeff(i)!=0&&p.get_coeff(i)!=1000000){
				none=0;
				break;
			}
		}
		if(none==1){
			System.out.println("0");
		}
		else{
			System.out.print("{");
			int flag=0;
			for(int i=0;i<=p.get_deg();i++){
				if(p.get_coeff(i)!=0&&p.get_coeff(i)!=1000000){
					if(flag==1){
						System.out.print(",");
					}
					System.out.print("("+p.get_coeff(i)+","+i+")");
					flag=1;
				}
			}
			System.out.print("}");	
		}	
	}
	
	public static void main(String args[]){
		Reader reader = new Reader();
		boolean check_pass;
		reader.read();
		check_pass=reader.check();
		try{
			if(check_pass){
				ComputePoly CP = new ComputePoly();
				CP.set_Poly(reader.get());
				if(CP.error!=1&&CP.error!=2){
					CP.Compute();
					CP.print();
				}
				else{
					switch(CP.error){
					case 1:System.out.println("ERROR\n#多项式内指数重复");break;
					case 2:System.out.println("ERROR\n#多项式内指数为负");break;
					}
				}	
			}	
		}catch(Exception e){
			System.out.println("ERROR\n#未知错误");
		}	
	}
}

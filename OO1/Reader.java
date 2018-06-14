package Code;

import java.util.Scanner;
import java.util.regex.*;

public class Reader {
	
	private Scanner s;
	private String in;
	private int Error;
	
	//构造器//
	public Reader(){
		s = new Scanner(System.in);
		in="";
		Error=0;
	}
	//读取

	public void read(){
		try{
			in=s.nextLine();
			s.close();
		}catch(Exception e){
			s.close();
			Error=1;
		}
	}
	//正则匹配
	public boolean regex(){
		String subString;
		int a=0;
		int b=0;
		String regEx1 = "^[+-]?\\{[0123456789+\\-(),]*\\}([+-]\\{[0123456789+\\-(),]*\\}){0,19}$";
		String regEx2 = "^(\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)(,\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)){0,49})?$";
		//String regEx = "^[+-]?\\{\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)(,\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)){0,49}\\}([+-]\\{\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)(,\\([+-]?[0-9]{1,6},[+-]?[0-9]{1,6}\\)){0,49}\\}){0,19}$";
		//Pattern pattern = Pattern.compile(regEx);
		//Matcher matcher = pattern.matcher(in);
		
		Pattern p1 = Pattern.compile(regEx1);
		Matcher m1 = p1.matcher(in);
		Pattern p2 = Pattern.compile(regEx2);
		boolean result = m1.matches();
		if(result){
			for(int i =0;i<in.length();i++){
				if(in.charAt(i)=='{'){
					a=i+1;
					while(in.charAt(i)!='}'){
						i++;
					}
					b=i;
				}
				subString=in.substring(a, b);
				Matcher m2 = p2.matcher(subString);
				result=m2.matches();
				if(!result){
					break;
				}
			}
		}
		if(!result){
			System.out.println("ERROR\n#输入格式错误");
		}
		return result;
	}
	//检查格式
	public boolean check(){
		if(Error==0){
			//space_filter
			in=in.replaceAll(" ", "");
			if(in.length()==0){
				Error=2;
			}
			else{
				for(int i=0;i<in.length();i++){
					if(in.charAt(i)!='+'&&in.charAt(i)!='-'&&in.charAt(i)!=','&&
						in.charAt(i)!='('&&in.charAt(i)!=')'&&in.charAt(i)!='{'&&in.charAt(i)!='}'&&!Character.isDigit(in.charAt(i))){
						Error=3;
						break;
					}
				}
			}
		}
		//print_Error
		if(Error!=0){
			switch(Error){
				case 1:System.out.println("ERROR\n#输入错误");break;
				case 2:System.out.println("ERROR\n#输入为空");break;
				case 3:System.out.println("ERROR\n#非法字符");break;
			}
			return false;
		}
		return this.regex();
	}
	
	public String get(){
		return in+'\0';
	}
}

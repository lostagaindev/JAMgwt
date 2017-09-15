package com.lostagain.JamGwt;


/** Shouldn't be used anymore, we have a SpiffyCalc for this now which
 * does the exact same stuff **/
@Deprecated
public class oldCalculator {

	public void calculator(){
		
	}
	
	public static boolean isCalculation(String input){
		boolean state=true;
		
		
		
		//state= input.matches("[\\+\\-\\*\\/0-9]*");
		
		
		//has at least one opp
		state= input.matches(".*[0-9]+[\\+\\-\\*\\/]+[0-9]+.*");
		
		
		return state;
		
	}
	
	public static boolean isBasicCalculation(String input){
		boolean state=true;
		//remove spaces
		input = input.replaceAll(" ", "");
		
		//split by operator
		String parts[] = input.split("\\+|-|/|\\*");
		
		//should be two parts
		if (parts.length!=2){
			state=false;
		}
		
		
		return state;
	}
	/** will take a string input of a calculation and return its numerical result 
	 * Example calculations would be;<br>
	 * "5*6"<br>
	 * "3-5-1"<br>
	 * ((5^2)*(5-4))/2 <br>
	 * calculations follow BODMAS ordering, so should be bracketed if you want a different order to apply **/
	public static float AdvanceCalculation(String input){
		float value=0;
		input = input.replaceAll(" ", "");
		
		while(!isBasicCalculation(input)){
			
			//process the answer one step at a time, based on bodmas.
			
			//test for / 
			if (input.contains("/")){
				
				int posofopp = input.indexOf("/");
		        int posofnextopp = smallestNonZero(input.indexOf("+",posofopp+1),input.indexOf("-",posofopp+1),input.indexOf("/",posofopp+1),input.indexOf("*",posofopp+1));
				
				if (posofnextopp>input.length()){
					posofnextopp=input.length();
				}
				int posoflastopp = biggestNonZero(input.lastIndexOf("+",posofopp-1),input.lastIndexOf("-",posofopp-1),input.lastIndexOf("/",posofopp-1),input.lastIndexOf("*",posofopp-1));
				
				if (posoflastopp<0){
					posoflastopp=0;
				}else {
					posoflastopp=posoflastopp+1;
				}
				System.out.print("\n first pos ="+posofopp);
				System.out.print("\n smallest ="+input.lastIndexOf("*",0));
				
				System.out.print("\n first pos ="+posoflastopp +" last pop "+posofnextopp);
				
				System.out.print("\n first bit="+input.substring(posoflastopp, posofnextopp));
				System.out.print("\n new bit="+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				
				//escape the sequence
				String replacethis = input.substring(posoflastopp, posofnextopp).replaceAll("\\/", "\\\\\\\\/");
				System.out.print("\n first bit="+replacethis);
				
				//input=input.replaceAll(replacethis, ""+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				input=input.substring(0, posoflastopp)+BasicCalculation(input.substring(posoflastopp, posofnextopp))+input.substring(posofnextopp, input.length());
				
				System.out.print("\n"+input);
				continue;
			} else if (input.contains("*")){
				int posofopp = input.indexOf("*");
		        int posofnextopp = smallestNonZero(input.indexOf("+",posofopp+1),input.indexOf("-",posofopp+1),input.indexOf("/",posofopp+1),input.indexOf("*",posofopp+1));
				
				if (posofnextopp>input.length()){
					posofnextopp=input.length();
				}
				int posoflastopp = biggestNonZero(input.lastIndexOf("+",posofopp-1),input.lastIndexOf("-",posofopp-1),input.lastIndexOf("/",posofopp-1),input.lastIndexOf("*",posofopp-1));
				
				if (posoflastopp<0){
					posoflastopp=0;
				} else {
					posoflastopp=posoflastopp+1;
				}
				System.out.print("\n first * pos ="+posofopp);
				System.out.print("\n smallest ="+input.lastIndexOf("*",posofopp-1));
				
				System.out.print("\n first pos ="+posoflastopp +" last pop "+posofnextopp);
				
				System.out.print("\n first bit="+input.substring(posoflastopp, posofnextopp));
				System.out.print("\n new bit="+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				
				//escape the sequence
				String replacethis = input.substring(posoflastopp, posofnextopp).replaceAll("\\*", "\\\\\\\\*");
				
				System.out.print("\n replace this bit="+replacethis);
				
			//	input=input.replaceAll(replacethis, ""+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				input=input.substring(0, posoflastopp)+BasicCalculation(input.substring(posoflastopp, posofnextopp))+input.substring(posofnextopp, input.length());
				
				System.out.print("\n"+input);
				
				continue;
			} else if (input.contains("+")){
				int posofopp = input.indexOf("+");
		        int posofnextopp = smallestNonZero(input.indexOf("+",posofopp+1),input.indexOf("-",posofopp+1),input.indexOf("/",posofopp+1),input.indexOf("*",posofopp+1));
				
				if (posofnextopp>input.length()){
					posofnextopp=input.length();
				}
				int posoflastopp = biggestNonZero(input.lastIndexOf("+",posofopp-1),input.lastIndexOf("-",posofopp-1),input.lastIndexOf("/",posofopp-1),input.lastIndexOf("*",posofopp-1));
				
				if (posoflastopp<0){
					posoflastopp=0;
				}else {
					posoflastopp=posoflastopp+1;
				}
				System.out.print("\n first pos ="+posofopp);
				System.out.print("\n smallest ="+input.lastIndexOf("*",0));
				
				System.out.print("\n first pos ="+posoflastopp +" last pop "+posofnextopp);
				
				System.out.print("\n first bit="+input.substring(posoflastopp, posofnextopp));
				System.out.print("\n new bit="+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				
				//escape the sequence
				String replacethis = input.substring(posoflastopp, posofnextopp).replaceAll("\\+", "\\\\\\\\+");
				System.out.print("\n first bit="+replacethis);
				
			//	input=input.replaceAll(replacethis, ""+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				input=input.substring(0, posoflastopp)+BasicCalculation(input.substring(posoflastopp, posofnextopp))+input.substring(posofnextopp, input.length());
				
				System.out.print("\n"+input);
				continue;
			} else if (input.contains("-")){
				int posofopp = input.indexOf("-");
		        int posofnextopp = smallestNonZero(input.indexOf("+",posofopp+1),input.indexOf("-",posofopp+1),input.indexOf("/",posofopp+1),input.indexOf("*",posofopp+1));
				
				if (posofnextopp>input.length()){
					posofnextopp=input.length();
				}
				int posoflastopp = biggestNonZero(input.lastIndexOf("+",posofopp-1),input.lastIndexOf("-",posofopp-1),input.lastIndexOf("/",posofopp-1),input.lastIndexOf("*",posofopp-1));
				
				if (posoflastopp<0){
					posoflastopp=0;
				}else {
					posoflastopp=posoflastopp+1;
				}
				System.out.print("\n first pos ="+posofopp);
				System.out.print("\n smallest ="+input.lastIndexOf("*",0));
				
				System.out.print("\n first pos ="+posoflastopp +" last pop "+posofnextopp);
				
				System.out.print("\n first bit="+input.substring(posoflastopp, posofnextopp));
				System.out.print("\n new bit="+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				
				//escape the sequence
				String replacethis = input.substring(posoflastopp, posofnextopp).replaceAll("\\-", "\\\\\\\\-");
				System.out.print("\n first bit="+replacethis);
				
				//input=input.replaceAll(replacethis, ""+BasicCalculation(input.substring(posoflastopp, posofnextopp)));
				input=input.substring(0, posoflastopp)+BasicCalculation(input.substring(posoflastopp, posofnextopp))+input.substring(posofnextopp, input.length());
				
				System.out.print("\n"+input);
				continue;
			}
			
		}
		
		value = BasicCalculation(input);
		System.out.print("\n"+value);
		
		return value;
	}
	
	public static float BasicCalculation(String input){
		input = input.replaceAll(" ", "");
		
		//split by operator
		String parts[] = input.split("\\+|-|/|\\*");
		
		String opp = input.substring(parts[0].length(), parts[0].length()+1);
		
		//each part should be a number
		
		float num1 = Float.parseFloat(parts[0]);
		float num2 = Float.parseFloat(parts[1]);
		
		return calculate_chunk(num1,opp,num2);
	}
	public static float calculate_chunk (float num1,String opp, float num2){
		float result =0;
		
		if (opp.compareTo("+")==0){
			result = num1 + num2; 
		} else if (opp.compareTo("-")==0){
			result = num1  - num2; 
		} else if (opp.compareTo("*")==0){
			result = num1 * num2; 
		} else if (opp.compareTo("/")==0){
			result = (float)(num1 / num2); 
			
		} 
		
		
		
		return result; 
	}
	
	/**returns the smallest non-negative number
	 * usefull for finding the earliest position of something in a string...if its present at all*/
	public static int smallestNonZero(int A, int B, int C, int D){
		
		int R = 0;
		
		//set all values to stupidly high if under 0
		if (A<0){
			A=Integer.MAX_VALUE;
		}
		if (B<0){
			B=Integer.MAX_VALUE;
		}
		if (C<0){
			C=Integer.MAX_VALUE;
		}
		if (D<0){
			D=Integer.MAX_VALUE;
		}
		
		R = Math.min((Math.min(D, C)),(Math.min(A, B)));
		
		
		return R;
	}
public static int biggestNonZero(int A, int B, int C, int D){
		
		int R = 0;
		
		//set all values to stupidly low if under 0
		if (A<0){
			A=-100000;
		}
		if (B<0){
			B=-100000;
		}
		if (C<0){
			C=-100000;
		}
		if (D<0){
			D=-100000;
		}
		
		R = Math.max((Math.max(D, C)),(Math.max(A, B)));
		
		
		return R;
	}
}

package com.condigence.neerseva.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class Test {
	public static void main(String[] args) {
		
		
		

        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(101);
        numbers.add(200);
        numbers.add(301);
        numbers.add(400);
        
        for(int i=0;i<numbers.size();i=i+2) {
        	  numbers.remove(numbers.get(i));
        	  
        }
        System.out.println(numbers);
        
      

//        System.out.println("ArrayList Before : " + numbers);
//
//        Iterator<Integer> itr = numbers.iterator();
//
//        // remove all even numbers
//        while (itr.hasNext()) {
//            Integer number = itr.next();
//
//            if (number % 2 == 0) {
//                numbers.remove(number);
//            }
//        }
//
//        System.out.println("ArrayList After : " + numbers);

    }
}

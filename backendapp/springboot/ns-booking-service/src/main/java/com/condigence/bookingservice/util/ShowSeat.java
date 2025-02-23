//package com.condigence.bookingservice.util;
//
//import java.util.Arrays;
//
//public class ShowSeat {
//    public static void main(String[] args) {
//        Seat[][] screen = new Seat[24][45];
//        // Initializing array element at position[0][0],
//        // i.e. 0th row and 0th column
//        Seat seatA1 = new Seat();
//        seatA1.setId(1L);
//        seatA1.setBooked(true);
//        seatA1.setNumber("A1");
//        seatA1.setRowNumber("A");
//        seatA1.setColumnNumber("1");
//        screen[0][0] = seatA1;
//        // Initializing array element at position[0][1],
//        // i.e. 0th row and 1st column
//        screen[0][1] = new Seat();
//        // Initializing array element at position[1][0],
//        // i.e. 1st row and 0th column
//
//
//        // printing the array elements individually
//        System.out.println("screen[0][0] = "
//                + screen[0][0]);
//        System.out.println("screen[0][1] = "
//                + screen[0][1]);
//
//        // printing 2D array using Arrays.deepToString() method
//        System.out.println(
//                "Printing 2D array using Arrays.deepToString() method: ");
//        System.out.println(Arrays.deepToString(screen));
//    }
//
//}

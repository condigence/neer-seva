package com.condigence.bookingservice.util;

import com.condigence.bookingservice.dto.SeatDTO;
import com.condigence.bookingservice.enums.SeatStatus;
import com.condigence.bookingservice.enums.SeatType;

import java.util.ArrayList;
import java.util.List;

public class SeatUtil {
    public static void main(String[] args) {
        String[] seatNames = getSeatNames();
        for(String str : seatNames){
            //System.out.print(str);
        }
        //System.out.print("Seating Capacity : "+seatNames.length);
        List<SeatDTO> seatsDetails = getSeatDetails(seatNames);
        for(SeatDTO seat : seatsDetails){
            System.out.print(seat.toString());
        }
    }

    public static List<SeatDTO> getSeatDetails(String[] seatNames) {
        List<SeatDTO> seatDTOS = new ArrayList<>();
        int count= 0;
        for(String str : seatNames){
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setNumber(str);
            if(count<=540){
                seatDTO.setSeatType(SeatType.CLASSIC.name());
                seatDTO.setPrice(400.00);
            }else{
                seatDTO.setSeatType(SeatType.PREMIUM.name());
                seatDTO.setPrice(600.00);
            }
            seatDTO.setSeatStatus(SeatStatus.AVAILABLE.name());
            seatDTOS.add(seatDTO);
            count++;
        }

        return seatDTOS;
    }

    public static List<SeatDTO> addSeatDetails(Long theaterId, Long screenId, Long showId, Double price) {
        String[] seatNames = getSeatNames();
        List<SeatDTO> seatDTOS = new ArrayList<>();
        int count= 0;
        for(String str : seatNames){
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setNumber(str);
            if(count<=(seatNames.length)/2){
                seatDTO.setSeatType(SeatType.CLASSIC.name());
                seatDTO.setPrice(price);
            }else{
                seatDTO.setSeatType(SeatType.PREMIUM.name());
                seatDTO.setPrice(price+200);
            }
            seatDTO.setSeatStatus(SeatStatus.AVAILABLE.name());
            seatDTO.setScreenId(screenId);
            seatDTO.setTheaterId(theaterId);
            seatDTO.setShowId(showId);
            seatDTOS.add(seatDTO);
            count++;
        }
        return seatDTOS;
    }

    public static String[] getSeatNames() {
        char[] seatArray = getSeatSuffix();
        String[] seatNameList = new String[1080];
        int count = 0;
        for (char c : seatArray) {
            for (int i = 1; i <= 45; i++) {
                String str = c + "" + i + " ";
                seatNameList[count++]= str;
            }
        }
        return seatNameList;
    }

    public static char[] getSeatSuffix() {
        char c;
        int count = 0;
        char[] res = new char[24];
        for (c = 'A'; c <= 'X'; ++c) {
            String str = c + " ";
            res[count++] = c;
        }
        return res;
    }
}

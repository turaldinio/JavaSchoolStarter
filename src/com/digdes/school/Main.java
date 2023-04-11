package com.digdes.school;


import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        SortStation sortStation = new SortStation(starter.getJavaSchoolServer());
        try {
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ' , 'cost'=12.1, 'id'=3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=24, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Мамедов ' , 'cost'=0.3, 'id'=4, 'age'=24, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Дмитриев ' , 'cost'=23.6, 'id'=2, 'age'=43, 'active'=true");
            sortStation.calculatePostfixRequest(sortStation.getPostfixRequest("( 'id'>=3 and 'cost'>0 ) or ( 'age'>25 and id>1 )"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



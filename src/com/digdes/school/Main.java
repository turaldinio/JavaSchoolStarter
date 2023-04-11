package com.digdes.school;


import com.digdes.school.server.SortStation;

public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        // SortStation sortStation = new SortStation(starter.getJavaSchoolServer());

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ' , 'cost'=12.1, 'id'=3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=24, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Мамедов ' , 'cost'=0.3, 'id'=4, 'age'=24, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Дмитриев ' , 'cost'=23.6, 'id'=2, 'age'=43, 'active'=true");

            var result = starter.execute("update values 'active'=false where id>2");
            //  System.out.println(sortStation.calculatePostfixRequest(sortStation.getPostfixRequest("'id'=3 or age>1 and 'lastname'like%ев")));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



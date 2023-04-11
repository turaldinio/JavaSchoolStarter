package com.digdes.school;


import com.digdes.school.server.SortStation;

public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        // SortStation sortStation = new SortStation(starter.getJavaSchoolServer());

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ' , 'cost'=12.1, 'id'=3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=null, 'active'=false");

            var result = starter.execute("update values 'active'=false where age!=0 and 'cost'<10");
            //  System.out.println(sortStation.calculatePostfixRequest(sortStation.getPostfixRequest("'id'=3 or age>1 and 'lastname'like%ев")));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



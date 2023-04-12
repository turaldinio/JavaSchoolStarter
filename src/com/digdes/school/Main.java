package com.digdes.school;


public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ' , 'cost'=12.1, 'id'=3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=null, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Исмаилов ' , 'cost'=10.4, 'id'=2, 'age'=34, 'active'=true");

            var result = starter.execute("update values 'active'=false where 'age'=15 and 'cost'=10.4 or id>=1 and age>1");
            System.out.println(result);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



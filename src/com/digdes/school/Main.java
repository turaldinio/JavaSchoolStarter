package com.digdes.school;


public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=2, 'age'=null, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Мамедов ' , 'cost'=32.1, 'id'=1, 'age'=52, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Тимуров ' , 'cost'=123.2, 'id'=3, 'age'=13, 'active'=false");
            var result = starter.execute("UPDATE VALUES 'age'='null,'cost'=12.1 where 'id'>1 and 'lastname'like%ов or id=1 or 'id'=2");

            result.forEach(System.out::println);

        } catch (Exception ex) {
            if (ex.getMessage() == null) {
                System.out.println("request error");
            }
            System.out.println(ex.getMessage());
        }
    }
}



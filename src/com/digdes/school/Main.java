package com.digdes.school;


public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=null, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Мамедов ' , 'cost'=32.1, 'id'=2, 'age'=52, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Тимуров ' , 'cost'=123.2, 'id'=3, 'age'=13, 'active'=false");

            starter.execute("update values 'active' = true where 'age'>12 or 'age'!=null");

            System.out.println(starter.execute("update values 'age'=null where 'id'>=2"));

        } catch (Exception ex) {
            if (ex.getMessage() == null) {
                System.out.println("request error");
            }
            System.out.println(ex.getMessage());
        }
    }
}



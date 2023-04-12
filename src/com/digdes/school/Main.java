package com.digdes.school;


public class Main {

    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        // DijkstraParser sortStation = new DijkstraParser(starter.getJavaSchoolServer());

        try {
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ' , 'cost'=12.1, 'id'=3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Гулуев ' , 'cost'=1.2, 'id'=1, 'age'=null, 'active'=false");

            var result = starter.execute("update values 'active'=false where age!=0 and 'cost'<10");
            System.out.println(result);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



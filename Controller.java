package com.mycompany.student;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Controller {

    // array to store Student objects
    static Student students[] = new Student[4];
    static int currentIndex;

    public static void main(String[] args) {
        boolean isDataLoaded = Controller.readAndStoreData();
        if (isDataLoaded) {
            System.out.println("Data is loaded and store in to an array.");
            for(int i = 0; i < students.length; i++){
                System.out.println(students[i]);
            }
            Controller.searchData();
        } else {
            System.out.println("Data not loaded.");
        }

    }

    public static boolean readAndStoreData() {
        try {
            // reading data from url
            String address = "https://hccs-advancejava.s3.amazonaws.com/student.json";
            URL url = new URL(address);
            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setRequestMethod("GET");
            httpURLConn.connect();
            // checking connection is ok
            if (httpURLConn.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                int id = -1;
                String firstName = "";
                double gpa = 0;
                String email = "";
                String gender = "";
                Student student;
                // traversing data
                while (scan.hasNext()) {
                    String data = scan.nextLine();
                    String temp[];
                    // parsing json data
                    if (data.contains(":")) {
                        temp = data.split(":");
                        temp[0] = temp[0].replaceAll("[^a-zA-Z0-9_]", "").replaceAll("\"", "");
                        temp[1] = temp[1].replaceAll("\"", "").replaceAll("\\,", "").trim();
                        if (temp[0].equalsIgnoreCase("id")) {
                            id = Integer.parseInt(temp[1]);
                        } else if (temp[0].equalsIgnoreCase("first_name")) {
                            firstName = temp[1];
                        } else if (temp[0].equalsIgnoreCase("gpa")) {
                            gpa = Double.parseDouble(temp[1]);
                        } else if (temp[0].equalsIgnoreCase("email")) {
                            email = temp[1];
                        } else if (temp[0].equalsIgnoreCase("gender")) {
                            gender = temp[1];
                            // making object of Student
                            student = new Student(id, firstName, gpa, email, gender);
                            // storing student in array
                            students[currentIndex] = student;
                            currentIndex++;
                        }
                    }
                }
                if (currentIndex == 4) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static void searchData() {
        Scanner input = new Scanner(System.in);
        String userChoice = "";
        do {
            System.out.println("\nPress 1 to search by gender.");
            System.out.println("Press 2 to search by name.");
            System.out.println("Press 3 to quit.");
            System.out.println("Enter your choice: ");
            userChoice = input.next();
            if (userChoice.compareTo("1") == 0) {
                Controller.searchByGender();
            } else if (userChoice.compareTo("2") == 0) {
                Controller.searchByName();
            } else if (userChoice.compareTo("3") == 0) {
                System.out.println("Program Ended!");
            } else {
                System.out.println("Invalid Choice. Try again.");
                Controller.searchData();
            }
        } while (!userChoice.equals("3"));

    }

    public static void searchByName() {
        Scanner input = new Scanner(System.in);
        boolean isFound = false;
        System.out.println("Enter first name to search: ");
        String name = input.nextLine();
        for (int i = 0; i < students.length; i++) {
            if (students[i].getFirstName().equalsIgnoreCase(name)) {
                System.out.println("Student data found.");
                System.out.println(students[i]);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Student data with name = " + name + " not found.");
        }
    }

    public static void searchByGender() {
        Scanner input = new Scanner(System.in);
        boolean isFound = false;
        System.out.println("Enter gender to search: ");
        String gender = input.nextLine();
        for (int i = 0; i < students.length; i++) {
            if (students[i].getGender().equalsIgnoreCase(gender)) {
                System.out.println(students[i]);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Student data with Gender = " + gender + " not found.");
        }
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /** Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("../data/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        /** Read the minimum degree of B+Tree first */
        int degree = scan.nextInt();
        BTree bTree = new BTree(degree);

        /** Reading the database student.csv into B+Tree Node */
        List<Student> studentsDB = getStudents();

        for (Student s : studentsDB) {
            bTree.insert(s);
        }

        /** Start reading the operations now from input file */
        try {
            while (scan.hasNextLine()) {
                try (Scanner s2 = new Scanner(scan.nextLine())) {
                    while (s2.hasNext()) {
                        String operation = s2.next();

                        switch (operation) {
                            case "insert": {
                                long studentId = Long.parseLong(s2.next());
                                String studentName = s2.next() + " " + s2.next();
                                String major = s2.next();
                                String level = s2.next();
                                int age = Integer.parseInt(s2.next());
                                /** logic to generate random recordID */
                                long recordID = (long) (Math.random() * 1000000);

                                Student s = new Student(studentId, age, studentName, major, level, recordID);
                                bTree.insert(s);
                                break;
                            }
                            case "delete": {
                                long studentId = Long.parseLong(s2.next());
                                boolean result = bTree.delete(studentId);
                                if (result)
                                    System.out.println("Student deleted successfully.");
                                else
                                    System.out.println("Student deletion failed.");
                                break;
                            }
                            case "search": {
                                long studentId = Long.parseLong(s2.next());
                                long recordID = bTree.search(studentId);
                                if (recordID != -1)
                                    System.out.println("Student exists in the database at " + recordID);
                                else
                                    System.out.println("Student does not exist.");
                                break;
                            }
                            case "print": {
                                List<Long> listOfRecordID = new ArrayList<>();
                                listOfRecordID = bTree.print();
                                System.out.println("List of recordIDs in B+Tree: " + listOfRecordID.toString());
                                break;
                            }
                            case "printTree": {
                                System.out.println("Detailed B+Tree structure:");
                                bTree.printTree();
                                break;
                            }
                            default:
                                System.out.println("Wrong Operation");
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> getStudents() {
        /**
         * Extract the students information from "Students.csv"
         * return the list<Students>
         */
        List<Student> studentList = new ArrayList<>();
        // use Scanner to read and parse through Student csv file
        try (Scanner scnr = new Scanner(new File("../data/Student.csv"))) {
            // for each line of the file
            while (scnr.hasNextLine()) {
                String[] data = scnr.nextLine().split(","); // split line data by comma
                
                // store values
                long studentId = Long.parseLong(data[0]);
                String studentName = data[1];
                String major = data[2];
                String level = data[3];
                int age = Integer.parseInt(data[4]);
                long recordId = Long.parseLong(data[5]);
                // create student object
                Student student = new Student(studentId, age, studentName, major, level, recordId);
                studentList.add(student); // store in studentList to return
            }
        } catch (FileNotFoundException e) {
            System.out.println("Student csv not found.");
        }
        return studentList;
    }
}

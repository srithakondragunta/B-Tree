import java.util.List;

public class BTreeInsertionTester {

    public static void main(String[] args) {
        // Minimum degree of B+Tree
        int degree = 3;
        BTree bTree = new BTree(degree);

        // Test insertions
        System.out.println("Inserting students...");
        bTree.insert(new Student(1, 20, "John Doe", "CS", "FR", 100));
        bTree.insert(new Student(2, 21, "Jane Smith", "EE", "SO", 101));
        bTree.insert(new Student(3, 22, "Alice Johnson", "ME", "JR", 102));
        bTree.insert(new Student(4, 23, "Bob Brown", "CE", "SR", 103));
        bTree.insert(new Student(5, 24, "Charlie Davis", "CS", "FR", 104));
        bTree.insert(new Student(6, 25, "Eve Evans", "EE", "SO", 105));
        bTree.insert(new Student(7, 26, "Franklin White", "ME", "JR", 106));
        bTree.insert(new Student(8, 27, "Grace Kelly", "CE", "SR", 107));
        bTree.insert(new Student(9, 28, "Hannah Brown", "CS", "FR", 108));
        bTree.insert(new Student(10, 29, "Ivy Green", "EE", "SO", 109));

        // Print B+ tree structure
        System.out.println("Printing all students in the B+Tree...");
        List<Long> listOfRecordID = bTree.print();
        System.out.println("List of recordIDs in B+Tree: " + listOfRecordID.toString());

        // Print detailed tree structure
        System.out.println("\nDetailed B+Tree structure:");
        bTree.printTree();
    }
}

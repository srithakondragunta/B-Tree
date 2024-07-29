import java.util.List;

public class BTreeDeletionTester {

    public static void main(String[] args) {
        // Minimum degree of B+Tree
        /* test case: degree = 2 with 13 nodes */
        int degree = 2;
        BTreeNode testRoot = new BTreeNode(degree, false);
        testRoot.keys[0] = 7;
        testRoot.n = 1;
        for (int i = 0; i < 2; i++) {
            testRoot.children[i] = new BTreeNode(degree, false);
            for (int j = 0; j < 3; j++) {
                testRoot.children[i].children[j] = new BTreeNode(degree, true);
            }
        }

        testRoot.children[0].children[0].next = testRoot.children[0].children[1];
        testRoot.children[0].children[1].next = testRoot.children[0].children[2];
        testRoot.children[0].children[2].next = testRoot.children[1].children[0];
        testRoot.children[1].children[0].next = testRoot.children[1].children[1];
        testRoot.children[1].children[1].next = testRoot.children[1].children[2];

        testRoot.children[0].keys[0] = 3;
        testRoot.children[0].keys[1] = 5;
        testRoot.children[0].n = 2;

        testRoot.children[1].keys[0] = 9;
        testRoot.children[1].keys[1] = 11;
        testRoot.children[1].n = 2;

        testRoot.children[0].children[0].keys[0] = 1;
        testRoot.children[0].children[0].keys[1] = 2;
        testRoot.children[0].children[0].values[0] = 100;
        testRoot.children[0].children[0].values[1] = 101;
        testRoot.children[0].children[0].n = 2;

        testRoot.children[0].children[1].keys[0] = 3;
        testRoot.children[0].children[1].keys[1] = 4;
        testRoot.children[0].children[1].values[0] = 102;
        testRoot.children[0].children[1].values[1] = 103;
        testRoot.children[0].children[1].n = 2;

        testRoot.children[0].children[2].keys[0] = 5;
        testRoot.children[0].children[2].keys[1] = 6;
        testRoot.children[0].children[2].values[0] = 104;
        testRoot.children[0].children[2].values[1] = 105;
        testRoot.children[0].children[2].n = 2;

        testRoot.children[1].children[0].keys[0] = 7;
        testRoot.children[1].children[0].keys[1] = 8;
        testRoot.children[1].children[0].values[0] = 106;
        testRoot.children[1].children[0].values[1] = 107;
        testRoot.children[1].children[0].n = 2;

        testRoot.children[1].children[1].keys[0] = 9;
        testRoot.children[1].children[1].keys[1] = 10;
        testRoot.children[1].children[1].values[0] = 108;
        testRoot.children[1].children[1].values[1] = 109;
        testRoot.children[1].children[1].n = 2;

        testRoot.children[1].children[2].keys[0] = 11;
        testRoot.children[1].children[2].keys[1] = 12;
        testRoot.children[1].children[2].keys[2] = 13;
        testRoot.children[1].children[2].values[0] = 110;
        testRoot.children[1].children[2].values[1] = 111;
        testRoot.children[1].children[2].values[2] = 112;
        testRoot.children[1].children[2].n = 3;
    
        BTree bTree = new BTree(degree, testRoot);

        /* test case: degree = 3 with 10 nodes
        int degree = 3;
        BTreeNode testRoot = new BTreeNode(degree, false);
        testRoot.keys[0] = 4;
        testRoot.keys[1] = 7;
        testRoot.n = 2;
        for (int i = 0; i < 3; i++) {
            testRoot.children[i] = new BTreeNode(degree, true);
        }
        for (int i = 0; i < 3; i++) {
            testRoot.children[0].keys[i] = i + 1;
            testRoot.children[0].values[i] = 100 + i;
        }
        for (int i = 0; i < 3; i++) {
            testRoot.children[1].keys[i] = i + 4;
            testRoot.children[1].values[i] = 103 + i;
        }
        for (int i = 0; i < 4; i++) {
            testRoot.children[2].keys[i] = i + 7;
            testRoot.children[2].values[i] = 106 + i;
        }
        testRoot.children[0].n = 3;
        testRoot.children[1].n = 3;
        testRoot.children[2].n = 4;
        testRoot.children[0].next = testRoot.children[1];
        testRoot.children[1].next = testRoot.children[2];
        BTree bTree = new BTree(degree, testRoot);
        */

        // Test insertions
        /*System.out.println("Inserting students...");
        bTree.insert(new Student(1, 20, "John Doe", "CS", "FR", 100));
        bTree.insert(new Student(2, 21, "Jane Smith", "EE", "SO", 101));
        bTree.insert(new Student(3, 22, "Alice Johnson", "ME", "JR", 102));
        bTree.insert(new Student(4, 23, "Bob Brown", "CE", "SR", 103));
        bTree.insert(new Student(5, 24, "Charlie Davis", "CS", "FR", 104));
        bTree.insert(new Student(6, 25, "Eve Evans", "EE", "SO", 105));
        bTree.insert(new Student(7, 26, "Franklin White", "ME", "JR", 106));
        //bTree.insert(new Student(8, 27, "Grace Kelly", "CE", "SR", 107));
        //bTree.insert(new Student(9, 28, "Hannah Brown", "CS", "FR", 108));
        //bTree.insert(new Student(10, 29, "Ivy Green", "EE", "SO", 109));*/
        /*for (int i = 9; i <= 10; i++) {
            bTree.delete(i);
        }
        bTree.delete(3);
        bTree.delete(5);
        bTree.delete(6);*/
   
        bTree.printTree();
        bTree.delete(1);

        // Print B+ tree structure
        System.out.println("Printing all students in the B+Tree...");
        List<Long> listOfRecordID = bTree.print();
        System.out.println("List of recordIDs in B+Tree: " + listOfRecordID.toString());

        // Print detailed tree structure
        System.out.println("\nDetailed B+Tree structure:");
        bTree.printTree();
    }
}
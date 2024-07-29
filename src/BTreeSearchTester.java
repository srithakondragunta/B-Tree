public class BTreeSearchTester {
    public static boolean searchEmptyTree(){
        int degree = 3;
        BTree bTree = new BTree(degree);
        if(bTree.search(301221823)!=-1){
            return false;
        }
        return true;
    }
    public static boolean searchHeight2(){
        int degree = 3;
        BTree bTree = new BTree(degree);
        bTree.insert(new Student(1, 20, "John Doe", "CS", "FR", 100));
        bTree.insert(new Student(2, 21, "Jane Smith", "EE", "SO", 101));
        bTree.insert(new Student(3, 22, "Alice Johnson", "ME", "JR", 102));
        bTree.insert(new Student(4, 23, "Bob Brown", "CE", "SR", 103));
        bTree.insert(new Student(5, 24, "Charlie Davis", "CS", "FR", 104));
        bTree.insert(new Student(7, 25, "Eve Evans", "EE", "SO", 105));
        bTree.insert(new Student(8, 26, "Franklin White", "ME", "JR", 106));
        bTree.insert(new Student(9, 27, "Grace Kelly", "CE", "SR", 107));
        bTree.insert(new Student(10, 28, "Hannah Brown", "CS", "FR", 108));
        bTree.insert(new Student(11, 29, "Ivy Green", "EE", "SO", 109));
        /*  correct BTree should now look like the following:
                |   4   |   8   |
                /        \       \
             |1,2,3|    |4,5,7|  |8,9,10,11|
        */
        
        //lets search in the left leaf node for a value that exists
        if(bTree.search(1)!= 100){
            return false;
        }
        //lets search in the left leaf node for a value that doesn't exist
        if(bTree.search(0)!= -1){
            return false;
        }

        //lets search in the middle leaf node for a value that exists
        if(bTree.search(5)!= 104){
            return false;
        }
        //lets search in the middle leaf node for a value that doesn't exist
        if(bTree.search(6)!= -1){
            return false;
        }

        //lets search in the right leaf node for a value that exists
        if(bTree.search(11)!= 109){
            return false;
        }
        //lets search in the right leaf node for a value that doesn't exist
        if(bTree.search(13)!= -1){
            return false;
        }
        return true;
    }
    public static boolean searchHeight3(){
        int degree = 2;
        BTree bTree = new BTree(degree);
        bTree.insert(new Student(1, 20, "John Doe", "CS", "FR", 100));
        bTree.insert(new Student(3, 21, "Jane Smith", "EE", "SO", 101));
        bTree.insert(new Student(5, 22, "Alice Johnson", "ME", "JR", 102));
        bTree.insert(new Student(7, 23, "Bob Brown", "CE", "SR", 103));
        bTree.insert(new Student(9, 24, "Charlie Davis", "CS", "FR", 104));
        bTree.insert(new Student(2, 25, "Eve Evans", "EE", "SO", 105));
        bTree.insert(new Student(4, 26, "Franklin White", "ME", "JR", 106));
        bTree.insert(new Student(6, 27, "Grace Kelly", "CE", "SR", 107));
        bTree.insert(new Student(8, 28, "Hannah Brown", "CS", "FR", 108));
        bTree.insert(new Student(10, 29, "Kate Anderson", "EE", "SO", 109));
        bTree.insert(new Student(12, 22, "Evan Kesley", "CS", "FR", 110));
        bTree.insert(new Student(14, 23, "Ethan Ross", "ME", "SR", 111));
        bTree.insert(new Student(13, 24, "Ella Roberts", "EE", "JR", 112));
        bTree.insert(new Student(15, 25, "Peter Burke", "CS", "SO", 113));
        bTree.insert(new Student(18, 26, "Ivy Green", "ME", "FR", 114));
        /*  correct BTree should now look like the following:
            	        |       9        |         |        |        |
                        /                 \          
                | 5 | 7 |  |  |           | 12 | 14 |  |  |
                /    \   \                /     \    \
        |1,2,3,4| |5,6,,| |7,8,,|  |9,10,,| |12,13,,| |14,15,18,|      
        */
        //lets search in the left child leaf node for a value that exists
        if(bTree.search(1)!= 100){
            return false;
        }
        //lets search in the left child leaf node for a value that doesn't exist
        if(bTree.search(0)!= -1){
            return false;
        }

        //lets search in the right child leaf node for a value that exists
        if(bTree.search(15)!= 113){
            return false;
        }
        //lets search in the right child leaf node for a value that doesn't exist
        if(bTree.search(11)!= -1){
            return false;
        }

        // Print the B-tree structure before returning true
        System.out.println("B-tree structure before returning true:");
        bTree.printTree();

        return true;
    }

    public static boolean searchRoot(){
        int degree = 2;
        BTree bTree = new BTree(degree);
        bTree.insert(new Student(1, 20, "John Doe", "CS", "FR", 100));
        bTree.insert(new Student(3, 21, "Jane Smith", "EE", "SO", 101));
        bTree.insert(new Student(5, 22, "Alice Johnson", "ME", "JR", 102));
        /*  correct BTree should now look like the following:
            |   1   |   3   |   5   |       |
        */
        //lets search the root for a value that exists
        if(bTree.search(5)!= 102){
            return false;
        }
        //lets search the root for a value that doesn't exist
        if(bTree.search(2)!= -1){
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        System.out.println("SearchEmptyTree(): " + searchEmptyTree());
        System.out.println("SearchRoot(): " + searchRoot());
        System.out.println("SearchHeight2(): " + searchHeight2());
        System.out.println("SearchHeight3(): " + searchHeight3());

    }
}
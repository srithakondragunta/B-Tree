import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
class BTree {

    /**
     * Pointer to the root node.
     */
    BTreeNode root;
    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;

    BTree(int t) {
        this.root = null;
        this.t = t;
    }
    /**
     * for debugging purposes
     * @param t number of key-value pairs allowed
     * @param r pre-formed root to pass in
     **/
    BTree(int t, BTreeNode r) {
        this.root = r;
        this.t = t;
    }

    long search(long studentId) {
        long result;
        if (root == null){
            result = -1;
        }else{        
            result = searchHelper(root,studentId);
        }
        if(result == -1){
            System.out.println("The studentId:" + studentId + " has not been found in the table");
        }
        return result;
    }

    private Long searchHelper(BTreeNode node, long k) {
        int i = 0;
        // Find the first key greater than or equal to k (Ki)
        while (i < node.n && k > node.keys[i]) {
            i++;
        }
        if (i < node.n && k == node.keys[i]) {
            // If the found key is equal to k, and it's a leaf node, return the value
            if (node.leaf) {
                return node.values[i];
            } else {
                // If the node is not a leaf, follow the child pointer
                return searchHelper(node.children[i + 1], k);
            }
        }
        // If the node is a leaf node and key is not found
        if (node.leaf) {
            return (long) -1;
        }
        // Follow the child pointer
        return searchHelper(node.children[i], k);
    }

    BTree insert(Student student) {
        System.out.println("Inserting student: " + student.studentId);

        if (root == null) { // if the tree is empty
            root = new BTreeNode(t, true); // make a new root + insert student record
            root.keys[0] = student.studentId;
            root.values[0] = student.recordId;
            root.n = 1;
            System.out.println("Created new root with studentId: " + student.studentId);
        } else {
            if (root.n == 2 * t) { // if the root is full
                BTreeNode newRoot = new BTreeNode(t, false); // make a new root
                newRoot.children[0] = root; // make the new root's child the old root
                splitChild(newRoot, 0, root); // split the old root into two children
                root = newRoot; // make the new root the root
                System.out.println("Root was full, created new root and split child");
            }
            insertNonFull(root, student); // insert the student record in the correct place
        }

        try (FileWriter fw = new FileWriter("../data/Student.csv", true)) {
            fw.write(student.studentId + "," + student.studentName + "," + student.major + "," + student.level + "," + student.age + "," + student.recordId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void insertNonFull(BTreeNode node, Student student) {
        int i = node.n - 1;

        if (node.leaf) {
            // find correct place to insert student record (shifts to the right until spot is found)
            while (i >= 0 && student.studentId < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                node.values[i + 1] = node.values[i];
                i--;
            }
            node.keys[i + 1] = student.studentId; // inserts in open slot
            node.values[i + 1] = student.recordId;
            node.n++;
            System.out.println("Inserted studentId: " + student.studentId + " into leaf node with keys: " + Arrays.toString(node.keys));
        } else {
            // find correct child to insert student record into
            while (i >= 0 && student.studentId < node.keys[i]) {
                i--;
            }
            i++; 
            if (node.children[i].n == 2 * t) { // if the child is full
                splitChild(node, i, node.children[i]); // split the child into two children
                if (student.studentId > node.keys[i]) {
                    i++;
                }
            }
            insertNonFull(node.children[i], student); // recursive call on children nodes
        }

        System.out.println("Inserted studentId: " + student.studentId + " into node with keys: " + Arrays.toString(node.keys));
    }

    private void splitChild(BTreeNode parent, int i, BTreeNode fullChild) {
        System.out.println("Splitting child node at index: " + i);
        BTreeNode newChild = new BTreeNode(t, fullChild.leaf); // make a new child node
        int mid = t; // middle index

        if (fullChild.leaf) {
            newChild.n = t;

            for (int j = 0; j < t; j++) { // copy keys and values from full child to new child
                newChild.keys[j] = fullChild.keys[mid + j];
                newChild.values[j] = fullChild.values[mid + j];
                fullChild.keys[mid + j] = 0;
                fullChild.values[mid + j] = 0;
            }
            fullChild.n = mid; // update number of nodes in full child

            newChild.next = fullChild.next; // update next pointer
            fullChild.next = newChild;

            for (int j = parent.n; j >= i + 1; j--) { // shift parent's children right by 1
                parent.children[j + 1] = parent.children[j];
            }
            for (int j = parent.n - 1; j >= i; j--) { // shift parent's keys right by 1
                parent.keys[j + 1] = parent.keys[j];
            }

            parent.children[i + 1] = newChild; // insert new child into parent
            parent.keys[i] = newChild.keys[0]; // insert new key into parent
            parent.n++; // increment number of nodes in parent

        } else {
            newChild.n = t - 1;
            for (int j = 0; j < t - 1; j++) {
                newChild.keys[j] = fullChild.keys[mid + 1 + j];
            }
            for (int j = 0; j < t; j++) {
                newChild.children[j] = fullChild.children[mid + 1 + j];
                fullChild.children[mid + 1 + j] = null;  // Clear the reference to avoid loose ends
            }
            fullChild.n = mid;

            for (int j = parent.n; j >= i + 1; j--) { // shift parent's children right by 1
                parent.children[j + 1] = parent.children[j];
            }
            for (int j = parent.n; j >= i; j--) { // shift parent's keys right by 1
                parent.keys[j + 1] = parent.keys[j];
            }
            parent.children[i + 1] = newChild; // insert new child into parent
            parent.keys[i] = fullChild.keys[mid];
            parent.n++;
        }

        System.out.println("Split child:");
        System.out.println("Parent keys: " + Arrays.toString(parent.keys));
        System.out.println("Full child keys: " + Arrays.toString(fullChild.keys));
        System.out.println("New child keys: " + Arrays.toString(newChild.keys));
    }

    boolean delete(long studentId) {
        /**
         * TODO:
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */
        if (search(studentId) == -1) { // in case the studentId is not in the tree
            return false;
        }
        deleteHelper(root, studentId);
        deleteRecord(studentId);
        return true;
    }

    private void deleteHelper(BTreeNode node, long studentId) {
        if(node.leaf == false) {
            // safeguard to prevent from recurring in more than one child
            boolean found = false;
            // for each tree key, if node key < tree key, search the tree key's left child
            // for each tree key, if node key >= tree key, search the tree key's right child
            // following rule that equal keys get put in right children
            if (studentId < node.keys[0] && found == false) {
                // search leftmost child
                found = true;
                deleteHelper(node.children[0], studentId);
            }
            for (int i = 1; i < node.n; i++) {
                // searching central children
                if (studentId >= node.keys[i - 1] && studentId < node.keys[i] && found == false) {
                    found = true;
                    deleteHelper(node.children[i], studentId);
                    if (studentId == node.keys[i - 1]) {
                        // if the current node is one we want to delete, then just replace its key with its leftmost descendant
                        // after deleting from descendants, of course
                        node.keys[i - 1] = findLeftMostChild(node.children[i]).keys[0];
                    }
                }
            }
            if (studentId >= node.keys[node.n - 1] && found == false) {
                // search rightmost child
                found = true;
                deleteHelper(node.children[node.n], studentId);
                if (studentId == node.keys[node.n - 1]) {
                    // if the current node is one we want to delete, then just replace its key with its leftmost descendant
                    // after deleting from descendants, of course
                    node.keys[node.n - 1] = findLeftMostChild(node.children[node.n]).keys[0];
                }
            }
        }

        // leaf case
        if (node.leaf == true) {
            boolean found = false;  
            for (int i = 0; i < node.n; i++) {
                if(studentId == node.keys[i] && found == false) {
                    // start from current key-value pair
                    for (int j = i; j < node.n - 1; j++) {
                        // shift all key-value pairs left 1 slot
                        node.keys[j] = node.keys[j + 1];
                        node.values[j] = node.values[j + 1];
                    }
                    // eliminate last key-value pair
                    node.keys[node.n - 1] = 0;
                    node.values[node.n - 1] = 0;
                    found = true;
                }
            }
            // decrement n
            if (found == true) {
                node.n--;
            }
        }

        // stealing or merging (working with leaves and their parents)
        if (node.leaf == false) {
            for (int i = 0; i < node.n + 1; i++) {
                // check if any children are deficient
                if (node.children[i].n < t) {
                    // try borrowing from right sibling first if possible
                    if (i < node.n && node.children[i + 1].n > t && node.children[i].leaf == true) {
                        stealFromRight(node, i);
                    // otherwise try borrowing from left sibling if possible
                    } else if (i > 0 && node.children[i - 1].n > t && node.children[i].leaf == true) {
                        stealFromLeft(node, i);
                    // otherwise try to merge with right sibling
                    } else if (i < node.n && node.children[i + 1] != null && node != root) {
                        merge(node, i);
                    // otherwise try to merge with left sibling
                    } else if (i > 0 && node.children[i - 1] != null && node != root) {
                        merge(node, i - 1);
                    }
                }
            }
        }
        /*
        // cycling (for all non-leaf nodes)
        if (node.leaf == false) {
            for (int i = 0; i < node.n + 1; i++) {
                // check if any children are deficient
                if (node.children[i].n < t) {
                    // try cycling left
                   if (i < node.n && node.children[i + 1].n > t) {
                        cycleLeft(node, i);
                    // otherwise try cycling right
                    } else if (i > 0 && node.children[i - 1].n > t) {
                        cycleRight(node, i - 1);
                    } 
                }
            }
        }
        */
        // if all else fails and root remains deficient, then merge the second layer with the root and decrease the tree height by 1
        
        if (node == this.root) {
            for (int i = 0; i < node.n + 1; i++) {
                if (node.children[i].n < t) {
                    mergeRoot();
                }
            }
        }
    }

    private void stealFromLeft(BTreeNode parent, int index) {
        BTreeNode child = parent.children[index];
        BTreeNode left = parent.children[index - 1];
        // move child keys right by 1
        for (int i = child.n; i > 0; i--) {
            child.keys[i] = child.keys[i - 1];
        }
        // replace leftmost child key with rightmost left key
        child.keys[0] = left.keys[left.n - 1];
        // also replace parent key with rightmost left key
        parent.keys[index - 1] = left.keys[left.n - 1];
        // delete rightmost left key
        left.keys[left.n - 1] = 0;
        if (child.leaf == true) {
            // repeat for values for leaves
            for (int i = child.n; i > 0; i--) {
                child.values[i] = child.values[i - 1];
            }
            child.values[0] = left.values[left.n - 1];
            left.values[left.n - 1] = 0;
        } else {
            // shift child's children right by 1
            for (int i = child.n + 1; i > 0; i--) {
                child.children[i] = child.children[i - 1];
            }
            // move left child's rightmost child to opened left slot
            child.children[0] = left.children[left.n];
            left.children[left.n] = null;
        }
        // update n accordingly
        left.n--;
        child.n++;
    }
    
    private void stealFromRight(BTreeNode parent, int index) {
        BTreeNode child = parent.children[index];
        BTreeNode right = parent.children[index + 1];
        // add right's leftmost key to child's first open slot
        child.keys[child.n] = right.keys[0];
        for (int i = 0; i < right.n - 1; i++) {
            // shift right keys left by 1
            right.keys[i] = right.keys[i + 1];
        }
        // delete remaining rightmost right key
        right.keys[right.n - 1] = 0;
        // update parent's key to be right's updated leftmost key
        parent.keys[index] = right.keys[0];
        if (child.leaf == true) {
            // repeat for values for leaves
            child.values[child.n] = right.values[0];
            for (int i = 0; i < right.n - 1; i++) {
                right.values[i] = right.values[i + 1];
            }
            right.values[right.n - 1] = 0;
        } else {
            // add right's leftmost child to open slot in left
            child.children[child.n + 1] = right.children[0];
            for (int i = 0; i < right.n; i++) {
                // shift right's children left by 1
                right.children[i] = right.children[i + 1];
            }
            // remove right's residual rightmost child
            right.children[right.n] = null;
        }
        // update n accordingly
        right.n--;
        child.n++;
    }

    private BTreeNode findLeftMostChild(BTreeNode node) {
        if (node.leaf == true) {
            return node;
        } else {
            return findLeftMostChild(node.children[0]);
        }
    }

    // only intended to be used for leaves
    private void merge(BTreeNode parent, int index) {
        BTreeNode pred = parent.children[index];
        BTreeNode succ = parent.children[index + 1]; 
        // merge keys and values from successor into predecessor  
        for (int i = 0; i < succ.n; i++) {
            pred.keys[i + pred.n] = succ.keys[i];
        }
        if (pred.leaf == true && succ.leaf == true) {
            for (int i = 0; i < succ.n; i++) {
                pred.values[i + pred.n] = succ.values[i];   
            }
        } else {
            for (int i = 0; i < succ.n + 1; i++) {
                pred.children[i + pred.n] = succ.children[i];
            }
        }
        // update n accordingly
        pred.n+=succ.n;
        // update the next pointer and delete successor
        if (pred.leaf == true && succ.leaf == true) {
            pred.next = succ.next;
        }
        succ = null;
        // delete according value from parent
        for (int i = index; i < parent.n - 1; i++) {
            parent.keys[i] = parent.keys[i + 1];
        }
        // shift parent's children left
        for (int i = index + 1; i < parent.n; i++) {
            parent.children[i] = parent.children[i + 1];
        }
        parent.keys[parent.n - 1] = 0;
        parent.children[parent.n] = null;
        // update parent n accordingly
        parent.n--;
        // move root pointer down if necessary
        if (parent == root && parent.n == 0) {
            root = parent.children[0];
        }
    }

    private void mergeRoot() {
        if (this.root.leaf == false) {
            // merge everything into the root's leftmost child
            for (int i = 1; i < root.n + 1; i++) {
                // add one key from the parent first
                root.children[0].keys[root.children[0].n - 1 + i] = root.keys[i - 1];
                root.children[0].n++;
                // then merge in keys from the child just to the right
                for (int j = 0; j < root.children[i].n; j++) {
                    root.children[0].keys[root.children[0].n + j] = root.children[i].keys[j];
                }
                // merge children also if appropriate
                if (root.children[i].leaf == false) {
                    for (int j = 0; j < root.children[i].n + 1; j++) {
                        root.children[0].children[root.children[0].n + j] = root.children[i].children[j];
                    }
                }
                // increment n
                root.children[0].n+=root.children[i].n;
            }
            // move the pointer to the new root
            this.root = this.root.children[0];
        }
    }

    

    public void deleteRecord(long studentId) {
        String csvFilePath = "../data/Student.csv";
        List<String> lines = new ArrayList<>();
        String line;

        // Read the CSV file and store all the lines except the ones that match 
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0 && !values[0].isEmpty()) {
                    
                        long id = Long.parseLong(values[0]);
                        if (id != studentId) {
                            lines.add(line);
                        } 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the remaining lines back to the original CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // provided print statements
    List<Long> print() {
        List<Long> listOfRecordID = new ArrayList<>();
        if (root != null) {
            printLeafNodes(root, listOfRecordID);
        }
        return listOfRecordID;
    }

    // helper print statement
    private void printLeafNodes(BTreeNode node, List<Long> listOfRecordID) {
        while (!node.leaf) {
            node = node.children[0];
        }

        while (node != null) {
            for (int i = 0; i < node.n; i++) {
                listOfRecordID.add(node.values[i]);
            }
            node = node.next;
        }
    }
    
    // additional print statement for debugging
    public void printTree() {
        printTreeHelper(root, 0);
    }

    // additional print statement helper for debugging
    private void printTreeHelper(BTreeNode node, int level) {
        if (node != null) {
            System.out.print("Level " + level + " ");
            System.out.print(node.leaf ? "Leaf: " : "Internal: ");
            for (int i = 0; i < node.n; i++) {
                System.out.print(node.keys[i] + " ");
            }
            System.out.println();

            if (!node.leaf) {
                for (int i = 0; i <= node.n; i++) {
                    printTreeHelper(node.children[i], level + 1);
                }
            }
        }
    }
}

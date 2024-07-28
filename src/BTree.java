import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        BTreeNode newChildEntry = null; // set to null initially

        // if the root is null, create a new root node
        if (root == null) {
            root = new BTreeNode(t, true); // create new leaf node as root
            root.keys[0] = student.studentId; // add student ID and record ID into root
            root.values[0] = student.recordId;
            root.n = 1; // number of key-value pairs in root
        } else {
            // Insert new entry into the right place in the tree (handles splitting)
            newChildEntry = insertHelper(root, student);

            // split root if newChildEntry was created (isn't null)
            if (newChildEntry != null) {
                BTreeNode newRoot = new BTreeNode(t, false); // Create a new internal node as the new root
                newRoot.keys[0] = newChildEntry.keys[0]; // Set the key for the new root
                newRoot.children[0] = root; // Set the old root as the first child
                newRoot.children[1] = newChildEntry; // Set the new child entry as the second child
                newRoot.n = 1; // Update the number of keys in the new root
                root = newRoot; // Update the root pointer
            }
        }

        // Write to CSV file
        try (FileWriter fw = new FileWriter("../data/Student.csv", true)) {
            fw.write(student.studentId + "," + student.studentName + "," + student.major + "," + student.level + "," + student.age + "," + student.recordId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private BTreeNode insertHelper(BTreeNode node, Student student) {
        if (node.leaf) {
            // handle insertion into a leaf node (if has space vs splitting)
            return insertIntoLeaf(node, student);
        } else {
            // handle insertion into an internal node
            int i = 0;
            // find which child to insert the new entry into
            while (i < node.n && student.studentId > node.keys[i]) {
                i++;
            }

            // Recursively insert new entry
            BTreeNode newChildEntry = insertHelper(node.children[i], student);

            // return null if no new child was created
            if (newChildEntry == null) {
                return null;
            } else {
                // insert new child entry into current node
                return insertIntoInternal(node, newChildEntry);
            }
        }
    }

    /*
     * Inserts a student into a leaf node. If the leaf node has space, inserts entry.
     * if leaf node is full, splits node and returns new child entry.
     */
    private BTreeNode insertIntoLeaf(BTreeNode leaf, Student student) {
        if (leaf.n < 2 * t - 1) {
            // Insert into the leaf without splitting
            int i = leaf.n - 1;
            // shift key-value pairs to make space for new entry
            while (i >= 0 && student.studentId < leaf.keys[i]) {
                leaf.keys[i + 1] = leaf.keys[i];
                leaf.values[i + 1] = leaf.values[i];
                i--;
            }
            // insert new entry
            leaf.keys[i + 1] = student.studentId;
            leaf.values[i + 1] = student.recordId;
            leaf.n++;
            return null;
        } else {
            // Split the leaf node
            BTreeNode newLeaf = new BTreeNode(t, true);
            // temp arrays to hold keys and values
            long[] tempKeys = new long[2 * t];
            long[] tempValues = new long[2 * t];
            // copy keys and values to temp arrays
            System.arraycopy(leaf.keys, 0, tempKeys, 0, leaf.n);
            System.arraycopy(leaf.values, 0, tempValues, 0, leaf.n);

            int i = leaf.n - 1;
            // shift keys and values in temp arrays to make space for entry
            while (i >= 0 && student.studentId < tempKeys[i]) {
                tempKeys[i + 1] = tempKeys[i];
                tempValues[i + 1] = tempValues[i];
                i--;
            }
            // insert entry into temp arrays
            tempKeys[i + 1] = student.studentId;
            tempValues[i + 1] = student.recordId;

            // split keys and values between old and new leaf
            leaf.n = t;
            newLeaf.n = t;
            System.arraycopy(tempKeys, 0, leaf.keys, 0, t);
            System.arraycopy(tempValues, 0, leaf.values, 0, t);
            System.arraycopy(tempKeys, t, newLeaf.keys, 0, t);
            System.arraycopy(tempValues, t, newLeaf.values, 0, t);

            // update pointers
            newLeaf.next = leaf.next;
            leaf.next = newLeaf;

            // create new child entry
            BTreeNode newChildEntry = new BTreeNode(t, false);
            newChildEntry.keys[0] = newLeaf.keys[0];
            newChildEntry.children[0] = leaf;
            newChildEntry.children[1] = newLeaf;
            newChildEntry.n = 1;
            return newChildEntry;
        }
    }


    /**
     * Inserts a new child entry into an internal node. If the internal node has space, inserts directly.
     * If internal node is full, splits node and returns the new child entry.
     */
    private BTreeNode insertIntoInternal(BTreeNode node, BTreeNode newChildEntry) {
        if (node.n < 2 * t - 1) {
            // Insert into the internal node without splitting
            int i = node.n - 1;
            // shift keys and children to make space for entry
            while (i >= 0 && newChildEntry.keys[0] < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                node.children[i + 2] = node.children[i + 1];
                i--;
            }
            // insert new entry
            node.keys[i + 1] = newChildEntry.keys[0];
            node.children[i + 2] = newChildEntry;
            node.n++;
            return null;
        } else {
            // Split the internal node
            BTreeNode newInternal = new BTreeNode(t, false);
            // temp arrays to hold keys and children
            long[] tempKeys = new long[2 * t];
            BTreeNode[] tempChildren = new BTreeNode[2 * t + 1];
            // copy exisitng keys and children to temp arrays
            System.arraycopy(node.keys, 0, tempKeys, 0, node.n);
            System.arraycopy(node.children, 0, tempChildren, 0, node.n + 1);

            int i = node.n - 1;
            // shift keys and children in temp arrays; make space for new entry
            while (i >= 0 && newChildEntry.keys[0] < tempKeys[i]) {
                tempKeys[i + 1] = tempKeys[i];
                tempChildren[i + 2] = tempChildren[i + 1];
                i--;
            }
            // insert new child entry into temp arrays
            tempKeys[i + 1] = newChildEntry.keys[0];
            tempChildren[i + 2] = newChildEntry;

            // split keys and children between internal node and new internal node
            node.n = t;
            newInternal.n = t - 1;
            System.arraycopy(tempKeys, 0, node.keys, 0, t);
            System.arraycopy(tempChildren, 0, node.children, 0, t + 1);
            System.arraycopy(tempKeys, t + 1, newInternal.keys, 0, t - 1);
            System.arraycopy(tempChildren, t + 1, newInternal.children, 0, t);

            // create new child entry
            BTreeNode newChildEntryParent = new BTreeNode(t, false);
            newChildEntryParent.keys[0] = tempKeys[t];
            newChildEntryParent.children[0] = node;
            newChildEntryParent.children[1] = newInternal;
            newChildEntryParent.n = 1;
            return newChildEntryParent;
        }
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
        //deleteHelper(root, studentId);
        return true;
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

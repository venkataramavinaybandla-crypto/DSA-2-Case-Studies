// MediFlow Hospital — AVL Tree Patient Indexing (Java)

class AVLNode {
    int id, height;
    AVLNode left, right;

    AVLNode(int id) {
        this.id = id;
        height = 1;
    }
}

class AVLTree {

    AVLNode root;

    int h(AVLNode n) {
        return n == null ? 0 : n.height;
    }

    int bf(AVLNode n) {
        return n == null ? 0 : h(n.left) - h(n.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(h(y.left), h(y.right)) + 1;
        x.height = Math.max(h(x.left), h(x.right)) + 1;

        System.out.println("LL Rotation at pivot " + y.id);
        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(h(x.left), h(x.right)) + 1;
        y.height = Math.max(h(y.left), h(y.right)) + 1;

        System.out.println("RR Rotation at pivot " + x.id);
        return y;
    }

    AVLNode insert(AVLNode node, int id) {

        if (node == null)
            return new AVLNode(id);

        if (id < node.id)
            node.left = insert(node.left, id);
        else
            node.right = insert(node.right, id);

        node.height = 1 + Math.max(h(node.left), h(node.right));

        int balance = bf(node);

        // LL
        if (balance > 1 && id < node.left.id)
            return rightRotate(node);

        // RR
        if (balance < -1 && id > node.right.id)
            return leftRotate(node);

        // LR
        if (balance > 1 && id > node.left.id) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && id < node.right.id) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.id + " ");
            inorder(node.right);
        }
    }
}

public class AVLTreeDemo {

    public static void main(String[] args) {

        AVLTree tree = new AVLTree();

        int[] patients = {20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85, 90};

        System.out.println("========================================");
        System.out.println(" MEDIFLOW HOSPITAL — AVL TREE DEMO ");
        System.out.println("========================================");

        for (int id : patients) {
            tree.root = tree.insert(tree.root, id);
        }

        System.out.println("\nInorder Traversal:");
        tree.inorder(tree.root);

        System.out.println("\n\nTime Complexity:");
        System.out.println("Insertion / Deletion / Search : O(log n)");
    }
}

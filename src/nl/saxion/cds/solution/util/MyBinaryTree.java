package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxBinaryTree;
import nl.saxion.cds.collection.SaxList;

/**
 * Custom binary search tree implementation with AVL balancing, supporting basic operations like add, remove, and search.
 *
 * @param <K> the type of keys, which must be comparable
 * @param <V> the type of values associated with the keys
 */
public class MyBinaryTree<K extends Comparable<K>, V> implements SaxBinaryTree<K, V> {
    TreeNode<K,V> root;
    int size;

    /**
     * Check if the key is part of this map.
     * Uses K.equals() to check for equality.
     *
     * @param key key to search for
     * @return if the key is in this collection
     */
    @Override
    public boolean contains(K key) {
        return getTreeNode(key) != null;
    }

    /**
     * Get a value which is mapped to the key.
     *
     * @param key key which is mapped to value to be found
     * @return the value mapped to the key or null if the key is not found
     */
    @Override
    public V get(K key) {
        TreeNode<K, V> node = getTreeNode(key);
        return (node != null) ? node.getValue() : null;
    }

    /**
     * Finds and returns the node associated with the specified key.
     *
     * @param key the key to locate
     * @return the node with the given key or null if not found
     */
    private TreeNode<K,V> getTreeNode(K key) {
        TreeNode<K,V> current = root;
        while (current != null) {
            int compareResult = key.compareTo(current.getKey());
            if (compareResult < 0)
                current = current.left;
            else if (compareResult > 0)
                current = current.right;
            else
                return current;
        }
        return null;
    }

    /**
     * Add the value which will be mapped to the key.
     * A duplicate key will throw a DuplicateKeyException.
     *
     * @param key   key which is mapped to value
     * @param value the value to add
     * @throws DuplicateKeyException if the key is already part of the collection
     */
    @Override
    public void add(K key, V value) throws DuplicateKeyException {
        if (this.contains(key)){
            throw new DuplicateKeyException(key.toString());
        }
        TreeNode<K,V> node = new TreeNode<>(key, value);
        root = addTreeNode(root, node);
        size++;
    }

    /**
     * Recursively adds a new node to the tree, balancing as necessary.
     *
     * @param current the current node in the recursion
     * @param node    the new node to add
     * @return the balanced subtree with the new node added
     */
    private TreeNode<K, V> addTreeNode(TreeNode<K, V> current, TreeNode<K, V> node) {
        if (current == null) {
            return node;
        }
        int compareResult = node.getKey().compareTo(current.getKey());
        if (compareResult <= 0) {
            current.left = addTreeNode(current.left, node);
            current.left.parent = current;
        } else {
            current.right = addTreeNode(current.right, node);
            current.right.parent = current;
        }
        updateHeight(current);
        return balance(current);
    }

    /**
     * Remove the value which is mapped with the key from the collection
     *
     * @param key key which is mapped to value
     * @return the value which is removed from the collection
     * @throws KeyNotFoundException if the key is not part oif the collection
     */
    @Override
    public V remove(K key) throws KeyNotFoundException {
        if (!this.contains(key)) {
            throw new KeyNotFoundException(key.toString());
        }
        V value = get(key);
        root = removeTreeNode(root, key);
        size--;
        return value;
    }

    /**
     * Recursively removes a node with the specified key from the tree, balancing as necessary.
     *
     * @param node the current node in the recursion
     * @param key  the key of the node to remove
     * @return the balanced subtree with the node removed
     */
    private TreeNode<K, V> removeTreeNode(TreeNode<K, V> node, K key) {
        if (node == null) return null;

        int compareResult = key.compareTo(node.getKey());
        if (compareResult < 0) {
            node.left = removeTreeNode(node.left, key);
            if (node.left != null) node.left.parent = node;
        } else if (compareResult > 0) {
            node.right = removeTreeNode(node.right, key);
            if (node.right != null) node.right.parent = node;
        } else {
            if (node.left == null) {
                TreeNode<K, V> rightChild = node.right;
                if (rightChild != null) rightChild.parent = node.parent;
                return rightChild;
            } else if (node.right == null) {
                TreeNode<K, V> leftChild = node.left;
                leftChild.parent = node.parent;
                return leftChild;
            } else {
                TreeNode<K, V> minNode = getMinNode(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = removeTreeNode(node.right, minNode.key);
                if (node.right != null) node.right.parent = node;
            }
        }
        updateHeight(node);
        return balance(node);
    }

    /**
     * Retrieves the minimum node in a given subtree.
     *
     * @param node the root of the subtree
     * @return the node with the minimum key
     */
    private TreeNode<K, V> getMinNode(TreeNode<K, V> node) {
        TreeNode<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Updates the height of a node based on its children's heights.
     *
     * @param node the node to update
     */
    private void updateHeight(TreeNode<K, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Retrieves the height of a node.
     *
     * @param node the node whose height is to be retrieved
     * @return the height of the node, or 0 if null
     */
    private int height(TreeNode<K, V> node) {
        return (node == null) ? 0 : node.height;
    }

    /**
     * Calculates the balance factor of a node.
     *
     * @param node the node whose balance factor is to be calculated
     * @return the balance factor of the node
     */
    private int getBalance(TreeNode<K, V> node) {
        return height(node.left) - height(node.right);
    }

    /**
     * Balances a node if it is unbalanced by performing rotations.
     *
     * @param node the node to balance
     * @return the balanced node
     */
    private TreeNode<K, V> balance(TreeNode<K, V> node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotate(node.left, Direction.LEFT);
                node.left.parent = node;
            }
            return rotate(node, Direction.RIGHT);
        } else if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotate(node.right, Direction.RIGHT);
                node.right.parent = node;
            }
            return rotate(node, Direction.LEFT);
        }
        return node;
    }

    /**
     * Performs a rotation on a node in the specified direction.
     *
     * @param node      the node to rotate
     * @param direction the direction of the rotation (left or right)
     * @return the new root of the rotated subtree
     */
    private TreeNode<K, V> rotate(TreeNode<K, V> node, Direction direction) {
        TreeNode<K, V> newRoot;
        if (direction == Direction.LEFT) {
            newRoot = node.right;
            node.right = newRoot.left;
            if (newRoot.left != null) newRoot.left.parent = node;
            newRoot.left = node;
        } else {
            newRoot = node.left;
            node.left = newRoot.right;
            if (newRoot.right != null) newRoot.right.parent = node;
            newRoot.right = node;
        }

        newRoot.parent = node.parent;
        node.parent = newRoot;

        if (newRoot.parent != null) {
            if (newRoot.parent.left == node) {
                newRoot.parent.left = newRoot;
            } else {
                newRoot.parent.right = newRoot;
            }
        } else {
            root = newRoot;
        }

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private enum Direction {
        LEFT, RIGHT
    }

    /**
     * Retrieves a list of all keys in the tree in sorted order.
     *
     * @return a list of all keys in ascending order
     */
    @Override
    public SaxList<K> getKeys() {
        SaxList<K> keys = new MyArrayList<>();
        getKeysInOrder(root, keys);
        return keys;
    }

    /**
     * Adds keys to the list in in-order traversal, ensuring sorted order.
     *
     * @param node the current node in the traversal
     * @param keys the list of keys being populated
     */
    private void getKeysInOrder(TreeNode<K, V> node, SaxList<K> keys) {
        if (node != null) {
            getKeysInOrder(node.left, keys);
            keys.addLast(node.getKey());
            getKeysInOrder(node.right, keys);
        }
    }

    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Create a String representation of the data in GraphViz (see <a href="https://graphviz.org">GraphViz</a>)
     * format, which you can print-copy-paste on the site see <a href="https://dreampuf.github.io/GraphvizOnline">GraphViz online</a>.
     *
     * @param name name of the produced graph
     * @return a GraphViz string representation of this collection
     */
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(name).append(" {\n");
        sb.append("    node [shape=circle];\n");
        generateGraphViz(sb, root);
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Recursively generates the GraphViz representation for the binary tree.
     *
     * @param sb   the StringBuilder to append GraphViz content
     * @param node the current node in the recursion
     */
    private void generateGraphViz(StringBuilder sb, TreeNode<K, V> node) {
        if (node != null) {
            sb.append("    ").append(node.getKey()).append(" [label=\"").append(node.getValue()).append("\"];\n");

            if (node.left != null) {
                sb.append("    ").append(node.getKey()).append(" -> ").append(node.left.getKey()).append(";\n");
                generateGraphViz(sb, node.left);
            }

            if (node.right != null) {
                sb.append("    ").append(node.getKey()).append(" -> ").append(node.right.getKey()).append(";\n");
                generateGraphViz(sb, node.right);
            }
        }
    }

    /**
     * Represents a node in the binary tree with key, value, height, and pointers to parent and children.
     *
     * @param <K> the type of keys
     * @param <V> the type of values
     */
    private static class TreeNode<K extends Comparable<K>, V> {
        K key;
        V value;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> parent;
        int height;

        /**
         * Gets the key stored in this node.
         *
         * @return the key of this node
         */
        public final K getKey() {
            return key;
        }

        /**
         * Gets the value stored in this node.
         *
         * @return the value of this node
         */
        public final V getValue() {
            return value;
        }

        /**
         * Initializes a new node with the specified key, value, and optional parent node.
         *
         * @param key    the key of the node
         * @param value  the value of the node
         * @param parent the parent node
         */
        public TreeNode(K key, V value, TreeNode<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.height = 1;
        }

        /**
         * Initializes a new node with the specified key and value.
         *
         * @param key   the key of the node
         * @param value the value of the node
         */
        public TreeNode(K key, V value) {
            this(key, value, null);
        }
    }
}

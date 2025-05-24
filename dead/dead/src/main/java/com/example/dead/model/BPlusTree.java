package com.example.dead.model;

import java.util.ArrayList;
import java.util.List;


public class BPlusTree<K extends Comparable<K>, V> {
    static class BPlusTreeNode<K extends Comparable<K>, V> {
        int t;
        boolean leaf;
        ArrayList<K> keys;
        ArrayList<V> values;
        ArrayList<BPlusTreeNode<K, V>> children;

        BPlusTreeNode(boolean leaf, int t) {
            this.leaf = leaf;
            this.t = t;
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    int t;
    BPlusTreeNode<K, V> root;

    public BPlusTree(int t) {
        this.t = t;
        this.root = new BPlusTreeNode<>(true, t);
    }

    public void insert(K key, V value) {
        BPlusTreeNode<K, V> r = root;
        if (r.keys.size() == (2 * t) - 1) {
            BPlusTreeNode<K, V> s = new BPlusTreeNode<>(false, t);
            s.children.add(r);
            splitChild(s, 0);
            root = s;
        }
        insertNonFull(root, key, value);
    }

    private void insertNonFull(BPlusTreeNode<K, V> x, K key, V value) {
        int i = x.keys.size() - 1;
        if (x.leaf) {
            x.keys.add(null);
            x.values.add(null);
            while (i >= 0 && key.compareTo(x.keys.get(i)) < 0) {
                x.keys.set(i + 1, x.keys.get(i));
                x.values.set(i + 1, x.values.get(i));
                i--;
            }
            x.keys.set(i + 1, key);
            x.values.set(i + 1, value);
        } else {
            while (i >= 0 && key.compareTo(x.keys.get(i)) < 0) {
                i--;
            }
            i++;
            if (x.children.get(i).keys.size() == (2 * t) - 1) {
                splitChild(x, i);
                if (key.compareTo(x.keys.get(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(x.children.get(i), key, value);
        }
    }

    private void splitChild(BPlusTreeNode<K, V> x, int i) {
        BPlusTreeNode<K, V> y = x.children.get(i);
        BPlusTreeNode<K, V> z = new BPlusTreeNode<>(y.leaf, t);
        x.children.add(i + 1, z);
        x.keys.add(i, y.keys.get(t - 1));

        for (int j = 0; j < t - 1; j++) {
            z.keys.add(y.keys.get(j + t));
            if (y.leaf) {
                z.values.add(y.values.get(j + t));
            }
        }
        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children.add(y.children.get(j + t));
            }
        }
        y.keys.subList(t - 1, y.keys.size()).clear();
        if (y.leaf) {
            y.values.subList(t - 1, y.values.size()).clear();
        }
        if (!y.leaf) {
            y.children.subList(t, y.children.size()).clear();
        }
    }

//    public void traverse() {
//        traverse(root);
//    }
//
//    private void traverse(BPlusTreeNode<K, V> node) {
//        for (int i = 0; i < node.keys.size(); i++) {
//            if (!node.leaf) {
//                traverse(node.children.get(i));
//            }
//            System.out.print(" " + node.keys.get(i) + ":" + node.values.get(i));
//        }
//        if (!node.leaf) {
//            traverse(node.children.get(node.keys.size()));
//        }
//    }

    public V search(K key) {
        BPlusTreeNode<K, V> node = root;

        while (!node.leaf) {
            int i = 0;
            while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
                i++;
            }
            node = node.children.get(i);
        }

        int index = node.keys.indexOf(key);
        return (index != -1) ? node.values.get(index) : null;
    }

    public List<V> searchAll(K key) {
        boolean value = search(key) != null;
        List<V> result = new ArrayList<>();
        while (value) {
            result.add(search(key));
            delete(key);
            value = search(key) != null;
        }
        for (V v : result) {
            insert(key, v);
        }
        return result;
    }

    public void delete(K key) {
        if (root == null) {
            return;
        }

        deleteRecursive(root, key);

        if (root.keys.isEmpty() && !root.leaf) {
            root = root.children.get(0);
        }
    }

    private void deleteRecursive(BPlusTreeNode<K, V> node, K key) {
        int idx = 0;
        while (idx < node.keys.size() && key.compareTo(node.keys.get(idx)) > 0) {
            idx++;
        }

        if (node.leaf) {
            if (idx < node.keys.size() && node.keys.get(idx).equals(key)) {
                node.keys.remove(idx);
                node.values.remove(idx);
            }
            return;
        }

        BPlusTreeNode<K, V> child = node.children.get(idx);

        if (idx < node.keys.size() && node.keys.get(idx).equals(key)) {
            if (!child.leaf) {
                K predecessorKey = getPredecessor(child);
                V predecessorValue = child.values.get(child.keys.indexOf(predecessorKey));
                node.keys.set(idx, predecessorKey);
                node.values.set(idx, predecessorValue);
                deleteRecursive(child, predecessorKey);
            } else {
                deleteRecursive(child, key);
            }
        } else {
            deleteRecursive(child, key);
        }

        if (child.keys.size() < t - 1) {
            fixDeficiency(node, idx);
        }
    }

    private K getPredecessor(BPlusTreeNode<K, V> node) {
        while (!node.leaf) {
            node = node.children.get(node.children.size() - 1);
        }
        return node.keys.get(node.keys.size() - 1);
    }

    private void fixDeficiency(BPlusTreeNode<K, V> parent, int idx) {
        BPlusTreeNode<K, V> child = parent.children.get(idx);

        if (idx > 0 && parent.children.get(idx - 1).keys.size() >= t) {
            BPlusTreeNode<K, V> leftSibling = parent.children.get(idx - 1);
            child.keys.add(0, parent.keys.get(idx - 1));
            parent.keys.set(idx - 1, leftSibling.keys.remove(leftSibling.keys.size() - 1));

            if (!leftSibling.leaf) {
                child.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
            }
        } else if (idx < parent.children.size() - 1 && parent.children.get(idx + 1).keys.size() >= t) {
            BPlusTreeNode<K, V> rightSibling = parent.children.get(idx + 1);
            child.keys.add(parent.keys.get(idx));
            parent.keys.set(idx, rightSibling.keys.remove(0));

            if (!rightSibling.leaf) {
                child.children.add(rightSibling.children.remove(0));
            }
        } else {
            if (idx > 0) {
                mergeNodes(parent, idx - 1);
            } else {
                mergeNodes(parent, idx);
            }
        }
    }

    private void mergeNodes(BPlusTreeNode<K, V> parent, int idx) {
        BPlusTreeNode<K, V> left = parent.children.get(idx);
        BPlusTreeNode<K, V> right = parent.children.get(idx + 1);

        left.keys.add(parent.keys.remove(idx));
        left.keys.addAll(right.keys);
        left.values.addAll(right.values);

        if (!right.leaf) {
            left.children.addAll(right.children);
        }

        parent.children.remove(idx + 1);
    }

}
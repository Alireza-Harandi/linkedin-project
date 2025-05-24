package com.example.dead.model;

import java.util.ArrayList;
import java.util.List;

public class TrieTree<T> {

    static class TrieNode<T> {
        TrieNode<T>[] children = new TrieNode[36];
        boolean isEndOfWord = false;
        T value = null;
    }

    private final TrieNode<T> root;

    public TrieTree() {
        root = new TrieNode<>();
    }

    private int getIndex(char ch) {
        ch = Character.toLowerCase(ch);
        if (Character.isLetter(ch)) return ch - 'a';
        if (Character.isDigit(ch)) return 26 + (ch - '0');
        throw new IllegalArgumentException("Unsupported character: " + ch);
    }


    public void insert(String key, T value) {
        TrieNode<T> node = root;
        for (char ch : key.toCharArray()) {
            int index = getIndex(ch);
            if (node.children[index] == null)
                node.children[index] = new TrieNode<>();
            node = node.children[index];
        }
        node.isEndOfWord = true;
        node.value = value;
    }

    public boolean delete(String key) {
        return deleteHelper(root, key, 0);
    }

    private boolean deleteHelper(TrieNode<T> node, String key, int depth) {
        if (node == null) return false;

        if (depth == key.length()) {
            if (!node.isEndOfWord) return false;
            node.isEndOfWord = false;
            node.value = null;
            return isEmpty(node);
        }

        int index = getIndex(key.charAt(depth));
        if (deleteHelper(node.children[index], key, depth + 1)) {
            node.children[index] = null;
            return !node.isEndOfWord && isEmpty(node);
        }
        return false;
    }

    private boolean isEmpty(TrieNode<T> node) {
        for (TrieNode<T> child : node.children) {
            if (child != null) return false;
        }
        return true;
    }

    public List<T> searchTop5(String prefix) {
        TrieNode<T> node = root;
        for (char ch : prefix.toCharArray()) {
            int index = getIndex(ch);
            if (node.children[index] == null) return new ArrayList<>();
            node = node.children[index];
        }

        List<T> result = new ArrayList<>();
        dfs(node, result);
        return result;
    }

    private void dfs(TrieNode<T> node, List<T> result) {
        if (node == null || result.size() >= 5) return;
        if (node.isEndOfWord) result.add(node.value);
        for (TrieNode<T> child : node.children) {
            if (child != null) dfs(child, result);
        }
    }
}

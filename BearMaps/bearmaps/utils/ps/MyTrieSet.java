package bearmaps.utils.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet implements TrieSet61BL {
    Node root;

    public MyTrieSet() {
        root = new Node();
    }

    private static class Node {
        Object item;
        HashMap<Character, Node> next = new HashMap<>();
        boolean isKey = false;
        int count = 0;

        public Node() {
        }

        public Node(Character s, boolean b, int c) {
            item = s;
            isKey = b;
            count = c;
        }
    }

    /** Clears all items out of Trie */
    public void clear() {
        root.next = new HashMap<Character, Node>();
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    public boolean contains(String key) {
        if (key == null) {
            return false;
        }
        return cointainsHelper(root, key);
    }

    private boolean cointainsHelper(Node n, String k) {
        if (k.length() == 1 && n.next.containsKey(k.charAt(0))
                && n.next.get(k.charAt(0)).isKey) {
            return true;
        }
        try {
            if (n.next.containsKey(k.charAt(0))) {
                return cointainsHelper(n.next.get(k.charAt(0)),
                        k.substring(1, k.length()));
            }
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    /** Inserts string KEY into Trie */
    public void add(String key) {
        addHelper(root, key);
    }

    public void addHelper(Node n, String k) {
        if (k.length() == 0) {
            return;
        }
        if (k.length() == 1) {
            if (!n.next.containsKey(k.charAt(0))) {
                n.next.put(k.charAt(0), new Node(k.charAt(0), true, 1));
            } else {
                n.next.get(k.charAt(0)).isKey = true;
            }
        } else {
            if (n.next.containsKey(k.charAt(0))) {
                n.next.get(k.charAt(0)).count += 1;
                addHelper(n.next.get(k.charAt(0)), k.substring(1, k.length()));
            } else {
                n.next.put(k.charAt(0), new Node(k.charAt(0), false, 1));
                addHelper(n.next.get(k.charAt(0)), k.substring(1, k.length()));
            }
        }
    }

    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        Node test = root;
        for (int i = 0; i < prefix.length(); i++) {
            test = test.next.get(prefix.charAt(i));
            if (test == null) {
                return null;
            }
        }
        return listString(prefix, test);
    }

    private List<String> listString(String prefix, Node n) {
        List<String> out = new ArrayList<>();
        if (n.isKey) {
            out.add(prefix);
        }
        for (Character k : n.next.keySet()) {
            for (String s : listString(n.next.get(k).item.toString(),
                    n.next.get(k))) {
                out.add(prefix + s);
            }
        }
        return out;
    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 18. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        MyTrieSet t = new MyTrieSet();
        t.add("hello");
        t.add("hi");
        t.add("hii");
        t.add("help");
        t.add("zebra");
        System.out.println(t.contains("hello"));
        System.out.println(t.contains("help"));
        System.out.println(t.contains("hii"));
        System.out.println(t.keysWithPrefix("h"));
        System.out.println(t.keysWithPrefix("he"));
        t.add("same");
        t.add("sam");
    }
}

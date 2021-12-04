package bearmaps.utils.pq;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    private int size;

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        return 2 * index;
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        return 2 * index + 1;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        return index / 2;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        if (getElement(index1) == null) {
            return index2;
        } else if (getElement(index2) == null) {
            return index1;
        } else if (getElement(index1).compareTo(getElement(index2)) <= 0) {
            return index1;
        }
        return index2;
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        if (size == 0) {
            return null;
        }
        return getElement(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        if (index != 1 && index != 0 && index <= size) {
            if (getElement(index).compareTo(getElement(index / 2)) < 0) {
                swap(index, index / 2);
                bubbleUp(index / 2);
            }
        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        if (index <= size() / 2) {
            E left = getElement(2 * index);
            E right = getElement(2 * index + 1);
            if (right != null) {
                if (getElement(index).compareTo(left) > 0
                        && getElement(index).compareTo(right) > 0) {
                    if (left.compareTo(right) <= 0) {
                        swap(index, index * 2);
                        bubbleDown(index * 2);
                    } else {
                        swap(index, index * 2 + 1);
                        bubbleDown(index * 2 + 1);
                    }
                } else if (getElement(index).compareTo(left) > 0) {
                    swap(index, index * 2);
                    bubbleDown(index * 2);
                } else {
                    swap(index, index * 2 + 1);
                    bubbleDown(index * 2 + 1);
                }
            } else {
                if (getElement(index).compareTo(left) > 0) {
                    swap(index, index * 2);
                    bubbleDown(index * 2);
                }
            }
        }
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return size;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if (contains(element)) {
            throw new IllegalArgumentException("Element is already in the MinHeap,");
        }
        size += 1;
        contents.add(element);
        bubbleUp(size);
    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        if (size == 0) {
            return null;
        }
        E output = contents.get(1);
        swap(1, size());
        contents.remove(size);
        size -= 1;
        bubbleDown(1);
        return output;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        if (contains(element)) {
            int index = 0;
            for (int i = 1; i <= size; i++) {
                if (contents.get(i).equals(element)) {
                    index = i;
                    break;
                }
            }
            setElement(index, element);
            if (index != 1 && element.compareTo(getElement(index / 2)) <= 0) {
                bubbleUp(index);
            } else if (getElement(index * 2) != null) {
                bubbleDown(index);
            }
        } else {
            throw new NoSuchElementException("element doesn't exist");
        }
    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        for (int i = 1; i <= size; i++) {
            if (contents.get(i).equals(element)) {
                return true;
            }
        }
        return false;
    }
}

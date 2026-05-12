import java.util.*;

class BinomialHeap {

    static class Node {
        int key;
        int degree;
        Node parent;
        Node child;
        Node sibling;

        Node(int key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.sibling = null;
        }
    }

    public Node head;
    public Node minNode;
    public long operationCount;

    public BinomialHeap() {
        head = null;
        minNode = null;
        operationCount = 0;
    }

    // Соединяются два одинаковой степени дерева
    private void link(Node child, Node parent) {
        operationCount++;
        child.parent = parent;
        child.sibling = parent.child;
        parent.child = child;
        parent.degree++;
    }

    // Слияние корневых списков
    private Node mergeRootLists(Node h1, Node h2) {
        if (h1 == null) return h2;
        if (h2 == null) return h1;

        Node dummy = new Node(-1);
        Node tail = dummy;

        while (h1 != null && h2 != null) {
            if (h1.degree <= h2.degree) {
                tail.sibling = h1;
                h1 = h1.sibling;
            } else {
                tail.sibling = h2;
                h2 = h2.sibling;
            }
            tail = tail.sibling;
            operationCount++;
        }
        tail.sibling = (h1 != null) ? h1 : h2;
        return dummy.sibling;
    }

    // Основная операция слияния двух куч
    private BinomialHeap union(BinomialHeap other) {
        BinomialHeap result = new BinomialHeap();
        result.head = mergeRootLists(this.head, other.head);
        result.operationCount = this.operationCount + other.operationCount;

        if (result.head == null) {
            result.minNode = null;
            return result;
        }

        Node prev = null;
        Node curr = result.head;
        Node next = curr.sibling;

        while (next != null) {
            if (curr.degree != next.degree ||
                    (next.sibling != null && next.sibling.degree == curr.degree)) {
                prev = curr;
                curr = next;
            } else {
                result.operationCount++;
                if (curr.key <= next.key) {
                    curr.sibling = next.sibling;
                    link(next, curr);
                } else {
                    if (prev == null) result.head = next;
                    else prev.sibling = next;
                    link(curr, next);
                    curr = next;
                }
            }
            next = curr.sibling;
        }

        // Обновляем указатель на минимум
        result.minNode = result.head;
        Node temp = result.head;
        while (temp != null) {
            result.operationCount++;
            if (temp.key < result.minNode.key) result.minNode = temp;
            temp = temp.sibling;
        }

        return result;
    }

    // Вставка элемента
    public void insert(int key) {
        BinomialHeap tempHeap = new BinomialHeap();
        Node newNode = new Node(key);
        tempHeap.head = newNode;
        tempHeap.minNode = newNode;
        tempHeap.operationCount = 1;

        BinomialHeap merged = union(tempHeap);
        this.head = merged.head;
        this.minNode = merged.minNode;
        this.operationCount += merged.operationCount;
    }

    // Поиск минимума
    public int findMin() {
        if (minNode == null) throw new NoSuchElementException("Heap is empty");
        operationCount++;
        return minNode.key;
    }

    // Поиск элемента по ключу
    public boolean search(int key) {
        operationCount = 0;
        return searchRecursive(head, key);
    }

    private boolean searchRecursive(Node node, int key) {
        if (node == null) return false;

        operationCount++;
        if (node.key == key) return true;

        if (searchRecursive(node.child, key)) return true;
        return searchRecursive(node.sibling, key);
    }

    // Удаление минимума
    public int deleteMin() {
        if (head == null) throw new NoSuchElementException("Heap is empty");

        int minKey = minNode.key;  // Сохраняем ключ

        Node prevMin = null;
        Node curr = head;
        Node prev = null;

        while (curr != minNode) {
            prev = curr;
            curr = curr.sibling;
            operationCount++;
        }
        prevMin = prev;

        if (prevMin == null) head = minNode.sibling;
        else prevMin.sibling = minNode.sibling;

        BinomialHeap childrenHeap = new BinomialHeap();
        Node child = minNode.child;
        Node reversedChildList = null;
        while (child != null) {
            Node next = child.sibling;
            child.sibling = reversedChildList;
            child.parent = null;
            reversedChildList = child;
            child = next;
            operationCount++;
        }
        childrenHeap.head = reversedChildList;

        BinomialHeap merged = union(childrenHeap);
        this.head = merged.head;
        this.minNode = merged.minNode;

        this.operationCount += merged.operationCount;

        return minKey;  // Возвращаем сохраненный ключ
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void resetOpCount() {
        this.operationCount = 0;
    }
}
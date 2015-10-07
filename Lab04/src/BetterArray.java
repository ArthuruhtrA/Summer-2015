import java.util.*;

/**
 * Created by Ari Sanders on 10-Jul-15.
 */
public class BetterArray implements List {
    private int size;
    private Node head;
    private Node tail;


    /**
     * Constructs new BetterArray of custom size
     * @param n Starting size
     */
    public BetterArray(int n){
        Node curr;
        Node prev;

        size = n;
        if (n > 0) {
            head = new Node();
        }
        curr = head;
        for (int i = 1; i < size; i++){
            prev = curr;
            curr.setNext(new Node());
            curr = curr.getNext();
            prev.setNext(curr);
            curr.setPrev(prev);
        }
        tail = curr;
    }

    /**
     * Constructs new BetterArray of default size
     */
    public BetterArray(){
        this(10);//Default
    }

    /**
     * Finds an element by index
     * @param index Index of requested element
     * @return Node at index
     */
    public Node seek(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        Node curr;

        if (index < size) {//For some reason, starting from the tail doesn't work / 2) {
            curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.getNext();
            }
        }
        else {
            curr = tail;
            for (int i = size - 1; i >= index; i--) {
                curr = curr.getPrev();
            }
        }
        return curr;
    }

    public void swap(int indexOne, int indexTwo){
        Node one = seek(indexOne);
        Node two = seek(indexTwo);
        Node temp = new Node();
        temp.setNext(one.getNext());
        temp.setPrev(one.getPrev());

        one.setNext(two.getNext());
        one.setPrev(two.getPrev());
        one.getNext().setPrev(one);
        one.getPrev().setNext(one);

        two.setNext(temp.getNext());
        two.setPrev(temp.getPrev());
        two.getNext().setPrev(two);
        two.getPrev().setNext(two);

        if (indexOne == 0){
            head = two;
        }
        else if (indexOne == size - 1){
            tail = two;
        }
        if (indexTwo == 0){
            head = one;
        }
        else if (indexTwo == size - 1){
            tail = one;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        Node curr = head;

        for (int i = 0; i < size; i++){
            if (curr.get() != null){
                return false;
            }
            curr = curr.getNext();
        }

        return true;
    }

    @Override
    public boolean contains(Object o) {
        Node curr = head;

        for (int i = 0; i < size; i++){
            if (curr.get() == o){
                return true;
            }
            curr = curr.getNext();
        }

        return false;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            private Node curr = head;
            @Override
            public boolean hasNext() {
                if (curr != null) {
                    return curr.getNext() != null;
                }
                return false;
            }

            @Override
            public Object next() {
                if (hasNext()) {
                    curr = curr.getNext();
                    return curr.get();
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node curr = head;

        for (int i = 0; i < size; i++){
            array[i] = curr.get();
            curr = curr.getNext();
        }
        return array;
    }

    @Override
    public Object[] toArray(Object[] a) {
        if (head.get().getClass() != a.getClass()){
            //Not the best way to do this
            throw new ArrayStoreException();
        }
        if (a.length <= size){
            Node curr = head;

            for (int i = 0; i < size; i++){
                a[i] = curr.get();
                curr = curr.getNext();
            }
            return a;
        }
        return toArray();
    }

    @Override
    public boolean add(Object o) {
        Node newNode = new Node(o);

        if (size != 0) {
            newNode.setPrev(tail);
            tail.setNext(newNode);
        }
        else {
            head = newNode;
        }
        tail = newNode;
        size++;
        return true;
    }

    @Override
    public void add(int index, Object element) {
        /*if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }*///Seek does this

        if (index == size){
            add(element);
            return;
        }

        Node curr = seek(index);
        Node newNode = new Node(element);
        newNode.setNext(curr);
        if (index != 0) {
            newNode.setPrev(curr.getPrev());
            curr.getPrev().setNext(newNode);
        }
        else {
            head = newNode;
        }
        curr.setPrev(newNode);
        if (index == size++){
            tail = newNode;
        }

    }

    /**
     * Inserts new element, expands if index > size
     * @param value Data to insert
     * @param index Index to insert value before
     */
    public void insert(Object value, int index){
        if (index > size){

            for (int i = size; i < index - 1; i++){
                add(null);
            }
            add(value);
        }
        else {
            add(index, value);
        }
    }

    @Override
    public boolean addAll(Collection c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        for (Object element : c){
            add(index++, element);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);

        if (index == -1) {
            return false;
        }
        else {
            remove(index);
            return true;
        }
    }

    @Override
    public Object remove(int index) {
        /*if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }*///Seek does this

        Node curr = seek(index);

        if (index != 0) {
            curr.getPrev().setNext(curr.getNext());
        }
        else {
            head = curr.getNext();
        }
        if (index >= size - 1) {
            curr.getNext().setPrev(curr.getPrev());
        }
        else {
            tail = curr.getPrev();
        }
        size--;
        return curr.get();
    }

    /**
     * Alias to remove to match assignment API
     * @param index
     */
    public void delete(int index){
        remove(index);
    }

    @Override
    public boolean removeAll(Collection c) {
        int index;
        for (Object element : c){
            index = indexOf(element);
            while (index != -1){
                remove(index);
                index = indexOf(element);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        Node curr = head;

        while (curr.getNext() != null){
            curr.set(null);
            curr = curr.getNext();
        }
    }

    @Override
    public boolean retainAll(Collection c) {
        for (Object element : this){
            for (Object keep : c){
                if (element.equals(keep)){
                    break;
                }
            }
            remove(element);
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object element : c){
            if (indexOf(element) == -1){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object get(int index){
        /*if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }*///Seek does this too
        return seek(index).get();
    }

    @Override
    public Object set(int index, Object element) {
        /*if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }*///Seek does this

        Node curr = seek(index);
        Object data = curr.get();

        curr.set(element);
        return data;
    }

    /**
     * Updates value in a node, expands if index > size
     * @param value Data to write
     * @param index Index to replace with value
     */
    public void put(Object value, int index){
        if (index > size - 1){
            insert(value, index);
        }
        else {
            set(index, value);
        }
    }

    @Override
    public int indexOf(Object o) {
        Node curr = head;

        for (int index = 0; index < size; index++){
            if (o.equals(curr.get())){
                return index;
            }
            curr = curr.getNext();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node curr = tail;

        for (int index = size - 1; index >= 0; index--){
            if (o.equals(curr.get())){
                return index;
            }
            curr = curr.getPrev();
        }
        return -1;
    }

    @Override
    public ListIterator listIterator() {
        return new ListIterator() {
            Node curr = head;
            int index = -1;
            boolean legalState = false;

            @Override
            public boolean hasNext() {
                return curr.getNext() != null;
            }

            @Override
            public Object next() {
                legalState = true;
                if (hasNext()) {
                    if (index > -1) {
                        curr = curr.getNext();
                    }
                    index++;
                    return curr.get();
                }
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasPrevious() {
                return curr.getPrev() != null;
            }

            @Override
            public Object previous() {
                legalState = true;
                if (hasPrevious()) {
                    curr = curr.getPrev();
                    index--;
                    return curr.get();
                }
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return index;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                if (!legalState){
                    throw new IllegalStateException();
                }
                if (index != 0) {
                    curr.getPrev().setNext(curr.getNext());
                }
                else {
                    head = curr.getNext();
                }
                if (index != size) {
                    curr.getNext().setPrev(curr.getPrev());
                }
                else {
                    tail = curr.getPrev();
                }
                size--;
                if (index < size){
                    curr = curr.getNext();
                }
                else while (index > size) {
                    curr = curr.getPrev();
                }
            }

            @Override
            public void set(Object o) {
                if (!legalState){
                    throw new IllegalStateException();
                }
                curr.set(o);
            }

            @Override
            public void add(Object o) {
                legalState = false;
                Node newNode = new Node(o);
                newNode.setNext(curr);
                if (index != 0) {
                    newNode.setPrev(curr.getPrev());
                    curr.getPrev().setNext(newNode);
                }
                else {
                    head = newNode;
                }
                curr.setPrev(newNode);
                if (index == size++){
                    tail = newNode;
                }
            }
        };
    }

    @Override
    public ListIterator listIterator(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        ListIterator iter = listIterator();
        for (int i = 0; i < index; i++){
            iter.next();
        }
        return iter;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex >= size || fromIndex > toIndex){
            throw new IndexOutOfBoundsException();
        }
        int newSize = toIndex - fromIndex;
        BetterArray betterArray = new BetterArray(newSize);
        if (fromIndex <= size - 1 - toIndex) {
            Node curr = head;
            for (int i = 0; i < fromIndex; i++) {
                curr = curr.getNext();
            }
            for (int i = 0; i < newSize; i++){
                betterArray.add(i, curr.get());
                curr = curr.getNext();
            }
        }
        else {
            Node curr = tail;
            for (int i = size; i > fromIndex; i--) {
                curr = curr.getPrev();
            }
            for (int i = newSize; i > 0; i--){
                betterArray.add(i,curr.get());
                curr = curr.getPrev();
            }
        }
        return betterArray;
    }


}

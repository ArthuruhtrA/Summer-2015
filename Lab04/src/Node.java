/**
 * Created by Ari Sanders on 10-Jul-15.
 */
public class Node {
    private Object value = null;
    private Node next = null;
    private Node prev = null;

    /**
     * Constructs new Node wih given value
     * @param v Integer value for node
     */
    public Node(Object v){
        this.value = v;
    }

    /**
     * Constructs new empty Node
     */
    public Node(){

    }

    /**
     * Getter
     * @return Integer value of Node
     */
    public Object get(){
        return this.value;
    }

    /**
     * Setter
     * @param v Integer value for Node
     */
    public void set(Object v){
        this.value = v;
    }

    public Node getNext(){
        return this.next;
    }

    public void setNext(Node node){
        this.next = node;
    }

    public Node getPrev(){
        return this.prev;
    }

    public void setPrev(Node node){
        this.prev = node;
    }
}

package rip.dfg;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph<T extends Comparable<T>> {
    public ArrayList<Node<T>> nodes = new ArrayList<>();

    public Node<T> obtain(T data) {
        for(Node<T> node : nodes){
            if(node.data.compareTo(data) == 0) return node;
        }
        Node<T> node = new Node<>(data);
        nodes.add(node);
        return node;
    }

    public Graph() {

    }

    public void link_1way(T A, T B, double weight ){
        Node<T> nA = obtain(A);
        Node<T> nB = obtain(B);
        nA.links.put(nB,weight);
    }

    public void link_2way(T A, T B, double weight ){
        link_2way(A, B, weight, weight);
    }
    public void link_2way(T A, T B, double weightAB, double weightBA ){
        link_1way(A, B, weightAB);
        link_1way(A, B, weightBA);
    }

    // A ---X--> B being removed
    // A <------ B remains
    public void unlink_1way(T A, T B) {
        Node<T> nA = obtain(A);
        Node<T> nB = obtain(B);
        nB.links.remove(nB);
    }
    public void unlink_2way(T A, T B) {
        unlink_1way(A,B);
        unlink_1way(B,A);
    }
    public void unlink_all(T A) {
        Node<T> nA = obtain(A);
        for(Node<T> node : nA.links.keySet()){
            unlink_2way(A, node.data);
        }
    }

}

class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
    T data;
    HashMap<Node<T>, Double> links = new HashMap<>();

    public Node() {}

    public Node(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return super.toString() + ":["+data.toString()+"]";
    }

    @Override
    public int compareTo(Node<T> o) {
        return this.data.compareTo(o.data);
    }


}
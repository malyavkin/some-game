package rip.dfg;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph<T extends Comparable<T>> {
    ArrayList<Node<T>> nodes = new ArrayList<>();

    public Node<T> findOrAdd(T data) {
        for(Node<T> node : nodes){
            if(node.data.compareTo(data) == 0) return node;
        }
        Node<T> node = new Node<>();

        node.data = data;
        nodes.add(node);
        return node;
    }
    public Graph() {

    }
}

class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
    T data;
    HashMap<Node, Double> links = new HashMap<>();
    
    public void link( Node other, double weight) {
        if(links.containsKey(other)){
            System.out.println("link already exist");
        }
        links.put(other, weight);
    }
    public void linkDual( Node other, double weight) {
        this.linkDual(other, weight, weight);
    }
    public void linkDual( Node other, double weight_to, double weight_from) {
        this.link(other, weight_to);
        other.link(this, weight_from);
    }
    public void unlink (Node other) {
        this.links.remove(other);
    }
    public void unlinkDual (Node other) {
        this.unlink(other);
        other.unlink(this);
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
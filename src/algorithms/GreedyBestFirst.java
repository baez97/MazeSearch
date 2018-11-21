package algorithms;

import java.util.PriorityQueue;
import search.Node;
import search.Node.HeuristicComparator;

public class GreedyBestFirst extends ParentAlgorithm {

    @Override
    public Node obtenerNodo() {
        return ((PriorityQueue<Node>)this.frontera).remove();
    }

    @Override
    public void setParams(String[] params) {
        this.frontera = new PriorityQueue<>(new HeuristicComparator());
    }
    
}

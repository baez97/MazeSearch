package algorithms;

import java.util.PriorityQueue;
import search.Node;
import search.Node.EvaluationComparator;

public class AStar extends ParentAlgorithm {

    @Override
    public Node obtenerNodo() {
        return ((PriorityQueue<Node>)this.frontera).remove();
    }

    @Override
    public void setParams(String[] params) {
        this.frontera = new PriorityQueue<>(new EvaluationComparator());
    }
    
}

package algorithms;

import java.util.LinkedList;
import search.Node;

public class DepthFirst extends ParentAlgorithm {
    
    @Override
    public void setParams(String[] params) {
        this.frontera = new LinkedList<>();
    }

    @Override
    public Node obtenerNodo() {
        return ((LinkedList<Node>) this.frontera).removeLast();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import problems.maze.MazeState;
import search.Action;
import search.Node;
import search.SearchAlgorithm;

/**
 *
 * @author josemanuelbaezsoriano
 */
public class BreathFirst extends ParentAlgorithm {
    
    @Override
    public void setParams(String[] params) {
        this.frontera = new LinkedList<>();
    }

    @Override
    public Node obtenerNodo() {
        return ((LinkedList<Node>) this.frontera).removeFirst();
    }
    
    @Override
    public void insertarEnFrontera(Node n) {
        int openSize = this.frontera.size();
        if ( openSize > this.openMaxSize )
            this.openMaxSize = openSize;
        frontera.add(n);
    }
    
}

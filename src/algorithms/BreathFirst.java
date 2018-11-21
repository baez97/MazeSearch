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
public class BreathFirst extends SearchAlgorithm {
    
    LinkedList<Node> frontera = new LinkedList<>();
    Node currentNode;

    @Override
    public void setParams(String[] params) {
        
    }

    @Override
    public void doSearch() {
        
        ArrayList<Node> successors = new ArrayList<>();
        
        frontera.add(new Node(this.problem.initialState()));
        
        while( !solutionFound && !frontera.isEmpty()) {
            
            currentNode = frontera.removeFirst();
            
            if ( this.problem.testGoal(currentNode.getState()) ) {
                this.establecerSolucion(currentNode);
                solutionFound = true;
            }
            
            if ( !(successors = this.getSuccessors(currentNode)).isEmpty() ) {
                this.frontera.addAll(successors);
            } 
        } 
    }
    
    public void establecerSolucion(Node node) {
        
        this.actionSequence.add(0, node.getAction());
        
        while ( (node = node.getParent()) != null ) {
            if ( node.getAction() != null ) {
                this.actionSequence.add(0, node.getAction());
            }
        }
    }
    
}

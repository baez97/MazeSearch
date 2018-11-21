package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import search.Node;
import search.SearchAlgorithm;

public abstract class ParentAlgorithm extends SearchAlgorithm {
    
    Collection<Node> frontera;
    Collection<Node> cerrados = new ArrayList<>();
    Collection<Node> sucesores= new ArrayList<>();
    Node nodoActual;
    
    @Override
    public void doSearch() {
        
        this.insertarEnFrontera(new Node(this.problem.initialState()));
        
        while( !solutionFound && !fronteraVacia()) {
            nodoActual = this.obtenerNodo();
            this.cerrarNodo(nodoActual);
            
            if ( this.problem.testGoal(nodoActual.getState()) ) {
                this.establecerSolucion(nodoActual);
                this.solutionFound = true;
            }
            
            sucesores = this.getSuccessors(nodoActual);
            this.expandedNodes++;
            for (Node s : sucesores) {
                this.generatedNodes++;
                if ( !cerrados.contains(s) ){
                    this.insertarEnFrontera(s);
                }
            }
        }
        
    }
    
    public void insertarEnFrontera(Node n) {
        int openSize = this.frontera.size();
        if ( openSize > this.openMaxSize )
            this.openMaxSize = openSize;
        frontera.add(n);
    }
    
    public boolean fronteraVacia() {
        return this.frontera.isEmpty();
    }
    
    public abstract Node obtenerNodo();
    
    public void cerrarNodo(Node n) {
        this.exploredMaxSize++;
        this.cerrados.add(n);
    }
    
    public void establecerSolucion(Node node) {
        
        this.actionSequence.add(0, node.getAction());
        this.totalCost += node.getCost();
        
        while ( (node = node.getParent()) != null ) {
            if ( node.getAction() != null ) {
                this.actionSequence.add(0, node.getAction());
            }
        }
    }

}

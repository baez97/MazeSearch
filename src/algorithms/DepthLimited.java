/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.LinkedList;
import search.Node;

/**
 *
 * @author josemanuelbaezsoriano
 */
public class DepthLimited extends DepthFirst{

    int limite; 
    
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
            
            if ( nodoActual.getDepth() < this.limite ) {
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
    }
    
    @Override
    public void setParams(String[] params) {
        this.frontera = new LinkedList<>();
        this.limite = Integer.parseInt(params[0]);
    }
    
    // Método auxiliar para IterativeDeepening
    public void setLimite(int l) {
        this.limite = l;
    }
    
}

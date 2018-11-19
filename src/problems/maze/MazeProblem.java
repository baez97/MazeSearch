package problems.maze;

import java.util.ArrayList;
import java.util.HashSet;

import search.State;
import search.Action;
import search.SearchProblem;
import utils.Position;

import visualization.ProblemView;
import visualization.ProblemVisualizable;

import java.util.Set;

/**
 * Extends SearchProblem and implements the functions which define the maze
 * problem. Always uses three cheeses.
 */
public class MazeProblem implements SearchProblem, ProblemVisualizable {

    // Uses always three cheeses (to make it easier implementation).
    private static final int NUM_CHEESES = 3;
    // Penalty factor for fight with the cat. 
    private static final double PENALTY = 2;

    /* Maze */
    Maze maze;

    /**
     * Builds a maze
     */
    public MazeProblem() {
        this.maze = new Maze(10);
    }

    public MazeProblem(int size, int seed, int cats) {
        this.maze = new Maze(size, seed, cats, NUM_CHEESES);
    }

    @Override
    public void setParams(String[] args) {
        // The maze already exists.
        // It is generated with the new parameters. 
        int size = this.maze.size;
        int seed = this.maze.seed;
        int cats = this.maze.numCats;

        if (args.length == 3) {
            try {
                size = Integer.parseInt(args[0]);
                cats = Integer.parseInt(args[1]);
                seed = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.println("Parameters for MazeProblem are incorrect.");
                return;
            }
        }

        // Generates the new maze. 
        this.maze = new Maze(size, seed, cats, NUM_CHEESES);
    }

    // PROBLEM REPRESENTATION (CORRESPONDS TO THE FUNCTIONS IN THE INTERFACE SearchProblem)
    /**
     * Obtiene el estado inicial del problema
     * @return MazeState correspondiente al estado inicial
     */
    @Override
    public State initialState() {
        Position initialPosition;
        int initialCats;
        Set<Position> initialCheese;
        initialPosition = this.maze.input();
        initialCheese   = new HashSet<>();
        initialCats     = 0;
        
        return new MazeState(initialPosition, initialCheese, initialCats);
    }

    /**
     * Obtiene el estado resultante de aplicar una acción a un estado
     * @param state MazeState al que se le aplica la acción
     * @param action Action que se aplica
     * @return nuevo MazeState resultante de aplicar la acción
     */
    @Override
    public State applyAction(State state, Action action) {
        MazeState  mazeState  = (MazeState)  state;
        MazeAction mazeAction = (MazeAction) action;
        Set<Position> eatenCheese; 
        int numCats;
        Position position;
        
        eatenCheese = mazeState.eatenCheese;
        numCats     = mazeState.numCats;
        position    = mazeState.position;        
        
        switch( mazeAction ) {
            case UP:
                position = new Position(position.x, position.y +1);
                break;
            case DOWN:
                position = new Position(position.x, position.y -1);
                break;
            case RIGHT:
                position = new Position(position.x +1, position.y);
                break;
            case LEFT:
                position = new Position(position.x -1, position.y);
                break;
            case EAT:
                eatenCheese.add(position);
                break;
        }
        
        if ( this.maze.containsCat(position) ) {
            numCats++;
        }
        
        return new MazeState(position, eatenCheese, numCats);
    }

    /**
     * Devuelve el conjunto de acciones que se pueden aplicar sobre un estado
     *
     * Estas acciones serán los movimientos que puede hacer según los muros que
     * tenga el Hamnster alrededor y si hay un queso en la posición del estado 
     * devolverá también la accion comer.
     * 
     * @param state Estado del que se devolverán las posibles acciones aplicables
     * @return ArrayList de acciones tipo Action que se pueden aplicar sobre el estado.
     */
    @Override
    public ArrayList<Action> getPossibleActions(State state) {
        MazeState mazeState = (MazeState) state;
        Position currentPosition = mazeState.position;
        ArrayList<Action> actions = new ArrayList<>();
        
        if ( mazeState.numCats < 2 ) {
            Set<Position> positions = this.maze.reachablePositions(currentPosition);
            positions.forEach((p) -> {
                if ( p.x > currentPosition.x ) {
                    actions.add(MazeAction.RIGHT);
                } else if ( p.x < currentPosition.x ) {
                    actions.add(MazeAction.LEFT);
                } else if ( p.y < currentPosition.y ) {
                    actions.add(MazeAction.UP);
                } else if ( p.y > currentPosition.y ) {
                    actions.add(MazeAction.DOWN);
                }
            });
        }
        
        if ( this.maze.containsCheese(currentPosition) ) {
            actions.add(MazeAction.EAT);
        }
        
        return actions;
    }

    /**
     * Devuelve el coste de aplicar una acción sobre un estado
     * 
     * Todas las acciones tienen coste 1 por defecto, salvo que
     * el num_cats de state sea 1, en cuyo caso las acciones que
     * implican movimiento (todas excepto comer) implican coste 2
     * @param state sobre el que se aplica la acción
     * @param action acción a aplicar
     * @return coste de aplicar la acción
     */
    @Override
    public double cost(State state, Action action) {
        double cost = 1.0;
        MazeState mazeState = (MazeState) state;
        
               
        return cost;
    }

    /** 
     * Comprueba si un MazeState es la meta según si su posición
     * es la salida y se ha comido todos los quesos
     * @param chosen MazeState que se quiere comprobar
     * @return Booleano indicando si ese estado es (true) o no (false) la meta
     */
    @Override
    public boolean testGoal(State chosen) {
        MazeState mazeState = (MazeState) chosen;
        
        boolean isInOutput = mazeState.position.equals(this.maze.output());
        boolean hasEatenCheeses = mazeState.num_cheese == NUM_CHEESES;
                
        return isInOutput && hasEatenCheeses;
    }

    @Override
    public double heuristic(State state) {
        
        return 0;
    }

    // VISUALIZATION
    /**
     * Returns a panel with the view of the problem.
     */
    @Override
    public ProblemView getView(int sizePx) {
        return new MazeView(this, sizePx);
    }
}

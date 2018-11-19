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
        int initialCheeses, initialCats;
        initialPosition = new Position(0,0);
        initialCheeses  = 0;
        initialCats     = 0;
        
        return new MazeState(initialPosition, initialCheeses, initialCats);
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
        int num_cheese, num_cats;
        Position position;
        
        num_cheese = mazeState.num_cheese;
        num_cats   = mazeState.num_cats;
        position   = mazeState.position;        
        
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
                num_cheese++;
                break;
        }
        
        if ( this.maze.containsCat(position) ) {
            num_cats++;
        }
        
        return new MazeState(position, num_cheese, num_cats);
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
        
        if ( mazeState.num_cats < 2 ) {
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
        
        if ( mazeState.num_cats == 1 && action != MazeAction.EAT) {
            cost++;
        }
        
        return cost;
    }

    @Override
    public boolean testGoal(State chosen) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public double heuristic(State state) {
        // TODO Auto-generated method stub
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

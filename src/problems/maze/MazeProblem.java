package problems.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

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
                System.out.println("Size: " + size + " | Seed: " + seed + " | Cats: " + cats);
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
        Set<Position> eatenCheese = new HashSet<>(); 
        int numCats;
        Position position;
        
        eatenCheese.addAll(mazeState.eatenCheese);
        numCats     = mazeState.numCats;
        position    = new Position(mazeState.position.asVector());
        
        switch( mazeAction ) {
            case UP:
                position = new Position(position.x, position.y -1);
                break;
            case DOWN:
                position = new Position(position.x, position.y +1);
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
        
        if ( this.maze.containsCat(position) && !mazeState.position.equals(position)) {
            numCats++;
        }
        
        return new MazeState(position, eatenCheese, numCats);
    }

    /**
     * Devuelve el conjunto de acciones que se pueden aplicar sobre un estado.
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
            
            if ( !positions.isEmpty() ) {
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
        
            if ( this.maze.containsCheese(currentPosition) && !mazeState.eatenCheese.contains(currentPosition)) {
                actions.add(MazeAction.EAT);
            }
        
            return actions;
        } else {
            return new ArrayList();
        }
    }

    /**
     * Devuelve el coste de aplicar una acción sobre un estado.
     * 
     * Todas las acciones tienen coste 1 por defecto, salvo que
     * el num_cats de state sea 1, en cuyo caso las acciones que
     * implican movimiento (todas excepto comer) implican coste 2.
     * @param state sobre el que se aplica la acción
     * @param action acción a aplicar
     * @return coste de aplicar la acción
     */
    @Override
    public double cost(State state, Action action) {
        double cost = 1.0;
        MazeState mazeState = (MazeState) state;
        
        if ( mazeState.numCats > 0 ) {
            cost++;
        }
        
        return cost;
    }

    /** 
     * Comprueba si un MazeState es la meta.
     * 
     * Comprobando si su posición es la salida y 
     * si se ha comido todos los quesos
     * @param chosen MazeState que se quiere comprobar
     * @return Booleano indicando si ese estado es (true) o no (false) la meta
     */
    @Override
    public boolean testGoal(State chosen) {
        MazeState mazeState = (MazeState) chosen;
        
        boolean isInOutput = mazeState.position.equals(this.maze.output());
        boolean hasEatenCheese = true;
        for ( Position cheese : this.maze.cheesePositions ) {
            if (!mazeState.eatenCheese.contains(cheese)) {
                hasEatenCheese = false;
            }
        }
                        
        return isInOutput && hasEatenCheese;
    }

    /**
     * Devuelve el valor de heurística.
     * 
     * La heuristica se calcula sumando:
     * 1.- Distancia manhattan hasta el queso no comido más cercano.
     * 2.- Distancia manhattan entre los quesos no comidos.
     * 3.- Distancia desde el queso no comido más lejano hasta la meta
     * @param state Estado del que calcula la heurística
     * @return Valor de la heurística
     */
    @Override
    public double heuristic(State state) {
        double heuristic;
        double distanceToFirstCheese;
        double distanceBetweenCheese = 0;
        double distanceFromLastCheese;
        MazeState mazeState = (MazeState) state;
        TreeMap<Integer, Position> cheeseManhattan = manhattanCheese(mazeState);
        
        if ( !cheeseManhattan.isEmpty() ) {
            distanceToFirstCheese = manhattanDistance(mazeState.position, cheeseManhattan.firstEntry().getValue());
            
            // Calculando la distancia entre quesos (distanceBetweenCheese)
            // -> Como en la primera iteración solo hay un queso y para calcular
            //    la distancia necesito 2, utilizo una posición negativa para que
            //    el método de manhattanDistance devuelva 0
            // -> La variable last me permite almacenar la posición del último queso
            //    que compruebo, que la necesito para calcular la distancia desde éste
            //    hasta la meta. 
            Position previous = new Position(-1, -1); 
            Position last     = new Position(-1, -1);

            for ( Position cheesePos : cheeseManhattan.values() ) {
                last = cheesePos;
                distanceBetweenCheese += manhattanDistance(previous, last);
                previous = cheesePos;
            }
            
            distanceFromLastCheese = manhattanDistance(last, this.maze.output());
            
            heuristic = distanceToFirstCheese +
                        distanceBetweenCheese +
                        distanceFromLastCheese;
        } else {
            heuristic = manhattanDistance(mazeState.position, this.maze.output());
        }
        
        return heuristic;
    }
    
    /**
     * Devuelve la distancia manhattan entre dos objetos Posición.
     * Devuelve 0 si la posición inicial contiene un valor negativo en la x.
     * 
     * @param initial posición inicial
     * @param objective posición final
     * @return valor de la distancia manhattan
     */
    public int manhattanDistance(Position initial, Position objective) {
        int distance = 0;
        if ( initial.x >= 0 ) {
            distance = Math.abs(initial.x - objective.x) +
                       Math.abs(initial.y - objective.y);
        }
        return distance;
    }
    
    /**
     * Devuelve las posiciones de los quesos no comidos ordenadas por
     * distancia a la posición del Hamster en el estado mazeState.
     * @param mazeState estado actual
     * @return TreeMap con la distancia manhattan como clave
     */
    public TreeMap<Integer, Position> manhattanCheese(MazeState mazeState) {
        TreeMap<Integer,Position> manhatCheese = new TreeMap<>();
        int distance;
        for ( Position p : this.maze.cheesePositions ) {
            if (!mazeState.eatenCheese.contains(p)) {
                distance = manhattanDistance(p, mazeState.position);
                manhatCheese.put(distance, p);
            }
        }
        return manhatCheese;
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

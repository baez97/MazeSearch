package problems.maze;

import problems.maze.MazeState;
import search.State;
import utils.Position;
import java.util.Set;

/**
 *  Representa un estado del problema del laberinto.
 * 
 *  Un estado está definido por la posición, el número de quesos
 *  que se ha comido y el número de gatos que se ha encontrado por el camino
 */
public class MazeState extends State implements Cloneable{
	
	/** An state is includes a position given by the coordinates (x,y) */
	public Position position;
        public Set<Position> eatenCheese;
        public int numCats;
        
        public MazeState(Position pos, Set<Position> eaten_cheese, int n_cats) {
            this.position    = pos;
            this.eatenCheese = eaten_cheese;
            this.numCats     = n_cats;
        }

	@Override
	public boolean equals(Object anotherState) {
            MazeState another = (MazeState) anotherState;
            
            boolean pos = this.position.equals(another.position);
            boolean che = this.eatenCheese.equals(another.eatenCheese);
            boolean cat = this.numCats == another.numCats;
            
            return pos && che && cat;
	}

	@Override
	public int hashCode() {
            int cheeseCounter = 6;
            int hash = this.position.x * (10^8) + 
                       this.position.y * (10^7) +
                       this.numCats;
            for ( Position cheesePos : this.eatenCheese ) {
                hash+= cheesePos.x * (10^cheeseCounter);
                cheeseCounter--;
                hash+= cheesePos.y * (10^cheeseCounter);
                cheeseCounter--;
            }
            return hash;
        }
        /**
         * Devuelve el string correspondiente a un MazeState
         * 
         * El string representa la posición, el número de quesos y el de gatos
         * @return String de la forma Position | Cheeses | Cats
         */
	@Override
	public String toString() {
            String s = "Position: " + this.position    + " | " +
                       "Cheeses: "  + this.eatenCheese + " | " +
                       "Cats: "     + this.numCats;
                
            return s;
	}
}

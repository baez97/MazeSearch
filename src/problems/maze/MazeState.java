package problems.maze;

import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 *  Representa un estado del problema del laberinto.
 * 
 *  Un estado está definido por la posición, el número de quesos
 *  que se ha comido y el número de gatos que se ha encontrado por el camino
 */
public class MazeState extends State implements Cloneable{
	
	/** An state is includes a position given by the coordinates (x,y) */
	public Position position;
        public int num_cheese;
        public int num_cats;

	@Override
	public boolean equals(Object anotherState) {
            MazeState another = (MazeState) anotherState;
            
            boolean pos = this.position.equals(another.position);
            boolean che = this.num_cheese == another.num_cheese;
            boolean cat = this.num_cats   == another.num_cats;
            
            return pos && che && cat;
	}

	@Override
	public int hashCode() {
            int hash = this.position.x * 100000 +
                       this.position.y * 100    +
                       this.num_cheese * 10     +
                       this.num_cats;
                               
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
            String s = "Position: " + this.position   + " | " +
                       "Cheeses: "  + this.num_cheese + " | " +
                       "Cats: "     + this.num_cats;
                
            return s;
	}
}

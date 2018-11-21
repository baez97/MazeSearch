# Algoritmos de búsqueda en el espacio de estados

Para ejecutarlo:

`java Solver <pixels-ventana>  maze.MazeProblem <n_celdas> <n_gatos> <semilla> -- <algoritmo> <parámetro>`

## Ejemplos de configuraciones

| Algoritmo | Comando |
| ------------ | --------- |
| Breath First | java Solver 800 maze.MazeProblem 12 4 4 -- BreathFirst |
| Depth First | java Solver 800 maze.MazeProblem 12 4 4 -- DepthFirst |
| Depth Limited | java Solver 800 maze.MazeProblem 10 4 4 -- DepthLimited 50 |
| Uniform Cost | java Solver 800 maze.MazeProblem 12 4 4 -- UniformCost |
| Greedy Best First | java Solver 800 maze.MazeProblem 12 4 4 -- GreedyBestFirst |
| A* | java Solver 800 maze.MazeProblem 15 12 10 -- AStar |

Ejecutar DeepTrip.jar mediante el comando java -jar.

Se debe especificar la estrategia mediante el flag strategy={strategy}
Las estrategias soportadas son: DFS, BFS, ID, AStar y GREEDY.

Si la estrategia es AStar o GREEDY se debe especificar la heristica mediante el flah heuristics={heuristica}
Las heuristicas soportadas son TILES, COLORS, STEPS, GROUP y EMPTY

Puede oparse levantar un tablero de un archivo mediante el flag file={path},
o puede opartse por generar un tablero aleatorio especificando sus dimensione sy cantidad de colores mediante los flags, en este orden,
rows={num1} cols={num2} colors={num3}

EJEMPLOS

java -jar DeepTrip.jar strategy=DFS file=1.board
java -jar DeepTrip.jar strategy=ID rows=2 cols=5 colors=4
java -jar DeepTrip.jar strategy=AStar heuristics=COLORS file=1.board
java -jar DeepTrip.jar strategy=GREEDY rows=4 cols=5 colors=3


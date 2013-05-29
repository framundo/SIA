Para entrenar la neurona se debe utilizar la siguiente funci—n en Matlab:
	learn(S, eta, func, layers, inLength, times, margin, b, adaptation, correction, momentum, calc_error, train, test)
donde cada par‡metro significa:

- S: funci—n que se desea que la neurona aprenda
- eta: tasa de aprendizaje
- func: array de enteros representando las funciones de activaci—n de cada capa, de abajo hacia arriba, donde: [1 => sigmoidea, 2=> exponencial, 3=> identidad]
- layers: array de enteros con la cantidad de neuronas por capa, de abajo hacia arriba
- inLength: tama–o de la entrada
- times: iteraciones totales
- margin: margen de error (se repetir‡n las iterations si no se alcanz— el margen deseado). Negativo si no es necesario
- b: beta o par‡metro de la funci—n de activaci—n
- correction: valor de correcci—n de la derivada de g
- adaptation: vector [a b t1 t2] de correcci—n de eta, t1 y t2 son cada cuanto se corrige cada uno, a y b los valores. [0 0 X X] si no se quiere
- calc_error: 1 si se quire graficar el error cuadr‡tico media, 0 sino.
- momentum: valor entre 0 y 1.
- train: cantidad de patrones de entrenamiento
- test: numero entero que se multiplica por 'traen' para obtener la cantidad de patrones de prueba
  (Si lmit es 500 y limitMult es 2, va a aprender los 500 primeros
  valores y va a generalizar los otros 500 (va a probalo con 1000 = 2*500)


Es necesario cargar los datos de prueba => load("TimeSerie_G5.mat")
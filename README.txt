Los parámetros recibidos por la función "genetic" son:

%
%   S = puntos de la funcion a aprender
%   layers = cantidad de neuronas x capa en forma de array
%   replacement = metodo de reemplazo (1 2 o 3)
%   N = cantidad de populantes
%   K = cantidad de poblacion que pasa a la primera etapa en los metodos 2 y 3
%   maxGen = cantidad de generaciones
%   mut = parametros de mutacion [mutP c genP]
%       mutP = probabilidad de mutar
%       c = valor por el que se mutiplica mutP
%       genP = cada cuantas generaciones
%   back = parametros de backpropagation[backP times]
%       backP = probabilidad de hacer backpropagation
%       times = cantidad de iteraciones
%   crossP = probabilidad de cruce
%   selectionCrits = criterios de seleccion, en forma de array donde cada numero representa:
%       1 = elitismo
%       2 = ruleta
%       3 = torneo
%       4 = boltzman
%       5 = elite-ruleta
%       6 = elite-boltzman
%   cross = funcion de cruzamiento
%   f = fitness pretendido: corta si se alcanza
%   fitestCond = condicion de corte de contenido: [fitChange, fitestGens]
%       fitChange = cambio que debe haber para que corte
%       fitestGens = cantidad de generaciones sin cambios
%   meanCond = condiciond e corte por estructura: [meanChange, meanGens]
%       meanChange = cambio que debe haber para que no corte
%       meanGens = cantidad de generaciones sin cambios
%   fitnessOpt = modo de fitness : 1 para 1/ecm, 2 para -ecm


ejemplo de ejecución:

genetic(S(1:500), 3, 50, 40, 50, [0.1 0 0], [0.05 1000], 0.9, [3 3], @piolaCross, 20000000, [0.1 0], [0.1 0], 1)
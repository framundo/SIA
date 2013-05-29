%
%   S = puntos de la funcion a aprender
%   g = array de funciones de activacion, de arriba para abajo: valores = [1 => sigmoid, 2=> exponential, 3=> identity]
%   layers = cantidad de neuronas x capa en forma de array
%   replacement = metodo de reemplazo (1 2 o 3)
%   N = cantidad de populantes
%   K = cantidad de poblacion que pasa a la primera etapa en los metodos 2
%       y 3
%   maxGen = cantidad de generaciones
%   mutP = probabilidad de mutar
%   mutAnnealing = annealing de la mutacion
%   selectionCrit = criterio de seleccion
%   selectionCrit2 = segundo criterio de seleccion
%   cross = funcion de cruzamiento
%
function out = genetic(S, g, layers, replacement, N, K, maxGen, mutP, mutAnnealing, selectionCrit, selectionCrit2, cross)
    popul = initializePopulation(N, layers);
    for gen=1:maxGen
        fitness = calculateFitness(popul, S, layers, g);
        newPopul = cell(1, N);
        if (replacement == 1)
            %metodo 1
            for n=1:N/2
                 selected = selectionCrit(fitness, 2);
                 [newPopul{2*n}, newPopul{2*n-1}] = cross(mutP, popul{selected(1)}, popul{selected(2)});
            end
            popul = newPopul;
        elseif (replacement == 2)
            %metodo 2
            selected = selectionCrit(fitness, K);
            for n=1:K/2
                [newPopul{2*n}, newPopul{2*n-1}] = cross(mutP, popul{selected(2*n)}, popul{selected(2*n-1)});
            end
            selectedOriginal = selectionCrit2(fitness, N-K);
            i = 1;
            for n=(K+1):N
                newPopul{n} = popul{selectedOriginal(i)};
                i = i+1;
            end
        elseif (replacement == 3)
            %metodo 3
            selected = selectionCrit(fitness, K);
            childs = cell(1, K);
            interPopul = cell(1, K+N);
            for n=1:K/2
                [childs{2*n}, childs{2*n-1}] = cross(mutP, popul{selected(2*n)}, popul{selected(2*n-1)});
            end
            childsFitness = calculateFitness(childs, S, layers, g);
            for n=1:K
                interPopul{n} = childs{n};
            end
            i = 1;
            for n=(K+1):N
                interPopul{n} = popul{i};
                i = i+1;
            end
            interFitness = [childsFitness fitness];
            selected = selectionCrit(interFitness, N);
            for k=1:N
                newPopul{n} = interPopul{selected(n)};
            end
        end
    end
    out = popul;
end


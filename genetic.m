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
%   selectionCrits = criterios de seleccion, en forma de array donde cada numero representa:
%       1 = elitismo
%       2 = ruleta
%       3 = torneo
%       4 = boltzman
%       5 = elite-ruleta
%       6 = elite-boltzman
%   cross = funcion de cruzamiento
%
function out = genetic(S, replacement, N, K, maxGen, mut, back, selectionCrits, cross)
    tic()
    layers = [2 11 8 1];
    popul = initializePopulation(N, layers);
    genP = mut(3);
    c = mut(2);
    mutP = mut(1);
    backP = back(1);
    times = back(2);
    selectionCrit = getSelectionCrit(selectionCrits(1));
    selectionCrit2 = getSelectionCrit(selectionCrits(1));
    g = cell(1,3);
    g{1} = @sigmoid;
    g{2} = @sigmoid;
    g{3} = @sigmoid;
    fitPlot = [];
    for gen=1:maxGen
        if (mod(gen, genP) == 0)
            mutP = mutP * c;
        end
        fitness = calculateFitness(popul, S, layers, g);
        fitPlot(gen) = max(fitness);
        newPopul = cell(1, N);
        if (replacement == 1)
            %metodo 1
            for n=1:N/2
                 selected = selectionCrit(fitness, 2);
                 [newPopul{2*n}, newPopul{2*n-1}] = cross(mutP, backP, popul{selected(1)}, popul{selected(2)}, layers, times, S);
            end
        elseif (replacement == 2)
            %metodo 2
            selected = selectionCrit(fitness, K);
            for n=1:K/2
                [newPopul{2*n}, newPopul{2*n-1}] = cross(mutP, backP, popul{selected(2*n)}, popul{selected(2*n-1)}, layers, times, S);
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
                [childs{2*n}, childs{2*n-1}] = cross(mutP, backP, popul{selected(2*n)}, popul{selected(2*n-1)}, layers, times, S);
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
        popul = newPopul;
        figure(1);
        plot(fitPlot);
    end
%     plot(fitPlot);
%     xlabel('generaciones', 'interpreter', 'latex');
%     ylabel('valor de fitness', 'interpreter', 'latex');
%     title('Fitness del mas apto', 'interpreter', 'latex');
    fitness = calculateFitness(popul, S, layers, g);
    [m, i] = max(fitness);
    out = arrayToLayers(popul{i}, layers);
    ecm = 1/m;
    toc()
end


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
function out = genetic(S, replacement, N, K, maxGen, mut, back, crossP, selectionCrits, cross, f, fitestCond, meanCond, fitnessOpt)
    tic()
    cut = 0;
    layers = [2 11 8 1];
    popul = initializePopulation(N, layers);
    genP = mut(3);
    c = mut(2);
    mutP = mut(1);
    backP = back(1);
    times = back(2);
    selectionCrit = getSelectionCrit(selectionCrits(1));
    selectionCrit2 = getSelectionCrit(selectionCrits(2));
    g = cell(1,3);
    g{1} = @sigmoid;
    g{2} = @sigmoid;
    g{3} = @sigmoid;
    fitChange = fitestCond(1);
    fitGensMax = fitestCond(2);
    fitGens = 0;
    meanGens = 0;
    meanChange = meanCond(1);
    meanGensMax = meanCond(2);
    fitPlot = [];
    meanFitPlot = [];
    leastFitPlot = [];
    gen = 1;
    x = [];
    while ~cut
        gen;
        T = maxGen/gen;
        x(gen)=gen;
        if (mod(gen, genP) == 0)
            mutP = mutP * c;
        end
        fitness = calculateFitness(popul, S, layers, g, fitnessOpt);
        fitPlot(gen) = max(fitness);
        meanFitPlot(gen) = mean(fitness);
        leastFitPlot(gen) = min(fitness);
        newPopul = cell(1, N);
        if (replacement == 1)
            %metodo 1
            for n=1:N/2
                 selected = selectionCrit(fitness, 2, T);
                 [newPopul{2*n}, newPopul{2*n-1}] = cross(crossP, mutP, backP, popul{selected(1)}, popul{selected(2)}, layers, times, S);
            end
        elseif (replacement == 2)
            %metodo 2
            selected = selectionCrit(fitness, K, T);
            for n=1:K/2
                [newPopul{2*n}, newPopul{2*n-1}] = cross(crossP, mutP, backP, popul{selected(2*n)}, popul{selected(2*n-1)}, layers, times, S);
            end
            selectedOriginal = selectionCrit2(fitness, N-K, T);
            i = 1;
            for n=(K+1):N
                newPopul{n} = popul{selectedOriginal(i)};
                i = i+1;
            end
        elseif (replacement == 3)
            %metodo 3
            selected = selectionCrit(fitness, K, T);
            childs = cell(1, K);
            interPopul = cell(1, K+N);
            for n=1:K/2
                [childs{2*n}, childs{2*n-1}] = cross(crossP, mutP, backP, popul{selected(2*n)}, popul{selected(2*n-1)}, layers, times, S);
            end
            childsFitness = calculateFitness(childs, S, layers, g, fitnessOpt);
            for n=1:K
                interPopul{n} = childs{n};
            end
            for n=(K+1):N+K
                interPopul{n} = popul{n-K};
            end
            interFitness = [childsFitness fitness];
            selected = selectionCrit(interFitness, N, T);
            for n=1:N
                newPopul{n} = interPopul{selected(n)};
            end
        end
         figure(2);
         plot(x, fitPlot, '-', x, meanFitPlot, '-.', x, leastFitPlot, ':');
        popul = newPopul;
         xlabel('generaciones', 'interpreter', 'latex');
         ylabel('valores de fitness', 'interpreter', 'latex');
         title('Evolucion del fitness', 'interpreter', 'latex');
         legend('Individuo mas apto', 'Promedio de la poblacion', 'Individuo menos apto');
        if (fitGensMax ~= 0 && gen ~= 1 && abs(fitPlot(gen-1) - fitPlot(gen)) < fitChange)
           fitGens = fitGens + 1;
           if (fitGens >= fitGensMax)
               cut = 1;
               disp('corto por contenido')
           end
        else
            fitGens = 0;
        end
        if (meanGensMax ~= 0 && ~cut && gen ~= 1 && abs(meanFitPlot(gen-1) - meanFitPlot(gen)) < meanChange)
           meanGens = meanGens + 1;
           if (meanGens >= meanGensMax)
               cut = 1;
               disp('corto por estructura')
           end
        else
            meanGens = 0;
        end
        cut = cut || gen > maxGen || fitPlot(gen) >= f;
        gen = gen+1;
    end
%         figure(2);
%         subplot(2,1,1)
%         plot(fitPlot);
%         xlabel('generaciones', 'interpreter', 'latex');
%         ylabel('valor de fitness', 'interpreter', 'latex');
%         title('Fitness del mas apto', 'interpreter', 'latex');
%         subplot(2,1,2)
%         plot(meanFitPlot);
%         xlabel('generaciones', 'interpreter', 'latex');
%         ylabel('valor de fitness', 'interpreter', 'latex');
%         title('Fitness promedio', 'interpreter', 'latex');
    fitness = calculateFitness(popul, S, layers, g, fitnessOpt);
    [m, i] = max(fitness);
    out = arrayToLayers(popul{i}, layers);
    if (fitnessOpt == 1)
        ecm = 1/m
    elseif (fitnessOpt == 2)
        ecm = -(m-10)
    end
    gen
    toc()
end


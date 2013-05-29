function out = genetic(S, g, layers, replacement, population, K, maxGen, mutation, selectionCrit, cross)
    popul = initializePopulation(population, layers);
    for gen=1:maxGen
       fitness = calculateFitness(popul, S, layers, g);
       %metodo 1
       if replacement == 1
           newPopul = cell(1, population);
           for n=1:population/2
                selectionIndexes = selectionCrit(fitness, 2);
                [newPopul{2*n}, newPopul{2*n-1}] = cross(popul{selectionIndexes(1)}, popul{selectionIndexes(2)});
           end
           popul = newPopul;
       end
    end
    out = popul;
end


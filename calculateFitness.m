function fitness = calculateFitness(population, S, layers, g)
    l = length(population);
    fitness = zeros(1,l);
    for k=1:l
       fitness(k) = geneticECM(S, arrayToLayers(population{k}, layers), g, layers);
    end
end
function fitness = calculateFitness(population, S, layers, g)
    l = length(population);
    fitness = zeros(1,l);
    for k=1:l
       fitness(k) = geneticECM(S, population{k}, g, layers);
    end
end
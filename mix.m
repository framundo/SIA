function indexes = mix(fitness, K, N, mixed)
    fit = elitism(fitness, N);
    f = fitness;
    %ver como hacer excepcion de los elegidos con fitness
    other = mixed(fitness, K-N);
end
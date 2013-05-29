function winners = tourney(fitness, K)
    winners = zeros(1,K);
    l = length(fitness);
    for k=1:K
        i = fix(rand() * l) +1;
        j = fix(rand() * l) +1;
        if (fitness(i) >= fitness(j))
            aux = i;
            i = j;
            j = aux;
            % J siempre es mas grande
        end
        if (rand() > 0.75)
            winners(k) = j;
        else
            winners(k) = i;
        end
    end
end
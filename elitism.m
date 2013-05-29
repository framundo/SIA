function elite = elitism(fitness, K)
    f = zeros(1, length(fitness));
    for k=1:length(fitness)
       f(k) = fitness(k);
    end
    elite = zeros(1, K);
    for k=1:K
        [m, index] = max(f);
        elite(k) = index;
        f(k)=0;
    end
end
function elite = elitism(fitness, K, T)
    f = fitness;
    elite = zeros(1, K);
    for k=1:K
        [m, index] = max(f);
        elite(k) = index;
        f(index)=0;
    end
end
function [child1, child2] = piolaCross(crossP, mutP, backP, gen1, gen2, layers, times, S)
    child1 = gen1;
    child2 = gen2; 
    if (rand() < crossP)
        random = rand();
        child1 = gen1 * random + gen2 * (1 - random);
        child2 = gen1 * (1 - random) + gen2 * random;
        [child1, child2] = mutate(mutP, child1, child2);
        [child1, child2] = backProp(backP, child1, child2, layers, times, S);
    end
end
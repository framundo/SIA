function [child1, child2] = paramCross(mutP, gen1, gen2)
    l = length(gen1);
    child1 = gen1;
    child2 = gen2;
    for k=1:l
        if (rand() > 0.7)
            k
            aux = child1(k);
            child1(k) = child2(k);
            child2(k) = aux;
        end
    end
    [child1, child2] = mutate(mutP, child1, child2);
end
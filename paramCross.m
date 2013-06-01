function [child1, child2] = paramCross(crossP, mutP, backP, gen1, gen2, layers, times, S)
    l = length(gen1);
    child1 = gen1;
    child2 = gen2;
    if (rand() < crossP)
        for k=1:l
            if (rand() > 0.7)
                aux = child1(k);
                child1(k) = child2(k);
                child2(k) = aux;
            end
        end
        [child1, child2] = mutate(mutP, child1, child2);
        [child1, child2] = backProp(backP, child1, child2, layers, times, S);  
    end
end
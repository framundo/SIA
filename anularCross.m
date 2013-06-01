function [child1, child2] = anularCross(crossP, mutP, backP, gen1, gen2, layers, times, S)
    l = length(gen1);
    child1 = gen1;
    child2 = gen2; 
    if (rand() < crossP)
        n = fix(rand() * l)+1;
        %L = fix(rand() * l)+1;
        L = 5;
        for k=n:(n+L-1)
            i = mod(k,l)+1;
            child1(i) = gen2(i);
            child2(i) = gen1(i);
        end
        [child1, child2] = mutate(mutP, child1, child2);
        [child1, child2] = backProp(backP, child1, child2, layers, times, S);
    end
end
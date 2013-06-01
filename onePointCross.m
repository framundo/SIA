function [child1, child2] = onePointCross(mutP, backP, gen1, gen2, layers, times, S)
    l = length(gen1);
    n = fix(rand() * l)+1;
    if (n == l)
        child1 = gen1;
        child2 = gen2;
    else
        child1 = [gen1(1:n) gen2(n+1:l)];
        child2 = [gen2(1:n) gen1(n+1:l)];
    end
    [child1, child2] = mutate(mutP, child1, child2);
    [child1, child2] = backProp(backP, child1, child2, layers, times, S);
end
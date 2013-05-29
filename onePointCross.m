function [child1, child2] = onePointCross(gen1, gen2)
    l = length(gen1);
    n = fix(rand() * l)+1;
    if (n == l)
        child1 = gen1;
        child2 = gen2;
    else
        child1 = [gen1(1:n) gen2(n+1:l)];
        child2 = [gen2(1:n) gen1(n+1:l)];
    end
end
function [child1, child2] = twoPointCross(crossP, mutP, backP, gen1, gen2, layers, times, S)
    if (rand() < crossP)
        l = length(gen1);
        n1 = fix(rand() * l)+1;
        while(1)
            n2 = fix(rand() * l)+1;
            if (n2 ~= n1) 
                break; %la forma de hacer un do while
            end
        end
        if (n1 > n2)
            aux = n1;
            n1 = n2;
            n2 = aux;
        end
        if (n2 == l)
            child1 = [gen1(1:n1) gen2(n1+1:l)];
            child2 = [gen2(1:n1) gen1(n1+1:l)];
        else
            child1 = [gen1(1:n1) gen2(n1+1:n2) gen1(n2+1:l)];
            child2 = [gen2(1:n1) gen1(n1+1:n2) gen2(n2+1:l)];
        end
        [child1, child2] = mutate(mutP, child1, child2);
        [child1, child2] = backProp(backP, child1, child2, layers, times, S);
    else
        child1 = gen1;
        child2 = gen2;
    end
end
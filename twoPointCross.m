function [child1, child2] = twoPointCross(gen1, gen2)
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
end
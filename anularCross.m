function [child1, child2] = anularCross(gen1, gen2)
    l = length(gen1);
    child1 = gen1;
    child2 = gen2; 
    n = fix(rand() * l)+1
    L = fix(rand() * l)+1
    for k=n:(n+L-1)
        i = mod(k,l)+1;
        child1(i) = gen2(i);
        child2(i) = gen1(i);
    end
end
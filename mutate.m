function [child1, child2] = mutate(mutP, child1, child2)
    child1 = innerMutate(mutP, child1);
    child2 = innerMutate(mutP, child2);
end

function gen = innerMutate(mutP, gen)
    if (rand() < mutP)
        disp('mutate!')
        n = fix(rand()*length(gen) +1);
        gen(n) = rand() - 0.5;
    end
end
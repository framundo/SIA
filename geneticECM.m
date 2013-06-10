
function out = geneticECM(S, W, g, layers, opt)
    l = length(layers);
    p = layers(1)+1;
    out = 0;
    limit = length(S);
    while (p < limit)
        pattern_o = calculateRecursive(S, W, g, layers(1), layers(2:l), 1, p);
        pattern_s = S(p);
        out = out + (pattern_s-pattern_o)^2;
        p = p + 1;
    end
    out = out/(length(S));
    if (opt ==1)
        out = 1/out;
    elseif (opt ==2)
        out = -out;
        out = out+10;
    end
end
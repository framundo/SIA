
function out = geneticECM(S, W, g, layers)
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
end
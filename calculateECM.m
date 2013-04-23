
function out = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, printIt)
    cuad(t) = 0;
    p = inLength+1;
    while (p < limit)
        pattern_o = calculateRecursive(S, W, g, inLength, layers, b, p);
        pattern_s = S(p);
        cuad(t) = cuad(t) + (pattern_s-pattern_o)^2;
        p = p + 1;
    end
    cuad(t) = cuad(t)/(limit-(inLength+1));
    out = cuad(t);
    if(printIt)
        figure(2);
        plot(cuad);
    end
end
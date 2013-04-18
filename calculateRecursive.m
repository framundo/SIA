function out = calculateRecursive(S, W, g, inLength, l, layers, b, i)
    for k=inLength:-1:1
        initialData(k) = S(i-k);
    end
    for k=1:l
       temp_y = calculate(W{k}, [-1 initialData], g{k}, layers(k), b);
       initialData = temp_y;
    end
    out = temp_y;
end
function out = calculateRecursive(S, W, g, inLength, layers, b, x)
    initialData = zeros(1, inLength);
    for k=1:inLength
        initialData(k) = S(x-k);
    end
    for k=1:length(layers)
       temp_y = calculate(W{k}, [-1 initialData], g{k}, layers(k), b);
       initialData = temp_y;
    end
    out = temp_y;
end
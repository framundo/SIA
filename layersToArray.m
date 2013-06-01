function o = layersToArray(cell, layers)
    l = length(layers);
    len = 0;
    for j=2:l
        len = len + layers(j) * (layers(j-1)+1);
    end
    o = zeros(1, len);
    z = 1;
    for k=1:length(cell)
        [rows, cols] = size(cell{k});
        for i=1:rows
            for j=1:cols
                o(z) = cell{k}(i, j);
                z = z + 1;
            end
        end
    end
end
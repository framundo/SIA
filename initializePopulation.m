function popul = initializePopulation(size, layers)
    l = length(layers);
    popul = cell(1, size);
    for k=1:size
       popul{k} = cell(1, l-1);
       for j=2:l
           len = len+ layers(j)* (layers(j-1)+1)
       end
    end
    (rand(layers(j), layers(j-1) + 1))-0.5;
end
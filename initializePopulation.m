function popul = initializePopulation(size, layers)
    l = length(layers);
    popul = cell(1, size);
    for k=1:size
       len = 0;
       for j=2:l
           len = len + layers(j) * (layers(j-1)+1);
       end
       popul{k} = rand(1, len)-0.5;
    end
end
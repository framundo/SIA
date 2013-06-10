function [child1, child2] = backProp(backP, child1, child2, layers, times, S)
    child1 = innerBack(backP, child1, layers, times, S);
    child2 = innerBack(backP, child2, layers, times, S);
end

function o = innerBack(backP, gen, layers, times, S)
    o = gen;
    if (rand() < backP)
%         disp('backP!')
        g = cell(1,3);
        eta = 0.3;
        g{1} = @sigmoid;
        g{2} = @sigmoid;
        g{3} = @sigmoid;
        g_d = cell(1,3);
        g_d{1} = @sigmoid_derivated;
        g_d{2} = @sigmoid_derivated;
        g_d{3} = @sigmoid_derivated;
        delta = cell(1,3);
        for k=1:3
            delta{k} = zeros(layers(k+1), layers(k)+1);
        end
        etaPlot = zeros(1, times);
        cuad = zeros(1, times);
        w = backpropagation(S, arrayToLayers(gen, layers), g, g_d, eta, delta, etaPlot, layers(2:length(layers)), 1, layers(1), -10e-5, [0.01 0.1 6 6], 0.1, 0.1, 0, length(S)-2, 1, cuad, times);
        o = layersToArray(w, layers);
    end
end
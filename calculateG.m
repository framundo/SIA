
function [g, g_d] = calculateG(val)
    if (val == 1) %sigmoid
        g = @sigmoid;
        g_d = @sigmoid_derivated;
    elseif (val == 2) %exp
        g = @expo;
        g_d = @expo_derivated;
    elseif (val == 3) %linear
        g = @identity;
        g_d = @identity_derivated;
    end
end

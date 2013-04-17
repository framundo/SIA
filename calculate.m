function [o, h] = calculate(W, data, g, neurons, b)
    h = (W*data')';
    o = g(h,b);
end
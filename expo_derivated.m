function out = expo_derivated(x,b)
    out = 2*b*expo(x, b).*(1-expo(x,b));
end
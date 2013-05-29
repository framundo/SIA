% S = serie de datos (previamente normalizados)
% W = cell array de pesos
% g = cell array de funciones de activacion
% inLength = tamanio de la entrada
% layers = array que representa la arquitectura
% b = valor de beta (usado en la funcion de activacion, normalmente 1)
% limit = cantidad de entradas de la serie a calcular
function check(S, W, g, inLength, layers, b, limit)
    y = zeros(1, limit - (inLength+1));
    x = zeros(1, limit - (inLength+1));
    c=0;
    for k=(inLength+1):limit
       x(k)=S(k);
       y(k)= calculateRecursive(S, W, g, inLength, layers, b, k);
       c = c+ (x(k) - y(k))^2;
    end
    figure(1)
    plot(x,y,'o')
    c/(limit-(inLength+1))
end
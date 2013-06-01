% Parametros
%
% S: funcion
% eta: eta 
% func: array de funciones de activacion, de arriba para abajo: valores = [1 => sigmoid, 2=> exponential, 3=> identity]
% layers: cantidad de neuronas por capa, de abajo hacia arriba
% inLength: tamano de la entrada
% times: iteraciones
% margin: margen de error. Negativo si no se quiere
% b: beta
% correction: valor de correccion de la derivada de g. Sugerido 0.1.
% adaptation: vector [a b t1 t2] de correccion de eta, t1 y t2 son cada cuanto se corrige cada uno, a y b los valores. [0 0 X X] si no se quiere
% calc_error: graficar error cuadratico medio
% momentum: valor entre 0 y 1
% limit: la cantidad limite de entradas iniciales de la serie a aprender
% limitMult: El valor a multiplicar el limite para probar si se aprendio o
%   no (Si lmit es 500 y limitMult es 2, va a aprender los 500 primeros
%   valores y va a generalizar los otros 500 (va a probalo con 1000 = 2*500)
function W = learn(S, eta, func, layers, inLength, times, margin, b, adaptation, correction, momentum, calc_error, limit, limitMult)
    tic()
    l = length(layers);
    if (func(1) == 1)
        S = S./max(S);
    elseif(func(1) ==2)
        S = (S./8)+0.5;
    end
    W = cell(1,l);
    g = cell(1,l);
    g_prime = cell(1,l);
    delta = cell(1,l);
    
    etaPlot = [];
    etaPlot(1) = eta;
    for k =1:l
        [G, G_prime] = calculateG(func(k));
        g{k} = G;
        g_prime{k} = G_prime;
    end
    W{1} = ((rand(layers(1), inLength+1))-0.5);
    delta{1} = zeros(layers(1), inLength+1);
    for k=2:l
        W{k} = ((rand(layers(k), layers(k-1)+1))-0.5);
        delta{k} = zeros(layers(k), layers(k-1)+1);
    end    
    cuad = zeros(1, fix(times/limit));
    
    W = backpropagation(S, W, g, g_prime, eta, delta, etaPlot, layers, b, inLength, margin, adaptation, correction, momentum, calc_error, limit, limitMult, cuad, times);

%     subplot(2,2,1), plot(y,y2, 'o',z,z2,'x');
%     xlabel('S(t)', 'interpreter', 'latex');
%     ylabel('$\hat{S}(t)$', 'interpreter', 'latex');
%     title('Serie real sobre serie aproximada', 'interpreter', 'latex');
%     legend('patrones de entrenamiento', 'patrones de prueba');
%     if(calc_error)
%         subplot(2,2,2), plot(cuad);
%         xlabel('\''epocas', 'interpreter', 'latex');
%         ylabel('E(t)', 'interpreter', 'latex');
%         title('Error cuadr\''atico medio', 'interpreter', 'latex');
%     end
%     if(length(etaPlot) >1)
%         subplot(2,2,3), plot(etaPlot);
%         ylabel('eta', 'interpreter', 'latex');
%         xlabel('tiempo', 'interpreter', 'latex');
%         title('Evoluci\''on de eta', 'interpreter', 'latex');
%     end
%     subplot(2,2,4), plot(dif);
%     ylabel('S - O', 'interpreter', 'latex');
%     xlabel('\''iteraciones', 'interpreter', 'latex');
%     title('Error en la salida', 'interpreter', 'latex');
    
    toc()
    ecm = calculateECM(cuad, S, t, W, g, layers, b, inLength, limitMult*limit, 0)
end



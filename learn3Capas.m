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
% adaptation: vector [a b t] de correccion de eta, t es cada cuanto se corrige, a y b los valores. [0 0 0] si no se quiere
% calc_error: graficar error cuadratico medio
% momentum: valor entre 0 y 1
% limit: la cantidad limite de entradas iniciales de la serie a aprender
% limitMult: El valor a multiplicar el limite para probar si se aprendio o
%   no (Si lmit es 500 y limitMult es 2, va a aprender los 500 primeros
%   valores y va a generalizar los otros 500 (va a probalo con 1000 = 2*500)
function W = learn3Capas(S, eta, func, layers, inLength, times, margin, b, adaptation, correction, momentum, calc_error, limit, limitMult)
    tic()
    l = length(layers);
    S = S./max(S);
    etaPlot = [];
    etaPlot(1) = eta;
    etaIndex=2;
    g{1} = @sigmoid;
    g{2} = @sigmoid;
    g{3} = @sigmoid;
    W1 = ((rand(layers(1), inLength+1))-0.5)/5;
    W1_old = zeros(layers(1), inLength+1);
        W2 = ((rand(layers(2), layers(1)+1))-0.5)/5;
        W2_old = zeros(layers(2), layers(1)+1);
        W3 = ((rand(layers(3), layers(2)+1))-0.5)/5;
        W3_old = zeros(layers(3), layers(2)+1);
       
    flag = 1;
	consecutive = [0 0];
	if(adaptation(1)==0 && adaptation(2)==0)
		withAdaptation = 0;
	else
		withAdaptation = 1;
    end
    
    cuad = zeros(1, fix(times/limit));
    cuadindex=1;
    dif = zeros(1, times);
   
    
    while (flag)
        for t = 1:times
            
            % Error cuadratico medio
            if(calc_error && rem(t, limit) == 0)
                W{1} = W1;
                W{2} = W2;
                W{3} = W3;
                cuad(cuadindex) = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, 0);
                if (cuad(cuadindex) <margin)
                    break;
                end
                cuadindex = cuadindex+1;
                
            end
%             e = rand()*2*pi;
            x = fix(rand()*limit)+inLength+1;
            e = [];
            for k=inLength:-1:1
               e(k) = S(x-k);
            end
            
            data1 = [-1 e];
            % ida
                [o1, h1] = calculate(W1, data1, @sigmoid, layers(1), b);
                data2 = [-1, o1];
                [o2, h2] = calculate(W2, data2, @sigmoid, layers(2), b);
                data3 = [-1, o2];
                [o3, h3] = calculate(W3, data3, @sigmoid, layers(3), b);
            
            
            % Vuelta
            
            dif(t) = S(x) - o3;
            d3 = (sigmoid_derivated(h3, b) + correction) * dif(t);
            delta3 = eta * d3 * data3;
                innerSum = W3(:, 2:end)' * d3';
                d2 = (correction + sigmoid_derivated(h2, b)) .* innerSum';
                delta2 = eta * d2' * data2;
                
                innerSum = W2(:, 2:end)' * d2';
                d1 = (correction + sigmoid_derivated(h1, b)) .* innerSum';
                delta1 = eta * d1' * data1;
            
     
            %Control de aumento o disminucion de eta
            addDelta = 1;
            if (withAdaptation && t>1)
                if (dif(t) > dif(t-1))
                    consecutive(2) = consecutive(2)+1;
                    consecutive(1)=0;
                    if (adaptation(3) == consecutive(2))
                        %bajo eta
                        consecutive(2) = 0;
                        eta = eta*(1 - adaptation(2));
                        etaPlot(etaIndex)=eta;
                        etaIndex = etaIndex+1;
                        t = t-adaptation(3);
                        addDelta = 0;
                    end
                elseif (dif(t) < dif(t-1))
                    if (consecutive(1) == 0)
                         previous_W1 = W1;
                         previous_W2 = W2;
                         previous_W3 = W3;

                    end
                    consecutive(1) = consecutive(1)+1;
                    consecutive(2)=0;
                    if (adaptation(3) == consecutive(1))
                        %subo eta
                        consecutive(1) = 0;
                        eta = eta+ adaptation(1);
                        etaPlot(etaIndex)=eta;
                        etaIndex = etaIndex+1;
                        W1 = previous_W1;
                        W2 = previous_W2;
                        W3 = previous_W3;
                    end
                end
            end
            if (addDelta)
                   W1 = W1 + delta1 + momentum * W1_old;
                   W2 = W2 + delta2 + momentum * W2_old;
                   W3 = W3 + delta3 + momentum * W3_old;
            end
        end
        W1_old = W1;
        W2_old = W2;
        W3_old = W3;

%         i=0;
        flag = 0;
%         if (margin > 0)
%             while (~flag && i < limit)	
%                 if(abs(calculate(W{2}, [-1 calculate(W{1}, [-1 i], g{1}, layers(1), b)], g{2}, layers(2), b) - S(i)) > margin)
%                     flag = 1;
%                 end
%                 i = i + 1;
%             end
%         end
    end
    W{1} = W1;
    W{2} = W2;
    W{3} = W3;
    
    
    i =inLength+1;
    y = zeros(1, limit - (inLength+1));
    y2 = zeros(1, limit - (inLength+1));
    if (limitMult ~= 1)
        z = zeros(1, limit - (inLength+1));
        z2 = zeros(1, limit - (inLength+1));
    else
       z =[];
       z2 = [];
    end
    while (i < limit)
        y(i)=calculateRecursive(S, W, g, inLength, l, layers, b, i);
        y2(i) = S(i);
        x(i)=i;
        i = i + 1;
    end
    while (i < limitMult*limit)
        z(i-limit+1)=calculateRecursive(S, W, g, inLength, l, layers, b, i);
        z2(i-limit+1) = S(i);
        x(i)=i;
        i = i + 1;
    end
    figure(1);
    plot(x,[y z], x, [y2 z2]);
    figure(2);
    plot(y,y2, '.',z,z2,'x');
  % for i = 1:length(E)
    % disp("RESULTADOS:")
    % e = E(i, :)
    % O = calculate(Wo, [-1 calculate(Wh, [-1 e], g, hidden_neurons)], g, 1)
  % endfor
    if(calc_error)
        figure(3);
        plot(cuad);
    end
    if(length(etaPlot) >1)
        figure(4);
        plot(etaPlot);
    end
    toc()
    ecm = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, 0)

end

function [g, g_d] = calculateG(val)
    if (val == 1) %sigmoid
        g = @sigmoid;
        g_d = @sigmoid_derivated;
    elseif (val == 2) %exp
        
    elseif (val == 3) %linear
        g = @identity;
        g_d = @identity_derivated;
    end
end

function out = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, printIt)
    cuad(t) = 0;
    p = inLength+1;
    l = length(layers);
    while (p < limit)
        pattern_o = calculateRecursive(S, W, g, inLength, l, layers, b, p);
        pattern_s = S(p);
        cuad(t) = cuad(t) + (pattern_s-pattern_o)^2;
        p = p + 1;
    end
    cuad(t) = cuad(t)/(limit-(inLength+1));
    out = cuad(t);
    if(printIt)
        figure(2);
        plot(cuad);
    end
end
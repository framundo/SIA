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
function W = learn(S, eta, func, layers, inLength, times, margin, b, adaptation, correction, momentum, calc_error, limit, limitMult)
    tic()
    l = length(layers);
    if (func(1) == 1)
        S = S./max(S);
    elseif(func(1) ==2)
        S = (S./8)+0.5;
    end
    W = cell(1,l);
    W_old = cell(1,l);
    g = cell(1,l);
    g_prime = cell(1,l);
    etaPlot = [];
    etaPlot(1) = eta;
    etaIndex=2;
    for k =1:l
        [G, G_prime] = calculateG(func(k));
        g{k} = G;
        g_prime{k} = G_prime;
    end
    W{1} = ((rand(layers(1), inLength+1))-0.5)/5;
    W_old{1} = zeros(layers(1), inLength+1);
    for k=2:l
        W{k} = ((rand(layers(k), layers(k-1)+1))-0.5)/5;
        W_old{k} = zeros(layers(k), layers(k-1)+1);
    end
	consecutive = [0 0];
	if(adaptation(1)==0 && adaptation(2)==0)
		withAdaptation = 0;
	else
		withAdaptation = 1;
    end
    
    cuad = zeros(1, fix(times/limit));
    cuadindex=1;
    dif = zeros(1, times);
    O = cell(l);
    H = cell(l);
    data = cell(l+1);
    d = cell(l);
    delta = cell(l);
    
    for t = 1:times
        
        % Error cuadratico medio
        if(calc_error && rem(t, limit) == 0)
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
        
        data{1} = [-1 e];
        % ida
        for k=1:l
            [o, h] = calculate(W{k}, data{k}, g{k}, layers(k), b);
            O{k} = o;
            H{k} = h;
            data{k+1} = [-1, O{k}];
        end
        
        % Vuelta
        
        dif(t) = S(x) - O{l};
        d{l} = (g_prime{l}(H{l}, b) + correction) * dif(t);
        delta{l} = eta * d{l} * data{l};
        for k=l-1:-1:1
            innerSum = W{k+1}(:, 2:end)' * d{k+1}';
            d{k} = (correction + g_prime{k}(H{k}, b)) .* innerSum';
            delta{k} = eta * d{k}' * data{k};
        end
 
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
                    previous_W = W;
                end
                consecutive(1) = consecutive(1)+1;
                consecutive(2)=0;
                if (adaptation(3) == consecutive(1))
                    %subo eta
                    consecutive(1) = 0;
                    eta = eta+ adaptation(1);
                    etaPlot(etaIndex)=eta;
                    etaIndex = etaIndex+1;
                    W = previous_W;
                end
            end
        end
        if (addDelta)
            for k=1:l
               W{k} = W{k} + delta{k} + momentum * W_old{k};
            end
        end
        W_old = W;
    end
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
%     figure(1);
%     plot(x,[y z], x, [y2 z2]);
    figure(2);
    plot(y,y2, '.',z,z2,'x');
    if(calc_error)
        figure(3);
        plot(cuad);
    end
    if (length(etaPlot) > 1)
        figure(4);
        plot(etaPlot);
    end
    toc()
    ecm = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, 0)
end



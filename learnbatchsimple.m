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
function W = learnbatchsimple(S, eta, func, layers, inLength, times, margin, b, calc_error)
    tic()
    limit = 500;
    l = length(layers);
    sampleSize = length(S);
    S = S./(max(S) * 2) + 0.5;
    W = cell(l);
    g = cell(l);
    g_prime = cell(l);
    
    for k =1:l
        [G, G_prime] = calculateG(func(k));
        g{k} = G;
        g_prime{k} = G_prime;
    end
    W{1} = ((rand(layers(1), inLength+1))-0.5)/5;
    for k=2:l
        W{k} = ((rand(layers(k), layers(k-1)+1))-0.5)/5;
    end
    flag = 1;

    
    cuad = zeros(1,fix(times/limit));
    cuadindex=1;
    dif = zeros(1, times);
    O = cell(l);
    H = cell(l);
    data = cell(l+1);
    d = cell(l);
    delta = cell(l);
    while (flag)
        for t = 1:times
            % Empiezo una �poca
            
            % Error cuadratico medio
            if(calc_error)
                cuad(cuadindex) = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, 0);
                cuadindex = cuadindex+1;
            end
            
            
            delta{1} = zeros(layers(1), inLength+1);
            for k=2:l
                delta{k} = zeros(layers(k), layers(k-1)+1);
            end
            
            %Calculo el delta batch
            for j = 1+inLength:limit
                e = [];
                for k=inLength:-1:1
                   e(k) = S(j-k);
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

                dif(t) = S(j) - O{l};
                d{l} = g_prime{l}(H{l}, b) * dif(t);
                delta{l} = eta * d{l} * data{l} / sampleSize;
                for k=l-1:-1:1
                    innerSum = W{k+1}(:, 2:end)' * d{k+1}';
                    d{k} = g_prime{k}(H{k}, b) .* innerSum';
                    delta{k} = delta{k} + eta * d{k}' * data{k};
                end

            end
        
            %corrijo los pesos
            for k=1:l
               W{k} = W{k} + delta{k};
            end
            %termina la �poca
        end
        %     batch
        i=0;
        flag = 0;
        if (margin > 0)
            while (~flag && i < limit)	
                if(abs(calculate(W{2}, [-1 calculate(W{1}, [-1 i], g{1}, layers(1), b)], g{2}, layers(2), b) - S(i)) > margin)
                    flag = 1;
                end
                i = i + 1;
            end
        end
    end
    i =inLength+1;
    j=1;
    x = zeros(1, 2*limit - (inLength+1));
    y = zeros(1, 2*limit - (inLength+1));
    y2 = zeros(1, 2*limit - (inLength+1));
    while (i < 2*limit)
        y(j)=calculateRecursive(S, W, g, inLength, l, layers, b, i);
        y2(j) = S(i);
        x(j)=i;
        j = j + 1;
        i = i + 1;
    end
    figure(1);
    plot(x,y, x, y2);
    figure(2);
    plot(y,y2, '.');
  % for i = 1:length(E)
    % disp("RESULTADOS:")
    % e = E(i, :)
    % O = calculate(Wo, [-1 calculate(Wh, [-1 e], g, hidden_neurons)], g, 1)
  % endfor
    if(calc_error)
        figure(3);
        plot(cuad);
    end
    toc()
    ecm = calculateECM(cuad, S, t, W, g, layers, b, inLength, limit, 0)

end
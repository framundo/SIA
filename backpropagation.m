function W = backpropagation(S, W, g, g_prime, eta, delta, etaPlot, layers, b, inLength, margin, adaptation, correction, momentum, calc_error, limit, limitMult, cuad, times)
    l = length(layers);
    delta_old = cell(1,l);
    O = cell(l);
    H = cell(l);
    data = cell(l+1);
    d = cell(l);    
    cuadindex=1;
    etaIndex=2;
    dif = zeros(1, times);
    consecutive = [0 0];
	if (adaptation(1)==0 && adaptation(2)==0)
		withAdaptation = 0;
	else
		withAdaptation = 1;
    end
    for t = 1:times
        % Error cuadratico medio
        if(calc_error && rem(t, limit) == 0)
            cuad(cuadindex) = calculateECM(cuad, S, t, W, g, layers, b, inLength, limitMult*limit, 0);
            plot(cuad)
            if (cuad(cuadindex) < margin)
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
        delta_old{l} = delta{l};
        delta{l} = eta * d{l} * data{l};
        for k=l-1:-1:1
            innerSum = W{k+1}(:, 2:end)' * d{k+1}';
            d{k} = (correction + g_prime{k}(H{k}, b)) .* innerSum';
            delta_old{k} = delta{k};
            delta{k} = eta * d{k}' * data{k};
        end
 
        %Control de aumento o disminucion de eta
        addDelta = 1;
        if (withAdaptation && t>1)
            if (dif(t) < dif(t-1))
                consecutive(1) = consecutive(1)+1;
                consecutive(2)=0;
                if (adaptation(3) == consecutive(1))
                    %subo eta
                    consecutive(1) = 0;
                    eta = eta+ adaptation(1);
                    etaPlot(etaIndex)=eta;
                    etaIndex = etaIndex+1;
                end
            elseif (dif(t) > dif(t-1))
                if (consecutive(2) == 0)
                    previous_W = W; %voy mal x primera vez, me guardo los W hasta ahora
                end
                consecutive(2) = consecutive(2)+1;
                consecutive(1)=0;
                if (adaptation(4) == consecutive(2))
                    %bajo eta
                    consecutive(2) = 0;
                    eta = eta*(1 - adaptation(2));
                    etaPlot(etaIndex)=eta;
                    etaIndex = etaIndex+1;
                    t = t-adaptation(4);
                    addDelta = 0;
                    W = previous_W;
                end
            end
        end
        if (addDelta)
            for k=1:l
               W{k} = W{k} + delta{k} + momentum * delta_old{k};
            end
        end
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
        y(i)=calculateRecursive(S, W, g, inLength, layers, b, i);
        y2(i) = S(i);
        x(i)=i;
        i = i + 1;
    end
    while (i < limitMult*limit)
        z(i-limit+1)=calculateRecursive(S, W, g, inLength, layers, b, i);
        z2(i-limit+1) = S(i);
        x(i)=i;
        i = i + 1;
    end
end
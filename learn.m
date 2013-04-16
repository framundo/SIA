% Parametros
%
% S: funcion
% eta: eta 
% func: array de funciones de activacion, de arriba para abajo: valores = [1 => sigmoid, 2=> exponential, 3=> identity]
% len: tamano de la entrada
% times: iteraciones
% margin: margen de error. Negativo si no se quiere
% b: beta
% correction: valor de correccion de la derivada de g. Sugerido 0.1.
% adaptation: vector [a b t] de correccion de eta, t es cada cuanto se corrige, a y b los valores. [0 0 0] si no se quiere
% calc_error: graficar error cuadratico medio
% momentum: valor entre 0 y 1
function learn(S, eta, func, hidden_neurons, len, times, margin, b, adaptation, correction, momentum,calc_error)
    tic()
    [g_h, g_h_deriv] = calculateG(func(1));    
    [g_o, g_o_deriv] = calculateG(func(2)); 

    Wh = rand(hidden_neurons, len+1);
    Wo = rand(1, hidden_neurons+1);
    Wh_old = 0;
    Wo_old = 0;
    flag = 1;
	consecutive = [0 0];
	if(adaptation(1)==0 && adaptation(2)==0)
		withAdaptation = 0;
	else
		withAdaptation = 1;
    end
    while (flag)
        for t = 1:times
      % Error cuadratico medio
            if(calc_error)
                cuad(t) = 0;
                p = -1;
                while (p < 1)
                    pattern_o = calculate(Wo, [-1 calculate(Wh, [-1 p], g_o, hidden_neurons, b)], g_h, 1, b);
                    pattern_s = S(p);
                    cuad(t) = cuad(t) + (pattern_s-pattern_o)^2;
                    p = p + 0.1;
                end
                cuad(t) = cuad(t)/(1/0.1);
            end

      % index = 1 +fix(rand()*length(E));
      % e = E(index, :);
      % e = generateBits(len);
            e = rand()*2*pi;
            data = [-1 e];
      % Atravesar neurona
            [hidden_o, hidden_h] = calculate(Wh, data, g_h, hidden_neurons, b);
            data2 = [-1 hidden_o];
            [O, H] = calculate(Wo, data2, g_o, 1, b);
      % Corregir para atr??s

            dif(t) = S(e) - O;
            d_o = (g_o_deriv(H, b) + correction) * dif(t);
            delta_o = eta * d_o * data2;
			d_h = (correction + g_h_deriv(hidden_h, b)) .* (Wo(1, 2:end) .* d_o);
            delta_h = eta*d_h'*data;
      % O
      % H
      % Wo
      % Wh
      % delta_o
      % delta_h
            addDelta = 1;
            if (withAdaptation && t>1)
                if (dif(t) > dif(t-1))
                    consecutive(2) = consecutive(2)+1;
                    consecutive(1)=0;
			%disp("eta bajo");
                    if (adaptation(3) == consecutive(2))
                        consecutive(2) = 0;
                        eta = eta*(1 - adaptation(2));
                        t = t-1;
                        addDelta = 0;
                    end
                elseif (dif(t) < dif(t-1))
                    consecutive(1) = consecutive(1)+1;
                    consecutive(2)=0;
			%disp("eta subio");
                    if (adaptation(3) == consecutive(1))
                        consecutive(1) = 0;
                        eta = eta+ adaptation(1);
                    end
                end
            end
            if (addDelta)						
                Wo = Wo + delta_o + momentum*Wo_old;
                Wh = Wh + delta_h + momentum*Wh_old;
            end
        end
        Wo_old = Wo;
        Wh_old = Wh;
  %batch
        i=0;
        flag = 0;
        if (margin > 0)
            while (~flag && i < 2*pi)	
                if(abs(calculate(Wo, [-1 calculate(Wh, [-1 i], g_o, hidden_neurons, b)], g_h, 1, b) - S(i)) > margin)
        % disp("una vuelta mas");
                    flag = 1;
                end
                i = i + 0.1;
            end
        end
    end
    i =0;
    j=1;
    while (i < 2*pi)
        y(j) = calculate(Wo, [-1 calculate(Wh, [-1 i], g_o, hidden_neurons, b)], g_h, 1, b);
        y2(j) = S(i);
        x(j)=i;
        j = j + 1;
        i = i + 0.1;
    end
    figure(1);
    plot(x,y, x, y2);
  % for i = 1:length(E)
    % disp("RESULTADOS:")
    % e = E(i, :)
    % O = calculate(Wo, [-1 calculate(Wh, [-1 e], g, hidden_neurons)], g, 1)
  % endfor
    if(calc_error)
        figure(2);
        plot(cuad);
    end
    toc()
end

function [E, S] = init()
  E = [-1 -1 ; -1 1; 1 -1; 1 1];
  S = [-1 1 1 -1]';
end

function [o, h] = calculate(W, data, g, neurons, b)
    h = (W*data')';
    o = g(h,b);
end

function a = square(x)
  a = x*x;
end

function out = generateBits(len)
  out = [];
  for i =1:len
    out(i) = step(rand()-0.5);
  end
end

function out = parity(x)
  par = 0;
  for i = 1:length(x)
    if x == 1
      par = par + x(i);
    end  
  end
  out = rem(par,2);
end

function out = palindrome(x)
  l = length(x);
  for i=1:floor(l/2)
    if(x(i) ~= x(l-i+1))
      out = -1;
      return;
    end
  end
  out = 1;
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

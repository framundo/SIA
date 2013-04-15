function [E, S] = init()
  E = [-1 -1 ; -1 1; 1 -1; 1 1];
  S = [-1 1 1 -1]';
endfunction

function [o, h] = calculate(W, data, g, neurons, b)
  h = (W*data')';
  for i = 1:neurons
    o(i) = g(h(i), b);
  endfor
endfunction 

function a = square(x)
  a = x*x;
end

function out = step(x)
  if (x>0) 
    out = 1;
  else
    out = -1;
  end
endfunction

function out = identity(x, b)
  out = b*x;
endfunction

function out = sigmoid(x, b)
  out = tanh(b*x);
endfunction

function out = derivate(x, g, b)
 % if (g == @sigmoid)
    out = b - b*g(x, b)**2;
  %elseif (g == @identity)
   % out=b;
  %end
endfunction

function out = generateBits(len)
  out = [];
  for i =1:len
    out(i) = step(rand()-0.5);
  end
endfunction

function out = parity(x)
  par = 0;
  for i = 1:length(x)
    if x == 1
      par+= x(i);
    end  
  end
  out = rem(par,2);
endfunction

function out = palindrome(x)
  l = length(x);
  for i=1:floor(l/2)
    if(x(i) != x(l-i+1))
      out = -1;
      return;
    end
  end
  out = 1;
end

% Parametros
% S: funcion
% eta: eta 
% g: funcion de activacion
% len: tamaño de la entrada
% times: iteraciones
% margin: margen de error. Negativo si no se quiere
% b: beta
% adaptation: valor de adaptacion. Sugerido 0.1.
% correction: vector [a b] de correccion de eta. [0 0] si no se quiere
function learn(S, eta, g, hidden_neurons, len, times, margin, b, adaptation, correction)
  tic()
  Wh = rand(hidden_neurons, len+1);
  Wo = rand(1, hidden_neurons+1);
  flag = 1;
	if(adaptation(1)==0 && adaptation(2)==0)
		withAdaptation = 0;
	else
		withAdaptation = 1;
	end
  while (flag)
    for t = 1:times
      % index = 1 +fix(rand()*length(E));
      % e = E(index, :);
      % e = generateBits(len);
      e = rand()*2*pi;
      data = [-1 e];
      % Atravesar neurona
      [hidden_o, hidden_h] = calculate(Wh, data, g, hidden_neurons, b);
      data2 = [-1 hidden_o];
      [O, H] = calculate(Wo, data2, g, 1, b);
      % Corregir para atrás
      dif(t) = S(e) - O;
      cuad(t) = 0.5*(S(e) - O)**2;
      d_o = (derivate(H, g, b) + correction) * dif(t);
      delta_o = eta * d_o * data2;
      d_h = [];
      l = length(data);
      for k = 1:hidden_neurons
        %cableado a una sola salida
        inner_sum = Wo(1, k+1) * d_o;
        d_h(k) = (correction + derivate(hidden_h(k),g,b)) * inner_sum;
      end
      delta_h = eta*d_h'*data;
      % O
      % H
      % Wo
      % Wh
      % delta_o
      % delta_h
			if (withAdaptation && t>1)
				if (dif(t) > dif(t-1))
					%disp("eta bajo");
					eta = eta*(1 - adaptation(2));
					t--;
				elseif (dif(t) < dif(t-1))
					%disp("eta subio");
					eta += adaptation(1);
					Wo += delta_o;
      		Wh += delta_h; 
				end
			else
				Wo += delta_o;
      	Wh += delta_h;
			end
    endfor
    %batch
    i=0;
    flag = 0;
    if (margin > 0)
      while (!flag && i < 2*pi)	
        if(abs(calculate(Wo, [-1 calculate(Wh, [-1 i], g, hidden_neurons, b)], g, 1, b) - S(i)) > margin)
          % disp("una vuelta mas");
          flag = 1;
        end
        i+= 0.1;
      endwhile
    endif
  endwhile
  i =0;
  j=1;
  while (i < 2*pi)
    y(j) = calculate(Wo, [-1 calculate(Wh, [-1 i], g, hidden_neurons, b)], g, 1, b);
    y2(j) = S(i);
    x(j)=i;
    j++;
    i+= 0.1;
  end
  plot(x,y, x, y2);
  % for i = 1:length(E)
    % disp("RESULTADOS:")
    % e = E(i, :)
    % O = calculate(Wo, [-1 calculate(Wh, [-1 e], g, hidden_neurons)], g, 1)
  % endfor
  % plot(cuad)
  toc()
endfunction

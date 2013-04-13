function [E, S] = init()
  E = [-1 -1 ; -1 1; 1 -1; 1 1];
  S = [-1 1 1 -1]';
endfunction

function [o, h] = calculate(W, data, g, neurons)
  l = length(data);
  h = zeros(1,neurons);
  for i = 1:neurons
    for j = 1:l
      h(i) += W(i,j)*data(j);
    endfor
  endfor
  for i = 1:neurons
    o(i) = g(h(i));
  endfor
endfunction 

function out = step(x)
  if (x>0) 
    out = 1;
  else
    out = -1;
  end
endfunction

function out = identity(x)
  out = x;
endfunction

function out = sigmoid(x)
  out = tanh(x);
endfunction

function out = derivate(x, g)
  if (g == @sigmoid)
    out = 1 - g(x)**2;
  elseif (g == @identity)
    out=1;
  end
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


function learn(S, eta, g, hidden_neurons, len, times, margin)
  Wh = rand(hidden_neurons, len+1);
  Wo = rand(1, hidden_neurons+1);
  flag = 1;
  while (flag)
    for i = 1:times
      % index = 1 +fix(rand()*length(E));
      % e = E(index, :);
      % e = generateBits(len);
      e = rand()*2*pi;
      data = [-1 e];
      % Atravesar neurona
      [hidden_o, hidden_h] = calculate(Wh, data, g, hidden_neurons);
      data2 = [-1 hidden_o];
      [O, H] = calculate(Wo, data2, g, 1);
      % Corregir para atrás
      dif(i) = S(e) - O;
      cuad(i) = 0.5*(S(e) - O)**2;
      d_o = derivate(H, g) * dif(i);
      delta_o = eta * d_o * data2;
      d_h = [];
      l = length(data);
      for i = 1:hidden_neurons
        %cableado a una sola salida
        inner_sum = Wo(1, i+1) * d_o;
        d_h(i) = derivate(hidden_h(i),g) * inner_sum;
      end
      delta_h = zeros(hidden_neurons,l);
      for i = 1:hidden_neurons
        for j= 1:l
          delta_h(i,j) = eta * d_h(i) * data(j);
        end
      end
      Wo += delta_o;
      Wh += delta_h; 
    endfor
    %batch
    i=0;
    flag = 0;
    while (!flag && i < 2*pi)
      if(abs(calculate(Wo, [-1 calculate(Wh, [-1 i], g, hidden_neurons)], g, 1) - S(i)) > margin)
        disp("una vuelta mas");
        flag = 1;
      end
      i+= 0.2;
    endwhile
  endwhile
  i =0;
  j=1;
  while (i < 2*pi)
    y(j) = calculate(Wo, [-1 calculate(Wh, [-1 i], g, hidden_neurons)], g, 1);
    y2(j) = S(i);
    x(j)=i;
    j++;
    i+= 0.2;
  end
  plot(x,y, x, y2)
  % for i = 1:length(E)
    % disp("RESULTADOS:")
    % e = E(i, :)
    % O = calculate(Wo, [-1 calculate(Wh, [-1 e], g, hidden_neurons)], g, 1)
  % endfor
  % plot(cuad)
endfunction

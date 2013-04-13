function [E, S] = init()
  E = [-1 -1; -1 1; 1 -1; 1 1];
  S = [-1 1 1 -1]';
endfunction

function o = calculate_hidden(Wh, data, g, hidden_neurons)
  l = length(data);
  h = zeros(l);
  for j = 1:hidden_neurons
    for i = 1:l
      h(j) += Wh(j,i)*data(i);
    endfor
  endfor
  for j = 1:hidden_neurons
    o(j) = g(h(j));
  endfor
endfunction 

function o = calculate_output(Wo, data, g)
  h = 0;
  l = length(data);
  for i = 1:l
    h += Wo(i)*data(i);
  endfor
  h;
  o = g(h);
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

function learn(E, S, eta, g, hidden_neurons, times)
  l = length(E(1, :));
  Wh = rand(hidden_neurons, l+1);
  Wo = rand(1, l+1);
  for i = 1:times
    index = 1+fix(rand()*length(E));
    e = E(index, :);
    data = [-1 e];
    % Atravesar neurona
    H = calculate_hidden(Wh, data, g, hidden_neurons);
    H
    O = calculate_output(Wo, H, @identity);
    O
    % Corregir para atrás
    dif(i) = S(index) - O;
    delta_o = eta*dif(i)*H;
    % TO DO corregir pesos capa oculta
    % O
    disp("=============");
  endfor
  for i = 1:length(E)
    e = E(i, :)
    O = calculate(Wo, calculate_hidden(Wh, [-1 e], g, hidden_neurons), @identity)
  endfor
  plot(dif)
endfunction

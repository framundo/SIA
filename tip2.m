function [E, S] = init()
  E = [-1 -1; -1 1; 1 -1; 1 1];
  S = [-1 -1 -1 1]';
endfunction

function o = calculate(W, data, g)
  h = 0;
  l = length(data);
  for i = 1:l
    h += W(i)*data(i);
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

function learn(E, S, eta, g, times)
  l = length(E(1, :));
  W = rand(1, l+1);
  for i = 1:times
    index = 1+fix(rand()*length(E));
    e = E(index, :);
    data = [-1 e];
    O = calculate(W, data, g);
    dif(i) = S(index) - O;
    delta = eta*dif(i)*data;
    W = W+delta;
    % O
    % disp("=============");
  endfor
  for i = 1:length(E)
    e = E(i, :)
    O = calculate(W, [-1 e], g)
  endfor
  plot(dif)
endfunction
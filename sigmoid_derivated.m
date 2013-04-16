function out = sigmoid_derivated(x, b)
  out = b - b*sigmoid(x, b).^2;
end
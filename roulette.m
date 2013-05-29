function roulette = roulette(fitness, K)
    f = fitness;
    l = length(fitness);
    sorted = zeros(2,l);
    F = sum(f);
    old = 0;
    for k=1:l
      [m, index] = max(f);
      sorted(1,k) = index;
      sorted(2,k) = old + m/F;
      old = sorted(2,k);
      f(index) = 0;
    end
    for k=1:K
      r = rand();
      for j=1:l
        if(sorted(2,j)>r)
          roulette(k)=sorted(1,j);
          break;
        end
      end
    end
end

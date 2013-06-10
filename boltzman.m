function boltz = boltzman(fitness, K, T)
	acum = 0;
    fNorm = fitness./max(fitness);
    l = length(fitness);
    f = zeros(1,l);
	for i=1:l
		acum = acum + exp(fNorm(i)/T);
    end
    acum = acum/l;
	for i=1:l
		f(i) = exp(fNorm(i)/T)/acum; 
        %probar el T para que en el primer paso queden todos parecidos y vayan agrandandose las diferencias 
	end
	boltz = roulette(f, K, T);
end

function boltzman = boltzman(fitness, K, T)
	acum = 0;
	count = 0;
	for i=1:length(fitness)
		acum = acum + fitness(i);
		count = count + 1;
	end
	avg = acum/count;
	for i=1:length(fitness)
		fitness(i) = exp(fitness(i)/T)/(exp(acum/T)); %probar el T para que en el primer paso queden todos parecidos y vayan agrandandose las diferencias 
	end
	boltzman = roulette(fitness, K, T);
end

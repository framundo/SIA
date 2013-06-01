function mix = mix(crit, fitness, K, T)
	N= round(0.5*K);
	elite = elitism(fitness, N);
	l = length(fitness);
	j=1;
	for k=1:l
		if (~ismember(k,elite))
			rest(j)=fitness(k);
			j=j+1;
		end
	end	
	r = crit(rest,K-N, T);
	mix = elite;
	for j=1:length(r)
		k=r(j);
		for z=1:N
			if(elite(z)<=k)
				k = k+1;
		end
		mix(N+j)=k;
	end
end

function results = boltzStatistic(times,t)
	results = zeros(1,8);
	for k=1:times
		out = boltzman([1 2 3 4 5 6 7 8], 4, t);%mod(k,10)+1);
		for i=1:4
			results(out(i)) = results(out(i)) + 1;
		end
	end
	results = results./(times*4);
end

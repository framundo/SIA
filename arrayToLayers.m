function out = arrayToLayers(array, layers)
	l = length(layers);
	out = cell(1,l-1);
	j = 1;
	for k=2:l
		rows = layers(k);
		cols = layers(k-1)+1;
		out{k-1	} = zeros(rows, cols);
		for z=1:layers(k)
			row = array(j:j+cols-1);
			out{k-1}(z,:) = row;
			j = j + cols;
		end
	end
end

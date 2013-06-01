function o = getSelectionCrit(i)
    switch(i)
        case 1
            o = @elitism;
        case 2
            o = @rouette;
        case 3
            o = @tourney;
        case 4
            o = @boltzman;
        case 5
            o = @mix-roulette;
        case 6
            o = @mix-boltzman;
    end
end
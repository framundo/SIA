function o = getSelectionCrit(i)
    switch(i)
        case 1
            o = @elitism;
        case 2
            o = @roulette;
        case 3
            o = @tourney;
        case 4
            o = @boltzman;
        case 5
            o = @mixRoulette;
        case 6
            o = @mixBoltzman;
    end
end
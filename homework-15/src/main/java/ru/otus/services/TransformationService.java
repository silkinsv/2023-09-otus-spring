package ru.otus.services;

import ru.otus.domain.Butterfly;
import ru.otus.domain.Caterpillar;
import ru.otus.domain.Cocoon;
import ru.otus.domain.Egg;

public interface TransformationService {
    Caterpillar transformToCaterpillar(Egg egg);

    Cocoon transformToCocoon(Caterpillar caterpillar);

    Butterfly transformToButterfly(Cocoon cocoon);
}

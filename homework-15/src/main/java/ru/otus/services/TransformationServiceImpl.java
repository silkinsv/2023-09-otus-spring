package ru.otus.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.domain.Butterfly;
import ru.otus.domain.Caterpillar;
import ru.otus.domain.Cocoon;
import ru.otus.domain.Egg;

@Service
@Slf4j
public class TransformationServiceImpl implements TransformationService {
    @Override
    public Caterpillar transformToCaterpillar(Egg egg) {
        log.info("Яйца становятся гусеницами");
        return new Caterpillar();
    }

    @Override
    public Cocoon transformToCocoon(Caterpillar caterpillar) {
        log.info("Гусеницы становятся коконами");
        return new Cocoon();
    }

    @Override
    public Butterfly transformToButterfly(Cocoon cocoon) {
        log.info("Коконы становятся бабочками");
        return new Butterfly();
    }
}

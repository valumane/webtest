package fr.kb.tp.tp1;

import fr.kb.tp.tp1.modele.Content;
import fr.kb.tp.tp1.modele.Tirage;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:tirage_test;DB_CLOSE_DELAY=-1",
    "spring.h2.console.enabled=false"
})
class ContentIntegrationTest {
    @Autowired Content content;

    @Test
    void douzeGroupesRepartisSansDoublonsEtUnTirageStable() {
        Set<Integer> sujets = new HashSet<>();
        for (int groupe = 1; groupe <= 12; groupe++) {
            Tirage premiereFois = content.tirerPourGroupe(groupe);
            Tirage deuxiemeFois = content.tirerPourGroupe(groupe);
            assertEquals(premiereFois.getSujet(), deuxiemeFois.getSujet());
            sujets.add(premiereFois.getSujet());
        }
        assertEquals(12, sujets.size());
        assertEquals(0, content.getRestants());
    }
}

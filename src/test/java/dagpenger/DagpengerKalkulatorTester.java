package dagpenger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.dagpenger.DagpengerKalkulator.BeregningsMetode;
import no.nav.årslønn.Årslønn;

public class DagpengerKalkulatorTester {

    private static DagpengerKalkulator kalkulatorMedFastG(double g) {
        return new DagpengerKalkulator(new no.nav.grunnbeløp.GrunnbeløpVerktøy(() -> g));
    }

    @Test
    public void testSkalHaRettigheterTilDagpengerUtifraSisteTreÅrslønner()  {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 445000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 465000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 300000));
        assertTrue(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalHaRetigheterTilDagpengerSisteÅrslønn() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 467000));
        assertTrue(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalIkkeHaRettigheterTilDagpengerSisteTreÅrslønner()  {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        assertFalse(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalIkkeHaRettigheterTilDagpengerSisteÅrslønn()  {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 130000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 0));
        assertFalse(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilSisteÅrslønn() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 550000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        // assertEquals("SISTE_ÅRSLØNN", dagpengerKalkulator.velgBeregningsMetode());
        assertEquals(BeregningsMetode.SISTE_ÅRSLØNN, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilMaksÅrslønnGrunnbeløp() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        // assertEquals("MAKS_ÅRLIG_DAGPENGERGRUNNLAG", dagpengerKalkulator.velgBeregningsMetode());
        assertEquals(BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilGjennomsnittetAvTreÅr() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 330000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 400000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 334000));
        // assertEquals("GJENNOMSNITTET_AV_TRE_ÅR", dagpengerKalkulator.velgBeregningsMetode());
        assertEquals(BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testDagsatsKalkulertUtifraSisteÅrslønn() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 550000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        assertEquals(2116, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertUtifraMaksÅrligGrunnbeløp() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        assertEquals(2308, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertUtifraTreÅrsGjennomsnitt() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 330000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 334000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 400000));
        assertEquals(1365, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertIkkeRettPåDagpenger() {
        DagpengerKalkulator dagpengerKalkulator = kalkulatorMedFastG(100_000);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 80000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 70000));
        assertEquals(0, dagpengerKalkulator.kalkulerDagsats());
    }
}

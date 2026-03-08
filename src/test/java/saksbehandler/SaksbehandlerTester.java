package saksbehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.saksbehandler.Behandlingsstatus;
import no.nav.saksbehandler.DagpengerResultat;
import no.nav.saksbehandler.ResultatRepository;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.saksbehandler.Spesialisering;

public class SaksbehandlerTester {

    @Test
    void henterKunUbehandledeInnenforEgenSpesialisering() {
        ResultatRepository lager = new ResultatRepository();

        // Ulike resultater i lageret
        DagpengerResultat innvilget1 = lager.opprettUbehandlet(1000, Spesialisering.INNVILGET);
        DagpengerResultat innvilget2 = lager.opprettUbehandlet(1100, Spesialisering.INNVILGET);
        DagpengerResultat maks = lager.opprettUbehandlet(2308, Spesialisering.INNVILGET_MED_MAKSSATS);
        DagpengerResultat avslag = lager.opprettUbehandlet(0, Spesialisering.AVSLAG_FOR_LAV_INNTEKT);

        // Gjør én innvilget behandlet for å teste status-filtrering
        lager.oppdaterStatus(innvilget2.id(), Behandlingsstatus.GODKJENT);

        Saksbehandler saksbehandler = new Saksbehandler("Alice", Spesialisering.INNVILGET, lager);

        List<DagpengerResultat> ubehandlede = saksbehandler.hentUbehandledeResultater();

        assertEquals(1, ubehandlede.size());
        assertEquals(innvilget1.id(), ubehandlede.get(0).id());
        assertEquals(Behandlingsstatus.UBEHANDLET, ubehandlede.get(0).behandlingsstatus());

        // Sanity (andre typer ligger der fortsatt, men skal ikke komme med)
        assertNotEquals(maks.id(), ubehandlede.get(0).id());
        assertNotEquals(avslag.id(), ubehandlede.get(0).id());
    }

    @Test
    void godkjennSetterStatusTilGodkjent() {
        ResultatRepository lager = new ResultatRepository();
        DagpengerResultat resultat = lager.opprettUbehandlet(1924, Spesialisering.INNVILGET);

        Saksbehandler saksbehandler = new Saksbehandler("Alice", Spesialisering.INNVILGET, lager);
        saksbehandler.godkjenn(resultat.id());

        DagpengerResultat etter = lager.finn(resultat.id()).orElseThrow();
        assertEquals(Behandlingsstatus.GODKJENT, etter.behandlingsstatus());
    }

    @Test
    void avslåSetterStatusTilAvslått() {
        ResultatRepository lager = new ResultatRepository();
        DagpengerResultat resultat = lager.opprettUbehandlet(0, Spesialisering.AVSLAG_FOR_LAV_INNTEKT);

        Saksbehandler saksbehandler = new Saksbehandler("Alice", Spesialisering.AVSLAG_FOR_LAV_INNTEKT, lager);
        saksbehandler.avslå(resultat.id());

        DagpengerResultat etter = lager.finn(resultat.id()).orElseThrow();
        assertEquals(Behandlingsstatus.AVSLÅTT, etter.behandlingsstatus());
    }

    @Test
    void kanIkkeBehandleResultatMedAnnenSpesialisering() {
        ResultatRepository lager = new ResultatRepository();
        DagpengerResultat resultat = lager.opprettUbehandlet(2308, Spesialisering.INNVILGET_MED_MAKSSATS);

        Saksbehandler saksbehandler = new Saksbehandler("Alice", Spesialisering.INNVILGET, lager);

        assertThrows(IllegalArgumentException.class, () -> saksbehandler.godkjenn(resultat.id()));
        assertEquals(Behandlingsstatus.UBEHANDLET, lager.finn(resultat.id()).orElseThrow().behandlingsstatus());
    }

    @Test
    void kanIkkeBehandleAlleredeBehandletResultat() {
        ResultatRepository lager = new ResultatRepository();
        DagpengerResultat resultat = lager.opprettUbehandlet(1924, Spesialisering.INNVILGET);

        Saksbehandler saksbehandler = new Saksbehandler("S1", Spesialisering.INNVILGET, lager);
        saksbehandler.godkjenn(resultat.id());

        assertThrows(IllegalStateException.class, () -> saksbehandler.godkjenn(resultat.id()));
        assertThrows(IllegalStateException.class, () -> saksbehandler.avslå(resultat.id()));
    }
}
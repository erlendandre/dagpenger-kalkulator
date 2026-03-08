package no.nav.saksbehandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory lagring av dagpengerresultater
 *
 * Brukes som et enkelt "pool" der saksbehandlere kan hente ubehandlede resultater
 * filtrert på spesialisering. Resultater blir liggende i lager, men forsvinner
 * fra ubehandlet-listen når status endres fra ubehandlet.
 *
 * @author Erlend André Høntorp
 * @version 1.0
 */
public class ResultatRepository {
    private final List<DagpengerResultat> resultater = new ArrayList<>();
    private long nextResultatId = 1;

    public DagpengerResultat opprettUbehandlet(double beregnetDagsats, Spesialisering spesialisering) {
        DagpengerResultat resultat = new DagpengerResultat(nextResultatId++, beregnetDagsats, spesialisering, Behandlingsstatus.UBEHANDLET);
        resultater.add(resultat);
        return resultat;
    }

    public List<DagpengerResultat> hentUbehandlede(Spesialisering spesialisering) {
        List<DagpengerResultat> ubehandlede = new ArrayList<>();
        for (DagpengerResultat r : resultater) {
            if (r.behandlingsstatus() == Behandlingsstatus.UBEHANDLET && r.spesialisering() == spesialisering) {
                ubehandlede.add(r);
            }
        }
        return ubehandlede;
    }

    public Optional<DagpengerResultat> finn(long resultatId) {
        for (DagpengerResultat r : resultater) {
            if (r.id() == resultatId) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public void oppdaterStatus(long resultatId, Behandlingsstatus nyStatus) {
        DagpengerResultat resultat = finn(resultatId)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke resultat med id=" + resultatId));
        resultat.settStatus(nyStatus);
    }
}
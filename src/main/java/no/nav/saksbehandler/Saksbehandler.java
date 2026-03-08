package no.nav.saksbehandler;

import java.util.List;
import java.util.Objects;

/**
 * Saksbehandler som kan hente ubehandlede resultater innen
 * sin spesialisering og godkjenne eller avslå disse.
 *
 * Saksbehandler kan kun behandle resultater innen egen 
 * spesialisering og kun resultater som fortsatt er ubehandlet
 * 
 * @author Erlend André Høntorp
 * @version 1.0
 */
public class Saksbehandler {
    private final String navn;
    private final Spesialisering spesialisering;
    private final ResultatRepository resultatRepository;

    public Saksbehandler(String navn, Spesialisering spesialisering, ResultatRepository resultatlager) {
        this.navn = Objects.requireNonNull(navn);
        this.spesialisering = Objects.requireNonNull(spesialisering);
        this.resultatRepository = Objects.requireNonNull(resultatlager);
    }

    public String navn() { return navn; }
    public Spesialisering spesialisering() { return spesialisering; }

    // Krav 1: hente ubehandlede resultater innenfor egen spesialisering
    public List<DagpengerResultat> hentUbehandledeResultater() {
        return resultatRepository.hentUbehandlede(spesialisering);
    }

    // Krav 2: godkjenne resultat innen egen spesialisering
    public void godkjenn(long resultatId) {
        DagpengerResultat resultat = resultatRepository.finn(resultatId)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke resultat med id = " + resultatId));

        sjekkSpesialisering(resultat);
        resultatRepository.oppdaterStatus(resultatId, Behandlingsstatus.GODKJENT);
    }

    // Krav 2: avslå resultat innen egen spesialisering
    public void avslå(long resultatId) {
        DagpengerResultat resultat = resultatRepository.finn(resultatId)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke resultat med id = " + resultatId));

        sjekkSpesialisering(resultat);
        resultatRepository.oppdaterStatus(resultatId, Behandlingsstatus.AVSLÅTT);
    }

    private void sjekkSpesialisering(DagpengerResultat resultat) {
        if (resultat.spesialisering() != this.spesialisering) {
            throw new IllegalArgumentException("Saksbehandler kan ikke behandle resultat med spesialisering " 
            + resultat.spesialisering() + " (har " + this.spesialisering + ")"
            );
        }
        if (resultat.behandlingsstatus() != Behandlingsstatus.UBEHANDLET) {
            throw new IllegalStateException("Resultatet er allerede behandlet: status = " + resultat.behandlingsstatus());
        }
    }
}
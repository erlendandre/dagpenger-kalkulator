package no.nav.saksbehandler;

import java.util.Objects;

/**
 * Representerer et beregnet dagpengeresultat som ligger til behandling hos saksbehandler
 *
 * Spesialisering beskriver utfallet av beregningen (avslag/innvilget/makssats) mens 
 * behandlingsstatus beskriver om resultatet er ubehandlet/godkjent/avslått av saksbehandler
 *
 * @author Erlend André Høntorp
 * @version 1.0
 */
public class DagpengerResultat {
    private final long resultatId;
    private final double beregnetDagsats;
    private final Spesialisering spesialisering;
    private Behandlingsstatus behandlingsstatus;

    public DagpengerResultat(long resultatId, double beregnetDagsats, Spesialisering spesialisering, Behandlingsstatus behandlingsstatus) {
        this.resultatId = resultatId;
        this.beregnetDagsats = beregnetDagsats;
        this.spesialisering = Objects.requireNonNull(spesialisering);
        this.behandlingsstatus = Objects.requireNonNull(behandlingsstatus);
    }

    public long id() { return resultatId; }
    public double beregnetDagsats() { return beregnetDagsats; }
    public Spesialisering spesialisering() { return spesialisering; }
    public Behandlingsstatus behandlingsstatus() { return behandlingsstatus; }

    public void settStatus(Behandlingsstatus nyStatus) {
        this.behandlingsstatus = Objects.requireNonNull(nyStatus);
    }
}
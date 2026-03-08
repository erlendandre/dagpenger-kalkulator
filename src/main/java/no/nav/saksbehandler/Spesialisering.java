package no.nav.saksbehandler;

/**
 * Utfall av dagpengeberegningen, brukt for å rute resultater til riktig type saksbehandler (spesialisering)
 *
 * @author Erlend André Høntorp
 * @version 1.0
 */
public enum Spesialisering {
    AVSLAG_FOR_LAV_INNTEKT,
    INNVILGET,
    INNVILGET_MED_MAKSSATS
}
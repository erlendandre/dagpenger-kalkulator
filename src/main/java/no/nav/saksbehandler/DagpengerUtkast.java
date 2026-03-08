package no.nav.saksbehandler;

/**
 * Utkast til dagpengeresultat fra kalkulatoren før saksbehandling
 * Inneholder beregnet dagsats og hvilken spesialisering kalkulasjonen faller innenfor
 *
 * @author Erlend André Høntorp
 * @version 1.0
 */
public record DagpengerUtkast(double beregnetDagsats, Spesialisering spesialisering) {}
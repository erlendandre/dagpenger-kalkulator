package no.nav;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.saksbehandler.DagpengerResultat;
import no.nav.saksbehandler.DagpengerUtkast;
import no.nav.saksbehandler.ResultatRepository;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.årslønn.Årslønn;

public class Main {
    public static void main(String[] args) {
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 500000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2022, 450000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2021, 400000));

        System.out.println("---🤖 Kalkulerer dagsats... 🤖---");
        DagpengerUtkast utkast = dagpengerKalkulator.lagUtkast();
        System.out.println("Utkast: " + utkast.spesialisering());
        System.out.println("Beregnet dagsats: " + utkast.beregnetDagsats());
        System.out.println("---🤖 Utkast sendes til saksbehandling 🤖---");

        ResultatRepository lager = new ResultatRepository();
        DagpengerResultat resultat = lager.opprettUbehandlet(utkast.beregnetDagsats(), utkast.spesialisering());

        Saksbehandler saksbehandler = new Saksbehandler("Alice", resultat.spesialisering(), lager);
        System.out.println("Ubehandlede saker for " + saksbehandler.spesialisering() + ": " + saksbehandler.hentUbehandledeResultater().size());

        System.out.println(saksbehandler.navn() + " godkjenner utfallet: " + resultat.spesialisering() + " ...");
        saksbehandler.godkjenn(resultat.id());

        DagpengerResultat etter = lager.finn(resultat.id()).orElseThrow();
        System.out.println("Etter behandling: id = " + etter.id() + ", utfall = " + etter.spesialisering() + ", status = " + etter.behandlingsstatus());
    }
}
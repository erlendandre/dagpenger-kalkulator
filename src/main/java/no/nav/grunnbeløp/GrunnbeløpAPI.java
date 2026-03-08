package no.nav.grunnbeløp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Har ansvaret for å kontakte grunnbeløp API'et til NAV og henter dagens grunnbeløp.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */
public class GrunnbeløpAPI implements GrunnbeløpProvider {

    private final static Dotenv DOTENV = Dotenv.load();

    private final HttpClient grunnbeløpHTTPKlient;

    public GrunnbeløpAPI() {
        this.grunnbeløpHTTPKlient = HttpClient.newHttpClient();
    }

    @Override
    public double hentGrunnbeløp() {
        try {
            return hentGrunnbeløpFraAPI();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Problemer med tilkobling til grunnbeløp API'et", e);
        }
    }

    /**
     * Kontakter grunnbeløp API'et til NAV og henter dagens grunnbeløp.
     * Metoden sender en HTTP request og konverterer HTTP responsen til et <code>JSONObject</code>,
     * som så henter dagens grunnbeløp fra JSON objektet.
     * @return dagens grunnbeløp.
     * @throws IOException
     * @throws InterruptedException
     */
    private double hentGrunnbeløpFraAPI() throws IOException, InterruptedException {
        HttpRequest grunnbeløpSpørring = HttpRequest.newBuilder(URI.create(DOTENV.get("G_API_URL"))).build();

        HttpResponse<String> grunnbeløpRespons =  this.grunnbeløpHTTPKlient.send(grunnbeløpSpørring, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(grunnbeløpRespons.body()).getDouble("grunnbeløp");
    }
}

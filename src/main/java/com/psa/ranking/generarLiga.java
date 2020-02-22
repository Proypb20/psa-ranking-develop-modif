package com.psa.ranking;

import com.psa.ranking.service.util.FixtureUtils;

public class generarLiga {
    static public void main(String[] args) {
        System.out.println("Liga con 8 equipos:");

        FixtureUtils.mostrarPartidos(FixtureUtils.calcularLiga(4));

        System.out.println();

        System.out.println("Liga con 7 equipos:");

        FixtureUtils.mostrarPartidos(FixtureUtils.calcularLiga(5));

    }
}

package br.com.jdm.model;

public record Investment(
        long id,
        long tax,
        long dayToRescue,
        long initialFunds

) {
}

package model.enums;

public enum StatusComanda {
    IN_PROCESARE,
    CONFIRMATA,
    EXPEDIATA,
    LIVRATA,
    ANULATA;

    public String getDescriere() {
        switch (this) {
            case IN_PROCESARE: return "În procesare";
            case CONFIRMATA:   return "Confirmată";
            case EXPEDIATA:    return "Expediată";
            case LIVRATA:      return "Livrată";
            case ANULATA:      return "Anulată";
            default:           return "Necunoscut";
        }
    }
}

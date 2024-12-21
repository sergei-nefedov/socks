package pers.nefedov.socks.utils;


public enum Comparisons {
    equal,
    lessThan,
    moreThan;

    public static boolean contains(String value) {
        for (Comparisons comparison : Comparisons.values()) {
            if (comparison.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}

package pl.akh.utils;

import java.util.*;

public class CountryUtils {

    private static Map<String, String> countryCodeToZip;

    public synchronized static Set<String> getAvailableCountriesCodes() {
        if (CountryUtils.countryCodeToZip == null) {
            initCountryCodeToZip();
        }
        return CountryUtils.countryCodeToZip.keySet();
    }

    public static String getPostCodePatternForCountry(String country) {
        if (CountryUtils.countryCodeToZip == null) {
            initCountryCodeToZip();
        }
        return CountryUtils.countryCodeToZip.get(country);
    }

    private static void initCountryCodeToZip() {
        CountryUtils.countryCodeToZip = new HashMap<>();
        CountryUtils.countryCodeToZip.put("GB", "GIR[ ]?0AA|((AB|AL|B|BA|BB|BD|BH|BL|BN|BR|BS|BT|CA|CB|CF|CH|CM|CO|CR|CT|CV|CW|DA|DD|DE|DG|DH|DL|DN|DT|DY|E|EC|EH|EN|EX|FK|FY|G|GL|GY|GU|HA|HD|HG|HP|HR|HS|HU|HX|IG|IM|IP|IV|JE|KA|KT|KW|KY|L|LA|LD|LE|LL|LN|LS|LU|M|ME|MK|ML|N|NE|NG|NN|NP|NR|NW|OL|OX|PA|PE|PH|PL|PO|PR|RG|RH|RM|S|SA|SE|SG|SK|SL|SM|SN|SO|SP|SR|SS|ST|SW|SY|TA|TD|TF|TN|TQ|TR|TS|TW|UB|W|WA|WC|WD|WF|WN|WR|WS|WV|YO|ZE)(\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}))|BFPO[ ]?\\d{1,4}");
        CountryUtils.countryCodeToZip.put("JE", "JE\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}");
        CountryUtils.countryCodeToZip.put("GG", "GY\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}");
        CountryUtils.countryCodeToZip.put("IM", "IM\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}");
        CountryUtils.countryCodeToZip.put("US", "\\d{5}([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("CA", "[ABCEGHJKLMNPRSTVXY]\\d[ABCEGHJ-NPRSTV-Z][ ]?\\d[ABCEGHJ-NPRSTV-Z]\\d");
        CountryUtils.countryCodeToZip.put("DE", "\\d{5}");
        CountryUtils.countryCodeToZip.put("JP", "\\d{3}-\\d{4}");
        CountryUtils.countryCodeToZip.put("FR", "\\d{2}[ ]?\\d{3}");
        CountryUtils.countryCodeToZip.put("AU", "\\d{4}");
        CountryUtils.countryCodeToZip.put("IT", "\\d{5}");
        CountryUtils.countryCodeToZip.put("CH", "\\d{4}");
        CountryUtils.countryCodeToZip.put("AT", "\\d{4}");
        CountryUtils.countryCodeToZip.put("ES", "\\d{5}");
        CountryUtils.countryCodeToZip.put("NL", "\\d{4}[ ]?[A-Z]{2}");
        CountryUtils.countryCodeToZip.put("BE", "\\d{4}");
        CountryUtils.countryCodeToZip.put("DK", "\\d{4}");
        CountryUtils.countryCodeToZip.put("SE", "\\d{3}[ ]?\\d{2}");
        CountryUtils.countryCodeToZip.put("NO", "\\d{4}");
        CountryUtils.countryCodeToZip.put("BR", "\\d{5}[\\-]?\\d{3}");
        CountryUtils.countryCodeToZip.put("PT", "\\d{4}([\\-]\\d{3})?");
        CountryUtils.countryCodeToZip.put("FI", "\\d{5}");
        CountryUtils.countryCodeToZip.put("AX", "22\\d{3}");
        CountryUtils.countryCodeToZip.put("KR", "\\d{3}[\\-]\\d{3}");
        CountryUtils.countryCodeToZip.put("CN", "\\d{6}");
        CountryUtils.countryCodeToZip.put("TW", "\\d{3}(\\d{2})?");
        CountryUtils.countryCodeToZip.put("SG", "\\d{6}");
        CountryUtils.countryCodeToZip.put("DZ", "\\d{5}");
        CountryUtils.countryCodeToZip.put("AD", "AD\\d{3}");
        CountryUtils.countryCodeToZip.put("AR", "([A-HJ-NP-Z])?\\d{4}([A-Z]{3})?");
        CountryUtils.countryCodeToZip.put("AM", "(37)?\\d{4}");
        CountryUtils.countryCodeToZip.put("AZ", "\\d{4}");
        CountryUtils.countryCodeToZip.put("BH", "((1[0-2]|[2-9])\\d{2})?");
        CountryUtils.countryCodeToZip.put("BD", "\\d{4}");
        CountryUtils.countryCodeToZip.put("BB", "(BB\\d{5})?");
        CountryUtils.countryCodeToZip.put("BY", "\\d{6}");
        CountryUtils.countryCodeToZip.put("BM", "[A-Z]{2}[ ]?[A-Z0-9]{2}");
        CountryUtils.countryCodeToZip.put("BA", "\\d{5}");
        CountryUtils.countryCodeToZip.put("IO", "BBND 1ZZ");
        CountryUtils.countryCodeToZip.put("BN", "[A-Z]{2}[ ]?\\d{4}");
        CountryUtils.countryCodeToZip.put("BG", "\\d{4}");
        CountryUtils.countryCodeToZip.put("KH", "\\d{5}");
        CountryUtils.countryCodeToZip.put("CV", "\\d{4}");
        CountryUtils.countryCodeToZip.put("CL", "\\d{7}");
        CountryUtils.countryCodeToZip.put("CR", "\\d{4,5}|\\d{3}-\\d{4}");
        CountryUtils.countryCodeToZip.put("HR", "\\d{5}");
        CountryUtils.countryCodeToZip.put("CY", "\\d{4}");
        CountryUtils.countryCodeToZip.put("CZ", "\\d{3}[ ]?\\d{2}");
        CountryUtils.countryCodeToZip.put("DO", "\\d{5}");
        CountryUtils.countryCodeToZip.put("EC", "([A-Z]\\d{4}[A-Z]|(?:[A-Z]{2})?\\d{6})?");
        CountryUtils.countryCodeToZip.put("EG", "\\d{5}");
        CountryUtils.countryCodeToZip.put("EE", "\\d{5}");
        CountryUtils.countryCodeToZip.put("FO", "\\d{3}");
        CountryUtils.countryCodeToZip.put("GE", "\\d{4}");
        CountryUtils.countryCodeToZip.put("GR", "\\d{3}[ ]?\\d{2}");
        CountryUtils.countryCodeToZip.put("GL", "39\\d{2}");
        CountryUtils.countryCodeToZip.put("GT", "\\d{5}");
        CountryUtils.countryCodeToZip.put("HT", "\\d{4}");
        CountryUtils.countryCodeToZip.put("HN", "(?:\\d{5})?");
        CountryUtils.countryCodeToZip.put("HU", "\\d{4}");
        CountryUtils.countryCodeToZip.put("IS", "\\d{3}");
        CountryUtils.countryCodeToZip.put("IN", "\\d{6}");
        CountryUtils.countryCodeToZip.put("ID", "\\d{5}");
        CountryUtils.countryCodeToZip.put("IL", "\\d{5}");
        CountryUtils.countryCodeToZip.put("JO", "\\d{5}");
        CountryUtils.countryCodeToZip.put("KZ", "\\d{6}");
        CountryUtils.countryCodeToZip.put("KE", "\\d{5}");
        CountryUtils.countryCodeToZip.put("KW", "\\d{5}");
        CountryUtils.countryCodeToZip.put("LA", "\\d{5}");
        CountryUtils.countryCodeToZip.put("LV", "\\d{4}");
        CountryUtils.countryCodeToZip.put("LB", "(\\d{4}([ ]?\\d{4})?)?");
        CountryUtils.countryCodeToZip.put("LI", "(948[5-9])|(949[0-7])");
        CountryUtils.countryCodeToZip.put("LT", "\\d{5}");
        CountryUtils.countryCodeToZip.put("LU", "\\d{4}");
        CountryUtils.countryCodeToZip.put("MK", "\\d{4}");
        CountryUtils.countryCodeToZip.put("MY", "\\d{5}");
        CountryUtils.countryCodeToZip.put("MV", "\\d{5}");
        CountryUtils.countryCodeToZip.put("MT", "[A-Z]{3}[ ]?\\d{2,4}");
        CountryUtils.countryCodeToZip.put("MU", "(\\d{3}[A-Z]{2}\\d{3})?");
        CountryUtils.countryCodeToZip.put("MX", "\\d{5}");
        CountryUtils.countryCodeToZip.put("MD", "\\d{4}");
        CountryUtils.countryCodeToZip.put("MC", "980\\d{2}");
        CountryUtils.countryCodeToZip.put("MA", "\\d{5}");
        CountryUtils.countryCodeToZip.put("NP", "\\d{5}");
        CountryUtils.countryCodeToZip.put("NZ", "\\d{4}");
        CountryUtils.countryCodeToZip.put("NI", "((\\d{4}-)?\\d{3}-\\d{3}(-\\d{1})?)?");
        CountryUtils.countryCodeToZip.put("NG", "(\\d{6})?");
        CountryUtils.countryCodeToZip.put("OM", "(PC )?\\d{3}");
        CountryUtils.countryCodeToZip.put("PK", "\\d{5}");
        CountryUtils.countryCodeToZip.put("PY", "\\d{4}");
        CountryUtils.countryCodeToZip.put("PH", "\\d{4}");
        CountryUtils.countryCodeToZip.put("PL", "\\d{2}-\\d{3}");
        CountryUtils.countryCodeToZip.put("PR", "00[679]\\d{2}([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("RO", "\\d{6}");
        CountryUtils.countryCodeToZip.put("RU", "\\d{6}");
        CountryUtils.countryCodeToZip.put("SM", "4789\\d");
        CountryUtils.countryCodeToZip.put("SA", "\\d{5}");
        CountryUtils.countryCodeToZip.put("SN", "\\d{5}");
        CountryUtils.countryCodeToZip.put("SK", "\\d{3}[ ]?\\d{2}");
        CountryUtils.countryCodeToZip.put("SI", "\\d{4}");
        CountryUtils.countryCodeToZip.put("ZA", "\\d{4}");
        CountryUtils.countryCodeToZip.put("LK", "\\d{5}");
        CountryUtils.countryCodeToZip.put("TJ", "\\d{6}");
        CountryUtils.countryCodeToZip.put("TH", "\\d{5}");
        CountryUtils.countryCodeToZip.put("TN", "\\d{4}");
        CountryUtils.countryCodeToZip.put("TR", "\\d{5}");
        CountryUtils.countryCodeToZip.put("TM", "\\d{6}");
        CountryUtils.countryCodeToZip.put("UA", "\\d{5}");
        CountryUtils.countryCodeToZip.put("UY", "\\d{5}");
        CountryUtils.countryCodeToZip.put("UZ", "\\d{6}");
        CountryUtils.countryCodeToZip.put("VA", "00120");
        CountryUtils.countryCodeToZip.put("VE", "\\d{4}");
        CountryUtils.countryCodeToZip.put("ZM", "\\d{5}");
        CountryUtils.countryCodeToZip.put("AS", "96799");
        CountryUtils.countryCodeToZip.put("CC", "6799");
        CountryUtils.countryCodeToZip.put("CK", "\\d{4}");
        CountryUtils.countryCodeToZip.put("RS", "\\d{6}");
        CountryUtils.countryCodeToZip.put("ME", "8\\d{4}");
        CountryUtils.countryCodeToZip.put("CS", "\\d{5}");
        CountryUtils.countryCodeToZip.put("YU", "\\d{5}");
        CountryUtils.countryCodeToZip.put("CX", "6798");
        CountryUtils.countryCodeToZip.put("ET", "\\d{4}");
        CountryUtils.countryCodeToZip.put("FK", "FIQQ 1ZZ");
        CountryUtils.countryCodeToZip.put("NF", "2899");
        CountryUtils.countryCodeToZip.put("FM", "(9694[1-4])([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("GF", "9[78]3\\d{2}");
        CountryUtils.countryCodeToZip.put("GN", "\\d{3}");
        CountryUtils.countryCodeToZip.put("GP", "9[78][01]\\d{2}");
        CountryUtils.countryCodeToZip.put("GS", "SIQQ 1ZZ");
        CountryUtils.countryCodeToZip.put("GU", "969[123]\\d([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("GW", "\\d{4}");
        CountryUtils.countryCodeToZip.put("HM", "\\d{4}");
        CountryUtils.countryCodeToZip.put("IQ", "\\d{5}");
        CountryUtils.countryCodeToZip.put("KG", "\\d{6}");
        CountryUtils.countryCodeToZip.put("LR", "\\d{4}");
        CountryUtils.countryCodeToZip.put("LS", "\\d{3}");
        CountryUtils.countryCodeToZip.put("MG", "\\d{3}");
        CountryUtils.countryCodeToZip.put("MH", "969[67]\\d([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("MN", "\\d{6}");
        CountryUtils.countryCodeToZip.put("MP", "9695[012]([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("MQ", "9[78]2\\d{2}");
        CountryUtils.countryCodeToZip.put("NC", "988\\d{2}");
        CountryUtils.countryCodeToZip.put("NE", "\\d{4}");
        CountryUtils.countryCodeToZip.put("VI", "008(([0-4]\\d)|(5[01]))([ \\-]\\d{4})?");
        CountryUtils.countryCodeToZip.put("PF", "987\\d{2}");
        CountryUtils.countryCodeToZip.put("PG", "\\d{3}");
        CountryUtils.countryCodeToZip.put("PM", "9[78]5\\d{2}");
        CountryUtils.countryCodeToZip.put("PN", "PCRN 1ZZ");
        CountryUtils.countryCodeToZip.put("PW", "96940");
        CountryUtils.countryCodeToZip.put("RE", "9[78]4\\d{2}");
        CountryUtils.countryCodeToZip.put("SH", "(ASCN|STHL) 1ZZ");
        CountryUtils.countryCodeToZip.put("SJ", "\\d{4}");
        CountryUtils.countryCodeToZip.put("SO", "\\d{5}");
        CountryUtils.countryCodeToZip.put("SZ", "[HLMS]\\d{3}");
        CountryUtils.countryCodeToZip.put("TC", "TKCA 1ZZ");
        CountryUtils.countryCodeToZip.put("WF", "986\\d{2}");
        CountryUtils.countryCodeToZip.put("XK", "\\d{5}");
        CountryUtils.countryCodeToZip.put("YT", "976\\d{2}");
    }

}


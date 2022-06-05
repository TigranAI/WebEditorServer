package ru.tigran.WebEditor.others;

public class F {
    public static int nthIndexOf(String source, String substr, int n) {
        int pos = source.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = source.indexOf(substr, pos + 1);
        return pos;
    }
}

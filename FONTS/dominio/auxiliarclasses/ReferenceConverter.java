package dominio.auxiliarclasses;

import dominio.model.Cell;

import java.util.ArrayList;

/**
 * Representa una clase auxiliar con métodos para convertir referencias a pares, letras a números y viceversa y convertir un vector de celdas a pares.
 */
public class ReferenceConverter {


    public static Pair getReferencePos(String ref) {

        int i = 0;
        StringBuilder referenciaLetter = new StringBuilder();
        while (!Character.isDigit(ref.charAt(i))) {
            referenciaLetter.append(ref.charAt(i));
            ++i;
        }

        int col = letterToNumber(referenciaLetter.toString());

        int referenciaNumber = 0;
        while (i < ref.length()) {
            referenciaNumber *= 10;
            referenciaNumber += Integer.parseInt(String.valueOf(ref.charAt(i)));
            ++i;
        }

        return new Pair(referenciaNumber, col);
    }

    public static String number2Letter(int par) {
        if(par < 0) {
            return "-"+number2Letter(par-1);
        }

        int quot = par / 26;
        int rem = par % 26;
        char letter = (char)((int)'A' + rem);
        if(quot == 0) {
            return "" + letter;
        } else {
            return number2Letter(quot - 1) + letter;
        }
    }
    public static int letterToNumber(String s) {
        int result = 0;
        int j = 0;
        for (int i = s.length() - 1; i >= 0; i--){  // la primera letra a mirar es la ultima
            char e = s.charAt(i);
            int aux = Character.hashCode(e) - Character.hashCode('A');

            if (i == s.length()-1) result += aux;   // si es la primera letra entonces hay que sumar solo
            else result += Math.pow(26, j) * (aux + 1); // si es alguna otra letra entonces no va de 1 en 1 sino que depende la posicion (si esta en la segunda posicion salta de 26 en 26)

            j++;
        }
        return result;
    }

    public static ArrayList<Pair> cellToPair(ArrayList<Cell> cells) {
        ArrayList<Pair> pairs = new ArrayList<>();
        for (Cell cell : cells) {
            pairs.add(new Pair(cell.getRow(), cell.getColumn()));
        }
        return pairs;
    }


}

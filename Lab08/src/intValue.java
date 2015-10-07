/**
 * Created by Ari Sanders on 15-Jul-15.
 */
public class intValue {
    String value;

    /**
     * Constructor for intValue
     * @param value Value of this object
     */
    public intValue(String value) {
        this.value = value;
    }

    /**
     * Multiplies two intValues
     * @param n Number to multiply by
     * @return An intValue of the product of the input and this object
     */
    public intValue multiply(intValue n){
        char[] N = value.toCharArray();
        char[] K = n.value.toCharArray();
        intValue product;
        String pad;
        intValue result = new intValue("0");
        /* N * K =
         * for i : N
         * for j : K
         * temp += (N[i] * K[j]).pad0((N.size - 1 - i) + (K.size - 1 - j))
         * end for
         * result += temp
         * end for
         */
        for (int i = 0; i < N.length; i++){
            product = new intValue("0");
            for (int j = 0; j < K.length; j++){
                pad = "";
                for (int k = 0; k < ((N.length - 1 - i) + (K.length - 1 - j)); k++){
                    pad += "0";
                    //System.out.print(k + " = " + pad + "; ");
                }
                product = product.add(new intValue((Character.getNumericValue(N[i])
                        * Character.getNumericValue(K[j])) + pad));
                /*System.out.println("\n" + product.value + " = " + N[i] + " * " + K[j]
                        + " = "
                        + (Character.getNumericValue(N[i]) * Character.getNumericValue(K[j]))
                        + pad);*/
            }
            result = result.add(product);
        }

        return result;
    }

    /**
     * Adds to intValues
     * @param n Number to add with
     * @return An intValue of the sum of the input and this object
     */
    public intValue add(intValue n){
        char[] first;
        char[] second;
        String result = "";

        if (value.length() >= n.value.length()){
            first = value.toCharArray();
            second = n.value.toCharArray();
        }
        else {
            first = n.value.toCharArray();
            second = value.toCharArray();
        }

        int carry = 0;
        int sum;
        int diff = first.length - second.length;
        for (int i = first.length - 1; i >= 0; i--){
            sum = Character.getNumericValue(first[i]) + carry;
            if (i - diff >= 0){
                sum += Character.getNumericValue(second[i - diff]);
            }
            if (sum > 9){
                carry = 1;
                sum -= 10;
            }
            else {
                carry = 0;
            }
            result = sum + result;
        }
        if (carry == 1){
            result = carry + result;
        }

        return new intValue(result);
    }
}

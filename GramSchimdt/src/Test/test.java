package Test;


import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class test {
    private double productoInternoCanonico(double[] A, double[] B){
        double result = 0;
        for (int i = 0; i < A.length ; i++) {
            result+= A[i]*B[i];
        }
        return result;
    }

    private double calculateAlpha(double[] a, double[] b){
        return -(productoInternoCanonico(a,b)/productoInternoCanonico(a,a));
    }

    private double[] vectorProductWithConstant(double[] a, double b){
        double[] result = new double[a.length];
        for (int i = 0; i < result.length ; i++) {
            result[i] = a[i]*b;
        }
        return result;
    }

    private double[] vectorSummation(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < result.length ; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }


    public List<double[]> gramSchimdt(List<double[]> A) {
        List<double[]> result = new ArrayList<>();
        result.add(A.get(0));// u1=v1
        for (int i = 1; i <A.size() ; i++) {
            double alpha = calculateAlpha(result.get(i-1),A.get(i));
            double[] newVector = vectorSummation(A.get(i),vectorProductWithConstant(result.get(i-1),alpha));
            result.add(newVector);
        }
        return result;
    }

@Test
 public void testGramSchimdt(){
     double[] v1={1,2,0};
     double[] v2={0,-3,4};
     double[] v3={0,0,2};
     List<double[]> b=new ArrayList<>();
     b.add(v1);
     b.add(v2);
     b.add(v3);
     List<double[]> result= gramSchimdt(b);
    for (int i = 0; i <result.size() ; i++) {
        System.out.println(vectorToString(result.get(i)));

    }



}

    private String vectorToString(double[] vector) {
        String vectorString="(";
        for (int i = 0; i < vector.length; i++) {
            vectorString=vectorString+" "+vector[i]+" ";
        }
        vectorString=vectorString+")";
        return vectorString;
    }


    @Test
    public void testGramSchimdt2() {
        double[] v1 = {1, 1, 0};
        double[] v2 = {0.5, -0.5, 0};

        List<double[]> b = new ArrayList<>();
        b.add(v1);
        b.add(v2);

        List<double[]> result = gramSchimdt(b);
        for (int i = 0; i < result.size(); i++) {
            System.out.println(vectorToString(result.get(i)));

        }
    }

}

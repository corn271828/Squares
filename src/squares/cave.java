
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class cave {

    public static void main (String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("cave.in"));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cave.out")));

        StringTokenizer st = new StringTokenizer(br.readLine());

        int numRows = Integer.parseInt(st.nextToken());
        int numCols = Integer.parseInt(st.nextToken());

        String[][] painting = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++)
            painting[i] = br.readLine().split("");

        int id = 47;
        HashMap<Integer, Container> getter = new HashMap<>();

        for (int rownum = numRows - 1; rownum > 0; rownum--) {

            while (true) {
                int colnum = 1;
                while (colnum < numCols && !painting[rownum][colnum].equals(".")) {
                    colnum++;
                }
                if (colnum >= numCols)
                    break;
                String idstr = Integer.toString(id);

                getter.put(id, new Container(id));


                ArrayList<Integer> checkthis = new ArrayList<>();
                ArrayList<Integer> checkbelow = new ArrayList<>();

                checkthis.add(colnum);
                while (checkthis.size() > 0 || checkbelow.size() > 0) {
                    if (checkthis.size() > 0) {
                        int cols = checkthis.remove(0);
                        if (painting[rownum][cols].equals(idstr))
                            continue;
                        while (painting[rownum][cols].equals(".")) {
                            painting[rownum][cols] = idstr;
                            if (!painting[rownum + 1][cols].equals("#")) {
                                checkbelow.add(cols);
                            }
                            cols++;
                        }
                    } else {
                        int cols = checkbelow.remove(0);
                        if (painting[rownum + 1][cols].equals(idstr))
                            continue;
                        System.out.println(rownum + 1 + " " + cols + " " + painting[rownum + 1][cols]);
                        
                        String holdijs = painting[rownum + 1][cols];
                        getter.get(id).add(getter.get(Integer.parseInt(painting[rownum + 1][cols])));

                        for (; cols < numCols; cols++ ){
                            if (painting[rownum + 1][cols].equals(holdijs)) {
                                painting[rownum + 1][cols] = idstr;
                                if (!painting[rownum][cols].equals("#")) {
                                    checkthis.add(cols);
                                }
                            }
                        }
                    }
                }

                id++;
            }


        }

        boolean[] accounted = new boolean[id];
        id--;

        long ret = 1;


        while (true) {
            int i = id;
            for (; i > 46 && accounted[i] == true; i--) {
            }
            if (i == 46)
                break;

            getter.get(i).update();
            ret *= getter.get(i).calc;
            ret %= 1000000007;

            boolean[] trip = getter.get(i).hierarch(new boolean[i + 1]);
            for (int j = 0; j < trip.length; j++)
                if (trip[j])
                    accounted[j] = true;
        }

        pw.println(ret);

        br.close();
        pw.close();
    }

    public static void printmatrix(String[][] matrix) {
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.printf("%3s", cell);
            }
            System.out.println();
        }
    }

    public static class Container {
        int id;
        ArrayList<Container> subsids;
        int calc = -1;

        public Container(int didij) {
            id = didij;
            subsids = new ArrayList<>();
        }

        public void add(Container c) {
            subsids.add(c);
        }

        public void update() {
            calc = 1;
            if (subsids.size() == 0) {
                calc++;
                return;
            }
            for (Container inside : subsids) {
                if (inside.calc == -1)
                    inside.update();
                calc *= inside.calc;
                calc %= 1000000007;
            }
            calc++;
            return;
        }

        public boolean[] hierarch (boolean[] into) {
            into[id] = true;
            for (Container inside : subsids)
                inside.hierarch(into);
            return into;
        }


    }


}


package advisor.view;

import java.util.List;

public class ProgramView {

    /**
     * Prompts new data from the new releases model.
     * @param data - Set of data from the new releases model.
     * @param currPage - Current page of the data.
     * @param totalPage - Total page of the data.
     */
    public static void displayNewReleasesPage(List<String[]> data,
                                              int currPage, int totalPage) {

        if(data == null) {
            System.out.println("No more pages.");
            return;
        }

        String[] songName = data.get(0);
        String[] artistsName = data.get(1);
        String[] urls = data.get(2);

        for (int i = 0; i < songName.length; i++) {
            System.out.println(songName[i]);
            System.out.println("[" + artistsName[i] + "]");
            System.out.println(urls[i]);
            System.out.println();
        }

        displayPageFooter(currPage, totalPage);
    }

    /**
     * general printer of the API models.
     * @param data - Set of data from API models.
     * @param currPage - Current page of the data.
     * @param totalPage - Total page of the data.
     */
    public static void displayDataPage(List<String[]> data, int currPage, int totalPage,
                                       boolean hasSpaceBetween) {
        if(data == null) {
            System.out.println("No more pages.");
            return;
        }

        for (int i = 0; i < data.get(0).length; i++) {
            for (String[] stringData : data) {
                System.out.println(stringData[i]);
            }
            System.out.print(hasSpaceBetween ? "\n" : "");
        }
        displayPageFooter(currPage, totalPage);
    }

    /**
     * Prompts the page footer.
     * @param currPage - Current page of the data.
     * @param totalPage - Total page of the data.
     */
    private static void displayPageFooter(int currPage, int totalPage) {
        System.out.printf("---PAGE %d OF %d---\n", currPage, totalPage);
    }
}

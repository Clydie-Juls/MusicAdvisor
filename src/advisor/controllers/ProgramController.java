package advisor.controllers;

import advisor.model.CategoryModel;
import advisor.model.FeaturedModel;
import advisor.model.NewReleasesModel;
import advisor.model.PlaylistModel;
import advisor.server.Config;
import advisor.server.Server;
import advisor.state.MoveState;
import advisor.utils.JsonAnalyzer;
import advisor.view.ProgramView;

import java.util.List;


public class ProgramController {

    // a state function that allows authorization to user
    // temporarily always returns true
    private static NewReleasesModel newReleasesModel;
    private static FeaturedModel featuredModel;
    private static CategoryModel categoryModel;
    private static PlaylistModel playlistModel;


    public static boolean authUser() throws Exception {
        Server.create();
        System.out.println("use this link to request the access code:");
        System.out.println(Server.spotifyApiUri());
        if(Server.waitForAuthConfirmation()) {
            if( Server.requestAccessToken()) {
                System.out.println("Success!");
                return true;
            }
        }
        System.out.println("Failed.");
        return false;
    }

    public static void showNewReleases(MoveState moveState) {
        try {
            switch (moveState) {
                case NEXT -> ProgramView.displayNewReleasesPage(newReleasesModel.next(),
                        newReleasesModel.getPage(), newReleasesModel.getTotalPage());
                case PREV -> ProgramView.displayNewReleasesPage(newReleasesModel.previous(),
                        newReleasesModel.getPage(), newReleasesModel.getTotalPage());
                case NONE -> {
                    newReleasesModel = new NewReleasesModel(Integer.parseInt(Config.PAGE.getInfo()));
                    ProgramView.displayNewReleasesPage(newReleasesModel.getData(),
                            newReleasesModel.getPage(), newReleasesModel.getTotalPage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showFeatured(MoveState moveState) {
        try {
            switch (moveState) {
                case NEXT -> ProgramView.displayDataPage(featuredModel.next(),
                        featuredModel.getPage(), featuredModel.getTotalPage(), true);
                case PREV -> ProgramView.displayDataPage(featuredModel.previous(),
                        featuredModel.getPage(), featuredModel.getTotalPage(), true);
                case NONE -> {
                    featuredModel = new FeaturedModel(Integer.parseInt(Config.PAGE.getInfo()));
                    ProgramView.displayDataPage(featuredModel.getData(),
                            featuredModel.getPage(), featuredModel.getTotalPage(), true);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showCategories(MoveState moveState) {
        try {
            switch (moveState) {
                case NEXT -> ProgramView.displayDataPage(categoryModel.next(),
                        categoryModel.getPage(), categoryModel.getTotalPage(), false);
                case PREV -> ProgramView.displayDataPage(categoryModel.previous(),
                        categoryModel.getPage(), categoryModel.getTotalPage(), false);
                case NONE -> {
                    categoryModel = new CategoryModel(Integer.parseInt(Config.PAGE.getInfo()));
                    ProgramView.displayDataPage(categoryModel.getData(),
                            categoryModel.getPage(), categoryModel.getTotalPage(), false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showMoodPlaylists(String name, MoveState moveState) {
        try {
            categoryModel = new CategoryModel(50);
            List<String[]> data = categoryModel.getData();
            String[] ids = categoryModel.getIds();
            String id = "-1";

            for (int i = 0; i < data.get(0).length; i++) {
                if(data.get(0)[i].equalsIgnoreCase(name)) {
                    id = ids[i];
                }
            }

            System.out.println("test 3");
            switch (moveState) {
                case NEXT -> ProgramView.displayDataPage(playlistModel.next(),
                        playlistModel.getPage(), playlistModel.getTotalPage(), true);
                case PREV -> ProgramView.displayDataPage(playlistModel.previous(),
                        playlistModel.getPage(), playlistModel.getTotalPage(), true);
                case NONE -> {
                    playlistModel = new PlaylistModel(Integer.parseInt(Config.PAGE.getInfo()), id);
                    List<String[]> playlistData = playlistModel.getData();

                    if (playlistData.size() == 2) {
                        ProgramView.displayDataPage(playlistModel.getData(),
                                playlistModel.getPage(), playlistModel.getTotalPage(), true);
                    } else {
                        String[] errorMessage = JsonAnalyzer.getJsonInfo(playlistData.get(0)[0],
                                "error/", "message", 0);
                        System.out.println(errorMessage[0]);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showExit() {
        System.out.print("");
    }
}

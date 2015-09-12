package project.com.pockethistory;

public class RecyclerContent {
    private String heading;
    private String content;

    public void setPaletteContent(String heading, String content) {
        this.heading = heading;
        this.content = content;
    }

    public String getPaletteHeading() {
        return heading;
    }

    public String getPaletteContent() {
        return content;
    }
}

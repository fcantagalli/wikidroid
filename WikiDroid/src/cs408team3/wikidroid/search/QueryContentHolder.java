/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs408team3.wikidroid.search;

/**
 *
 * @author Felipe
 */
public class QueryContentHolder {

    private String title;
    private String link;
    private String displayLink;

    public QueryContentHolder(String title, String link, String displayLink) {
        setTitle(title);
        setLink(link);
        setDisplayLink(displayLink);
    }

    @Override
    public String toString() {
        return "QueryContentHolder{" + "title=" + title + ", link=" + link + ", displayLink=" + displayLink + '}';
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    private void setLink(String link) {
        this.link = link;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    private void setDisplayLink(String displayLink) {
        this.displayLink = displayLink;
    }

}

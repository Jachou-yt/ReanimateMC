package fr.jachou.reanimatemc.data;


public class KOData {
    private boolean isKo;
    private int taskId;
    private boolean crawling;

    public boolean isKo() {
        return isKo;
    }

    public void setKo(boolean isKo) {
        this.isKo = isKo;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isCrawling() {
        return crawling;
    }

    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }
}



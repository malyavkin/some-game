class Animation {
    boolean interrupting;
    int[] timings;
    int[] tiles;
    String name;
    public Animation(String name, boolean interrupting, int[] timings, int[] tiles) {
        this.interrupting = interrupting;
        this.timings = timings;
        this.tiles = tiles;
        this.name = name;
    }
}
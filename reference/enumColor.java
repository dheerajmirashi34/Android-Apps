enum Colors {
    RED("Red"), GREEN("Green");

    private String color;
    private static List<String> colorsList = new ArrayList<>();

	Colors(String color) {
		this.color = color;
	}

    static {
        for (Colors color : Colors.values()) {
            colorsList.add(color.getColor());
        }
    }

	public static List<String> getColorsList(){
    	return colorsList;
	}

    public String getColor() {
        return this.color;
    }
}
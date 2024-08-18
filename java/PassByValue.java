/**
 * A demonstration of the pass-by-value nature of java.
 */

public class PassByValue {

    void updateMyString(String val) {
        val = new String("not your string anymore");
    }

    void updateMyObject(PersonObject po) {
        po.name = "modified";
        po.favoriteColor = "modified";
        po.favoriteGame = "modified";
    }

    void updateMyPrimitive(int val) {
        val = 999;
    }

    public static void main(String[] args) {
        String inputStr = "my original string";
        int inputPrimitive = 0;
        PersonObject inputObject = new PersonObject("original","original","original");

        var passByValue = new PassByValue();
        passByValue.updateMyPrimitive(inputPrimitive);
        passByValue.updateMyString(inputStr);
        passByValue.updateMyObject(inputObject);

        System.out.println("after function primitive = " + inputPrimitive);
        if (inputPrimitive == 0 )
            System.out.println("\tthe primitive object was NOT modified by function actions on it");

        System.out.println(inputStr);
        if(inputStr.equals("my original string"))
            System.out.println("\tThe string was NOT modified by function actions on it (string special case??  it's an object..)");

        System.out.println(inputObject);
        if(!inputObject.name.equals("original") || !inputObject.favoriteColor.equals("original") || !inputObject.favoriteGame.equals("original"))
            System.out.println("\tThe object WAS SUCCESSFULLY modified because a copy of the object location was passed, allowing object reference");
    }

}


class PersonObject {
    String name;
    String favoriteColor;
    String favoriteGame;
    public PersonObject(String name, String color, String game) {
        this.name = name;
        this.favoriteColor = color;
        this.favoriteGame = game;
    }

    @Override
    public String toString() {
        return "PersonObject{" +
                "name='" + name + '\'' +
                ", favoriteColor='" + favoriteColor + '\'' +
                ", favoriteGame='" + favoriteGame + '\'' +
                '}';
    }
}
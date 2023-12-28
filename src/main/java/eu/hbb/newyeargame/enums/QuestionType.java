package eu.hbb.newyeargame.enums;

public enum QuestionType {

    DEFAULT,
    TEXT,
    IMAGE,
    SOUND;

    public static boolean equalsAttributeType(String attribute, QuestionType questionType) {
        return attribute.split("_")[0].equals(questionType.toString());
    }

}

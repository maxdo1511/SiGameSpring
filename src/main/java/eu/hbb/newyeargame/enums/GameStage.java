package eu.hbb.newyeargame.enums;

public enum GameStage {

    GAME_SELECTION(0, "Сначала вы должны выбрать игру"),
    TEAMS_INIT(1, "Вам нужно создать команды"),
    ROUND_START(2, "Подождите загрузки раунда"),
    QUESTION_SELECTION(3, "Сначала выбирите вопрос"),
    QUESTION(4, "Вам нужно сначала ответить на вопрос"),
    QUESTION_ANS(5, "Ведущий должен подтвердить верность ответа"),
    RESULT(6, "Конец, вы можете только увидеть результат (:"),
    STATISTIC(7, "Конец, вы можете только увидеть статистику (:");

    public final int priority;
    public final String message;

    GameStage(int priority, String message) {
        this.priority = priority;
        this.message = message;
    }

    public void checkPermissionAndThrowExceptionIfNotValid(GameStage current) {
        if (this != current) {
            throw new RuntimeException(current.message);
        }
    }

    public boolean stageMoreThan(GameStage current) {
        return this.priority < current.priority;
    }
}

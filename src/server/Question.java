package server;

public class Question {

	private String category;
	private String question;
	private String difficulty;
	private String correct_answer;

	public Question(String category, String type, String question, String difficulty, String correct_answer) {
		this.category = category;
		this.question = question;
		this.difficulty = difficulty;
		this.correct_answer = correct_answer;
	}

	@Override
	public String toString() {
		return "Question [category=" + category + ", question=" + question + ", difficulty=" + difficulty
				+ ", correct_answer=" + correct_answer + "]";
	}

	public String getCategory() {
		return category;
	}

	public String getQuestion() {
		return question;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getCorrect_answer() {
		return correct_answer;
	}
}

package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class QuestionLoader {

	public Question loadRandomQuestion() throws IOException {
		String apiURL = "http://www.opentdb.com/api.php?amount=1";
		System.out.println("loading question");
		URL url = new URL(apiURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();

		request.connect();

		// Convert to a JSON object
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		JsonObject rootobj = (JsonObject) root.getAsJsonObject().getAsJsonArray("results").get(0);

		System.out.println("loaded JSON object... : " + rootobj.toString());

		Gson gson = new Gson();
		// convert to java Question object
		Question question = gson.fromJson(rootobj, Question.class);
		System.out.println("Successfully created Java object: " + question.toString());
		return question;
	}
}

package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		String InputData=request.getParameter("city");
		System.out.println(InputData);
		
		//ApI Setup
		String apikey="0786139ba734734fc7f4ee0fe364d6ee";
		String city=request.getParameter("city");
		
		
		//create url for openweatherMap API request
		 String apiurl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey;
		 
		//API integration
		 try
		 {
			 URL url=new URL(apiurl);
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				
				
				//reading data from network
				InputStream inputstream=conn.getInputStream();
				InputStreamReader reader=new InputStreamReader(inputstream);
				
				//store data in string
				StringBuilder responseContent=new StringBuilder();
				
				
				//to take input from user create scanner object
				Scanner sc=new Scanner(reader);
				
				while(sc.hasNext())
				{
					responseContent.append(sc.nextLine());
				}
				
				sc.close();
				
				//typecast or parsing string data into json format
				Gson gson=new Gson();   //this is from google
				JsonObject jsonobject=gson.fromJson(responseContent.toString(),JsonObject.class);
				
				
				 //Date & Time
		        long dateTimestamp = jsonobject.get("dt").getAsLong() * 1000;
		        String date = new Date(dateTimestamp).toString();
		        
		        //Temperature
		        double temperatureKelvin = jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
		        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		       
		        //Humidity
		        int humidity = jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
		        
		        //Wind Speed
		        double windSpeed = jsonobject.getAsJsonObject("wind").get("speed").getAsDouble();
		        
		        //Weather Condition
		        String weatherCondition = jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		        
		        // Set the data as request attributes (for sending to the jsp page)
		        request.setAttribute("date", date);
		        request.setAttribute("city", city);
		        request.setAttribute("temperature", temperatureCelsius);
		        request.setAttribute("weatherCondition", weatherCondition); 
		        request.setAttribute("humidity", humidity);    
		        request.setAttribute("windSpeed", windSpeed);
		        request.setAttribute("weatherData", responseContent.toString());
		        
		        conn.disconnect();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		// Forward the request to the index.jsp page for rendering
	        request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}

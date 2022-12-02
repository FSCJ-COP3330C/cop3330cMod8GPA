#COP3330C Module 8 GPA

In this assignment we will implement a program which prompts the user from the  
console to enter various types of data and displays them using a variety of locales.  
Prompt the user for 3 data items:  
	•	a currency value, using US dollars and cents, less than $100, in the format $dd.cc (dd = dollars, cc = cents)  
	•	a date, in the format mm/dd/yyyy)  (mm == 2-digit month, dd = 2-digit day, yyyy = 4-digit year)  
	•	a time, using a 24-hour clock, in the format hh:mm:ss  (hh:mm:ss)  
	Choose 2 one European locale and one Asia locale to display them.  
	
My code uses 3 locales: Spain, China, and India.  

Note that your console will not display the correct symbols unless you have the associated language pack loaded (for Windows). We won't doing this for this assignment; instead we will add a wrapper (using the decorator pattern) to our code which displays the hexadecimal Unicode values for 
each character in the localized output.  

Please enter a currency value in the format dd.cc 22.33  
Please enter a date in the format mm/dd/yy 11/26/22  
Please enter a time in the format hh:mm:ss 10:15:25  
22.33 11/26/22 10:15:25  
22.33 spanish (Spain) \u0024\u0032\u0032\u002e\u0033\u0033  
22.33 chino (China) \u00a5\u0032\u0032\u002e\u0033\u0033  
22.33 urdu (India) \u20b9\u00a0\u06f2\u06f2\u066b\u06f3\u06f3  

Submit your Java source file(s) and a screen snip showing the application's execution output.  

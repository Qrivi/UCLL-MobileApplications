using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using System.Net;
using System.Collections.Specialized;

namespace Dictionary
{
	[Activity (Label = "Dictionary", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{

		EditText searchField;
		ImageButton searchButton;
		ListView wordList;

		WebClient client;
		Uri uri;
		NameValueCollection parameters;

		String[] test = { "yolo", "swag", "wafa", "jan" };

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			searchField = FindViewById<EditText> (Resource.Id.searchField);
			searchButton = FindViewById<ImageButton> (Resource.Id.searchButton);
			wordList = FindViewById<ListView> (Resource.Id.wordList);

			client = new WebClient();
			uri = new Uri ("http://services.aonaware.com/DictService/DictService.asmx/Define");
			parameters= new NameValueCollection();

			parameters.Add ("word", "waffle");

			client.UploadValuesCompleted += (sender, e) => populateWordList();
			client.UploadValuesAsync (uri, parameters);


			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, Resource.Layout.ListItem ,test);
			//wordList.Adapter = adapter;
		}

		private void populateWordList(){
			Console.WriteLine ("tis gelukt");
		}

	}
}



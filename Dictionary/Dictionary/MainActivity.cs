using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using System.Net;
using System.Collections.Specialized;
using System.Linq;
using System.IO;
using System.Xml;
using System.Collections.Generic;
using Android.Util;

namespace Dictionary
{
	[Activity (Label = "Dinky Dictionary", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{

		EditText SearchField;
		ImageButton SearchButton;
		TextView SearchInfo;
		ListView WordList;

		WordListAdapter Adapter;

		WebClient Client;
		Uri Uri;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			Log.Info ("Dictionary", "Hello World!");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			SearchField = FindViewById<EditText> (Resource.Id.SearchField);
			SearchButton = FindViewById<ImageButton> (Resource.Id.SearchButton);
			SearchInfo = FindViewById<TextView> (Resource.Id.SearchInfo);
			WordList = FindViewById<ListView> (Resource.Id.WordList);

			SearchButton.Enabled = false;

			SearchField.AfterTextChanged += (sender, e) => SearchHandler();
			SearchButton.Click += (sender, e) => SearchHandler();

			Client = new WebClient();

			Uri = new Uri ("http://services.aonaware.com/DictService/DictService.asmx/Define");
		}

		private void PopulateWordList( object sender, UploadValuesCompletedEventArgs e){
			String SearchQuery = SearchField.Text.Trim ();

			if (SearchQuery != "") {
				if (e.Error == null) {
					 
					List<SearchResult> Results = new List<SearchResult> ();
					XmlDocument Response = new XmlDocument ();

					Response.Load (new MemoryStream (e.Result));
				
					XmlNodeList Definitions = Response.GetElementsByTagName ("Definition");

					SearchInfo.Text = Definitions.Count + " definitions for “" + SearchQuery + "”.";
					if(Definitions.Count == 1 )
						SearchInfo.Text = Definitions.Count + " definition for “" + SearchQuery + "”.";

					foreach (XmlNode d in Definitions) {
						//String dictionary = definition["Dictionary"]["Name"].InnerText;
						//String worddefinition = definition["WordDefinition"].InnerText;
						//Console.WriteLine ("Element: " + dictionary + " " + worddefinition);
						Results.Add (new SearchResult {
							Id = Results.Count,
							Dictionary = d ["Dictionary"] ["Name"].InnerText,
							Definition = d ["WordDefinition"].InnerText
						});
					}

					Adapter = new WordListAdapter (this, Results);
					WordList.Adapter = Adapter;

				} else {
					SearchInfo.Text = "Looking for definitions...";
				}
			} else {
				SearchInfo.Text = "";
			}
		}

		private void SearchHandler(){
			Log.Info ("Dictionary", "SearchHandler()");

			SearchButton.Enabled = ( SearchField.Text.Trim() == "" ) ? false : true;

			NameValueCollection Parameters = new NameValueCollection();

			Parameters.Add ( "word", SearchField.Text.Trim() );

			Client.UploadValuesCompleted += PopulateWordList;
			Client.CancelAsync ();
			try{
				Client.UploadValuesAsync (Uri, Parameters);
			}catch(Exception e){
				// fuck the police
				Log.Info ("MainActivity", "SearchHandler() -> EXCEPTION CAUGHT");
			}
		}
	
	}

	public class SearchResult{
		public long Id{ get; set;}
		public String Dictionary{ get; set;}
		public String Definition{ get; set;}
	}
}